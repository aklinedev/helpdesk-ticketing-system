package com.anthonyk.Helpdesk.dto;

import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;

import jakarta.annotation.Nullable;

public record UpdateTicketRequestDTO(

        @Nullable String title,
        @Nullable String description,
        @Nullable TicketPriority ticketPriority,
        @Nullable TicketStatus ticketStatus

) {
}
