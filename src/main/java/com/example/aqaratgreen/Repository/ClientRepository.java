package com.example.aqaratgreen.Repository;

import com.example.aqaratgreen.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findClientById(int id);


}
