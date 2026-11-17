
export const TICKET_PRIORITIES = ["LOW",  "MEDIUM",  "HIGH", "URGENT" ] as const
export const TICKET_STATUSES = ["OPEN",  "IN_PROGRESS",  "RESOLVED", "CLOSED" ] as const

export type TicketPriority = typeof TICKET_PRIORITIES[number]
export type TicketStatus = typeof TICKET_STATUSES[number]

export interface Ticket {
    id: number
    title: string
    description: string
    ticketPriority: TicketPriority
    ticketStatus: TicketStatus
    createdAt: string
    updatedAt: string
    resolvedAt: string | null


}