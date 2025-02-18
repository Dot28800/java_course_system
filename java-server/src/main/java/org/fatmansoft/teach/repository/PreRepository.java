package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.PreInformation;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Max;
import java.util.List;
import java.util.Optional;

public interface PreRepository extends JpaRepository<PreInformation,Integer> {
    @Query(value = "from PreInformation where ?1='' or student.person.num like %?1% or student.person.name like %?1% ORDER BY student.studentId")//可以对输入的numNa me进行判断，为空则返回所有记录，否则在num和name两个字段里面查找所有包含 输入的numName的子串的记录
    List<PreInformation> findPreListByNumName(String numName);//可以输入num或者name进行查询

    @Query(value = "from PreInformation where sourcePlace=?1 or ?1=''")
    List<PreInformation> findBySourcePlace(String sourcePlace);


    @Query(value = "from PreInformation where (?1='' or student.person.num like %?1% or student.person.name like %?1%) and(?2='' or ?2=sourcePlace) ORDER BY student.studentId")
    List<PreInformation> comprehensiveSearch(String numName,String province);


    @Query(value = "from PreInformation where ?1=student.studentId")
    Optional<PreInformation> findByStudentStudentId(Integer studentId);

    @Query(value = "from PreInformation where sourcePlace=?1")
    List<PreInformation> findByProvince(String province);



}
