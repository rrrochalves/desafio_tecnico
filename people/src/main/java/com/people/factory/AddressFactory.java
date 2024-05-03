package com.people.factory;

import com.people.domain.model.Address;
import com.people.domain.model.People;
import com.people.dtos.address.request.AddressRequestDTO;
import com.people.dtos.address.response.AddressResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressFactory {

    public static AddressResponseDTO convertAddressToDTO(Address address) {
        return AddressResponseDTO.builder()
                .id(address.getId())
                .logradouro(address.getLogradouro())
                .cep(address.getCep())
                .numero(address.getNumero())
                .cidade(address.getCidade())
                .estado(address.getEstado())
                .principal(address.getPrincipal())
                .peopleId(address.getPeople().getId())
                .build();
    }

    public static Address convertAddressDtoToAddress(AddressRequestDTO addressRequestDTO) {
        return Address.builder()
                .cep(addressRequestDTO.getCep())
                .logradouro(addressRequestDTO.getLogradouro())
                .numero(addressRequestDTO.getNumero())
                .cidade(addressRequestDTO.getCidade())
                .estado(addressRequestDTO.getEstado())
                .people(People.builder().id(addressRequestDTO.getPeopleId()).build())
                .build();
    }

    public static List<AddressResponseDTO> buildAddressResponseList(List<Address> addressList) {
        return addressList != null && !addressList.isEmpty() ?
                addressList.stream().map(AddressFactory::convertAddressToDTO).collect(Collectors.toList()) :
                new ArrayList<>();
    }
}
