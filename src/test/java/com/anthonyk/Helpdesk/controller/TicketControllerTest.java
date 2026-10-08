package com.anthonyk.Helpdesk.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.anthonyk.Helpdesk.dto.CreateTicketRequestDTO;
import com.anthonyk.Helpdesk.dto.UpdateTicketRequestDTO;
import com.anthonyk.Helpdesk.exception.InvalidTicketStatusTransitionException;
import com.anthonyk.Helpdesk.exception.InvalidTicketUpdateException;
import com.anthonyk.Helpdesk.exception.TicketImmutableException;
import com.anthonyk.Helpdesk.exception.TicketNotFoundException;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;
import com.anthonyk.Helpdesk.service.TicketService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

        @MockitoBean
        private TicketService ticketService;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void createTicket_shouldReturnTicketResponseDtoAndStatus() throws Exception {
                Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
                CreateTicketRequestDTO ticketRequestDTO = new CreateTicketRequestDTO("Test", "testing",
                                TicketPriority.LOW);
                when(ticketService.createTicket("Test", "testing", TicketPriority.LOW))
                                .thenReturn(ticket);

                String jsonBody = objectMapper.writeValueAsString(ticketRequestDTO);

                mockMvc.perform(
                                post("/api/tickets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title").value("Test"))
                                .andExpect(jsonPath("$.description").value("testing"))
                                .andExpect(jsonPath("$.ticketPriority").value("LOW"))
                                .andExpect(jsonPath("$.ticketStatus").value("OPEN"))
                                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                                .andExpect(jsonPath("$.resolvedAt").value(nullValue()));
        }

        @Test
        public void createTicket_shouldReturnBadRequest_whenGivenOversizedInput() throws Exception {
                String invalidTitle = "a".repeat(201);
                CreateTicketRequestDTO ticketRequestDTO = new CreateTicketRequestDTO(invalidTitle, "testing",
                                TicketPriority.LOW);
                String jsonBody = objectMapper.writeValueAsString(ticketRequestDTO);

                mockMvc.perform(
                                post("/api/tickets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Title must be 200 characters or fewer"));

                verifyNoInteractions(ticketService);
        }

        @Test
        public void createTicket_shouldReturnBadRequest_whenGivenMissingField() throws Exception {
                String jsonBody = """
                                {
                                    "title": "Test",
                                    "description": "testing",
                                    "ticketPriority": null
                                }
                                """;

                mockMvc.perform(
                                post("/api/tickets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Ticket priority is required"));

                verifyNoInteractions(ticketService);
        }

        @Test
        public void createTicket_shouldReturnBadRequest_whenGivenMalformedField() throws Exception {
                String jsonBody = """
                                {
                                    "title": "Test",
                                    "description": "testing",
                                    "ticketPriority": "INVALID"
                                }
                                """;

                mockMvc.perform(
                                post("/api/tickets")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Request contains invalid or malformed data"));

                verifyNoInteractions(ticketService);
        }

        @Test
        public void getTicketById_shouldReturnTicket() throws Exception {
                Long id = 1L;
                Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
                when(ticketService.getTicketById(id)).thenReturn(ticket);

                mockMvc.perform(
                                get("/api/tickets/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Test"))
                                .andExpect(jsonPath("$.description").value("testing"))
                                .andExpect(jsonPath("$.ticketPriority").value("LOW"))
                                .andExpect(jsonPath("$.ticketStatus").value("OPEN"))
                                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                                .andExpect(jsonPath("$.resolvedAt").value(nullValue()));
        }

        @Test
        public void getTicketById_shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
                Long id = 1L;
                when(ticketService.getTicketById(id)).thenThrow(new TicketNotFoundException("Ticket Not Found"));
                mockMvc.perform(
                                get("/api/tickets/{id}", id))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Ticket Not Found"));
        }

        @Test
        public void getAllTickets_returnsAllTickets() throws Exception {
                Ticket ticket1 = new Ticket("Test1", "testing1", TicketPriority.LOW);
                Ticket ticket2 = new Ticket("Test2", "testing2", TicketPriority.MEDIUM);
                List<Ticket> list = new ArrayList<>();
                list.add(ticket1);
                list.add(ticket2);
                when(ticketService.getAllTickets()).thenReturn(list);

                mockMvc.perform(
                                get("/api/tickets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                // Ticket1 Check
                                .andExpect(jsonPath("$[0].title").value("Test1"))
                                .andExpect(jsonPath("$[0].description").value("testing1"))
                                .andExpect(jsonPath("$[0].ticketPriority").value("LOW"))
                                .andExpect(jsonPath("$[0].ticketStatus").value("OPEN"))
                                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                                .andExpect(jsonPath("$[0].updatedAt").isNotEmpty())
                                .andExpect(jsonPath("$[0].resolvedAt").value(nullValue()))
                                // Ticket2 Check
                                .andExpect(jsonPath("$[1].title").value("Test2"))
                                .andExpect(jsonPath("$[1].description").value("testing2"))
                                .andExpect(jsonPath("$[1].ticketPriority").value("MEDIUM"))
                                .andExpect(jsonPath("$[1].ticketStatus").value("OPEN"))
                                .andExpect(jsonPath("$[1].createdAt").isNotEmpty())
                                .andExpect(jsonPath("$[1].updatedAt").isNotEmpty())
                                .andExpect(jsonPath("$[1].resolvedAt").value(nullValue()));
        }

        @Test
        public void getAllTickets_returnsEmptyList() throws Exception {
                List<Ticket> list = new ArrayList<>();
                when(ticketService.getAllTickets()).thenReturn(list);

                mockMvc.perform(
                                get("/api/tickets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        public void updateTicket_shouldUpdateTicket() throws Exception {
                Long id = 1L;
                UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO("updatedTitle", null,
                                TicketPriority.LOW, null);
                Ticket ticket = new Ticket("updatedTitle", "testing", TicketPriority.LOW);
                when(ticketService.updateTicket(id, "updatedTitle", null, TicketPriority.LOW, null)).thenReturn(ticket);

                String jsonBody = objectMapper.writeValueAsString(updateTicketRequestDTO);

                mockMvc.perform(
                                patch("/api/tickets/{id}", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("updatedTitle"))
                                .andExpect(jsonPath("$.description").value("testing"))
                                .andExpect(jsonPath("$.ticketPriority").value("LOW"))
                                .andExpect(jsonPath("$.ticketStatus").value("OPEN"))
                                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                                .andExpect(jsonPath("$.resolvedAt").value(nullValue()));

                verify(ticketService).updateTicket(id, "updatedTitle", null, TicketPriority.LOW, null);

        }

        @Test
        public void updateTicket_shouldReturnBadRequest_whenTitleIsBlank() throws Exception {
                Long id = 1L;
                String invalidTitle = "    ";
                UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(invalidTitle, null, null,
                                null);

                String jsonBody = objectMapper.writeValueAsString(updateTicketRequestDTO);

                mockMvc.perform(
                                patch("/api/tickets/{id}", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Title must not be blank"));

                verifyNoInteractions(ticketService);
        }

        @Test
        public void updateTicket_shouldReturnBadRequest_whenAllFieldsAreNull() throws Exception {
                Long id = 1L;
                UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(null, null, null, null);
                when(ticketService.updateTicket(id, null, null, null, null))
                                .thenThrow(new InvalidTicketUpdateException("At least one field must be provided"));
                String jsonBody = objectMapper.writeValueAsString(updateTicketRequestDTO);

                mockMvc.perform(
                                patch("/api/tickets/{id}", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("At least one field must be provided"));

                verify(ticketService).updateTicket(id, null, null, null, null);

        }

        @Test
        public void updateTicket_shouldReturnConflict_whenStatusTransitionIsInvalid() throws Exception {
                Long id = 1L;
                UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO("Title", null, null,
                                TicketStatus.IN_PROGRESS);

                String jsonBody = objectMapper.writeValueAsString(updateTicketRequestDTO);
                when(ticketService.updateTicket(id, "Title", null, null, TicketStatus.IN_PROGRESS)).thenThrow(
                                new InvalidTicketStatusTransitionException("Invalid ticket status transition request"));
                mockMvc.perform(
                                patch("/api/tickets/{id}", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("Invalid ticket status transition request"));

                verify(ticketService).updateTicket(id, "Title", null, null, TicketStatus.IN_PROGRESS);
        }

        @Test
        public void updateTicket_shouldReturnForbidden_whenTicketIsClosed() throws Exception {
                Long id = 1L;
                UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO("Title", null, null,
                                TicketStatus.IN_PROGRESS);

                String jsonBody = objectMapper.writeValueAsString(updateTicketRequestDTO);
                when(ticketService.updateTicket(id, "Title", null, null, TicketStatus.IN_PROGRESS)).thenThrow(
                                new TicketImmutableException("Ticket is closed, unable to modify"));
                mockMvc.perform(
                                patch("/api/tickets/{id}", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonBody))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.message").value("Ticket is closed, unable to modify"));

                verify(ticketService).updateTicket(id, "Title", null, null, TicketStatus.IN_PROGRESS);
        }

        @Test
        public void deleteById_shouldDeleteTicket() throws Exception {
                Long id = 1L;

                mockMvc.perform(
                                delete("/api/tickets/{id}", id))
                                .andExpect(status().isNoContent());

                verify(ticketService).deleteTicketById(id);
        }

        @Test
        public void deleteById_shouldThrowTicketNotFoundException() throws Exception {
                Long id = 1L;
                doThrow(new TicketNotFoundException("Ticket Not Found"))
                                .when(ticketService).deleteTicketById(id);

                mockMvc.perform(
                                delete("/api/tickets/{id}", id))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Ticket Not Found"));

                verify(ticketService).deleteTicketById(id);
        }

        @Test
        public void getTicketById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() throws Exception {
                Long id = 1L;

                when(ticketService.getTicketById(id))
                        .thenThrow(new RuntimeException("Sensitive internal details"));

                mockMvc.perform(
                        get("/api/tickets/{id}", id))
                        .andExpect(status().isInternalServerError())
                        .andExpect(jsonPath("$.message").value("An unexpected error occurred"));

                verify(ticketService).getTicketById(id);
                
                }
}
