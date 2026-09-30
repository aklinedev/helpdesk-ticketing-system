package com.anthonyk.Helpdesk.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anthonyk.Helpdesk.exception.InvalidTicketUpdateException;
import com.anthonyk.Helpdesk.exception.TicketNotFoundException;
import com.anthonyk.Helpdesk.model.Ticket;
import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;
import com.anthonyk.Helpdesk.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket_shouldSaveTicket() {

        ticketService.createTicket("Test", "testing", TicketPriority.LOW);
        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        Ticket capturedTicket = captor.getValue();

        assertNotNull(capturedTicket);
        assertEquals("Test", capturedTicket.getTitle());
        assertEquals("testing", capturedTicket.getDescription());
        assertEquals(TicketPriority.LOW, capturedTicket.getTicketPriority());
        assertEquals(TicketStatus.OPEN, capturedTicket.getTicketStatus());

    }

    @Test
    void getTicketById_shouldReturnTicketWhenFound() {
        Long id = 1L;
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        Ticket returnedTicket = ticketService.getTicketById(id);
        verify(ticketRepository).findById(id);

        assertEquals(ticket, returnedTicket);

    }

    @Test
    void getTicketById_shouldThrowTicketNotFoundException_whenMissing() {
        Long id = 1L;
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> {
                    ticketService.getTicketById(id);
                });
        verify(ticketRepository, times(1)).findById(any());

    }

    @Test
    void updateTicket_shouldUpdateOnlyProvidedFields() {

        Long id = 1L;
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        ticketService.updateTicket(id, "updated_title", null, null, null);
        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        Ticket capturedTicket = captor.getValue();

        assertEquals("updated_title", capturedTicket.getTitle());
        assertEquals("testing", capturedTicket.getDescription());
        assertEquals(TicketPriority.LOW, capturedTicket.getTicketPriority());
        assertEquals(TicketStatus.OPEN, capturedTicket.getTicketStatus());

    }

    @Test
    void updateTicket_shouldRejectAllNullFields() {

        Long id = 1L;

        assertThrows(InvalidTicketUpdateException.class,
                () -> {
                    ticketService.updateTicket(id, null, null, null, null);
                });

        verifyNoInteractions(ticketRepository);
    }

    @Test
    void updateTicket_shouldRejectBlankTitle() {

        Long id = 1L;
        String invalidTitle = "    ";
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidTicketUpdateException.class,
                () -> {
                    ticketService.updateTicket(id, invalidTitle, null, null, null);
                });
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void updateTicket_shouldRejectBlankDescription() {

        Long id = 1L;
        String invalidDescription = "    ";
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidTicketUpdateException.class,
                () -> {
                    ticketService.updateTicket(id, null, invalidDescription, null, null);
                });
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void deleteTicketById_shouldDeleteExistingTicket() {

        Long id = 1L;
        Ticket ticket = new Ticket("Test", "testing", TicketPriority.LOW);
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicketById(id);

        InOrder inOrder = inOrder(ticketRepository);
        inOrder.verify(ticketRepository).findById(id);
        inOrder.verify(ticketRepository).deleteById(id);
    }

    @Test
    void deleteTicketById_shouldThrowWhenTicketMissing() {

        Long id = 1L;
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class,
                () -> {
                    ticketService.deleteTicketById(id);
                });
        verify(ticketRepository, never()).deleteById(any());
    }

    @Test
    void getAllTickets_shouldReturnAllTickets() {

        Ticket ticket1 = new Ticket("Test1", "testing1", TicketPriority.LOW);
        Ticket ticket2 = new Ticket("Test2", "testing2", TicketPriority.MEDIUM);
        List<Ticket> list = new ArrayList<>();
        list.add(ticket1);
        list.add(ticket2);

        when(ticketRepository.findAll()).thenReturn(list);
        List<Ticket> results = ticketService.getAllTickets();

        assertThat(results).containsExactlyElementsOf(list);

    }

}
