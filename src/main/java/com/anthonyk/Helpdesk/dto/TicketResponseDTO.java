package com.anthonyk.Helpdesk.dto;

import java.time.LocalDateTime;

import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;

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
