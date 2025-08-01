package ism.atelier.atelier.web.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}