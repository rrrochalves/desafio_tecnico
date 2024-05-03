package com.people.dtos.address.request;

import com.people.domain.enums.Estados;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDTO {

    @Schema(description = "Logradouro do endereço")
    @NotBlank(message = "Logradouro é obrigatório")
    private String logradouro;

    @Schema(description = "CEP do endereço")
    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    @Schema(description = "Número do endereço")
    @NotNull(message = "Logradouro é obrigatório")
    private Integer numero;

    @Schema(description = "Cidade do endereço")
    @NotBlank(message = "Cidade é obrigatório")
    private String cidade;

    @Schema(description = "Estados do endereço")
    private Estados estado;

    @Schema(description = "Id da pessoa")
    @NotNull(message = "Id é obrigatório")
    private Integer peopleId;
}
