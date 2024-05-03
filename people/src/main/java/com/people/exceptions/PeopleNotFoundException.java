package com.people.exceptions;

public class PeopleNotFoundException extends RuntimeException {

    public PeopleNotFoundException(Integer peopleId) {
        super("Pessoa com id: %d não encontrada!".formatted(peopleId));
    }
}
