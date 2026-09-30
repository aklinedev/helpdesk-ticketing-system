package com.anthonyk.Helpdesk.dto;

import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequestDTO(

                @Pattern(regexp = ".*\\S.*", message = "Title must not be blank") @Size(max = 200, message = "Title must be 200 characters or fewer") String title,
                @Pattern(regexp = ".*\\S.*", message = "Description must not be blank") @Size(max = 200, message = "Description must be 200 characters or fewer") String description,
                TicketPriority ticketPriority,
                TicketStatus ticketStatus

) {
}
