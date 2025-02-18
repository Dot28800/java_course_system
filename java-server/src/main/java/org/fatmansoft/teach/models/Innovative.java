package org.fatmansoft.teach.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "innovative",uniqueConstraints = {})
@Setter
@Getter
public class Innovative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer innovativeId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @Size(max=20)
    private String title;//名称
    @Size(max=25)
    private String time; //时间
    @Size(max=25)
    private String level;//奖励层次

    @Size(max=20)
    private String type;//什么类型的奖项或者称号

    @Size(max=50)
    private String host;//颁奖的单位

    private Integer innovativeType;

}

