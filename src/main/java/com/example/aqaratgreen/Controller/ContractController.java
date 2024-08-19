package com.example.aqaratgreen.Controller;


import com.example.aqaratgreen.Model.Contract;
import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.Introspection;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contract")
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/get")
    public ResponseEntity getContract() {

        return ResponseEntity.status(200).body(contractService.getAllContracts());
    }



    @PostMapping("/add/{unitId}/{investorId}/{clientId}")// add new contract
    public ResponseEntity addContract(@PathVariable Integer unitId ,@PathVariable Integer investorId ,@PathVariable Integer clientId,@Valid @RequestBody Contract contract, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        contractService.addContract(unitId,investorId,clientId,contract);
        return ResponseEntity.status(201).body(" contract Added Successfully");
    }


    @PutMapping("/update/{id}")// update contract by id
    public ResponseEntity updateContract(@PathVariable Integer id ,@Valid@RequestBody Contract contract ,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        contractService.updateContract(id,contract);
        return ResponseEntity.status(201).body("contract Updated Successfully");
    }


    @DeleteMapping("/delete/{id}")// delete contract by id
    public ResponseEntity deleteContract(@PathVariable Integer id){
        contractService.deleteContract(id);
        return ResponseEntity.status(201).body("contract Deleted Successfully");
    }

  /// ----------------------------------------- End Point -------------------------------

    @PostMapping("/applyDiscount/{contractId}")// تطبيق خصم للعميل عن طريق التاكد من العقد اذا دفع مقدم 6 اشهر او مر على التاريخ سنة
    public ResponseEntity<Double> applyDiscount(@PathVariable Integer contractId) {
        return ResponseEntity.status(200).body(contractService.applyDiscount(contractId));
    }

    @PostMapping("/Payment-updated/{contractId}/{paymentAmount}") // عندما يقوم العميل بعملية دفع سيتم تحديث قيمة المبلغ المدفوع في النظام
    public ResponseEntity<String> processPayment(@PathVariable int contractId, @PathVariable double paymentAmount) {
       // contractService.processPayment(contractId,paymentAmount);
        return ResponseEntity.status(200).body(contractService.processPayment(contractId,paymentAmount));
    }

    @GetMapping("/analyzeContracts") // حساب متوسط جميع العقود بالنظام وحساب قيمة إجمالية لجميع العقود و وأعلى قيمة عقد
    public ResponseEntity displayContractAnalyze() {
        return ResponseEntity.status(200).body(contractService.analyzeContractsData());
    }

    @GetMapping("/valid-contract/{contractId}")// التحقق من صحة العقد
    public ResponseEntity displayContractValid(@PathVariable Integer contractId) {
        return ResponseEntity.status(200).body(contractService.validateLeaseContractDocuments(contractId));
    }



}
