package com.anthonyk.Helpdesk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthonyk.Helpdesk.dto.UpdateTicketRequestDTO;
import com.anthonyk.Helpdesk.dto.CreateTicketRequestDTO;
import com.anthonyk.Helpdesk.dto.TicketResponseDTO;
import com.anthonyk.Helpdesk.mapper.TicketMapper;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;
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
    public ResponseEntity<TicketResponseDTO> createTicket(
            @Valid @RequestBody CreateTicketRequestDTO createTicketRequestDTO) {
        Ticket createdTicket = ticketService.createTicket(
                createTicketRequestDTO.title(),
                createTicketRequestDTO.description(),
                createTicketRequestDTO.ticketPriority());
        return new ResponseEntity<>(TicketMapper.mapToResponseDTO(createdTicket), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TicketResponseDTO> getAllTickets() {
        List<Ticket> allTickets = ticketService.getAllTickets();

        List<TicketResponseDTO> ticketResponseDTOs = allTickets.stream()
                .map(TicketMapper::mapToResponseDTO)
                .toList();

        return ticketResponseDTOs;
    }

    @GetMapping("/{id}")
    public TicketResponseDTO getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return TicketMapper.mapToResponseDTO(ticket);
    }

    @PatchMapping("/{id}")
    public TicketResponseDTO updateTicket(@PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequestDTO updateTicketRequestDTO) {

        String title = updateTicketRequestDTO.title();
        String description = updateTicketRequestDTO.description();
        TicketPriority ticketPriority = updateTicketRequestDTO.ticketPriority();
        TicketStatus ticketStatus = updateTicketRequestDTO.ticketStatus();

        Ticket ticket = ticketService.updateTicket(id, title, description, ticketPriority, ticketStatus);

        return TicketMapper.mapToResponseDTO(ticket);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketById(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
        return ResponseEntity.noContent().build();
    }

}