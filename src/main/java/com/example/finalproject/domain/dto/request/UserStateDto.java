package com.example.finalproject.domain.dto.request;

import com.example.finalproject.domain.entity.user.UserState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStateDto {
    private Long chatId;
    private UserState state;
}
