package org.fatmansoft.teach.repository;


import org.fatmansoft.teach.models.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<Consumption,Integer> {

    @Query(value = "from Consumption where ?1=0 or ?1=student.studentId")
    List<Consumption> findByStudentStudentId(Integer studentId);

    @Query(value = "from Consumption  where ?1=student.studentId")
    List<Consumption> findByStudentId(Integer studentId);

    @Query(value = "from Consumption where ?1=student.person.num and ?2=student.person.name and (?3='' or ?3=consumeReason)")
    List<Consumption> findByReasonAndStudent(String num,String name,String reason);

    @Query(value = "from Consumption  where ?1=consumeReason and ?2=student.person.num")
    List<Consumption> findByConsumeReason(String reasonName,String num);

}
