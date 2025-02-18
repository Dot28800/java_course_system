package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Competition;
import org.fatmansoft.teach.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave,Integer> {

    @Query(value = "from Leave where ?1=0 or ?1=student.studentId")
    List<Leave> findByStudentStudentId(Integer studentId);

    @Query(value = "from Leave  where (?1=student.person.num or ?1='') and (?2=student.person.name or ?2='') and (?3='' or ?3=flag)")
    List<Leave> findByNumNameFlag(String num,String name,String flag);


}
