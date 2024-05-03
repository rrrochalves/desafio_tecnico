package com.people.controllers;

import com.people.domain.model.People;
import com.people.dtos.people.request.PeopleRequestDTO;
import com.people.dtos.people.response.PeopleIdResponseDTO;
import com.people.dtos.people.response.PeopleResponseDTO;
import com.people.service.PeopleService;
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
@Tag(name = "Pessoas", description = "End-points para o dominio de Pessoas da API.")
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;


    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    @Operation(summary = "Listar pessoas", description = "Listar todas as pessoas cadastradas")
    public ResponseEntity<List<PeopleResponseDTO>> getAllPeople() {
        return ResponseEntity.ok(peopleService.getAllPeople());
    }

    @GetMapping("/{peopleId}")
    @Operation(summary = "Listar pessoa", description = "Listar pessoas por Id")
    public ResponseEntity<PeopleResponseDTO> getPeopleById(@PathVariable Integer peopleId) {
        return ResponseEntity.ok(peopleService.getPeopleDTOById(peopleId));
    }

    @PostMapping
    @Operation(summary = "Cadastrar pessoa", description = "Cadastrar uma pessoa nova")
    public ResponseEntity<PeopleIdResponseDTO> createPeople(@RequestBody @Valid PeopleRequestDTO peopleRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        PeopleIdResponseDTO peopleId = peopleService.createPeople(peopleRequestDTO);

        var uri = uriComponentsBuilder.path("/people/{id}").buildAndExpand(peopleId.id()).toUri();

        return ResponseEntity.created(uri).body(peopleId);
    }

    @PutMapping("/{peopleId}")
    @Operation(summary = "Atualizar pessoa", description = "Atualizar uma pessoa por Id")
    public ResponseEntity<PeopleResponseDTO> putPeople(@PathVariable Integer peopleId, @RequestBody @Valid PeopleRequestDTO people) {
        return ResponseEntity.ok(peopleService.putPeople(peopleId, people));
    }
}
