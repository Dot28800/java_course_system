package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * Student 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  Student 表中的最大的student_id;    JPQL 注解
 * Optional<Student> findByPersonPersonId(Integer personId); 根据关联的Person的personId查询获得Option<Student>对象 命名规范
 * Optional<Student> findByPersonNum(String num); 根据关联的Person的num查询获得Option<Student>对象  命名规范
 * List<Student> findByPersonName(String name); 根据关联的Person的name查询获得List<Student>对象集合  可能存在相同姓名的多个学生 命名规范
 * List<Student> findStudentListByNumName(String numName); 根据输入的参数 如果参数为空，查询所有的学生，输入参数不为空，查询学号或姓名包含输入参数串的所有学生集合
 */

public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByPersonPersonId(Integer personId);
    Optional<Student> findByPersonNum(String num);


    @Query(value = "from Student where ?1='' or person.num=?1")
    List<Student> findByNum(String num);
    List<Student> findByPersonName(String name);

    @Query(value = "from Student where ?1='' or person.num like %?1% or person.name like %?1% ORDER BY studentId")//可以对输入的numNa me进行判断，为空则返回所有记录，否则在num和name两个字段里面查找所有包含 输入的numName的子串的记录
    List<Student> findStudentListByNumName(String numName);//可以输入num或者name进行查询

    @Query(value="select s from Student s, User u where u.person.personId = s.person.personId and u.userId=?1")//在student 以及 user两个实体类中分别查找
    Optional<Student> findByUserId(Integer userId);

    @Query(value = "from Student where ?1='' or person.num like %?1% or person.name like %?1% ",
            countQuery = "SELECT count(studentId) from Student where ?1='' or person.num like %?1% or person.name like %?1% ")
    Page<Student> findStudentPageByNumName(String numName,  Pageable pageable);

    @Query(value = "from Student where ?1=person.num or ?1=person.name")
    Optional<Student> findByStudentNumName(String numName);

    @Query(value = "from Student where ?1=person.personId")
    Optional<Student> findByPersonId(Integer personId);

    @Query(value = "from Student  where person.num=?1 and person.name=?2")
    Optional<Student> getByNumAndName(String num,String name);


}
