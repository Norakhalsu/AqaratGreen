package com.example.aqaratgreen.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Please 'Full Name' is Require ")
    @Size(max = 30 ,message = "Please a 'full Name' must be max 30 characters  ")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "please 'Full Name' must contain only letters")
    @Column(columnDefinition = "varchar(30) UNIQUE NOT NULL ")
    private String fullName;

    @NotEmpty(message = "Please User 'password' is Require ")
    @Size(min = 8, max = 25,message = "Password must be between 18-25 characters ")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,25}$", message = "Password must contain characters, integers, and symbols")
    @Column(columnDefinition = "varchar(25) UNIQUE NOT NULL")
    private String password;

    @NotEmpty(message = "Please 'Email' is Require ")
    @Size(max = 30 , message = "Please 'Email' length Max 30 characters")
    @Email(message = "Please 'Email' must be valid Format ")
    @Column(columnDefinition = "varchar(30) UNIQUE NOT NULL ")
    private String email;

    @NotEmpty(message = "Please 'Phone Number' is Require ")
    @Size(max = 10 , message = "Please 'Phone Number' length Max 10 Integers ")
    @Pattern(regexp = "^[0-9]*$", message = "Please 'Phone Number' must contain only digits")
    @Column(columnDefinition = "varchar(10) UNIQUE NOT NULL")
    private String phoneNumber;


    @NotNull(message = "Please 'Birthday Date' is Required")
    @Past(message = "Birthday date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "datetime")
    private LocalDate birthdayDate;


    @NotNull(message = "Please 'Age' is Require ")
    @Digits(integer = 2, fraction = 0, message = "Age must be an integer")
    @Positive(message = "Age must be a positive integer")
    @Min(value = 16 ,message = "Sorry 'Age' must be Grater than 16 Years Old")
    // @Column(columnDefinition = "int CHECK(age= >16) ")
    private int age;

    @NotEmpty(message = "Please 'Role' must be not empty")
    @Pattern(regexp = "^(admin|user)$", message = " Please Role must be 'admin' or 'user'")
    // @Column(columnDefinition = "varchar(5) NOT NULL CHECK(role='admin' or role='user' )")
    private String role;

    @NotEmpty(message = "Client must be not Empty ")
    @Pattern(regexp = "^(tenant|buyer)$", message = "Client Status must be 'Tenant' or 'Buyer'")
    private String client; // مستأجر او مشتري

    @NotEmpty(message = "Please Bank Account Number is Required")
    @Size(min = 25 ,max = 25,message = "Bank account Number must be 25 Characters")
    @Pattern(regexp = "^[0-9]*$", message = "Please 'Bank Account Number' must contain only digits")
    @Column(columnDefinition = "varchar(25) UNIQUE NOT NULL")
    private String bankAccountNumber;

    @NotNull(message = " Please -Total Amount Paid- is Required ")
    @Column(columnDefinition = "double NOT NULL")
    private double totalAmountPaid; // المبلغ الإجمالي المدفوع

    @NotNull(message = " Please -Total Amount Paid- is Required ")
    @Column(columnDefinition = "double NOT NULL")
    private double totalAmountDue; // المبلغ الإجمالي المستحق الدفع

    @NotNull(message = " Please -Total Amount Paid- is Required ")
    @Column(columnDefinition = "double NOT NULL")
    private double remainingAmount; // المبلغ المتبقي للدفع

    @Column(columnDefinition = "datetime")// تاريخ سداد الدفعه الاقادمة
    private LocalDate NextPaymentDate;

    @Column(columnDefinition = "datetime")
    private LocalDate registrationDate=LocalDate.now();

    @Positive(message = "Unit Id must be Positive number")
    private Integer unitId;

    private double totalTaxes=0;


}
