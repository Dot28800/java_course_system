package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    @Query(value = "from Activity where ?1=0 or ?1=student.studentId")
    List<Activity> findByStudentStudentId(Integer studentId);

    @Query(value = "from Activity where ?1=student.person.num and ?2=student.person.name and (?3='' or ?3=type)")
    List<Activity> findByTypeAndNumName(String num,String name,String type);
}
