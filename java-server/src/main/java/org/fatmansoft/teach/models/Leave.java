package org.fatmansoft.teach.models;


import org.fatmansoft.teach.models.Student;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "leave",
        uniqueConstraints = {
        })
/*
主键 leaveId (Integer)

外键 studentId (Integer)

请假时间 leaveTime (string)

归来时间  returnTime (String)

请假原因 leaveReason (string)

请假去向 destination  (string)

是否按时归来(销假情况) flag (string)

UI组件要求：两个时间用datePicker,销假情况用选择框（直接手动设置选项是/否），其余用文本框
* */
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveId;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;


    @Size(max = 25)
    private String leaveTime;
    @Size(max = 25)
    private String returnTime;
    @Size(max = 20)
    private String leaveReason;
    @Size(max = 20)
    private String destination;
    @Size(max = 20)
    private String flag;

    public Integer getLeaveId(){
        return leaveId;
    }
    public void setLeaveId(Integer leaveId){
        this.leaveId = leaveId;
    }
    public String getLeaveTime(){
        return leaveTime;
    }
    public void setLeaveTime(String leaveTime){ this.leaveTime = leaveTime;}
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        this.student = student;
    }
    public String getReturnTime(){
        return returnTime;
    }
    public void setReturnTime(String returnTime){
        this.returnTime = returnTime;
    }
    public String getLeaveReason(){
        return leaveReason;
    }
    public void setLeaveReason(String leaveReason){
        this.leaveReason = leaveReason;
    }
    public String getDestination(){
        return destination;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getFlag(){
        return flag;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }

}

