import { useState } from "react"
import { TICKET_PRIORITIES, TICKET_STATUSES, type Ticket, type TicketPriority, type TicketStatus } from "../types/ticket"
import type { CreateTicketRequest } from "../types/requests/create-ticket"
import type { UpdateTicketRequest } from "../types/requests/update-ticket"

type CreateFormProps = {
    mode: "create"
    ticket?: never
}

type UpdateFormProps = {
    mode: "update"
    ticket: Ticket
}

type TicketFormProps =
        (CreateFormProps | UpdateFormProps) & {
        onSubmit: (request: CreateTicketRequest | UpdateTicketRequest) => Promise<void>
    }

export function TicketForm({mode, ticket, onSubmit}: TicketFormProps) {

    const [formError, setFormError] = useState<string | null>(null)
    const [title, setTitle] = useState<string>(ticket?.title ?? "")
    const [description, setDescription] = useState<string>(ticket?.description ?? "")
    const [ticketPriority, setTicketPriority] = useState<TicketPriority>(ticket?.ticketPriority ?? "LOW")
    const [ticketStatus, setTicketStatus] = useState<TicketStatus>(ticket?.ticketStatus ?? "OPEN")
        

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setFormError(null)

        const trimmedTitle = title.trim()
        const trimmedDescription = description.trim()

        if (mode === "create") {
            if (trimmedTitle === "") {
                        setFormError("Title must not be blank.")
                        return
                    }
             if (trimmedDescription === "") {
                        setFormError("Description must not be blank.")
                        return
                    }
            const createTicketRequest: CreateTicketRequest = {
                title: trimmedTitle,
                description: trimmedDescription,
                ticketPriority
            }
            await onSubmit(createTicketRequest)
        }

        else {
                const updateTicketRequest: UpdateTicketRequest = {}

                if (trimmedTitle !== ticket.title) {
                    if (trimmedTitle === "") {
                        setFormError("Title must not be blank.")
                        return
                    }
                    updateTicketRequest.title = trimmedTitle
                }

                if (trimmedDescription !== ticket.description) {
                    if (trimmedDescription === "") {
                        setFormError("Description must not be blank.")
                        return
                    }
                    updateTicketRequest.description = trimmedDescription
                }

                if (ticketPriority !== ticket.ticketPriority) {
                    updateTicketRequest.ticketPriority = ticketPriority
                }

                if (ticketStatus !== ticket.ticketStatus) {
                    updateTicketRequest.ticketStatus = ticketStatus
                }

                if(Object.keys(updateTicketRequest).length === 0) {
                    setFormError("No fields changed.")
                    return
                }

            
            await onSubmit(updateTicketRequest)
                
            
        }

        
    }

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="inputTitle">Title</label>
                <input
                    value={title}
                    onChange={(event) => setTitle(event.target.value)}
                    id="inputTitle"
                    type="text"
                />
<br />


                <label htmlFor="inputDescription">Description</label>
                <textarea
                    value={description}
                    onChange={(event) => setDescription(event.target.value)}
                    id="inputDescription"
                />


<br />
                <label htmlFor="inputTicketPriority">Ticket Priority</label>
                <select id="inputTicketPriority"
                value={ticketPriority}
                onChange={(event) => setTicketPriority(event.target.value as TicketPriority)}>
                    {TICKET_PRIORITIES.map((option) => (
                        <option key = {option} value = {option}>
                            {option}
                        </option>
                    ))}
                </select>
<br />


                {mode === "update" && (
                    <>
                        <label htmlFor="inputTicketStatus">Ticket Status</label>
                        <select id="inputTicketStatus"
                        value={ticketStatus}
                        onChange={(event) => setTicketStatus(event.target.value as TicketStatus)}>
                            {TICKET_STATUSES.map((option) => (
                                <option key={option} value={option}>
                                    {option}
                                </option>
                            ))}
                        </select>
                    </>
                )}
            </div>
<br />
            <button type="submit">
                {mode === "create" ? "Create Ticket" : "Update Ticket"}
            </button>

            
            {formError && (
                <p>{formError}</p>
            )}
            
        </form>
    )

}