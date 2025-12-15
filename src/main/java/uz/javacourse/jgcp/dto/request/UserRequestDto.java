package uz.javacourse.jgcp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class UserRequestDto {

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank
    @Size(min = 5, max = 255)
    private String address;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    private String photoUrl;

    @NotBlank
    @Size(min = 14, max = 14)
    private String pinfl;

    @NotNull
    @Positive
    @Min(0)
    @Max(150)
    private Integer age;

    @NotNull
    private Gender gender;

    @NotNull
    private DocumentType documentType;

    @NotNull
    @Past
    private LocalDate issueDate;

    @NotNull
    @Future
    private LocalDate expiryDate;

    @NotBlank
    @Size(min = 2, max = 100)
    private String citizenship;

    @PastOrPresent
    private LocalDate deathDate;
}
