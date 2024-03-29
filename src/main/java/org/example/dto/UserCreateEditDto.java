package org.example.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.database.entity.Role;
import org.example.validation.annotation.UserInfo;
import org.example.validation.group.CreateAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@FieldNameConstants
@UserInfo(groups = CreateAction.class)
public class UserCreateEditDto {
    @Email
    String username;

    @NotBlank(groups = CreateAction.class)
    String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;

    @Size(min = 3, max = 64)
    String firstname;

    String lastname;

    Role role;

    Integer companyId;

    MultipartFile image;
}
