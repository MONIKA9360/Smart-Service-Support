package com.example.support.repository;

import com.example.support.entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    List<TicketComment> findByTicket_IdOrderByCreatedAtAsc(Long ticketId);

    List<TicketComment> findByUser_Id(Long userId);
}
