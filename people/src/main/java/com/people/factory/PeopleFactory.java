package com.people.factory;


import com.people.domain.model.People;
import com.people.dtos.people.request.PeopleRequestDTO;
import com.people.dtos.people.response.PeopleResponseDTO;

public class PeopleFactory {

    public static PeopleResponseDTO convertPeopleToDTO(People people) {
        return PeopleResponseDTO.builder()
                .id(people.getId())
                .nome(people.getNome())
                .birthDate(people.getBirthDate())
                .address(AddressFactory.buildAddressResponseList(people.getAddresses()))
                .build();
    }

    public static People convertDtoToPeople(PeopleRequestDTO peopleRequestDTO) {
        return People.builder()
                .nome(peopleRequestDTO.getNome())
                .birthDate(peopleRequestDTO.getBirthDate())
                .build();
    }
}
