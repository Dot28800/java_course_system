package org.fatmansoft.teach.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "practice")
@Getter
@Setter
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer practiceId;

    @ManyToOne
    @JoinColumn(name = "student_Id")
    private Student student;

    @Size(max = 20)
    private String num;

    @Size(max =50)
    private  String studentName;

    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String place;
    private String type;
    private String teamName;
    private String time;
    private String evaluation;

    public String getNumName() {
        return num + "-" + name;
    }
}


