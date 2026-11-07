import type { Ticket } from "../types/Ticket";

interface TicketCardProps {
    ticket: Ticket
}


function TicketCard( {ticket}: TicketCardProps ){

    return(
        <header>
            <h1>{ticket.title} Id: {ticket.id}</h1>
            <h2>Description: {ticket.description}</h2>
            <h2>Priority: {ticket.ticketPriority}</h2>
            <h2>Status: {ticket.ticketStatus}</h2>
            <h2>Ticket created at {ticket.createdAt}</h2>
            <h2>Last updated: {ticket.updatedAt}</h2>
            <h2>Resolved: {ticket.resolvedAt}</h2>
        </header>
    );
}

export default TicketCard