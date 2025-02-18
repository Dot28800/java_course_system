package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Integer> {
    @Query(value = "from Practice where ?1=0 or ?1=student.studentId")
    List<Practice> findByStudentStudentId(Integer studentId);
    @Query(value = "from Practice where ?1=student.studentId")
    List<Practice> findByStudentId(Integer studentId);

    @Query(value = "from Practice where student.person.num=?1 and student.person.name=?2")
    List<Practice> findByStudentNumAndName(String num,String name);
}



