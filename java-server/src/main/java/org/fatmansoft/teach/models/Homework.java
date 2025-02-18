package org.fatmansoft.teach.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Setter
@Getter
@Entity
@Table(name = "homework")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne
    @JoinColumn(name = "id")
    private CourseChoose courseChoose;

    @Size(max=25)
    private String time;

    @Size(max=12)
    private String workMark="0.0";
}
