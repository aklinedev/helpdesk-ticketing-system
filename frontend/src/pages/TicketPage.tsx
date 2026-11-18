import { createTicket, deleteTicket, updateTicket } from "../api/tickets"
import { TicketForm } from "../components/TicketForm"
import type { CreateTicketRequest } from "../types/requests/create-ticket"
import { useTickets } from "../hooks/use-tickets"
import { TicketList } from "../components/TicketList"
import { useState } from "react"
import type { Ticket } from "../types/ticket"
import type { UpdateTicketRequest } from "../types/requests/update-ticket"
import { ApiError } from "../api/api-error"

export function TicketsPage() {

    const [editingTicket, setEditingTicket] = useState<Ticket | null> (null)
    const [curDeleteTicket, setCurDeleteTicket] = useState<Ticket | null> (null)
    const [deleteError, setDeleteError] = useState<string | null> (null)
    
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

    async function handleDelete(ticket: Ticket) {
    try {
        await deleteTicket(ticket.id)
        setCurDeleteTicket(null)

    } catch (error) {
        if (error instanceof ApiError) {
            setDeleteError(error.message)
        } else {
            setDeleteError("Unexpected Error, please try again.")
        }
    }
}

    function handleEdit(ticket: Ticket) {
        setCurDeleteTicket(null)
        setDeleteError(null)
        setEditingTicket(ticket)
    }
    function handleDeleteValue(ticket: Ticket) {
        setEditingTicket(null)
        setDeleteError(null)
        setCurDeleteTicket(ticket)
    }

    return (
        <>
            <TicketForm
                mode="create"
                onSubmit={handleCreate}
            />

            {curDeleteTicket && (
                <div>
                    Delete ticket #{curDeleteTicket.id}?
                    <button onClick={() => handleDelete(curDeleteTicket)}>
                    Confirm
                    </button>
                    <button
                        onClick={() => {
                            setCurDeleteTicket(null) 
                            setDeleteError(null)
                        }}>
                        Cancel
                    </button>
                </div>
            )}
            
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

            {deleteError && (
                <p>{deleteError}</p>
            )}
            
            {(hasLoaded || loading || error) && (
            <TicketList
                tickets={tickets}
                loading={loading}
                error={error}
                onEdit={handleEdit}
                onDelete={handleDeleteValue}
            /> )}
        </>
    )
}