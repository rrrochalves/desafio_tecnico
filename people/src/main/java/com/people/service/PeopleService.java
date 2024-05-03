package com.people.service;


import com.people.domain.model.People;
import com.people.dtos.people.request.PeopleRequestDTO;
import com.people.dtos.people.response.PeopleIdResponseDTO;
import com.people.dtos.people.response.PeopleResponseDTO;
import com.people.exceptions.PeopleNotFoundException;
import com.people.factory.PeopleFactory;
import com.people.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository repository;
    public List<PeopleResponseDTO> getAllPeople() {
        List<People> peopleList = repository.findAll();

        return peopleList.stream()
                .map(PeopleFactory::convertPeopleToDTO)
                .collect(Collectors.toList());
    }

    public PeopleResponseDTO getPeopleDTOById(Integer peopleId) {
        People people = getPeopleById(peopleId);
        return PeopleFactory.convertPeopleToDTO(people);
    }

    public PeopleIdResponseDTO createPeople(PeopleRequestDTO peopleRequestDTO) {
        People people = PeopleFactory.convertDtoToPeople(peopleRequestDTO);
        repository.save(people);
        return new PeopleIdResponseDTO(people.getId());
    }

    public PeopleResponseDTO putPeople(Integer peopleId, PeopleRequestDTO peopleRequestDTO) {
        People peopleUpdate = getPeopleById(peopleId);

        peopleUpdate.setNome(peopleRequestDTO.getNome());
        peopleUpdate.setBirthDate(peopleRequestDTO.getBirthDate());

        repository.save(peopleUpdate);
        return PeopleFactory.convertPeopleToDTO(peopleUpdate);
    }

    public People getPeopleById(Integer peopleId) {
        return repository.findById(peopleId)
                .orElseThrow(() -> new PeopleNotFoundException(peopleId));
    }
}
