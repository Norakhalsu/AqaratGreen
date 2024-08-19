package com.example.aqaratgreen.Repository;

import com.example.aqaratgreen.Model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer> {
      Investor findInvestorById(int id);


}
