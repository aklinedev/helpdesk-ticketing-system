import type { CreateTicketRequest } from "../types/requests/create-ticket";
import type { UpdateTicketRequest } from "../types/requests/update-ticket";
import type { Ticket } from "../types/ticket";
import { apiRequest } from "./client";

export const getTickets = () => apiRequest<Ticket[]>("/tickets")

export const getTicket = (id: number) => apiRequest<Ticket>(`/tickets/${id}`)

export const createTicket = (request: CreateTicketRequest) => {
    
    return apiRequest<Ticket>(
        "/tickets",
    {
        method: "POST",
        body: JSON.stringify(request)
    }
)}

export const deleteTicket = (id: number) =>

    apiRequest<void>(
        `/tickets/${id}`,
        {
            method: "DELETE"
        }
)

export const updateTicket = (id: number, request: UpdateTicketRequest) => {

    return apiRequest<Ticket>(
        `/tickets/${id}`,
        {
            method: "PATCH",
            body: JSON.stringify(request)
        }
    )
}