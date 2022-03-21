package com.assignment.peopletrackingapp.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meetings")
public class Meeting {

    @Id
    @GeneratedValue
    private long id;

    private String person;

    private Date date;

    private String location;

}
