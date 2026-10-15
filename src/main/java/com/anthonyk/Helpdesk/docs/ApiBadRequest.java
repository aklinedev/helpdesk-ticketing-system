package com.anthonyk.Helpdesk.docs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthonyk.Helpdesk.dto.ErrorResponseDTO;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "400", description = "Invalid request: validation failed, malformed JSON, or no update fields provided", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
public @interface ApiBadRequest {

}
