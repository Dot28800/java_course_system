package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course 数据操作接口，主要实现Course数据的查询操作
 */

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {//会根据接口注解和函数来创建具体实现对象重写方法并调用实现
    @Query(value = "from Course where ?1='' or num like %?1% or name like %?1% ")//JPQL写法，第一个参数是空或者
    List<Course> findCourseListByNumName(String numName);

    Optional<Course> findByNum(String num);//返回唯一对象，只能有一个
    @Query(value = "from Course where name=?1")
    Optional<Course> findCourseByName(String name);

    @Query(value = "from  Course where num=?1 or name=?2")
    Optional<Course> findByNumOrName(String num,String name);
    Optional<Course> findByName(String name);//返回多个对象，查出所有该名字的课程

    @Query(value = "from Course where num=?1 and name=?2")
    Optional<Course> findByNumAndName(String num,String name);

    @Query(value = "from Course where num=?1 and name=?2")
    List<Course> getList(String num,String name);
    /*Integer getMaxId();*/
}
