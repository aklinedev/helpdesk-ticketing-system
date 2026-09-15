package com.anthonyk.Helpdesk.dto;

import com.anthonyk.Helpdesk.model.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequestDTO(

    @NotBlank String title,
    @NotBlank String description,
    @NotNull TicketPriority ticketPriority

){}
