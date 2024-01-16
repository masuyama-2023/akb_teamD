package com.example.akb_teamD.app.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.not;


@SpringBootTest
@AutoConfigureMockMvc
class GlobalMoveControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void address() throws Exception{
        mockMvc.perform(post("/user_contact_address"))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("電話番号")))
                .andExpect(content().string(containsString("メールアドレス")))
                .andExpect(content().string(containsString("備考")))
                .andExpect(content().string(not(containsString("退勤"))))
                .andExpect(content().string(not(containsString("出勤"))));
    }
}