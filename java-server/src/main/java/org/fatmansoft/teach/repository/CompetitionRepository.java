package org.fatmansoft.teach.repository;

import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;
import org.fatmansoft.teach.models.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition,Integer> {

    @Query(value = "from Competition where ?1=0 or ?1=student.studentId")
    List<Competition> findByStudentStudentId(Integer studentId);

    @Query(value = "from Competition where  ?1=student.studentId")
    List<Competition> getByStudentStudentId(Integer studentId);

    @Query(value = "from Competition  where (?1=rank or ?1='') and student.person.name=?3 and ?2=student.person.num")
    List<Competition> getByLevel(String level,String num,String name);


}
