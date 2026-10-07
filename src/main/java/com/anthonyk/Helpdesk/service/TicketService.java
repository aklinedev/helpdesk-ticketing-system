package com.anthonyk.Helpdesk.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.anthonyk.Helpdesk.exception.InvalidTicketStatusTransitionException;
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

    private static final EnumMap<TicketStatus, Set<TicketStatus>> ALLOWED_STATUS_TRANSITIONS = new EnumMap<>(
            TicketStatus.class);

    static {

        ALLOWED_STATUS_TRANSITIONS.put(TicketStatus.OPEN,
                Set.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
        ALLOWED_STATUS_TRANSITIONS.put(TicketStatus.IN_PROGRESS,
                Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED));
        ALLOWED_STATUS_TRANSITIONS.put(TicketStatus.RESOLVED,
                Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED, TicketStatus.CLOSED));
        ALLOWED_STATUS_TRANSITIONS.put(TicketStatus.CLOSED,
                Set.of(TicketStatus.CLOSED));

    }

    // Helpers -----------------------------------------------

    private Ticket getTicketOrThrow(long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket Not Found"));
    }

    private void checkValidStatusTransition(TicketStatus currentStatus, TicketStatus updateStatusRequest) {
        boolean isAllowed = ALLOWED_STATUS_TRANSITIONS
                .getOrDefault(currentStatus, Set.of())
                .contains(updateStatusRequest);

        if (!isAllowed) {
            throw new InvalidTicketStatusTransitionException(
                    "Invalid ticket status transition request");
        }
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

        if (ticketStatus != null) {
            
            TicketStatus currentTicketStatus = ticket.getTicketStatus();

            checkValidStatusTransition(currentTicketStatus, ticketStatus);

            if (currentTicketStatus == TicketStatus.IN_PROGRESS && ticketStatus == TicketStatus.RESOLVED) {
                ticket.markResolved();
            }
            if (currentTicketStatus == TicketStatus.RESOLVED && ticketStatus == TicketStatus.IN_PROGRESS) {
                ticket.clearResolvedAt();
            }

            ticket.setTicketStatus(ticketStatus);
        }

        if (title != null) {
            ticket.setTitle(title.trim());
        }

        if (description != null) {
            ticket.setDescription(description.trim());
        }

        if (ticketPriority != null) {
            ticket.setTicketPriority(ticketPriority);
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
