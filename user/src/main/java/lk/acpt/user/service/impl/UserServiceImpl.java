package lk.acpt.user.service.impl;

import lk.acpt.user.dto.UserDto;
import lk.acpt.user.entity.User;
import lk.acpt.user.exception.ExistingPasswordException;
import lk.acpt.user.repository.UserRepository;
import lk.acpt.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> modelMapper.map(value, UserDto.class)).orElse(null);
    }

    @Override
    public UserDto updateUser(Integer id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userPresent = user.get();
            userPresent.setName(userDto.getName());
            userPresent.setEmail(userDto.getEmail());
            userPresent.setRole(userDto.getRole());
            userPresent.setStatus(userDto.getStatus());
            return modelMapper.map(userRepository.save(userPresent), UserDto.class);
        }
        return null;
    }

    @Override
    public UserDto updatePassword(Integer id, String password) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userPresent = user.get();
            if (bCryptPasswordEncoder.matches(password, userPresent.getPassword())) {
                throw new ExistingPasswordException("New password cannot be same as old password");
            }
            userPresent.setPassword(bCryptPasswordEncoder.encode(password));
            return modelMapper.map(userRepository.save(userPresent), UserDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
    }
}
