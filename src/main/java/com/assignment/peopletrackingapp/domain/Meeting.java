package com.assignment.peopletrackingapp.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank
    private String person;

    @NotNull
    private Date date;

    @NotBlank
    private String location;

}
