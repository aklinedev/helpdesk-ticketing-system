package com.anthonyk.Helpdesk.mapper;

import com.anthonyk.Helpdesk.dto.TicketResponseDTO;
import com.anthonyk.Helpdesk.model.Ticket;

public class TicketMapper {

    public static TicketResponseDTO mapToResponseDTO(Ticket ticket) {
        return new TicketResponseDTO(
            ticket.getId(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getTicketPriority(),
            ticket.getTicketStatus(),
            ticket.getCreatedAt()
        );
    }

    
    
}
