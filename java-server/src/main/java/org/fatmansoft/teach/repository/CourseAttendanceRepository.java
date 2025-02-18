package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.CourseAttendance;
import org.fatmansoft.teach.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface CourseAttendanceRepository extends JpaRepository<CourseAttendance,Integer> {
    @Query(value="from CourseAttendance where (?1=0 or student.studentId=?1) and (?2=0 or course.courseId=?2)" )
    List<CourseAttendance> findByStudentCourse(Integer studentId, Integer courseId);


    List<CourseAttendance> findByStudentPersonName(String name);

    @Query(value = "select max(attendanceId) from CourseAttendance  ")
    Integer getMaxId();

    @Query(value="from CourseAttendance where ( student.studentId=?1) and ( course.courseId=?2)" )
    Optional<CourseAttendance> findByStudentOrCourse(Integer studentId, Integer courseId);

    @Query(value = "from CourseAttendance where (student.studentId=?1) and (course.courseId=?2)")
    List<CourseAttendance> findByCourseAndStudent(Integer studentId,Integer courseId);

    List<CourseAttendance> findByStudentStudentId(Integer studentId);

    @Query(value = "from CourseAttendance where student.person.num=?1 and student.person.name=?2 and (course.courseId=?3 or ?3=0)")
    List<CourseAttendance> findByNumName(String num,String name,Integer courseId);

    List<CourseAttendance> findByCourse_CourseId(Integer courseId);
}
