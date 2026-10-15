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
@ApiResponse(responseCode = "404", description = "Unexpected server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
public @interface ApiServerError {

}
