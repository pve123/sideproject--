package com.example.demo.user.service;

import com.example.demo.jwt.TokenProvider;
import com.example.demo.user.entity.QUser;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.request.RequestUser;
import com.example.demo.user.entity.response.ResponseUser;
import com.example.demo.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    private final PasswordEncoder passwordEncoder;

    public ResponseUser addUserInfo(RequestUser requestUser) {

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


    public List<User> findAllUserList(Integer pageNum, Integer pageSize) {


        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        QUser qUser = QUser.user;
        List<User> userList = queryFactory
                .selectFrom(qUser)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return userList;
    }//회원 목록


    public User findByUserInfo(Long id) {


        QUser qUser = QUser.user;

        User user = queryFactory.selectFrom(qUser)
                .where(qUser.userId.eq(id))
                .fetchOne();

        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원번호입니다.");


        return user;
    }//회원 조회


}
