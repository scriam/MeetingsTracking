package com.assignment.peopletrackingapp.service;

import com.assignment.peopletrackingapp.domain.Meeting;
import com.assignment.peopletrackingapp.exceptionhandling.InvalidMeetingException;
import com.assignment.peopletrackingapp.repository.MeetingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingsRepository meetingsRepository;

    public Meeting create(Meeting meeting) {
        validateMeeting(meeting);
        return meetingsRepository.save(meeting);
    }

    private void validateMeeting(Meeting meeting) {
        // Validate meeting fields
        String person = meeting.getPerson();
        String location = meeting.getLocation();
        Date date = meeting.getDate();
        if (person==null || person.isBlank()) {
            throw new InvalidMeetingException("Name");
        }
        if (location==null || location.isBlank()) {
            throw new InvalidMeetingException("Location");
        }
        if (date==null) {
            throw new InvalidMeetingException("Date");
        }

    }


    // Get all the meetings from the db
    public List<Meeting> getAll() {
        return meetingsRepository.findAll();
    }

}
