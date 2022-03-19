package com.assignment.peopletrackingapp.repository;

import com.assignment.peopletrackingapp.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingsRepository extends JpaRepository<Meeting, Long> {

}
