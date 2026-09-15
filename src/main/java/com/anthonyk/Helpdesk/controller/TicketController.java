package com.anthonyk.Helpdesk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthonyk.Helpdesk.dto.CreateTicketRequestDTO;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets") 
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
    @PostMapping
        public ResponseEntity<Ticket> createTicket(@Valid @RequestBody CreateTicketRequestDTO createTicketRequestDTO) {
            Ticket createdTicket = ticketService.createTicket(createTicketRequestDTO.title(),
                                        createTicketRequestDTO.description(), 
                                        createTicketRequestDTO.ticketPriority());
            return new ResponseEntity<> (createdTicket, HttpStatus.CREATED);
        }
}
