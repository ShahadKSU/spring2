package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    private UsersRepository Repository;

    //CRUD : Create - Read - Update - Delete

public Users addUser(Users user){
    user.setId(user.getId());
    user.setAccountNumber(user.getAccountNumber());
    user.setFullName(user.getFullName());
    user.setBalance(user.getBalance());
    user.setUsername(user.getUsername());
    user.setPassword(user.getPassword());
    user.setCardNumber(user.getCardNumber());
    user.setTransactions(user.getTransactions());

    return Repository.save(user);
}

public String findpassword(String username){
    Users user = Repository.findByUsername(username);
    if (user != null) {
        return user.getPassword();
    } else {
        return null;
    }
}
public List<Users> findAllUser(){
     return Repository.findAll();
}

public Users getUserById(String id){

    return Repository.findById(id).get();
}

public Users getUserByUsername(String UserName){
    return Repository.findByUsername(UserName);
}

public Users updateUser (Users Updatedinfo){
    Users user = Repository.findById(Updatedinfo.getId()).get();//Adding get() because the methode is optional
    user.setFullName(Updatedinfo.getFullName());
    return Repository.save(user);
}

public String DeleteUser(String id){
    Repository.deleteById(id);
    return " User Deleted";
}

public String findAccountNumber(String id){
    Users userID = Repository.findById(id).get();
    return userID.getAccountNumber();
}

public String findCardNumber(String id){
        Users userID = Repository.findById(id).get();
        return userID.getCardNumber();
}

public String findFullName(String id){
        Users userID = Repository.findById(id).get();
        return userID.getFullName();
}

public double findBalance(String id){
        Users userID = Repository.findById(id).get();
        return userID.getBalance();
}

public List<Users.Transaction> findTransactions (String id){
        Users userID = Repository.findById(id).get();
        return userID.getTransactions();
}

    public double findJanaPoints(String id){
        Users userID = Repository.findById(id).get();
        return userID.getJanaPoints();
    }

}
