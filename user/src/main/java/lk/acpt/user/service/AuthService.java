package lk.acpt.user.service;

import lk.acpt.user.dto.LoginRequestDto;
import lk.acpt.user.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}

