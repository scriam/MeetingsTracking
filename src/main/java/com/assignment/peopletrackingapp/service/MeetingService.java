package com.assignment.peopletrackingapp.service;

import com.assignment.peopletrackingapp.domain.Meeting;

import java.util.List;

public interface MeetingService {

    public Meeting create(Meeting meeting);

    public List<Meeting> getAll();
}
