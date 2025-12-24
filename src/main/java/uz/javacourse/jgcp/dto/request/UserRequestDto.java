package uz.javacourse.jgcp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;

import java.time.LocalDate;

@Builder
public record UserRequestDto(
        // полное имя пользователя
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        String fullName,

        // адрес проживания
        @NotBlank(message = "Address is required")
        @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
        String address,

        // номер телефона
        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        // электронная почта
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        // ссылка на фотографию
        String photoUrl,

        // персональный идентификационный номер (14 символов)
        @NotBlank(message = "PINFL is required")
        @Size(min = 14, max = 14, message = "PINFL must be exactly 14 characters")
        String pinfl,

        // возраст пользователя
        @NotNull(message = "Age is required")
        @Positive(message = "Age must be a positive number")
        @Min(value = 0, message = "Age must be at least 0")
        @Max(value = 150, message = "Age must not exceed 150")
        Integer age,

        // пол (мужской/женский)
        @NotNull(message = "Gender is required")
        Gender gender,

        // тип документа (паспорт, id-карта и др.)
        @NotNull(message = "Document type is required")
        DocumentType documentType,

        // дата выдачи документа
        @NotNull(message = "Issue date is required")
        @PastOrPresent(message = "Issue date must be in the past or present")
        LocalDate issueDate,

        // дата окончания срока действия документа
        @NotNull(message = "Expiry date is required")
        @FutureOrPresent(message = "Expiry date must be in the future or present")
        LocalDate expiryDate,

        // гражданство
        @NotBlank(message = "Citizenship is required")
        @Size(min = 2, max = 100, message = "Citizenship must be between 2 and 100 characters")
        String citizenship,

        // дата смерти (если применимо)
        @PastOrPresent(message = "Death date must be in the past or present")
        LocalDate deathDate
) {
}
