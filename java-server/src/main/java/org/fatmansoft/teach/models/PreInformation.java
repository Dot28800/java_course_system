package org.fatmansoft.teach.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter

@Entity
@Table(	name = "pre_information",
        uniqueConstraints = {
        })
public class PreInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer preInformationId;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String sourcePlace;

    private String preSchool;

    private String preScore;

    private String preRank;

    private String preHonor;

}
