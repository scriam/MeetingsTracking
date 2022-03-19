package com.assignment.peopletrackingapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@Entity
@Table(name = "meetings")
public class Meeting {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    private String person;

    @NotNull
    private Date date;

    @NotBlank
    private String location;
}
