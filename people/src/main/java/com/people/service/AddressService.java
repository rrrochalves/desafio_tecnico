package com.people.service;


import com.people.domain.model.Address;
import com.people.domain.model.People;
import com.people.dtos.address.request.AddressRequestDTO;
import com.people.dtos.address.response.AddressIdResponseDTO;
import com.people.dtos.address.response.AddressResponseDTO;
import com.people.exceptions.AddressNotFoundException;
import com.people.factory.AddressFactory;
import com.people.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    private final PeopleService peopleService;

    public List<AddressResponseDTO> getAllAddress() {
        List<Address> addressList = repository.findAll();

        return addressList.stream()
                .map(AddressFactory::convertAddressToDTO)
                .collect(Collectors.toList());
    }

    public AddressResponseDTO getAddressDTOById(Integer addressId) {
        Address address = getAddressById(addressId);
        return AddressFactory.convertAddressToDTO(address);
    }

    public AddressIdResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        Address address = AddressFactory.convertAddressDtoToAddress(addressRequestDTO);
        repository.save(address);
        return new AddressIdResponseDTO(address.getId());
    }

    public AddressResponseDTO putAddress(Integer addressId, AddressRequestDTO addressRequestDTO) {
        Address addressUpdate = getAddressById(addressId);

        addressUpdate.setCep(addressRequestDTO.getCep());
        addressUpdate.setLogradouro(addressRequestDTO.getLogradouro());
        addressUpdate.setNumero(addressRequestDTO.getNumero());
        addressUpdate.setCidade(addressRequestDTO.getCidade());
        addressUpdate.setEstado(addressRequestDTO.getEstado());
        addressUpdate.setPeople(People.builder().id(addressRequestDTO.getPeopleId()).build());

        repository.save(addressUpdate);
        return AddressFactory.convertAddressToDTO(addressUpdate);
    }

    public void setPrincipalAddress(Integer addressId, Integer peopleId) {
        People people = peopleService.getPeopleById(peopleId);

        Address address = people.getAddresses().stream().filter(a -> a.getId().equals(addressId))
                .findFirst().orElseThrow(() -> new AddressNotFoundException(addressId));

        Address principalAddress = people.getPrincipalAddress();

        if(principalAddress != null) {
            principalAddress.setPrincipal(false);
            repository.save(principalAddress);
        }

        address.setPrincipal(true);
        repository.save(address);
    }

    public Address getAddressById(Integer addressId) {
        return repository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
    }


}
