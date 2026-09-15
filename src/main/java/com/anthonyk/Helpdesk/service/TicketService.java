package com.anthonyk.Helpdesk.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    private Ticket getTicketOrThrow(long id){ 
        return ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException("Ticket Not Found") );
    }
    // Create Ticket -----------------------------------------------

    public Ticket createTicket(String title, String description, TicketPriority priority) {
        Ticket ticket = new Ticket(title, description, priority);
        return ticketRepository.save(ticket);
    }

    // Update Ticket -----------------------------------------------


    public Ticket updateTicketTitle(long id, String title) {
        Ticket ticket = getTicketOrThrow(id);
        ticket.setTitle(title);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketDescription(long id, String description) {
        Ticket ticket = getTicketOrThrow(id);
        ticket.setDescription(description);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketPriority(long id, TicketPriority ticketPriority) {
        Ticket ticket = getTicketOrThrow(id);
        ticket.setTicketPriority(ticketPriority);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicketStatus(long id, TicketStatus ticketStatus) {
        Ticket ticket = getTicketOrThrow(id);
        ticket.setTicketStatus(ticketStatus);
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
