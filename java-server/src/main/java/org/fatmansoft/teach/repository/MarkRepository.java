package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark,Integer> {

    @Query(value = "select max(markId) from Mark  ")
    Integer getMaxId();



    @Query(value = "from Mark where courseChoose.id=?1")
    Optional<Mark> getByCourseChooseId(Integer id);

    @Query(value = "from Mark  where courseChoose.course.courseId=?1")
    List<Mark> getByCourseId(Integer courseId);

    @Query(value = "from Mark where courseChoose.student.studentId=?1")
    List<Mark> getByStudentId(Integer studentId);

    @Query(value = "from  Mark where (?1=0 or courseChoose.student.studentId=?1) and (?2=0 or courseChoose.course.courseId=?2) ORDER BY rank")
    List<Mark> getByCourseOrStudent(Integer studentId,Integer courseId);

    @Query(value = "from Mark where (?1=courseChoose.student.person.num) and (?2=courseChoose.student.person.name) and (?3=courseChoose.course.courseId or ?3=0)")
    List<Mark> getByStudentNumName(String num,String name,Integer courseId);

    List<Mark> findAll();
}
