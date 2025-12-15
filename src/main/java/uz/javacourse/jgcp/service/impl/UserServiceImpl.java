package uz.javacourse.jgcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;
import uz.javacourse.jgcp.exception.BusinessException;
import uz.javacourse.jgcp.exception.UserNotFoundException;
import uz.javacourse.jgcp.mapper.UserMapper;
import uz.javacourse.jgcp.repository.UserRepository;
import uz.javacourse.jgcp.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        validateUniqueness(requestDto);

        User user = userMapper.toEntity(requestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByPinfl(String pinfl) {
        User user = userRepository.findByPinfl(pinfl)
                .orElseThrow(() -> new UserNotFoundException("pinfl", pinfl));
        return userMapper.toResponseDto(user);
    }

    @Override
    public boolean isUserAlive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user.getDeathDate() == null;
    }

    @Override
    public UserResponseDto markUserAsDeceased(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getDeathDate() != null) {
            throw new BusinessException("User already marked as deceased", HttpStatus.CONFLICT);
        }

        user.setDeathDate(deathDate);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    private void validateUniqueness(UserRequestDto requestDto) {
        if (userRepository.existsByPinfl(requestDto.getPinfl())) {
            throw new BusinessException("User already exists with pinfl: " + requestDto.getPinfl(), HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BusinessException("User already exists with email: " + requestDto.getEmail(), HttpStatus.CONFLICT);
        }
    }
}
