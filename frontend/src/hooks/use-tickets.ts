import { useEffect, useState } from "react"
import type { Ticket } from "../types/ticket"
import { ApiError } from "../api/api-error"
import { getTickets } from "../api/tickets"

export function useTickets() {

    const [tickets, setTickets] = useState<Ticket[]>([])
    const [loading, setLoading] = useState<boolean>(true)
    const [error, setError] = useState<ApiError | null>(null)


        useEffect(() => {
            async function loadTickets() {
                try {
                    setLoading(true)
                    setError(null)

                    const data = await getTickets()
                    setTickets(data)
                } catch (error) {
                    if(error instanceof ApiError) {
                        setError(error)
                    } else {
                        setError(new ApiError(0, "Unexpected error occurred."))
                    }
                } finally {
                    setLoading(false)
                }
            }

            loadTickets()
        }, [])
    return {
        tickets,
        loading,
        error
    }
}