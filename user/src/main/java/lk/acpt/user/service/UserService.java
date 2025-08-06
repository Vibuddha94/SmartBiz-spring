package lk.acpt.user.service;

import lk.acpt.user.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto saveUser(UserDto userDto) throws Exception;
    UserDto getUserById(Integer id);
    UserDto updateUser(UserDto userDto);
    Boolean deleteUser(Integer id);
    List<UserDto> getAll();
}
