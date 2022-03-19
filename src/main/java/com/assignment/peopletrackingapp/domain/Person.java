package com.assignment.peopletrackingapp.domain;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Person {

    @Id
    @Column
    private long id;

    @Column
    //@NotNull(message="{NotNull.Person.lastName}")
    @NotNull
    private String name;

}
