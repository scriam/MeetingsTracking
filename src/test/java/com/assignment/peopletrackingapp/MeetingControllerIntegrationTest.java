package com.assignment.peopletrackingapp;


import com.assignment.peopletrackingapp.domain.Meeting;
import com.assignment.peopletrackingapp.repository.MeetingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MeetingsTrackingApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class MeetingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MeetingsRepository repository;

    @Before
    @After
    public void resetDb() {
        repository.deleteAll();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    @Transactional
    public void givenEmployees_whenGetEmployees_thenStatus200() throws Exception {
        Meeting meeting1 = Meeting.builder()
                .person("Alex")
                .date(new Date())
                .location("Area 51")
                .build();
        Meeting meeting2 = Meeting.builder()
                .person("Alex")
                .date(new Date())
                .location("Area 52")
                .build();

        repository.saveAllAndFlush(List.of(meeting1, meeting2));
        List<Meeting> allMeetings = repository.findAll();
        mvc.perform(get("/meetings").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].location", containsInAnyOrder("Area 51", "Area 52")));

    }

    @Test
    @Transactional
    public void whenPostMeeting_thenCreateMeeting() throws Exception {
        Meeting meeting = Meeting.builder()
                .person("Alex")
                .date(new Date())
                .location("Area 51")
                .build();
        String jsonStringRequest = objectMapper.writeValueAsString(meeting);
        mvc.perform(post("/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringRequest)).andExpect(status().isOk());

        List<Meeting> list = repository.findAll();
        assertThat(list).extracting(Meeting::getPerson).containsOnly("Alex");
    }


}
