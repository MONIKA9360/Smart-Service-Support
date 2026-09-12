package com.example.support.repository;

import com.example.support.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByTicket_Id(Long ticketId);

    List<Feedback> findByCustomer_Id(Long customerId);

    boolean existsByTicket_Id(Long ticketId);
}
