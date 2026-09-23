package com.anthonyk.Helpdesk.dto;

import com.anthonyk.Helpdesk.model.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequestDTO(

    @NotBlank(message = "Title must not be blank") String title,
    @NotBlank(message = "Ticket description is required") String description,
    @NotNull(message = "Ticket priority is required") TicketPriority ticketPriority

){}
