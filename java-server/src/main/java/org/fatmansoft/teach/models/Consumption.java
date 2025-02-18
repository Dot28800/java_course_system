package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;


/*
* ### 消费信息

主键 consumptionId(Integer)

外键 studentId(Integer)

消费地点 consumePlace(string)

消费原因 consumeReason(String)

消费金额 money(Double)

交易时间 consumeTime(String)

UI组件要求：时间用datePicker,其余用文本框

前端显示属性：学生学号，姓名以及这里的属性（ID不用），
注意对输入的money格式进行判断，不符合要拒绝录入并返回提示信息
* */
@Entity
@Table(	name = "Consumption",
        uniqueConstraints = {
        })
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer consumptionId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @Size(max = 20)
    private String consumePlace;

    @Size(max = 30)
    private String consumeReason;

    @Size(max = 20)
    private String consumeTime;


    private Double money;

    public String getConsumePlace(){
        return consumePlace;
    }
    public void setConsumePlace(String consumePlace){
        this.consumePlace = consumePlace;
    }
    public String getConsumeReason(){
        return consumeReason;
    }
    public void setConsumeReason(String consumeReason){
        this.consumeReason = consumeReason;
    }
    public String getConsumeTime(){
        return consumeTime;
    }
    public void setConsumeTime(String consumeTime){
        this.consumeTime = consumeTime;
    }

    public Double getMoney(){
        return money;
    }
    public void setMoney(Double money){
        this.money = money;
    }

    public Integer getConsumptionId(){
        return consumptionId;
    }
    public void setCompetitionId(Integer competitionId){
        this.consumptionId = consumptionId;
    }

    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        this.student = student;
    }

}
