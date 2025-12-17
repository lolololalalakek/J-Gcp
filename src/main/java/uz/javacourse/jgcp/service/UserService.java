package uz.javacourse.jgcp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.javacourse.jgcp.constant.enums.entity.DocumentType;
import uz.javacourse.jgcp.constant.enums.entity.Gender;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto requestDto);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByPinfl(String pinfl);

    boolean isUserAlive(Long id);

    MarkDeceasedResponseDto markUserAsDeceased(Long id, LocalDate deathDate);

    // поиск по имени
    List<UserResponseDto> searchByName(String name);

    // живые vs умершие пользователи
    List<UserResponseDto> getAllAliveUsers();

    List<UserResponseDto> getAllDeceasedUsers();

    long getAliveUsersCount();

    long getDeceasedUsersCount();

    // поиск по дате смерти
    List<UserResponseDto> getUsersDeceasedBetween(LocalDate start, LocalDate end);

    // документы
    List<UserResponseDto> getUsersWithExpiredDocuments();

    List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end);

    List<UserResponseDto> getUsersByDocumentType(DocumentType documentType);

    // демографическая статистика
    List<UserResponseDto> getUsersByGender(Gender gender);

    long getUsersCountByGender(Gender gender);

    List<UserResponseDto> getUsersByCitizenship(String citizenship);

    List<UserResponseDto> getUsersByAgeRange(Integer minAge, Integer maxAge);

    // комбинированные запросы
    List<UserResponseDto> getAliveUsersWithExpiredDocuments();

    List<UserResponseDto> getUsersByGenderAndAgeRange(Gender gender, Integer minAge, Integer maxAge);
}
