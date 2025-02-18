package org.fatmansoft.teach.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/*
 *
 *   统计数据库模式，主要存放学生统计信息
 *   统计包括GPA，排名（随时计算），选课总数，不及格科目数，消费总额等
 *
 * */
@Entity
@Table(	name = "statistics",
        uniqueConstraints = {
        })

@Getter
@Setter
public class Statistics {

    //主键，可自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer statisticsId;

    //排名
    @Column
    private Integer rank;
    //学号
    @Column
    private String studentNum;
    //绩点
    @Column(nullable = true)
    private String gpa;
    @Column
    private String name;
    @Column
    //选课总数
    private Integer sumOfCourse;
    @Column
    //不及格课程数目
    private Integer sumOfDisPassCourse;

    @Column
    private String practiceTimes;

    @Column
    private String competitionTimes;
    @Column
    private String honorTimes;


    //总消费金额
    @Column
    private String sumMoney;
    @Column
    private String dailyTimes;



}
