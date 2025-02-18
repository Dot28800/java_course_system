package org.fatmansoft.teach.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Science学生表实体类 保存每个学生的信息，
 * Integer scienceId 用户表 science 主键 science_id
 * Student student 关联到该用户所用的Student对象，账户所对应的人员信息 person_id 关联 person 表主键 person_id
 * String projectName 项目名称
 * String achievement 项目成果
 * String teacherName 指导教师
 *
 */
@Entity
@Table(	name = "science",
        uniqueConstraints = {
        })
public class Science {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scienceId;

    @ManyToOne

    @JoinColumn(name="student_id")
    private Student student;

    @Size(max = 20)
    private String projectName;

    @Size(max = 50)
    private String achievement;
    @Size(max = 50)
    private String teacherName;


    public Integer getScienceId() {
        return scienceId;
    }

    public void setScienceId(Integer scienceId) {
        this.scienceId = scienceId;
    }



    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

