package com.people.factory;

import com.people.config.ErrorApi;

public class ErrorApiFactory {

    public static ErrorApi build(String message) {
        return ErrorApi.builder()
                .message(message)
                .build();
    }
}
