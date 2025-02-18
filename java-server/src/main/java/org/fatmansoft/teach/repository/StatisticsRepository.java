package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {

    //@Query(value = "from Statistics where ?1 = Statistics.studentNum")
    //Optional<Statistics> findStatisticsByStudentNum(String num);


    Optional<Statistics> findByStudentNum(String num);

    @Query(value = "from Statistics  where  studentNum like %?1% or name like %?1% ")
    List<Statistics> findByNumName(String numName);
}