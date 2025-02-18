package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker,Integer> {
    @Query(value = "from Worker where ?1='' or person.num like %?1% or person.name like %?1% ")
    List<Worker> findWorkerListByNumName(String numName);
}
