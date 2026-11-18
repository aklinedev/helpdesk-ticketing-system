import { createTicket, updateTicket } from "../api/tickets"
import { TicketForm } from "../components/TicketForm"
import type { CreateTicketRequest } from "../types/requests/create-ticket"
import { useTickets } from "../hooks/use-tickets"
import { TicketList } from "../components/TicketList"
import { Component, useState } from "react"
import type { Ticket } from "../types/ticket"
import type { UpdateTicketRequest } from "../types/requests/update-ticket"

export function TicketsPage() {

    const [editingTicket, setEditingTicket] = useState<Ticket | null> (null)

    const {
        tickets,
        loading,
        error,
        hasLoaded,
        loadTickets
    } = useTickets()

    async function handleCreate(request: CreateTicketRequest) {
        await createTicket(request)
    }

    async function handleUpdate(request: UpdateTicketRequest) {
        if (editingTicket === null) {
            return
        }
        await updateTicket(editingTicket.id, request)
        setEditingTicket(null)
    }

    function handleEdit(ticket: Ticket) {
        setEditingTicket(ticket)
    }

    return (
        <>
            <TicketForm
                mode="create"
                onSubmit={handleCreate}
            />

            {editingTicket && (
                <TicketForm
                key={editingTicket.id}
                mode="update"
                ticket={editingTicket}
                onSubmit={handleUpdate}
                /> )}

            <button onClick={loadTickets}>
                {hasLoaded ? "Refresh Tickets" : "Get All Tickets"}
            </button>
            
            {(hasLoaded || loading || error) && (
            <TicketList
                tickets={tickets}
                loading={loading}
                error={error}
                onEdit={handleEdit}
            /> )}
        </>
    )
}