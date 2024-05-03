package com.people.mocks;

public class AddressMock {

    public static final String ADDRESS = """
                {
                  "logradouro": "Rua Teste",
                  "cep": "14825879",
                  "numero": 758,
                  "cidade": "Cidade Teste",
                  "estado": "ACRE",
                  "peopleId": 9999
                }    
            """;

    public static final String ADDRESS_PUT = """
                {
                  "logradouro": "Rua Teste 2",
                  "cep": "14825879",
                  "numero": 758,
                  "cidade": "Cidade Teste",
                  "estado": "ACRE",
                  "peopleId": 9999
                } 
            """;
    public static final String ADDRESS_WITHOUT_LOGRADOURO = """
            {
                  "logradouro": "",
                  "cep": "15689789",
                  "numero": 758,
                  "cidade": "Cidade Teste",
                  "estado": "ACRE",
                  "peopleId": 9999
                }
            """;

    public static final String ADDRESS_WITHOUT_CEP = """
            {
                  "logradouro": "Logradouro Teste",
                  "cep": "",
                  "numero": 758,
                  "cidade": "Cidade TEste",
                  "estado": "ACRE",
                  "peopleId": 9999
                }
            """;

    public static final String ADDRESS_WITHOUT_NUMERO = """
            {
                  "logradouro": "Logradouro Teste",
                  "cep": "15689789",
                  "numero": null,
                  "cidade": "Cidade TEste",
                  "estado": "ACRE",
                  "peopleId": 9999
                }
            """;

    public static final String ADDRESS_WITHOUT_CIDADE = """
            {
                  "logradouro": "Logradouro Teste",
                  "cep": "15689789",
                  "numero": 758,
                  "cidade": "",
                  "estado": "ACRE",
                  "peopleId": 9999
                }
            """;

    public static final String ADDRESS_WITHOUT_PEOPLEID = """
            {
                  "logradouro": "Logradouro Teste",
                  "cep": "15689789",
                  "numero": 758,
                  "cidade": "Cidade Teste",
                  "estado": "ACRE",
                  "peopleId": null
                }
            """;
}
