package uz.javacourse.jgcp.dto.response;

import lombok.Builder;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;

import java.time.LocalDate;

@Builder
public record UserResponseDto(
        // уникальный идентификатор пользователя
        Long id,
        // полное имя пользователя
        String fullName,
        // адрес проживания
        String address,
        // номер телефона
        String phoneNumber,
        // электронная почта
        String email,
        // ссылка на фотографию
        String photoUrl,
        // персональный идентификационный номер (14 символов)
        String pinfl,
        // возраст пользователя
        Integer age,
        // пол (мужской/женский)
        Gender gender,
        // тип документа (паспорт, id-карта и др.)
        DocumentType documentType,
        // дата выдачи документа
        LocalDate issueDate,
        // дата окончания срока действия документа
        LocalDate expiryDate,
        // гражданство
        String citizenship,
        // дата смерти (если применимо)
        LocalDate deathDate
) {
}
