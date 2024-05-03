package com.people.dtos.people.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PeopleIdResponseDTO (

        @Schema(description = "Id da pessoa")
        Integer id
){
}
