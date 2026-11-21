package com.anthonyk.Helpdesk.ticket.mapper;

import com.anthonyk.Helpdesk.ticket.dto.TicketResponseDTO;
import com.anthonyk.Helpdesk.ticket.model.Ticket;

public class TicketMapper {

    public static TicketResponseDTO mapToResponseDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketPriority(),
                ticket.getTicketStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getResolvedAt());
    }

}
