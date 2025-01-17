package com.feeManagmentSystem.repository;

import com.feeManagmentSystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,  Long> {
    Admin findByUsernameAndPassword(String username, String password);
}
