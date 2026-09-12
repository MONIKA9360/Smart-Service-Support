package com.example.support.repository;

import com.example.support.entity.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {

    List<TicketAttachment> findByTicket_Id(Long ticketId);

    List<TicketAttachment> findByUploadedBy_Id(Long userId);
}
