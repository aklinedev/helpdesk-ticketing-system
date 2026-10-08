package com.anthonyk.Helpdesk.dto;

import com.anthonyk.Helpdesk.model.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequestDTO(

        @NotBlank(message = "Title must not be blank") @Size(max = 200, message = "Title must be 200 characters or fewer") String title,
        @NotBlank(message = "Ticket description is required") @Size(max = 200, message = "Description must be 200 characters or fewer") String description,
        @NotNull(message = "Ticket priority is required") TicketPriority ticketPriority

) {
}
