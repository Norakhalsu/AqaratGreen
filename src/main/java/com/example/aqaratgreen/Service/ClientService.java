package com.example.aqaratgreen.Service;


import com.example.aqaratgreen.Model.Client;
import com.example.aqaratgreen.Model.Contract;
import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Repository.ClientRepository;
import com.example.aqaratgreen.Repository.ContractRepository;
import com.example.aqaratgreen.Repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    private final UnitRepository unitRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    public void addClient(Client client) {
        clientRepository.save(client);
    }



    public void updateClient(Integer id ,Client client) {
          Client c=clientRepository.findClientById(id);
        if(c == null) {
            throw new RuntimeException("Client not found");
        }
       c.setEmail(client.getEmail());
        c.setAge(client.getAge());
        c.setPassword(client.getPassword());
        c.setClient(client.getClient());
        c.setPhoneNumber(client.getPhoneNumber());
        c.setRole(client.getRole());
        clientRepository.save(c);

    }


    public void deleteClient(Integer id) {
        Client c = clientRepository.findClientById(id);
        if (c == null) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.delete(c);
    }

       //---------- End Point -----------------------

    // العميل يستعلم عن قائمة بوحدات (الايجار) و وحدات (الشراء)
    public List<Unit> getUnitOfferType(String offerType) {
        List<Unit> listOfferType=unitRepository.SearchRentOrBuyList(offerType);
        if(listOfferType.isEmpty()) {
            throw new RuntimeException("**** لايوجد قائمة وحدات المطلوبه ***");
        }
        return listOfferType;
    }

    //حساب متوسط العقود لعميل معين
    public double calculateAverageContractValue(Integer clientId) {
        List<Contract> clientContracts = contractRepository.findContractsByClientId(clientId);
        if (clientContracts.isEmpty()) {
            throw new RuntimeException("**** لايوجد عقود برقم العميل ****");
        }
        double totalValue = 0.0;
        for (Contract contract : clientContracts) {
            totalValue += contract.getTotalContract();
        }
        return totalValue / clientContracts.size();
    }


     // قائمتان للعقود المنتهيه و المستمره
    public Map<String,List<Contract>> getExpiredAndActiveContractsForClient(int clientId) {
        List<Contract> allContracts = contractRepository.findContractsByClientId(clientId);
        List<Contract> expiredContracts = new ArrayList<>();
        List<Contract> activeContracts = new ArrayList<>();

        if (allContracts.isEmpty()) {
            throw new RuntimeException("**** لا يوجد عقود في النظام ****");
        }
        for (Contract contract : allContracts) {
            if (contract.getStateContract().equalsIgnoreCase("expired")) {
                expiredContracts.add(contract);
            } else {
                activeContracts.add(contract);
            }
        }
        Map<String,List<Contract>> contractMap = new HashMap<>();
        contractMap.put("العقود المنتهية ", expiredContracts);
        contractMap.put("العقودالنشطة ", activeContracts);
        return contractMap;
    }



    // جمع أو تحصيل إيجار من عميل معين لوحدة معينة
    public double collectRent(Integer clientId, Integer unitId) {
        Client client = clientRepository.findClientById(clientId);
        Unit unit = unitRepository.findUnitById(unitId);

        if (unit == null) {
            throw new RuntimeException("****** لايوجد وحدة سكنية بهذا الرقم ******");
        }
        if (client == null) {
            throw new RuntimeException("***** لايوجد عميل بهذا الرقم ******");
        }

        double amountDue = client.getTotalAmountDue();
        double totalAmountPaid = client.getTotalAmountPaid() + amountDue;
        double remainingAmount = amountDue - totalAmountPaid;
        LocalDate nextPaymentDate = LocalDate.now().plusMonths(1);

        unit.setTotalAmountPaid(totalAmountPaid);
        unit.setRemainingAmount(remainingAmount);
        unit.setNextPaymentDate(nextPaymentDate);

        client.setTotalAmountPaid(totalAmountPaid);
        client.setRemainingAmount(remainingAmount);
        client.setNextPaymentDate(nextPaymentDate);

        clientRepository.save(client);
        unitRepository.save(unit);

        return amountDue; // Return the amount due
    }



}
