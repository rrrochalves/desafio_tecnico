package com.people.controllers;

import com.people.domain.model.Address;
import com.people.dtos.address.request.AddressRequestDTO;
import com.people.dtos.address.response.AddressIdResponseDTO;
import com.people.dtos.address.response.AddressResponseDTO;
import com.people.dtos.people.response.PeopleIdResponseDTO;
import com.people.dtos.people.response.PeopleResponseDTO;
import com.people.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@Tag(name = "Endereços", description = "End-points para o dominio de Endereços da API.")
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;


    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "Listar endereços", description = "Listar todos os endereços cadastradas")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress() {
        return ResponseEntity.ok(addressService.getAllAddress());
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Listar endereço", description = "Listar endereço por Id")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Integer addressId) {
        return ResponseEntity.ok(addressService.getAddressDTOById(addressId));
    }

    @PostMapping
    @Operation(summary = "Cadastrar endereço", description = "Cadastrar um endereço nova")
    public ResponseEntity<AddressIdResponseDTO> createPeople(@RequestBody @Valid AddressRequestDTO addressRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        AddressIdResponseDTO addressId = addressService.createAddress(addressRequestDTO);

        var uri = uriComponentsBuilder.path("/people/{id}").buildAndExpand(addressId.id()).toUri();

        return ResponseEntity.created(uri).body(addressId);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Atualizar endereço", description = "Atualizar uma endereço por Id")
    public ResponseEntity<AddressResponseDTO> putPeople(@PathVariable Integer addressId, @RequestBody @Valid AddressRequestDTO address) {
        return ResponseEntity.ok(addressService.putAddress(addressId, address));
    }

    @PutMapping("/{peopleId}/principal/{addressId}")
    @Operation(summary = "Definir endereço principal", description = "Definir endereço como principal por Id")
    public ResponseEntity<String> isPrincipalAddress(@PathVariable Integer addressId, @PathVariable Integer peopleId) {
        addressService.setPrincipalAddress(addressId, peopleId);
        return ResponseEntity.ok("Definindo endereço com id: %d como principal!".formatted(addressId));
    }
}
