package com.example.demo.domain.user.service;

import com.example.demo.domain.user.entity.QUser;
import com.example.demo.domain.user.entity.QWithdrawal;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.Withdrawal;
import com.example.demo.domain.user.entity.request.RequestUser;
import com.example.demo.domain.user.entity.response.ResponseUser;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user.repository.WithdrawalRepository;
import com.example.demo.exception.ResponseMessage;
import com.example.demo.jwt.TokenProvider;
import com.example.demo.util.MailSenderRunner;
import com.example.demo.util.RedisUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {


    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final WithdrawalRepository withdrawalRepository;

    private final PasswordEncoder passwordEncoder;
    private final MailSenderRunner mailSenderRunner;

    private final RedisUtil redisUtil;


    public ResponseUser sign(RequestUser requestUser) {

        QUser qUser = QUser.user;

        User temp = queryFactory.selectFrom(qUser)
                .where(qUser.userEmail.eq(requestUser.getUserEmail()))
                .fetchFirst();

        if (temp != null) throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

        requestUser = requestUser.toMember(passwordEncoder);
        User user = new User(requestUser);
        userRepository.save(user);
        ResponseUser responseUser = new ResponseUser(user);
        return responseUser;
    } // 회원가입

    @Transactional
    public ResponseUser login(RequestUser requestUser) {

        QUser qUser = QUser.user;
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestUser.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성

        User user = queryFactory.selectFrom(qUser)
                .where(qUser.userEmail.eq(requestUser.getUserEmail()))
                .fetchOne();
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다.");

        ResponseUser responseUser = new ResponseUser(user);
        responseUser = tokenProvider.generateUserInfo(authentication, responseUser);
        user = new User(responseUser);

        // 4. RefreshToken 저장
        userRepository.save(user);
        // 5. 토큰 발급
        return responseUser;

    }//로그인


    @Transactional
    public ResponseUser reissue(RequestUser requestUser) {
        // 1. Refresh Token 검증

        if (!tokenProvider.validateToken(requestUser.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(requestUser.getAccessToken());


        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴


        User userInfoTemp = userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!userInfoTemp.getRefreshToken().equals(requestUser.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성

        Optional<User> optUserInfo = userRepository.findByRefreshToken(requestUser.getRefreshToken());
        if (!optUserInfo.isPresent()) throw new RuntimeException("토큰으로 검색된 사용자 정보가 없습니다");
        User user = optUserInfo.get();
        ResponseUser responseUser = new ResponseUser(user);
        responseUser = tokenProvider.generateUserInfo(authentication, responseUser);

        user = new User(responseUser);
        // 6. 저장소 정보 업데이트
        userRepository.save(user);
        // 토큰 발급
        return responseUser;

    }// 토큰 재발급


    @Transactional
    public ResponseUser update(RequestUser requestUser) {

        QUser qUser = QUser.user;

        Long result = queryFactory.update(qUser)
                .set(qUser.userAddress, requestUser.getUserAddress())
                .set(qUser.userBirth, requestUser.getUserBirth())
                .set(qUser.userNickname, requestUser.getUserNickname())
                .set(qUser.updateDateTime, LocalDateTime.now())
                .where(qUser.userId.eq(requestUser.getUserId()))
                .execute();
        if (result == 1) {
            User user = queryFactory
                    .selectFrom(qUser)
                    .where(qUser.userId.eq(requestUser.getUserId()))
                    .fetchOne();
            ResponseUser responseUser = new ResponseUser(user);
            return responseUser;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 회원을 수정하지 못했습니다.");
        }
    }//회원 수정

    @Transactional
    public Withdrawal delete(Long id) {

        QUser qUser = QUser.user;
        QWithdrawal qWithdrawal = QWithdrawal.withdrawal;


        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.userId.eq(id))
                .fetchOne();

        Long result = queryFactory.delete(qUser)
                .where(qUser.userId.eq(id).and(qUser.userAuthority.notEqualsIgnoreCase("ROLE_ADMIN")))
                .execute();
        if (result == 1) {
            Withdrawal withdrawal = new Withdrawal(user);
            withdrawalRepository.save(withdrawal); // 저장
            return withdrawal;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 회원을 삭제하지 못했습니다.");
        }

    }//회원 삭제

    @Transactional
    public ResponseUser updatePassword(RequestUser requestUser) {

        QUser qUser = QUser.user;

        Long result = queryFactory.update(qUser)
                .set(qUser.userPassword, passwordEncoder.encode(requestUser.getNewPassword()))
                .set(qUser.updateDateTime, LocalDateTime.now())
                .where(qUser.userId.eq(requestUser.getUserId()).and(qUser.userPassword.eq(requestUser.getUserPassword())))
                .execute();
        if (result == 1) {

            User user = queryFactory
                    .selectFrom(qUser)
                    .where(qUser.userId.eq(requestUser.getUserId()))
                    .fetchOne();
            ResponseUser responseUser = new ResponseUser(user);
            return responseUser;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 변경에 실패했습니다.");
        }
    } //비밀번호 변경


    public void sendEmail(RequestUser requestUser) {


        QUser qUser = QUser.user;

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.userEmail.eq(requestUser.getUserEmail()))
                .fetchOne();

        if (user != null) {
            Random random = new Random();
            String authKey = String.valueOf(random.nextInt(888888) + 111111);      // 범위 : 111111 ~ 999999

            redisUtil.setDataExpire(requestUser.getUserEmail(), authKey, 60 * 3l); //3분
            mailSenderRunner.sendAuthEmail(requestUser.getUserEmail(), authKey, "code");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다.");
        }
    } //이메일 인증코드 발송


    @Transactional
    public ResponseMessage authEmail(RequestUser requestUser) {

        String emailVerificationCode = redisUtil.getData(requestUser.getUserEmail());

        if (requestUser.getEmailVerificationCode().equals(emailVerificationCode)) {

            QUser qUser = QUser.user;
            String tempPassword = mailSenderRunner.getRamdomPassword(10);
            mailSenderRunner.sendAuthEmail(requestUser.getUserEmail(), tempPassword, "password");
            queryFactory
                    .update(qUser)
                    .set(qUser.userPassword, passwordEncoder.encode(tempPassword))
                    .execute();
            return ResponseMessage.builder().Message("인증이 완료되었습니다.").statusCode(200).build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 인증 코드입니다.");
        }
    }// 이메일 인증코드 검증

}
