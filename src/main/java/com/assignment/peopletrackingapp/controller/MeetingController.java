package com.assignment.peopletrackingapp.controller;

import com.assignment.peopletrackingapp.domain.Meeting;
import com.assignment.peopletrackingapp.service.MeetingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    MeetingServiceImpl meetingService;

    @PostMapping
    public ResponseEntity createMeeting(@RequestBody @Valid Meeting meeting) {
        Meeting createdMeeting = meetingService.create(meeting);
        return ResponseEntity.ok(createdMeeting);
    }

    @GetMapping
    public List<Meeting> getMeetings() {
        return meetingService.getAll();
    }

}
