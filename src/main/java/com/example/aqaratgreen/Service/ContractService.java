package com.example.aqaratgreen.Service;

import com.example.aqaratgreen.Model.Client;
import com.example.aqaratgreen.Model.Contract;
import com.example.aqaratgreen.Model.Investor;
import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Repository.ClientRepository;
import com.example.aqaratgreen.Repository.ContractRepository;
import com.example.aqaratgreen.Repository.InvestorRepository;
import com.example.aqaratgreen.Repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;
    private final InvestorRepository investorRepository;
    private final UnitRepository unitRepository;

  public List<Contract> getAllContracts() {
      return contractRepository.findAll();
  }

  public void addContract(Integer unitId,Integer investorId,Integer clientId,Contract contract) {
      Investor investor=investorRepository.findInvestorById(investorId);
      Client client=clientRepository.findClientById(clientId);
      Unit unit=unitRepository.findUnitById(unitId);
      if(investor ==null){
          throw new RuntimeException("المستثمر غير موجود في النظام");
      }
      if(client ==null){
          throw new RuntimeException("العميل ليس موجود في النظام");
      }
      if(unit ==null){
          throw new RuntimeException("الوحدة السكنية غير موجوده في النظام ");
      }
      if (contract.getStartDate().isAfter(contract.getEndDate())) {
          throw new RuntimeException("تاريخ البداية يجب أن يكون قبل تاريخ النهاية");
      }
      contractRepository.save(contract);
  }



   public void updateContract(Integer id ,Contract contract) {
        Contract c = contractRepository.findContractById(id);

        if(c == null) {
            throw new RuntimeException("**** العقد غير موجود بالنظام ****");
        }
        c.setDescription(contract.getDescription());
        c.setStateContract(contract.getStateContract());
        c.setEndDate(contract.getEndDate());
        c.setPayState(contract.getPayState());

        contractRepository.save(contract);

   }


  public void deleteContract(Integer id) {
      Contract contract = contractRepository.findContractById(id);
      if (contract == null) {
          throw new RuntimeException("**** العقد غير موجود بالنظام ****");
      }
      contractRepository.delete(contract);
  }

             /// ----------------------- END POINT -----------------------------------------------

    // تطبيق خصم للعميل عن طريق التاكد من العقد اذا دفع مقدم 6 اشهر او مر على التاريخ سنه
    public double applyDiscount(Integer contractId) {
        Contract contract = contractRepository.findContractById(contractId);

        if (contract == null) {
            throw new RuntimeException("العقد غير موجود بالنظام ");
        }

        if (!contract.getContractType().equalsIgnoreCase("rent")) {
            throw new RuntimeException("يجب ان يكون نوع الوحدة السكنية: rent ");
        }

        int rentalPeriod = RentalPeriodInMonths(contractId);

        if (rentalPeriod < 12) {
            throw new RuntimeException("مدة الإيجار أقل من 12 شهرا ، لا استطبع لتطبيق الخصم");
        }

        double totalCost = contract.getTotalContract();
        double discount = 0.0;

        if(rentalPeriod >= 12&&contract.getAdvancePaymentInMonths() >= 6){
            discount = totalCost * 0.15; // %15
        }
         else if (rentalPeriod >= 12) {
            discount = totalCost * 0.1; // 10% discount for rental contracts exceeding one year
        }

        else if (contract.getAdvancePaymentInMonths() >= 6) {
            discount += totalCost * 0.05; // Additional 5% discount for customers paying at least 6 months in advance
        }

        double discounteCost = totalCost - discount;
        contract.setTotalContract(discounteCost);
        return discounteCost;
  }

    public int RentalPeriodInMonths(Integer id) {
        Contract contract = contractRepository.findContractById(id);
        int startYear = contract.getStartDate().getYear();
        int startMonth = contract.getStartDate().getMonthValue();
        int endYear = contract.getEndDate().getYear();
        int endMonth = contract.getEndDate().getMonthValue();
        return Math.abs((endYear - startYear) * 12 + endMonth - startMonth);
    }



        // عندما يقوم العميل بعملية دفع سيتم تحديث قيمة المبلغ المدفوع في النظام
        public String processPayment(int contractId, double paymentAmount) {
            Contract contract = contractRepository.findById(contractId).orElse(null);

            if (contract != null) {
                double currentAmountPaid = contract.getAmountPaid();
                double newAmountPaid = currentAmountPaid + paymentAmount;

                contract.setAmountPaid(newAmountPaid);
                contractRepository.save(contract);
                return "تم تحديث المبلغ المدفوع بنجاح للعقد رقم: " + contractId;
            } else {
                throw new RuntimeException("لم يتم العثور في النظام على العقد برقم: " + contractId+" ");
            }
        }


    // حساب متوسط جميع العقود بالنظام وحساب قيمة إجمالية لجميع العقود و وأعلى قيمة عقد
    public Map<String, Double> analyzeContractsData() {
        Map<String, Double> analysisResults = new HashMap<>();

        List<Contract> allContracts = contractRepository.findAll();
        if (allContracts.isEmpty()) {
            throw new RuntimeException(" لا يوجد عقود في النظام");
        }

        double totalContractsValue = 0.0;
        double averageContractsValue = 0.0;
        double maxContractValue = Double.MIN_VALUE;

        for (Contract contract : allContracts) {
            double contractValue = contract.getTotalContract();

            totalContractsValue += contractValue;

            if (contractValue > maxContractValue) {
                maxContractValue = contractValue;
            }
        }

        int numberOfContracts = allContracts.size(); // حساب متوسط قيمة العقود
        if (numberOfContracts > 0) {
            averageContractsValue = totalContractsValue / numberOfContracts;
        }

        analysisResults.put("إجمالي قيمة العقود", totalContractsValue);
        analysisResults.put("متوسط قيمة العقود", averageContractsValue);
        analysisResults.put("اعلى قيمة عقد ", maxContractValue);
        return analysisResults;
    }



    // توثيق عقود الإيجار والتحقق من المستندات المطلوبة
    public String validateLeaseContractDocuments(Integer id) {
      if (contractRepository.findContractById(id) == null) {
          throw new RuntimeException(" **** لايوجد عقد بالنظام بهذا الرقم *** ");
      }
        Contract contract = contractRepository.findContractById(id);
        if (contract.getStartDate() == null ||
                contract.getEndDate() == null ||
                contract.getDescription() == null ||
                contract.getPayState() == null ||
                contract.getEstateMediatorName() == null ||
                contract.getStateContract() == null ||
                contract.getAdvancePaymentInMonths() <= 0 ||
                contract.getTotalContract() <= 0 ||
                contract.getAmountPaid() < 0 ||
                !contract.isTermsAndConditions() ||
                contract.getUnitId() == null ||
                contract.getCriteriaId() == null ||
                contract.getClientId() == null ||
                contract.getInvestorId() == null) {
            throw new RuntimeException("فشل التحقق من صحة العقد");
        }
        return "تم توثيق العقد بنجاح";
    }





    // -------------- jjj ----------------------------------------------
