package com.people.dtos.address.response;

import com.people.domain.enums.Estados;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {

    private Integer id;

    @Schema(description = "Logradouro do endereço")
    private String logradouro;

    @Schema(description = "CEP do endereço")
    private String cep;

    @Schema(description = "Número do endereço")
    private Integer numero;

    @Schema(description = "Cidade do endereço")
    private String cidade;

    @Schema(description = "Estados do endereço")
    private Estados estado;

    @Schema(description = "É endereço principal")
    private Boolean principal;

    @Schema(description = "Id da pessoa responsável pelo endereço")
    private Integer peopleId;
}
