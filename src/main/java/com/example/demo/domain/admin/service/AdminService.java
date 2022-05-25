package com.example.demo.domain.admin.service;


import com.example.demo.domain.user.entity.QUser;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {



    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;



    public List<User> findAllUserList(Integer pageNum, Integer pageSize) {


        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        QUser qUser = QUser.user;
        List<User> userList = queryFactory
                .selectFrom(qUser)
                .where(qUser.userAuthority.notEqualsIgnoreCase("ROLE_ADMIN"))
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

        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");


        return user;
    }//회원 조회
}
