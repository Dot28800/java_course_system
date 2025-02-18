package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.Innovative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InnovativeRepository extends JpaRepository<Innovative,Integer> {
    @Query(value = "from Innovative where ?1=0 or ?1=student.studentId")
    List<Innovative> findByStudentStudentId(Integer studentId);

    @Query(value = "from Innovative where  ?1=student.studentId")
    List<Innovative> findByStudentId(Integer studentId);

    @Query(value = "from Innovative where level=?1 and student.person.num=?1")
    List<Innovative> findByLevelAndUser(String title,String userNum);


}

