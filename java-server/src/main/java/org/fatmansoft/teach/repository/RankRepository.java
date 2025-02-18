package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank,Integer> {
    @Query(value = "from Rank where student.studentId=?1")
    Optional<Rank> getByStudentId(Integer studentId);

    @Query(value = "from Rank where ?1='' or student.person.num like %?1% or student.person.name like %?1% ORDER BY totalRank")
    List<Rank> getByStudentNumName(String numName);

    @Query(value = "select max(rankId) from Rank  ")
    Integer getMaxId();

    @Query(value = "from Rank where totalRank=?1 or ?1=''")
    List<Rank> getByTotalRank(String rank);
}
