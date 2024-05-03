package com.people.dtos.people.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
public class PeopleRequestDTO {


    @Schema(description = "Nome da pessoa")
    @NotBlank(message = "Nome da pessoa é obrigatório!")
    private String nome;

    @Schema(description = "Data de nascimento", example = "03/05/1994")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;
}
