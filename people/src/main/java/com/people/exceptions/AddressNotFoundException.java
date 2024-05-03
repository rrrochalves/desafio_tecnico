package com.people.exceptions;

public class AddressNotFoundException extends RuntimeException{

    public AddressNotFoundException(Integer addressId) {
        super("Endereço com id: %d não encontrado!".formatted(addressId));
    }
}
