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
import com.anthonyk.Helpdesk.docs.ApiBadRequest;
import com.anthonyk.Helpdesk.docs.ApiConflict;
import com.anthonyk.Helpdesk.docs.ApiForbidden;
import com.anthonyk.Helpdesk.docs.ApiNotFound;
import com.anthonyk.Helpdesk.docs.ApiServerError;
import com.anthonyk.Helpdesk.dto.CreateTicketRequestDTO;
import com.anthonyk.Helpdesk.dto.TicketResponseDTO;
import com.anthonyk.Helpdesk.mapper.TicketMapper;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;
import com.anthonyk.Helpdesk.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Create, view, update, and delete help desk tickets")
public class TicketController {

        private final TicketService ticketService;

        public TicketController(TicketService ticketService) {
                this.ticketService = ticketService;
        }

        @PostMapping
        @Operation(summary = "Create a help desk ticket", description = "Creates a new help desk ticket. New tickets are initialized with status OPEN.", responses = {
                        @ApiResponse(responseCode = "201", description = "Ticket created successfully")
        })
        @ApiBadRequest 
        @ApiServerError 
        public ResponseEntity<TicketResponseDTO> createTicket(
                        @Valid @RequestBody CreateTicketRequestDTO createTicketRequestDTO) {
                Ticket createdTicket = ticketService.createTicket(
                                createTicketRequestDTO.title(),
                                createTicketRequestDTO.description(),
                                createTicketRequestDTO.ticketPriority());
                return new ResponseEntity<>(TicketMapper.mapToResponseDTO(createdTicket), HttpStatus.CREATED);
        }

        @GetMapping
        @Operation(summary = "Get all help desk tickets", description = "Returns all help desk tickets currently available.", responses = {
                        @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
        })
        @ApiServerError 
        public List<TicketResponseDTO> getAllTickets() {
                List<Ticket> allTickets = ticketService.getAllTickets();

                List<TicketResponseDTO> ticketResponseDTOs = allTickets.stream()
                                .map(TicketMapper::mapToResponseDTO)
                                .toList();

                return ticketResponseDTOs;
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get ticket by ID", description = "Returns a help desk ticket using its unique ID.", responses = {
                        @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully")
        })
        @ApiNotFound 
        @ApiServerError 
        public TicketResponseDTO getTicketById(
                        @Parameter(description = "Unique ID of the ticket to retrieve", example = "1") @PathVariable Long id) {
                Ticket ticket = ticketService.getTicketById(id);
                return TicketMapper.mapToResponseDTO(ticket);
        }

        @PatchMapping("/{id}")
        @Operation(summary = "Update a help desk ticket", description = """
                        Updates one or more fields on an existing help desk ticket.
                        Fields that are omitted are left unchanged.
                        Ticket status changes must follow the allowed status transition rules.
                        Closed tickets cannot be modified.
                        """, responses = {
                        @ApiResponse(responseCode = "200", description = "Ticket updated successfully")
        })
        @ApiBadRequest 
        @ApiForbidden 
        @ApiNotFound 
        @ApiConflict 
        @ApiServerError 
        public TicketResponseDTO updateTicket(
                        @Parameter(description = "Unique ID of the ticket to update", example = "1") @PathVariable Long id,
                        @Valid @RequestBody UpdateTicketRequestDTO updateTicketRequestDTO) {

                String title = updateTicketRequestDTO.title();
                String description = updateTicketRequestDTO.description();
                TicketPriority ticketPriority = updateTicketRequestDTO.ticketPriority();
                TicketStatus ticketStatus = updateTicketRequestDTO.ticketStatus();

                Ticket ticket = ticketService.updateTicket(id, title, description, ticketPriority, ticketStatus);

                return TicketMapper.mapToResponseDTO(ticket);

        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete a help desk ticket", description = "Deletes an existing help desk ticket using its unique ID.", responses = {
                        @ApiResponse(responseCode = "204", description = "Ticket deleted successfully")
        })
        @ApiNotFound
        @ApiServerError
        public ResponseEntity<Void> deleteTicketById(
                        @Parameter(description = "Unique ID of the ticket to delete", example = "1") @PathVariable Long id) {
                ticketService.deleteTicketById(id);
                return ResponseEntity.noContent().build();
        }

}