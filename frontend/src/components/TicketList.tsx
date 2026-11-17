import type { ApiError } from "../api/api-error";
import type { Ticket } from "../types/ticket";
import TicketCard from "./TicketCard";

interface TicketListProps {
    tickets: Ticket[]
    loading: boolean
    error: ApiError | null
}
export function TicketList({
    tickets,
    loading,
    error
}: TicketListProps) {

    if (loading == true ) {
        return (
            <h3>
                Loading...
            </h3>
        );
    }

    if (error != null) {
       return (
        <header>
            <h2>Error Status: {error.status}</h2>
            <h2>{error.message}</h2>
        </header>
       );
    }

    if (tickets.length === 0) {
        return (
            <h2>No tickets to display</h2>
        );
    }

    return ( 
    <div>
        {tickets.map((item) => (
            <TicketCard
                key = {item.id}
                ticket = {item}
                />
            ))}
    </div>
    );

}