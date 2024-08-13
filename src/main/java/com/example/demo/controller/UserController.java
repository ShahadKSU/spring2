package com.example.demo.controller;
import com.example.demo.model.ActivateBeneficiary;
import com.example.demo.model.BSFAccountResponse;
import com.example.demo.model.CreateBSFBen;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/Users")
public class UserController {

    @Autowired
    private UsersService service;

    @GetMapping("/")
    public String welcomepage(){
        return "Welcome to the spring boor project";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

            return service.login(credentials);
    }

    @GetMapping("/ProductList")
    @ResponseStatus(HttpStatus.OK)
    public Object getProductList(){
        return service.getProductsList();
    }

    @GetMapping("/Transactions")
    @ResponseStatus(HttpStatus.OK)
    public Object getTransactions(){
        return service.getTransactionsList();
    }

    @GetMapping("/getBeneficiaries")
    @ResponseStatus(HttpStatus.OK)
    public Object getBeneficiaries(){
        return service.getBeneficiaries();
    }

    @PostMapping("/validateBSFAccount")
    public BSFAccountResponse validate_BSF_Account(@RequestBody Map<String, String> account){
        return service.validate_BSF_Account(account);
    }

    @PostMapping("/createBSFBeneficiary")
    public CreateBSFBen createBSFBeneficiary(){
        return service.create_BSF_Beneficiary();
    }

    @PostMapping("/activateBeneficiary")
    public ActivateBeneficiary activateBeneficiary(){
        return service.bsfactivateBeneficiary();
    }


    @DeleteMapping("/deleteBeneficiary")
    public String deleteBeneficiary(@RequestBody Map<String, String> BenfSequence){
        return service.deleteBeneficiary(BenfSequence);
    }



}
