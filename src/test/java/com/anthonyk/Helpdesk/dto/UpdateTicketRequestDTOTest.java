package com.anthonyk.Helpdesk.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.anthonyk.Helpdesk.model.TicketPriority;
import com.anthonyk.Helpdesk.model.TicketStatus;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UpdateTicketRequestDTOTest {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = factory.getValidator();
    }

    @Test
    public void fullyValidDtoTest() {

        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO("TEST", "TESTING",
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void fullyValidDtoTest_withNullFields() {

        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(null, null,
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void invalidDtoTest_withWhitespaceTitle() {

        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO("       ", null,
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).containsExactly("title");
    }

    @Test
    void invalidDtoTest_withWhitespaceDescription() {

        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(null, "      ",
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).containsExactly("description");
    }

    @Test
    void invalidDtoTest_withOverSizeTitle() {

        String overSizedTitle = "a".repeat(201);
        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(overSizedTitle, null,
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).containsExactly("title");
    }

    @Test
    void invalidDtoTest_withOverSizeDescription() {

        String overSizedDescription = "a".repeat(201);
        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(null, overSizedDescription,
                TicketPriority.LOW, TicketStatus.OPEN);

        Set<ConstraintViolation<UpdateTicketRequestDTO>> violations = validator.validate(updateTicketRequestDTO);

        assertThat(violations).extracting(v -> v.getPropertyPath().toString()).containsExactly("description");
    }

}
