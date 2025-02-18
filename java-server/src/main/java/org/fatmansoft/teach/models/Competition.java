package org.fatmansoft.teach.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(	name = "competition",
        uniqueConstraints = {
        })

public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competitionId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @Size(max = 20)
    private String contestName;//赛事名称
    @Size(max = 25)
    private String contestTime;//比赛时间
    @Size(max = 25)
    private String prize;//所获奖项 ：国家一等奖、二等奖、金牌、银牌、铜牌、优胜奖
    @Size(max = 20)
    private String rank;//级别：国家级、省级、市级、校级
    @Size(max = 20)
    private String type; //个人赛、团队赛
    @Size(max = 20)
    private String instructor;//指导老师
    @Size(max = 20)
    private String position;//竞赛地点

    public String getContestName(){
        return contestName;
    }
    public void setContestName(String contestName){
        this.contestName = contestName;
    }
    public Integer getCompetitionId(){
        return competitionId;
    }
    public void setCompetitionId(Integer competitionId){
        this.competitionId = competitionId;
    }
    public String getContestTime(){
        return contestTime;
    }
    public void setContestTime(String contestTime){ this.contestTime = contestTime;}
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        this.student = student;
    }
    public String getPrize(){
        return prize;
    }
    public void setPrize(String prize){
        this.prize = prize;
    }
    public String getRank(){
        return rank;
    }
    public void setRank(String rank){
        this.rank = rank;
    }
    public String getInstructor(){
        return instructor;
    }
    public void setInstructor(String instructor){
        this.instructor = instructor;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }
}