package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Homework;
import org.fatmansoft.teach.models.Honor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HonorRepository extends JpaRepository<Honor,Integer> {
    @Query(value = "from Honor where ?1=0 or ?1=student.studentId")
    List<Honor> findByStudentStudentId(Integer studentId);

    @Query(value = "from Honor where  ?1=student.studentId")
    List<Honor> findByStudentId(Integer studentId);

    @Query(value = "from Honor where level=?1 and student.person.num=?2")
    List<Honor> findByLevelAndUser(String title,String userNum);

    @Query(value = "from Honor where student.person.num =?1 and student.person.name=?2 and (?3='' or level=?3) ")
    List<Honor> findByNumNameLevel(String num,String name,String level);
}
