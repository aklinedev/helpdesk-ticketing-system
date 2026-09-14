package com.anthonyk.Helpdesk.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity 
@Table(name = "tickets")
public class Ticket {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority ticketPriority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;

    protected Ticket(){
    }

    public Ticket(String title, String description, TicketPriority ticketPriority) {
        this.title = title;
        this.description = description;
        this.ticketPriority = ticketPriority;
        this.ticketStatus = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    // Setters

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setTicketPriority(TicketPriority newTicketPriority) {
        this.ticketPriority = newTicketPriority;
    }

    public void setTicketStatus(TicketStatus newTicketStatus) {
        this.ticketStatus = newTicketStatus;
    }

    // Getters

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketPriority getTicketPriority() {
        return ticketPriority;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
