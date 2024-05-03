package com.people.dtos.people.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.people.dtos.address.response.AddressResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeopleResponseDTO {

    @Schema(description = "Id da pessoa")
    private Integer id;

    @Schema(description = "Nome da pessoa")
    private String nome;

    @Schema(description = "Data de nascimento")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date birthDate;

    @Schema(description = "Lista de endereços")
    private List<AddressResponseDTO> address;
}
