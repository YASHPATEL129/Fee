package com.feeManagmentSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String sex;
    private String course;
    private double fee;
    private double paid;
    private double due;
    private String address;
    private String contact;
    private String password;
    private String studentFeeStatus;

}
