package ism.atelier.atelier.web.dto.response;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String avatar;

    public AuthResponseDto(String token, String refreshToken, Long id, String email,
                           String firstName, String lastName, String role, String avatar) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.avatar = avatar;
    }
}