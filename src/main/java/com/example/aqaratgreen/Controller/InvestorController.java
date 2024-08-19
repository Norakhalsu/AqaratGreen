package com.example.aqaratgreen.Controller;

import com.example.aqaratgreen.Model.Criteria;
import com.example.aqaratgreen.Model.Investor;
import com.example.aqaratgreen.Service.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/investor")
public class InvestorController {

    private final InvestorService investorService;

    @GetMapping("/get")// get all investors
    public ResponseEntity getInvestors() {
        return  ResponseEntity.status(200).body(investorService.getAllInvestors());
    }



    @PostMapping("/add")// add new investor
    public ResponseEntity addInvestor(@Valid @RequestBody Investor investor, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        investorService.addInvestor(investor);
        return ResponseEntity.status(201).body(" Investor Added Successfully");
    }


    @PutMapping("/update/{id}")// update Investor by id
    public ResponseEntity updateInvestor(@PathVariable Integer id ,@Valid@RequestBody Investor investor ,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        investorService.UpdateInvestor(id,investor);
        return ResponseEntity.status(201).body("Investor Updated Successfully");
    }


    @DeleteMapping("/delete/{id}")// delete investor by id
    public ResponseEntity deleteInvestor(@PathVariable Integer id){
        investorService.DeleteInvestor(id);
        return ResponseEntity.status(201).body("investor Deleted Successfully");
    }


     // ------------------------------ End Point -----------------------------------------------

    @GetMapping("/totalRevenue/{investorId}") //حساب اجمالي الايرادات السنوي لمستثمر معين
    public ResponseEntity getTotalRevenueForInvestor(@PathVariable Integer investorId) {
        return ResponseEntity.status(200).body(investorService.getTotalRevenueForInvestor(investorId));
    }


  @PostMapping("/Transfer/{unitId}/{investorId}/{clientId}") // الايجار المنتهي بالتملك : نقل الملكية
  public ResponseEntity endLeaseWithOwnership(@PathVariable Integer unitId, @PathVariable Integer investorId, @PathVariable Integer clientId ){
        investorService.endLeaseWithOwnership(unitId,investorId,clientId);
        return ResponseEntity.status(201).body(" تم تحديث الحالة الى نقل الملكية ");
  }

    @GetMapping("/contractByInvestor/{investorId}")// قائمة العقود لمستثمر معين مصنفة الى المدفوعة والغير مدفوعه
    public ResponseEntity displayContractsByOwner(@PathVariable Integer investorId) {
        return ResponseEntity.status(200).body(investorService.getContractsByPaymentStatus(investorId));
    }


//  // اشتراك المستثمر شهريا والحصول على مزايا
//    @GetMapping("/subscribe/{investorId}/{months}/{subscriptionPlan}")
//    public ResponseEntity subscribeInvestor(@PathVariable Integer investorId , int months, String subscriptionPlan) {
//        investorService.subscribeInvestor(investorId,months,subscriptionPlan);
//        return ResponseEntity.status(201).body("Investor Subscribed Successfully");
//    }


}
