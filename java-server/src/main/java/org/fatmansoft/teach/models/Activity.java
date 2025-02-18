package org.fatmansoft.teach.models;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;
@Entity
@Table(name = "activity")
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

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
    private String activityPlace;
    @Size(max=50)
    private String type;
    private String content;
    private String activityTime;
    private String activityEvaluation;

    public String getNumName() {
        return num + "-" + name;
    }
}


