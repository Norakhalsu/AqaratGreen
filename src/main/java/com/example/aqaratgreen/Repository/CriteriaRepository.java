package com.example.aqaratgreen.Repository;

import com.example.aqaratgreen.Model.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {
    Criteria findCriteriaById(int id);
    Criteria findCriteriaByCarbon(boolean carbon);
}
