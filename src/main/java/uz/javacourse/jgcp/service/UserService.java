package uz.javacourse.jgcp.service;

import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto requestDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByPinfl(String pinfl);

    boolean isUserAlive(Long id);

    UserResponseDto markUserAsDeceased(Long id, LocalDate deathDate);
}
