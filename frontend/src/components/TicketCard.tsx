import type { Ticket } from "../types/ticket";

interface TicketCardProps {
    ticket: Ticket
    onEdit: (ticket: Ticket) => void
    onDelete: (ticket: Ticket) => void
}  

function TicketCard( {ticket, onEdit, onDelete}: TicketCardProps ){

    return(
        <header>
            <h1>{ticket.title} Id: {ticket.id}</h1>
            <h2>Description: {ticket.description}</h2>
            <h2>Priority: {ticket.ticketPriority}</h2>
            <h2>Status: {ticket.ticketStatus}</h2>
            <h2>Ticket created at {ticket.createdAt}</h2>
            <h2>Last updated: {ticket.updatedAt}</h2>
            <h2>Resolved: {ticket.resolvedAt}</h2>
            <button onClick={() => onEdit(ticket)}>
                Edit
            </button>
            <button onClick={() => onDelete(ticket)}>
                Delete
            </button>
        </header>
    );
}

export default TicketCard