package lk.acpt.user.service.impl;

import lk.acpt.user.dto.LoginRequestDto;
import lk.acpt.user.dto.LoginResponseDto;
import lk.acpt.user.dto.UserDto;
import lk.acpt.user.entity.User;
import lk.acpt.user.repository.UserRepository;
import lk.acpt.user.service.AuthService;
import lk.acpt.user.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElse(null);
        if (user == null || !bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return null;
        }
        UserDto userDto = modelMapper.map(user, UserDto.class);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new LoginResponseDto(userDto, token);
    }
}

