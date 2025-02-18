package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.CourseAttendance;
import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepository extends JpaRepository<Homework,Integer> {

    @Query(value = "from Homework where (?1=0 or ?1=courseChoose.student.studentId) and (?2=0 or ?2=courseChoose.course.courseId)")
    List<Homework> findByStudentCourse(Integer studentId,Integer courseId);

    @Query(value="from Homework where ( courseChoose.student.studentId=?1) and ( courseChoose.course.courseId=?2)" )
    Optional<Homework> findByStudentOrCourse(Integer studentId, Integer courseId);

    @Query(value = "from Homework where courseChoose.student.studentId=?1 and courseChoose.course.courseId=?2")
    Optional<Homework> findOpByStudentCourse(Integer studentId, Integer courseId);
    @Query(value = "from Homework where  courseChoose.course.courseId=?1")
    List<Homework> findByCourseChooseCourseId(Integer courseId);

    @Query(value = "from Homework where courseChoose.student.studentId=?1")
    List<Homework> findByCourseChooseStudentId(Integer studentId);

    @Query(value = "from Homework where courseChoose.id=?1")
    List<Homework> findByCourseChooseId(Integer courseChooseId);

    @Query(value = "from Homework  where courseChoose.student.person.num=?1 and courseChoose.student.person.name=?2 and (?3=courseChoose.course.courseId or ?3=0)")
    List<Homework> findByStudentNumAndName(String num,String name,Integer courseId);
}
