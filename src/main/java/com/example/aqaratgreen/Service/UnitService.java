package com.example.aqaratgreen.Service;


import com.example.aqaratgreen.Model.*;
import com.example.aqaratgreen.Repository.ClientRepository;
import com.example.aqaratgreen.Repository.CriteriaRepository;
import com.example.aqaratgreen.Repository.InvestorRepository;
import com.example.aqaratgreen.Repository.UnitRepository;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnitService {


    private final UnitRepository unitRepository;
    private final CriteriaRepository criteriaRepository;
    private final ClientRepository clientRepository;
    private final InvestorRepository investorRepository;

    public List<Unit> getAllUnits() {
       return unitRepository.findAll();
    }

    public void addUnit(Unit unit) {
        if (unit.getOfferType().equalsIgnoreCase("rent")) {
            unitRepository.save(unit);
        } else if (unit.getOfferType().equalsIgnoreCase("buy")) {
            unitRepository.save(unit);
        } else {
              throw new RuntimeException(" نوع العرض غير معروف، يرجى تحديد 'rent' أو 'buy' ");
        }
    }

    public void deleteUnit(Integer id) {
        Unit u=unitRepository.findUnitById(id);
        if (u ==null) {
            throw new  RuntimeException("No Unit found with id "+id);
        }
        unitRepository.delete(u);
    }


    public void updateUnit(Integer id,Unit unit) {
        Unit u=unitRepository.findUnitById(id);
        if (u ==null) {
            throw new  RuntimeException("No Unit found with id "+id);
        }
        u.setName(unit.getName());
        u.setDescription(unit.getDescription());
        u.setArea(unit.getArea());
        u.setAvailability(unit.isAvailability());
        u.setOfferType(unit.getOfferType());
        u.setPrice(unit.getPrice());
        u.setRooms(unit.getRooms());
        unitRepository.save(u);
    }

// --------------------------------- End Point ----------------------------------

    // حساب التكلفه الاضافيه لعدد الغرف الاضافية
    public double calculateMonthlyRentCost(Integer unitId, int additionalRooms) {
        Unit unit = unitRepository.findUnitById(unitId);
        if (unit==null) {
            throw new  RuntimeException(" الوحدة السكنية غير موجوده بالنظام "+unitId+" *****");
        }
        else if (!unit.getOfferType().equalsIgnoreCase("rent")) {
            throw new RuntimeException(" ****** الوحدة ليست للإيجار ****** ");
        }
        double baseCost = unit.getPrice();
        double additionalCostPerRoom = 650; // تكلفة إضافية لكل غرفة
        double totalCost = baseCost + (additionalRooms * additionalCostPerRoom);
        return totalCost;
    }



    // حساب مستوى استدامة المبنى
    public String calculateSustainabil(Integer unitId) {
        Unit unit = unitRepository.findUnitById(unitId);
        if (unit == null) {
            throw new RuntimeException("***** لايوجد بالنظام وحدة سكنية برقم  " + unitId+"*******");
        }

        Criteria criteria = criteriaRepository.findCriteriaById(unit.getCriteriaId());
        if (criteria == null) {
            throw new RuntimeException("******لا يوجد خصائص برقم  " + unitId+" ******");
        }

        double sustainability = 0; // الاستدامه

        if (criteria.getEnergyEfficiencyRating() >= 8) {
            sustainability += 30;
        } else if (criteria.getEnergyEfficiencyRating() >= 6) {
            sustainability += 20;
        } else if (criteria.getEnergyEfficiencyRating() >= 5) {
            sustainability += 10;
        }
        if (criteria.isRecyclingFacilities() && criteria.isSolarPanels()) {
            sustainability += 40;
        } else if (criteria.isRecyclingFacilities() || criteria.isSolarPanels()) {
            sustainability += 20;
        }
        if (criteria.isWaterConservationFeatures() && "Eco-Friendly".equals(criteria.getGreenBuildingMaterials())) {
            sustainability += 30;
        } else {
            sustainability += 15;
        }
        if (sustainability >= 90) {
            return "هذا المبنى لديه استدامة فائقة!";
        } else if (sustainability >= 70) {
            return " هذا المبنى يتفوق في الاستدامة.";
        } else if (sustainability >= 50) {
            return " يوضح هذا المبنى الاستدامة الجيدة.";
        } else {
            return " هذا المبنى لديه القدرة على زيادة الاستدامة.";
        }
    }


    //  حساب المبلغ الذي دفعه العميل والمبلغ المتبقي له لدفعه لوحدة معينة
    public String PaymentAndRemainAmountForUnit(Integer clientId, Integer unitId) {
        Unit unit = unitRepository.findUnitById(unitId);
        Client client = clientRepository.findClientById(clientId);

        if (client == null) {
            throw new RuntimeException(" العميل برقم ****" + clientId + " .****ليس في النظام");
        }

        if (unit == null) {
            throw new RuntimeException(" الوحدة السكنية برقم**** " + unitId + ".**** ليست في النظام");
        }

        // Calculating the amount the client paid for the unit
        double currentTotalAmountPaid = client.getTotalAmountPaid();

        // Setting the new payment amount for the client
        double paymentAmount = 500.0; // You can adjust this value
        client.setTotalAmountPaid(currentTotalAmountPaid + paymentAmount);

        // Calculating the remaining amount for the client to pay for the unit
        double totalAmountDue = unit.getPrice();
        double remainingAmount = totalAmountDue - client.getTotalAmountPaid();

        if (remainingAmount < 0) {
            throw new RuntimeException("**** Error: Total amount paid exceeds total amount due for unit " + unit.getName());
        } else if (remainingAmount == 0) {
            return "**** No remaining amount for unit " + unit.getName() + ". Payment is complete for client " + client.getFullName() + ". Amount paid: " + currentTotalAmountPaid;
        } else {
            return "**** Remaining amount to pay for unit " + unit.getName() + " for client " + client.getFullName() + ": " + remainingAmount + ". Amount paid: " + currentTotalAmountPaid;
        }
    }



// توقعات الارباح للوحدات السكنية بناء على المدينه و الحي و المساحة خلال السنه الحالية
public Map<String,List<Unit>> expectedProfitUnitPerYear() {

    List<Unit> allUnits = getAllUnits();
    List<Unit> expectedList = new ArrayList<>();

    if (allUnits == null || allUnits.isEmpty()) {
        throw new RuntimeException("**** لايوجد وحدات سكنية متوفرة في النظام ****");
    } else {

        for (Unit unit : allUnits) {
            if (unit.getCity().equalsIgnoreCase("riyadh")) {
                if (unit.getDistrictName().equalsIgnoreCase("safarat") || unit.getDistrictName().equalsIgnoreCase("Ergah") || unit.getDistrictName().equalsIgnoreCase("Khozama")) {
                    if (unit.getArea() >= 450.9 && unit.getPrice() < 600000 && unit.getPrice() > 500000) {
                        expectedList.add(unit);
                    }
                } else if (unit.getCity().equalsIgnoreCase("jeddah")) {
                    if (unit.getDistrictName().equalsIgnoreCase("shatea") || unit.getDistrictName().equalsIgnoreCase("naeam")) {
                        if (unit.getArea() >= 420.9 && unit.getPrice() < 500000 && unit.getPrice() > 400000) {
                            expectedList.add(unit);
                        }
                    }
                } else if (unit.getCity().equalsIgnoreCase("khobar")) {
                    if (unit.getDistrictName().equalsIgnoreCase("rawabi") || unit.getDistrictName().equalsIgnoreCase("hamra") || unit.getDistrictName().equalsIgnoreCase("olya")) {
                        if (unit.getArea() >= 300.9 && unit.getPrice() < 700000 && unit.getPrice() > 500000) {
                            expectedList.add(unit);
                        }
                    }
                }
            }
        }
        if (expectedList.isEmpty()) {
            throw new RuntimeException("لايوجد توقعات ربحية للوحدات السكنية الموجودة");
        }
    }
    Map<String, List<Unit>> expectedMap = new HashMap<>();
    expectedMap.put("القائمة المتوقع ربحها خلال السنة الحالية", expectedList);
    return expectedMap;
}




}
