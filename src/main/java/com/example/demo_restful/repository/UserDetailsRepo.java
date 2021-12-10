package com.example.demo_restful.repository;

import com.example.demo_restful.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User, String> {

}
