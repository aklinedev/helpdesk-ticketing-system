package com.anthonyk.Helpdesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error body returned for every failed request")
public record ErrorResponseDTO(
        @Schema(description = "HTTP status code", example = "404") int status,
        @Schema(description = "Human-readable explanation of the error", example = "Ticket Not Found") String message
) {

}