//        // ميثود لعرض قائمة العقود المدفوعة والغير مدفوعة
//        public List<Contract> displayPaidAndUnpaidContracts() {
//        List<Contract> allContracts = contractRepository.findAll();
//        List<Contract> paidContracts = new ArrayList<>();
//        List<Contract> unpaidContracts = new ArrayList<>();
//
//        for (Contract contract : allContracts) {
//            if ("paid".equalsIgnoreCase(contract.getPayState())) {
//                paidContracts.add(contract);
//            } else {
//                unpaidContracts.add(contract);
//            }
//        }
//        System.out.println("قائمة العقود المدفوعة:");
//        for (Contract contract : paidContracts) {
//            System.out.println("Contract ID: " + contract.getId() + " - تم الدفع بالكامل");
//        }
//        System.out.println("\nقائمة العقود الغير مدفوعة:");
//        for (Contract contract : unpaidContracts) {
//            double amountDue = contract.getTotalContract() - contract.getAmountPaid();
//            System.out.println("Contract ID: " + contract.getId() + " - المبلغ المتبقي: " + amountDue);
//        }
//        return allContracts;
//    }



    //    // استرجاع قائمة بالعقود لمالك معين
//    public List<Contract> getContractByOwner(Integer investorId) {
//        List<Contract> listByOwner = new ArrayList<>();
//
//        for (Contract contracts : getAllContracts()) {
//            if (contracts.getInvestorId() == investorId) {
//                listByOwner.add(contracts);
//            }
//        }
//        return listByOwner;
//    }


}
