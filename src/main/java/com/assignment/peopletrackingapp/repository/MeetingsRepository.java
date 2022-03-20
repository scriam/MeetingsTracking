package com.assignment.peopletrackingapp.repository;

import com.assignment.peopletrackingapp.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MeetingsRepository extends JpaRepository<Meeting, Long> {

}
