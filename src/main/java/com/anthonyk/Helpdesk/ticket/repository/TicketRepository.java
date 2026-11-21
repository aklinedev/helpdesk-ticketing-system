package com.anthonyk.Helpdesk.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonyk.Helpdesk.ticket.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
