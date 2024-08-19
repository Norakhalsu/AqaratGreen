package com.example.aqaratgreen.Controller;


import com.example.aqaratgreen.Model.Client;
import com.example.aqaratgreen.Model.Contract;
import com.example.aqaratgreen.Service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/get")
    public ResponseEntity getClients() {

        return ResponseEntity.status(200).body(clientService.getAllClients());
    }


    @PostMapping("/add")// add new Client
    public ResponseEntity addContract(@Valid @RequestBody Client client, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        clientService.addClient(client);
        return ResponseEntity.status(201).body(" Client Added Successfully");
    }


    @PutMapping("/update/{id}")// update client by id
    public ResponseEntity updateContract(@PathVariable Integer id, @Valid @RequestBody Client client, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        clientService.updateClient(id, client);
        return ResponseEntity.status(201).body("Client Updated Successfully");
    }


    @DeleteMapping("/delete/{id}")// delete client by id
    public ResponseEntity deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.status(201).body("Client Deleted Successfully");
    }

   // -------------------------------------- End Point ---------------------------------------

    @GetMapping("/unit-OfferType/{offerType}")// العميل يستعلم عن قائمة بوحدات (الايجار) و وحدات (الشراء)
    public ResponseEntity getUnitOfferType(@PathVariable String offerType) {
        return ResponseEntity.status(200).body(clientService.getUnitOfferType(offerType));
    }

    @GetMapping("/average/{clientId}") // حساب متوسط العقود لعميل معين
    public ResponseEntity getAverageContractValue(@PathVariable Integer clientId) {
        return ResponseEntity.status(200).body(clientService.calculateAverageContractValue(clientId));
    }

    @GetMapping("/expired-and-active/{clientId}")//قائمتان للعقود المنتهيه و المستمره
    public ResponseEntity expiredAndActiveContract(@PathVariable Integer clientId) {
        return ResponseEntity.status(200).body(clientService.getExpiredAndActiveContractsForClient(clientId));
    }

    @PostMapping("/collect/{clientId}/{unitId}")// تحصيل الإيجار بشكل شخصي لوحدة سكنية معينة
    public ResponseEntity collectRent(@PathVariable Integer clientId, @PathVariable Integer unitId) {
        return ResponseEntity.status(200).body(clientService.collectRent(clientId,unitId));
    }



}