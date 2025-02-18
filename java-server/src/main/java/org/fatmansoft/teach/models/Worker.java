package org.fatmansoft.teach.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "worker",
        uniqueConstraints = {
        })
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workerId;

    @OneToOne
    @JoinColumn(name="person_id")
    @JsonIgnore
    private Person person;

    @Size(max=15)
    private String name;

    @Size(max=20)
    private String job;

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
    public String getNumName(){
        return person.getNum()+"-" + person.getName();
    }

    public Person getPerson() {
        return person;
    }
}
