package com.anthonyk.Helpdesk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anthonyk.Helpdesk.exception.InvalidTicketUpdateException;
import com.anthonyk.Helpdesk.exception.TicketNotFoundException;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;
import com.anthonyk.Helpdesk.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // Helpers -----------------------------------------------

    private Ticket getTicketOrThrow(long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket Not Found"));
    }
    // Create Ticket -----------------------------------------------

    public Ticket createTicket(String title, String description, TicketPriority priority) {
        Ticket ticket = new Ticket(title, description, priority);
        return ticketRepository.save(ticket);
    }

    // Update Ticket -----------------------------------------------

    public Ticket updateTicket(
            long id,
            String title,
            String description,
            TicketPriority ticketPriority,
            TicketStatus ticketStatus) {

        if (title == null
                && description == null
                && ticketPriority == null
                && ticketStatus == null) {

            throw new InvalidTicketUpdateException(
                    "At least one field must be provided");
        }

        Ticket ticket = getTicketOrThrow(id);

        if (title != null) {
            ticket.setTitle(title.trim());
        }

        if (description != null) {
            ticket.setDescription(description.trim());
        }

        if (ticketPriority != null) {
            ticket.setTicketPriority(ticketPriority);
        }

        if (ticketStatus != null) {
            ticket.setTicketStatus(ticketStatus);
        }

        return ticketRepository.save(ticket);
    }

    // Delete Ticket -----------------------------------------------

    public void deleteTicketById(long id) {
        getTicketOrThrow(id);
        ticketRepository.deleteById(id);
    }

    // Retrieval Methods -------------------------------------------

    public long getTicketCount() {
        return ticketRepository.count();
    }

    public Ticket getTicketById(long id) {
        return getTicketOrThrow(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}
