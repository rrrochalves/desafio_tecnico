package com.people.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.PeopleApplication;
import com.people.domain.model.People;
import com.people.dtos.people.response.PeopleIdResponseDTO;
import com.people.exceptions.PeopleNotFoundException;
import com.people.mocks.PeopleMocks;
import com.people.repository.PeopleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PeopleApplication.class)
public class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PeopleRepository repository;

    @Autowired
    private PeopleService service;


    @Test
    @Transactional
    public void WhenGetAllPeopleReturnZero() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.get("/people")
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_five_people.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_all_people.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenGetAllPeopleReturnFivePeople() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_people_with_id.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_people_with_id.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenGetPeopleByIdExistingReturnPeopleDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void whenGetPeopleByIdExistingReturnPeopleNotFoundException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/people/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(PeopleNotFoundException.class);
        assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("Pessoa com id: 9999 não encontrada!");
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_people_with_id.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_people_with_id.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void whenPutPeopleReturnOK() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/people/9999")
                        .contentType(MediaType.APPLICATION_JSON).content(PeopleMocks.PEOPLE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    public void whenPutPeopleReturnPeopleNotFoundException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/people/9999")
                        .contentType(MediaType.APPLICATION_JSON).content(PeopleMocks.PEOPLE))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(PeopleNotFoundException.class);
        assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("Pessoa com id: 9999 não encontrada!");
    }

    @Test
    @Transactional
    @Sql(value = "/scripts/create_new_people.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/scripts/delete_people.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createNewPeopleReturnStatusCreated() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PeopleMocks.PEOPLE))
                .andExpect(status().isCreated())
                .andReturn();

        PeopleIdResponseDTO response = mapper.readValue(mvcResult.getResponse().getContentAsString(), PeopleIdResponseDTO.class);

        assertNotNull(response);
        assertNotNull(response.id());

        People people = repository.findById(response.id()).orElseThrow(() -> new PeopleNotFoundException(response.id()));

        assertEquals(response.id(), people.getId());
    }

    @Test
    public void createNewPeopleWithoutANameReturnStatusBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PeopleMocks.PEOPLE_WITHOUT_A_NAME))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("[\"nome: Nome da pessoa é obrigatório!\"]",
                mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
