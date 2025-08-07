package lk.acpt.user.service;

import lk.acpt.user.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto saveUser(UserDto userDto) throws Exception;
    UserDto getUserById(Integer id);
    UserDto updateUser(Integer id, UserDto userDto);
    UserDto updatePassword(Integer id, String password);
    Boolean deleteUser(Integer id);
    List<UserDto> getAll();
}
