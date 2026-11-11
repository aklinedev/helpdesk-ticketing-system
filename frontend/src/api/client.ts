import type { ErrorResponse } from "../types/responses/error-response";
import { ApiError } from "./api-error";

export async function apiRequest<T>(path: string, options: RequestInit = {}): Promise<T> {

    const BASE_URL = "/api"

    const headers = new Headers(options.headers)
    // If sending a body without a label, this labels it as JSON for the backend.
    if (options.body !== undefined && !headers.has("Content-Type")) {
        headers.set("Content-Type", "application/json")
    }

    let response: Response
    try {
        response = await fetch(`${BASE_URL}${path}`, { ...options, headers })
    } catch {
    // If server is down, or network connection failed.
        throw new ApiError(0, "Unable to reach the server, please try again.")
    }

    // If the fetch request succeeded but its a 4/5xx status.
    if (!response.ok) {
        let message = response.statusText || "Request failed"
        try {
            const body = (await response.json()) as ErrorResponse
            if (body.message) {
                message = body.message
            }
        } catch {
            // If parsing the error message failed (response body was not valid ErrorResponse JSON), we keep the fallback message created.
        }
        throw new ApiError(response.status, message)
    }

    // Successful status with no body (delete end point returns no body and 204).
    if (response.status === 204) {
        return undefined as T
    }

    // returns the successful JSON body.
    return (await response.json()) as T
}