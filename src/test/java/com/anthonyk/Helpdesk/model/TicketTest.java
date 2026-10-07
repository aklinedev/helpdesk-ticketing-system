package com.anthonyk.Helpdesk.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.anthonyk.Helpdesk.repository.TicketRepository;

@DataJpaTest
public class TicketTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void updatedAtPersist_whenUpdatingTicket() {
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        ticketRepository.saveAndFlush(ticket);
        LocalDateTime previousUpdatedAt = ticket.getUpdatedAt();

        ticket.setTitle("newTitle");
        ticketRepository.saveAndFlush(ticket);

        assertThat(ticket.getUpdatedAt()).isAfter(previousUpdatedAt);

    }

    @Test 
    public void updatedAtRemainsUnchanged_whenNoFieldsActuallyChange() {
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        ticketRepository.saveAndFlush(ticket);
        LocalDateTime previousUpdatedAt = ticket.getUpdatedAt();

        ticket.setTicketStatus(TicketStatus.OPEN);
        ticketRepository.saveAndFlush(ticket);

        assertThat(ticket.getUpdatedAt()).isEqualTo(previousUpdatedAt);
    }
}
