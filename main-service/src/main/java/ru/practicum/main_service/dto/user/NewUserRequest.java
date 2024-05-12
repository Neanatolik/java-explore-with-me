package ru.practicum.main_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
    @NotBlank
    @Length(min = 6, max = 254)
    @Email
    private String email;
}
