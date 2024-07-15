package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

        String DBpassword = service.findpassword(username);

        if (DBpassword == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

        boolean login = password.equals(DBpassword);

        if (login) {
            Users user = service.getUserByUsername(username);
            String userID = user.getId();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Successful login");
            responseBody.put("userID", userID);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
//            return ResponseEntity.status(HttpStatus.OK).body("Successful login");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Users createUsers(@RequestBody Users user){

        return service.addUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Users> getAllUsers(){

        return service.findAllUser();
    }

   @GetMapping("/getUserbyId/{id}")
    public Users getuserbyId(@PathVariable String id){
        return service.getUserById(id);
    }

    @GetMapping("/getUserbyUserName/{UserName}")
    public Users getuserbyusername(@PathVariable String UserName){
        return service.getUserByUsername(UserName);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Users Updateuser (@RequestBody Users Updatedinfo) {

        return service.updateUser(Updatedinfo);
    }

    @DeleteMapping("/DeleteAccountNumber/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String DelUser(@PathVariable String id){
        service.DeleteUser(id);
        return id+" Deleted";
    }


//    @GetMapping("/Accountnumber/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public String UserAccountNumber(@PathVariable String id){
//        return service.findAccountNumber(id);
//    }
//
//    @GetMapping("/Cardsnumber/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public String UserCardNumber(@PathVariable String id){
//        return service.findCardNumber(id);
//    }
//
//    @GetMapping("/FullName/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public String UserFullName(@PathVariable String id){
//        return service.findFullName(id);
//    }
//
//    @GetMapping("/Balance/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public double UserBalance(@PathVariable String id){
//        return service.findBalance(id);
//    }
//
//    @GetMapping("/Transactions/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Users.Transaction> UserTransactions(@PathVariable String id){
//        return service.findTransactions(id);
//    }
//
//    @GetMapping("/JanaPoints/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public double UserJanaPoints(@PathVariable String id){
//        return service.findJanaPoints(id);
//    }

}
