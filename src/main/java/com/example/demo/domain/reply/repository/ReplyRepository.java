package com.example.demo.domain.reply.repository;

import com.example.demo.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {



    List<Reply> findByReplyGroupAndReplyType(Integer replyGroup, Integer replyType);
}
