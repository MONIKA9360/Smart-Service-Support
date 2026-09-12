package com.example.support.repository;

import com.example.support.entity.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {

    List<TicketStatusHistory> findByTicket_IdOrderByCreatedAtAsc(Long ticketId);

    List<TicketStatusHistory> findByChangedBy_Id(Long userId);
}
