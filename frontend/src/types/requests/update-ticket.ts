import type { TicketPriority, TicketStatus } from "../ticket"

export interface UpdateTicketRequest {
    title?: string
    description?: string
    ticketPriority?: TicketPriority
    ticketStatus?: TicketStatus
}