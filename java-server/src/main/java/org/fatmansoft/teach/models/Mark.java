package org.fatmansoft.teach.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Setter
@Getter
@Entity
@Table(	name = "mark",
        uniqueConstraints = {
        })//对应的数据表
public class Mark {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//说明主键是ID
    private Integer markId;

    @OneToOne
    @JoinColumn(name = "id")
    private CourseChoose courseChoose;

    private Double mark;

    private Double GPA;

    @Size(max=20)
    private String rank;


    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public CourseChoose getCourseChoose() {
        return courseChoose;
    }

    public void setCourseChoose(CourseChoose courseChoose) {
        this.courseChoose = courseChoose;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public Double getGPA() {
        return GPA;
    }

    public void setGPA(Double GPA) {
        this.GPA = GPA;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


}
