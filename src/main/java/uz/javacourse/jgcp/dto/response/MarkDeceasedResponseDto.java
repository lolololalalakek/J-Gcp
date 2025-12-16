package uz.javacourse.jgcp.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MarkDeceasedResponseDto(
        // уникальный идентификатор пользователя
        Long id,
        // полное имя пользователя
        String fullName,
        // дата смерти пользователя
        LocalDate deathDate
) {
}
