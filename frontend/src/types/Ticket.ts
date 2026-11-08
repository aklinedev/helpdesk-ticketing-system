
type TicketPriority = "LOW" | "MEDIUM" | "HIGH" | "URGENT"
type TicketStatus = "OPEN" | "IN_PROGRESS" | "RESOLVED" | "CLOSED"

interface Ticket {
    id: number
    title: string
    description: string
    ticketPriority: TicketPriority
    ticketStatus: TicketStatus
    createdAt: string
    updatedAt: string
    resolvedAt: string | null


} 

export type {Ticket, TicketPriority, TicketStatus}