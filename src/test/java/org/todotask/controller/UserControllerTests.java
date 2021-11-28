package org.todotask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.todotask.config.TestConfiguration;
import org.todotask.config.WebMvcApplicationConfiguration;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringJUnitWebConfig({WebMvcApplicationConfiguration.class, TestConfiguration.class})
public class UserControllerTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Test
    void gettingUserById() throws Exception {
        mockMvc.perform(
                get("/user/1")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk()
        );
    }

    @Test
    void checkSuccessCreateUsers() throws Exception {
        Map<String, String> user = Map.of("username", "user4", "password", "user");
        String json = new ObjectMapper().writeValueAsString(user);
        mockMvc.perform(
                post("/user/create").contentType(MediaType.APPLICATION_JSON).content(json)
                )
                .andDo(print())
                .andExpectAll(
                        status().isCreated()
        );
    }

    @Test
    void badLogin() throws Exception {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("username", "user1");
        param.add("password", "user");
        mockMvc.perform(
                get("/user/login").params(param)
                )
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
        );
    }
}

