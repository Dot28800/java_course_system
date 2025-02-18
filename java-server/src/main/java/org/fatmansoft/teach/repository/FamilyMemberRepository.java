package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember,Integer> {
    @Query(value = "from FamilyMember where ?1='' " )
    List<FamilyMember> findByStudentStudentId(String studentId);

    @Query(value = "from FamilyMember where  name like %?1%")
    Optional<FamilyMember> findByMemberName(String memberName);

    @Query(value = "from FamilyMember where ?1='' or student.person.num like %?1% or name like %?1% or student.person.name like %?1%")
    List<FamilyMember> findByNumIdName(String numIdName);

    @Query(value = "from FamilyMember where (?1= student.person.num or ?1=student.person.name) and name=?2")
    Optional<FamilyMember> findByStudentNumNameAndName(String numName,String name);

    @Query(value = "from  FamilyMember  where student.person.name=?1 and name=?2")
    Optional<FamilyMember> findByStudentNumAndMemberName(String studentName,String name);
    @Query(value = "from FamilyMember where student.studentId=?1")
    List<FamilyMember> findByStudent(Integer studentId);


}
