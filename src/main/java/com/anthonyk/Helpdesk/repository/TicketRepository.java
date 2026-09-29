package com.anthonyk.Helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anthonyk.Helpdesk.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
