package com.anthonyk.Helpdesk.ticket.dto;

import java.time.LocalDateTime;

import com.anthonyk.Helpdesk.ticket.model.TicketPriority;
import com.anthonyk.Helpdesk.ticket.model.TicketStatus;

public record TicketResponseDTO(

        Long id,
        String title,
        String description,
        TicketPriority ticketPriority,
        TicketStatus ticketStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime resolvedAt

) {

}
