package com.assignment.peopletrackingapp;

import com.assignment.peopletrackingapp.domain.Meeting;
import com.assignment.peopletrackingapp.exceptionhandling.InvalidMeetingException;
import com.assignment.peopletrackingapp.repository.MeetingsRepository;
import com.assignment.peopletrackingapp.service.MeetingService;
import com.assignment.peopletrackingapp.service.MeetingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MeetingServiceImplIntegrationTest {

    @Autowired
    private MeetingService meetingService;
    @MockBean
    private MeetingsRepository meetingsRepository;
    private Meeting meeting1;
    private Meeting meeting2;
    private Meeting meeting3;

    @Before
    public void setUp() throws ParseException {
        String date = "20-03-2022";
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        meeting1 = Meeting.builder()
                .id(1)
                .person("John")
                .date(formatter.parse(date))
                .location("Area 51")
                .build();
        meeting2 = Meeting.builder()
                .id(2)
                .person("John")
                .date(formatter.parse(date))
                .location("Area 52")
                .build();
        meeting3 = Meeting.builder()
                .id(3)
                .person("John")
                .date(formatter.parse(date))
                .location("Area 52")
                .build();
        List<Meeting> allMeetings = Arrays.asList(meeting1, meeting2);

        Mockito.when(meetingsRepository.findAll()).thenReturn(allMeetings);
        Mockito.when(meetingsRepository.save(Mockito.any(Meeting.class)))
                .thenReturn(meeting3);
    }

    @Test
    public void given2Meetings_whenGetAll_return2Meetings() {
        List<Meeting> allMeetings = meetingService.getAll();
        verifyFindAllMeetingsIsCalledOnce();
        assertThat(allMeetings).hasSize(2).extracting(Meeting::getId).contains(meeting1.getId(), meeting2.getId());
    }

    @Test
    public void givenMeeting_whenSave_returnMeeting() {
        Meeting createdMeeting = meetingService.create(meeting3);
        verifyCreateMeetingIsCalledOnce();
        assertThat(createdMeeting).usingRecursiveComparison().isEqualTo(meeting3);
    }

    @Test(expected = InvalidMeetingException.class)
    public void givenInvalidMeetingNoDate_whenSave_shouldThrowException() {
        Meeting meetingWithoutDate = Meeting.builder()
                .id(1)
                .person("John")
                .date(null)
                .location("Area 51")
                .build();
        Meeting createdMeeting = meetingService.create(meetingWithoutDate);
    }

    @Test(expected = InvalidMeetingException.class)
    public void givenInvalidMeetingNoLocation_whenSave_shouldThrowException() {
        Meeting meetingWithoutDate = Meeting.builder()
                .id(1)
                .person("John")
                .date(new Date())
                .location("")
                .build();
        Meeting createdMeeting = meetingService.create(meetingWithoutDate);
    }

    private void verifyFindAllMeetingsIsCalledOnce() {
        Mockito.verify(meetingsRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(meetingsRepository);
    }

    private void verifyCreateMeetingIsCalledOnce() {
        Mockito.verify(meetingsRepository, VerificationModeFactory.times(1)).save(Mockito.any(Meeting.class));
        Mockito.reset(meetingsRepository);
    }

    @TestConfiguration
    static class MeetingServiceImplTestContextConfiguration {
        @Bean
        public MeetingService employeeService() {
            return new MeetingServiceImpl();
        }
    }


}
