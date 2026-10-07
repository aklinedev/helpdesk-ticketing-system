package com.anthonyk.Helpdesk.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.anthonyk.Helpdesk.dto.TicketResponseDTO;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;

public class TicketMapperTest {

    
    @Test 
    void mapToResponseDTO_shouldReturnResponseDTO() {
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        ticket.setTicketStatus(TicketStatus.RESOLVED);
        ticket.markResolved();
        TicketResponseDTO result = TicketMapper.mapToResponseDTO(ticket);

        // id requires persistence to populate, not covered here.
        assertEquals(ticket.getTitle(), result.title());
        assertEquals(ticket.getDescription(), result.description());
        assertEquals(ticket.getTicketPriority(), result.ticketPriority());
        assertEquals(ticket.getTicketStatus(), result.ticketStatus());
        assertEquals(ticket.getCreatedAt(), result.createdAt());
        assertEquals(ticket.getUpdatedAt(), result.updatedAt());
        assertEquals(ticket.getResolvedAt(), result.resolvedAt());
    }
}
