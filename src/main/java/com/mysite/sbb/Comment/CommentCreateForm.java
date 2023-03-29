package com.mysite.sbb.Comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentCreateForm {

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
