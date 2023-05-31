package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;

    @NotEmpty(message = "Please enter your first name.")
    private String first_name;

    @NotEmpty(message = "Please enter your last name.")
    private String last_name;

    @NotEmpty(message = "Please enter your email.")
    @Email
    private String email;

    @NotEmpty(message = "Please enter your password.")
    private String password;

    //@NotEmpty(message = "Please choose an amount.")
    private Float user_amount;
}
