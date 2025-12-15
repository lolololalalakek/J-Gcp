package uz.javacourse.jgcp.mapper;

import org.springframework.stereotype.Component;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto requestDto) {
        return User.builder()
                .fullName(requestDto.getFullName())
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .email(requestDto.getEmail())
                .photoUrl(requestDto.getPhotoUrl())
                .pinfl(requestDto.getPinfl())
                .age(requestDto.getAge())
                .gender(requestDto.getGender())
                .documentType(requestDto.getDocumentType())
                .issueDate(requestDto.getIssueDate())
                .expiryDate(requestDto.getExpiryDate())
                .citizenship(requestDto.getCitizenship())
                .deathDate(requestDto.getDeathDate())
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .photoUrl(user.getPhotoUrl())
                .pinfl(user.getPinfl())
                .age(user.getAge())
                .gender(user.getGender())
                .documentType(user.getDocumentType())
                .issueDate(user.getIssueDate())
                .expiryDate(user.getExpiryDate())
                .citizenship(user.getCitizenship())
                .deathDate(user.getDeathDate())
                .build();
    }
}
