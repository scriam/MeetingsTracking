package com.assignment.peopletrackingapp.controller;

import com.assignment.peopletrackingapp.domain.Meeting;
import com.assignment.peopletrackingapp.exceptions.InvalidMeeting;
import com.assignment.peopletrackingapp.service.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @PostMapping
    public ResponseEntity createMeeting(@RequestBody @Valid Meeting meeting) throws URISyntaxException {
        Meeting createdMeeting = meetingService.create(meeting);
        return ResponseEntity.ok(createdMeeting);
    }

    @GetMapping
    public List<Meeting> getMeetings() {
        return meetingService.getAll();
    }

}
