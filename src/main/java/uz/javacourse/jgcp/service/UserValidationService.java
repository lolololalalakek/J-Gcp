package uz.javacourse.jgcp.service;

import uz.javacourse.jgcp.dto.request.UserRequestDto;

public interface UserValidationService {

    void validateUniqueness(UserRequestDto requestDto);
}
