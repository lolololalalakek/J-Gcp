package uz.javacourse.jgcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.exception.ConflictException;
import uz.javacourse.jgcp.repository.UserRepository;
import uz.javacourse.jgcp.service.UserValidationService;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    // проверяет уникальность email, pinfl и номера телефона перед созданием нового пользователя
    @Override
    public void validateUniqueness(UserRequestDto requestDto) {
        if (userRepository.existsByPinfl(requestDto.pinfl())) {
            throw new ConflictException("User with this PINFL already exists");
        }

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new ConflictException("User with this email already exists");
        }

        if (userRepository.existsByPhoneNumber(requestDto.phoneNumber())) {
            throw new ConflictException("User with this phone number already exists");
        }
    }
}
