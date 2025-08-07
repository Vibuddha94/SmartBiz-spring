package lk.acpt.user.controller;

import lk.acpt.user.dto.PasswordDto;
import lk.acpt.user.dto.ResponseDto;
import lk.acpt.user.dto.UserDto;
import lk.acpt.user.service.UserService;
import lk.acpt.user.exception.ExistingPasswordException;
import lk.acpt.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> saveUser(@RequestBody UserDto userDto) {
        try {
            UserDto savedUser = userService.saveUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("User saved successfully", savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable Integer id) {
        UserDto user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User not found", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User found successfully", user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User not found", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("User updated successfully", updatedUser));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<ResponseDto> updatePassword(@PathVariable Integer id, @RequestBody PasswordDto password) {
        try {
            UserDto updatedUser = userService.updatePassword(id, password.getPassword());
            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("User not found or password unchanged", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Password updated successfully", updatedUser));
        } catch (ExistingPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("New password cannot be same as old password", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization) {
        if (!jwtUtil.validateJwtToken(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!jwtUtil.getRoleFromToken(authorization).equals("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }
}
