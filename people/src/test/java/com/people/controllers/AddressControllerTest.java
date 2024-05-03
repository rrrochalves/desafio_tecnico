package com.people.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.PeopleApplication;
import com.people.domain.model.Address;
import com.people.domain.model.People;
import com.people.dtos.address.response.AddressIdResponseDTO;
import com.people.exceptions.AddressNotFoundException;
import com.people.exceptions.PeopleNotFoundException;
import com.people.mocks.AddressMock;
import com.people.mocks.PeopleMocks;
import com.people.repository.AddressRepository;
import com.people.service.AddressService;
import com.people.service.PeopleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PeopleApplication.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private AddressRepository repository;

    @Test
    @Transactional
    public void WhenGetAllAddressReturnZero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_five_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_all_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenGetAllAddressReturnFiveAddress() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_new_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenGetAddressByIdExistingReturnAddressDTO() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/address/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    public void whenGetAddressByIdExistingReturnAddressNotFoundException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/address/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(AddressNotFoundException.class);
        assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("Endereço com id: 9999 não encontrado!");
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_new_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenPutAddressReturnOK() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/address/9999")
                        .contentType(MediaType.APPLICATION_JSON).content(AddressMock.ADDRESS_PUT))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    public void whenPutAddressReturnAddressNotFoundException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/address/9999")
                        .contentType(MediaType.APPLICATION_JSON).content(AddressMock.ADDRESS_PUT))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(AddressNotFoundException.class);
        assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("Endereço com id: 9999 não encontrado!");

    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_new_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenCreateNewAddressReturnOnCreated() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS))
                .andExpect(status().isCreated())
                .andReturn();

        AddressIdResponseDTO response = mapper.readValue(mvcResult.getResponse().getContentAsString(), AddressIdResponseDTO.class);

        assertNotNull(response);
        assertNotNull(response.id());

        Address address = repository.findById(response.id()).orElseThrow(() -> new AddressNotFoundException(response.id()));

        assertEquals(response.id(), address.getId());
    }

    @Test
    @Transactional
    public void whenCreateNewAddressWithoutLogradouroReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS_WITHOUT_LOGRADOURO))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"logradouro: Logradouro é obrigatório\"]"
                , mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    public void whenCreateNewAddressWithoutCepReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS_WITHOUT_CEP))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"cep: CEP é obrigatório\"]"
                , mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    public void whenCreateNewAddressWithoutNumeroReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS_WITHOUT_NUMERO))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"numero: Logradouro é obrigatório\"]"
                , mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    public void whenCreateNewAddressWithoutCidadeReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS_WITHOUT_CIDADE))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"cidade: Cidade é obrigatório\"]"
                , mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    public void whenCreateNewAddressWithoutPeopleIdReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AddressMock.ADDRESS_WITHOUT_PEOPLEID))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"peopleId: Id é obrigatório\"]"
                , mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_address.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_new_address.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testIsPrincipalAddress() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/address/9999/principal/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Definindo endereço com id: 9999 como principal!")));
    }

}
