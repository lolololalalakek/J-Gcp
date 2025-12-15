package uz.javacourse.jgcp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.javacourse.jgcp.constant.enums.entity.DocumentType;
import uz.javacourse.jgcp.constant.enums.entity.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String photoUrl;
    private String pinfl;
    private Integer age;
    private Gender gender;
    private DocumentType documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String citizenship;
    private LocalDate deathDate;
}
