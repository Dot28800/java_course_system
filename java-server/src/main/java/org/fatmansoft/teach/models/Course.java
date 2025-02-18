package org.fatmansoft.teach.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Course 课程表实体类  保存课程的的基本信息信息，
 * Integer courseId 人员表 course 主键 course_id
 * String num 课程编号
 * String name 课程名称
 * Integer credit 学分
 * Course preCourse 前序课程 pre_course_id 关联前序课程的主键 course_id
 */
@Setter
@Getter
@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })//对应的数据表
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//说明主键是ID
    private Integer courseId;
    @NotBlank
    @Size(max = 20)//最大长度20
    private String num;

    @Size(max = 50)
    private String name;
    private String credit;
    /*@ManyToOne//多对一
    @JoinColumn(name="pre_course_id")
    private  preCourse;*/

    
    @Size(max = 12)
    private String coursePath;

    @Size(max = 12)
    private String time;

    @Size(max = 20)
    private String position;

    @Size(max = 20)
    private String resource;

    @Size(max = 20)
    private String preCourse;

    @Size(max = 20)
    private String type;

    @Size(max = 20)
    private String exam;
    @Size(max=20)
    private String teacherName;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

   /*public Course getPreCourse() {
        return preCourse;
    }

    public void setPreCourse(Course preCourse) {
        this.preCourse = preCourse;
    }

    public String getCoursePath() {
        return coursePath;
    }

    public void setCoursePath(String coursePath) {
        this.coursePath = coursePath;
    }*/



}
