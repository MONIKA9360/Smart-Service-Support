package com.example.support.repository;

import com.example.support.entity.Ticket;
import com.example.support.entity.enums.TicketPriority;
import com.example.support.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    boolean existsByTicketNumber(String ticketNumber);

    List<Ticket> findByCustomer_Id(Long customerId);

    List<Ticket> findByAssignedEmployee_Id(Long employeeId);

    List<Ticket> findByService_Id(Long serviceId);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    List<Ticket> findByCustomer_IdAndStatus(Long customerId, TicketStatus status);

    List<Ticket> findByAssignedEmployee_IdAndStatus(Long employeeId, TicketStatus status);

    long countByStatus(TicketStatus status);
}
