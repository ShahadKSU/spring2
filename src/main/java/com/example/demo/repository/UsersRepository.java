package com.example.demo.repository;

import com.example.demo.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {

    Users findByUsername(String UserName);
}
