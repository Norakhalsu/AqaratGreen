package com.example.aqaratgreen.Service;


import com.example.aqaratgreen.Model.Criteria;
import com.example.aqaratgreen.Model.Investor;
import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Repository.CriteriaRepository;
import com.example.aqaratgreen.Repository.InvestorRepository;
import com.example.aqaratgreen.Repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriteriaService {
    private final CriteriaRepository criteriaRepository;
    private final UnitRepository unitRepository;
    private final InvestorRepository investorRepository;

    public List<Criteria> getAllCriteria() {
        return criteriaRepository.findAll();
    }

    public void addCriteria(Criteria criteria) {
        criteriaRepository.save(criteria);
    }


    public void deleteCriteria(Integer id) {
        Criteria c = criteriaRepository.findCriteriaById(id);
        if (c == null) {
            throw new RuntimeException("Criteria not found");
        }
        criteriaRepository.delete(c);
    }

    public void updateCriteria(Integer id, Criteria criteria) {
        Criteria c = criteriaRepository.findCriteriaById(id);
        if (c == null) {
            throw new RuntimeException("Criteria not found");
        }
        c.setEnergyEfficiencyRating(criteria.getEnergyEfficiencyRating());
        criteriaRepository.save(c);
    }


    // ---------------------- End Point ---------------------


       // تشيك على الوحدة السكنية اذا استخدام الكربون يتم فرض الضرائب
        public String calculateCarbonTax(Integer unitId, Integer investorId, Integer criteriaId) {
            Criteria criteria = criteriaRepository.findCriteriaById(criteriaId);
            Unit unit = unitRepository.findUnitById(unitId);
            Investor investor = investorRepository.findInvestorById(investorId);

            if (unit == null) {
                throw new RuntimeException("الوحدة غير موجودة في النظام");
            }

            if (investor == null) {
                throw new RuntimeException("المستثمر غير موجود في النظام");
            }

            double carbonAmountTaxUnit = 1200;
            if(criteria.getCarbonCo2()>400){
                criteria.setCarbon(true);
                double tax = carbonAmountTaxUnit + (unit.getPrice() / 100); // حساب قيمة ضريبة القرض
                unit.setTotalTaxes(tax);
                return "تم فرض الضريبة على الوحدة السكنية رقم: " + unitId + ": " + tax;
            } else {
                throw new RuntimeException("لم يتم تفعيل الضريبة");
            }


        }
















    // فرض الضرائب على الوحدة السكنيةوالمالك لها عند انبعاث الكربون
//    public void applyCarbonTax(Integer unitId, Integer investorId) {
//        Unit unit = unitRepository.findUnitById(unitId);
//        Investor investor = investorRepository.findInvestorById(investorId);
//        Criteria criteria = criteriaRepository.findCriteriaById();
//
//        if (unit == null) {
//            throw new RuntimeException("Unit not found");
//        }
//        if (investor == null) {
//            throw new RuntimeException("Investor not found");
//        }
//        if (criteria.isCarbon() == true) {
//            double unitTax = carbonEmission * taxRate;
//            double investorTax = unitTax * 0.3; // Assuming investor pays 30% of the unit tax
//
//            // Apply tax to the unit
//            unit.setCarbonTax(unit.getCarbonTax() + unitTax);
//
//            // Apply tax to the investor
//            investor.setCarbonTax(investor.getCarbonTax() + investorTax);
//
//            // Save updated unit and investor information
//            unitRepository.save(unit);
//            investorRepository.save(investor);
//        } else {
//            throw new RuntimeException("Carbon tax cannot be applied as unit is not emitting carbon.");
//        }
//
//    }
//


}