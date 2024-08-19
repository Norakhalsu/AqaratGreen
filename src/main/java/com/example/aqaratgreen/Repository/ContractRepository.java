package com.example.aqaratgreen.Repository;

import com.example.aqaratgreen.Model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

      Contract findContractById(int id);


      List<Contract> findContractsByClientId(Integer clientId);
      List<Contract> findContractsByInvestorId(Integer investorId);


}
