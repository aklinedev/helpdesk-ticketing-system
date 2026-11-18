import type { Ticket } from "../types/ticket";

interface TicketCardProps {
    ticket: Ticket
    onEdit: (ticket: Ticket) => void
    onDelete: (ticket: Ticket) => void
}  

function TicketCard({ ticket, onEdit, onDelete }: TicketCardProps) {
    return (
        <article className="ticket-card">
            <h2>
                {ticket.title} — #{ticket.id}
            </h2>

            <p>
                <strong>Description:</strong> {ticket.description}
            </p>

            <p>
                <strong>Priority:</strong> {ticket.ticketPriority}
            </p>

            <p>
                <strong>Status:</strong> {ticket.ticketStatus}
            </p>

            <p>
                <strong>Created:</strong> {ticket.createdAt}
            </p>

            <p>
                <strong>Last Updated:</strong> {ticket.updatedAt}
            </p>

            <p>
                <strong>Resolved:</strong> {ticket.resolvedAt ?? "-"}
            </p>

            <div className="ticket-card-actions">
                <button
                    className="button button-primary"
                    onClick={() => onEdit(ticket)}
                >
                    Edit
                </button>

                <button
                    className="button button-danger"
                    onClick={() => onDelete(ticket)}
                >
                    Delete
                </button>
            </div>
        </article>
    )
}

export default TicketCard