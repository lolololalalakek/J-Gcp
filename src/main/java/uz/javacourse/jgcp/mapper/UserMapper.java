package uz.javacourse.jgcp.mapper;

import org.springframework.stereotype.Component;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto requestDto) {
        return User.builder()
                .fullName(requestDto.fullName())
                .address(requestDto.address())
                .phoneNumber(requestDto.phoneNumber())
                .email(requestDto.email())
                .photoUrl(requestDto.photoUrl())
                .pinfl(requestDto.pinfl())
                .age(requestDto.age())
                .gender(requestDto.gender())
                .documentType(requestDto.documentType())
                .issueDate(requestDto.issueDate())
                .expiryDate(requestDto.expiryDate())
                .citizenship(requestDto.citizenship())
                .deathDate(requestDto.deathDate())
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

    public MarkDeceasedResponseDto toMarkDeceasedResponseDto(User user) {
        return MarkDeceasedResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .deathDate(user.getDeathDate())
                .build();
    }
}
