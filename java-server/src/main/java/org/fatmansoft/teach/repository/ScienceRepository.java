package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Science;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScienceRepository extends JpaRepository<Science,Integer> {
    @Query(value = "from Science where (?1='' or student.person.num like %?1% or student.person.name like %?1%) and (?2='' or teacherName like %?2%) ")
    List<Science> findScienceListByNumName(String numName,String teacherName);


    @Query(value = "from Science where ?1= student.person.num")
    Optional<Science> findByPersonNum(String personNum);

    @Query(value = "from Science where ?1=student.studentId")
    List<Science> getByStudentId(Integer studentId);

    @Query(value = "from Science where ?1=student.person.num and ?2=student.person.name and (?3='' or ?3=teacherName)")
    List<Science> getOwn(String num,String name,String teacherName);
}

