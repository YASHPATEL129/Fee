package com.feeManagmentSystem.repository;

import com.feeManagmentSystem.entity.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountantRepository extends JpaRepository<Accountant , Long> {

    Accountant findByEmailAndPassword(String email, String password);
}
