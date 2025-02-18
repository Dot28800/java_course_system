package org.fatmansoft.teach.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(	name = "rank",
        uniqueConstraints = {
        })//对应的数据表
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//说明主键是ID
    private Integer rankId;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double avgMark;

    private Double avgGPA;

    private String totalRank;



}
