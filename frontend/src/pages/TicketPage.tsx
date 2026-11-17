import { createTicket } from "../api/tickets"
import { TicketForm } from "../components/TicketForm"
import type { CreateTicketRequest } from "../types/requests/create-ticket"
import { useTickets } from "../hooks/use-tickets"
import { TicketList } from "../components/TicketList"

export function TicketsPage() {
    const {
        tickets,
        loading,
        error,
        loadTickets
    } = useTickets()

    async function handleCreate(request: CreateTicketRequest) {
        await createTicket(request)
    }

    return (
        <>
            <TicketForm
                mode="create"
                onSubmit={handleCreate}
            />

            <button onClick={loadTickets}>
                Get All Tickets
            </button>

            <TicketList
                tickets={tickets}
                loading={loading}
                error={error}
            />
        </>
    )
}