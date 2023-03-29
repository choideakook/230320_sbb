package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserModifyForm {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자 ID 는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
}
