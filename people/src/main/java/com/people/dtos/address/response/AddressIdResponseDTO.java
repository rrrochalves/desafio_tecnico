package com.people.dtos.address.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressIdResponseDTO(

        @Schema(description = "Id do endereço")
        Integer id
) {
}
