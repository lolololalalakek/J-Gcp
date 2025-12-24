package uz.javacourse.jgcp.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;

import java.time.LocalDate;

public interface UserService {

    UserResponseDto createUser(UserRequestDto requestDto);

    Slice<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByPinfl(String pinfl);

    boolean isUserAlive(Long id);

    MarkDeceasedResponseDto markUserAsDeceased(Long id, LocalDate deathDate);

    // поиск по имени с пагинацией
    Slice<UserResponseDto> searchByName(String name, Pageable pageable);

    // живые vs умершие пользователи с пагинацией
    Slice<UserResponseDto> getAllAliveUsers(Pageable pageable);

    Slice<UserResponseDto> getAllDeceasedUsers(Pageable pageable);

    long getAliveUsersCount();

    long getDeceasedUsersCount();

    // поиск по дате смерти с пагинацией
    Slice<UserResponseDto> getUsersDeceasedBetween(LocalDate start, LocalDate end, Pageable pageable);

    // документы
    Slice<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable);

    Slice<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end, Pageable pageable);

    Slice<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable);

    // демографическая статистика
    Slice<UserResponseDto> getUsersByGender(Gender gender, Pageable pageable);

    long getUsersCountByGender(Gender gender);

    Slice<UserResponseDto> getUsersByCitizenship(String citizenship, Pageable pageable);

    Slice<UserResponseDto> getUsersByAgeRange(Integer minAge, Integer maxAge, Pageable pageable);

    // комбинированные запросы
    Slice<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable);

    Slice<UserResponseDto> getUsersByGenderAndAgeRange(Gender gender, Integer minAge, Integer maxAge, Pageable pageable);

    // статистика - count методы
    long getTotalUsersCount();

    long getUsersWithExpiredDocumentsCount();

    long getUsersWithDocumentsExpiringBetweenCount(LocalDate start, LocalDate end);

    long getUsersByDocumentTypeCount(DocumentType documentType);

    long getUsersByAgeRangeCount(Integer minAge, Integer maxAge);

    long getUsersDeceasedBetweenCount(LocalDate start, LocalDate end);

    long getAliveUsersWithExpiredDocumentsCount();

    long getUsersByGenderAndAgeRangeCount(Gender gender, Integer minAge, Integer maxAge);

    long getUsersByCitizenshipCount(String citizenship);

    // === KEYSET (CURSOR-BASED) ПАГИНАЦИЯ ===
    // Быстрая пагинация через afterId - всегда быстро, независимо от глубины

    Slice<UserResponseDto> getAllUsersAfter(Long afterId, int size);

    Slice<UserResponseDto> searchByNameAfter(String name, Long afterId, int size);

    Slice<UserResponseDto> getAllAliveUsersAfter(Long afterId, int size);

    Slice<UserResponseDto> getAllDeceasedUsersAfter(Long afterId, int size);

    Slice<UserResponseDto> getUsersDeceasedBetweenAfter(LocalDate start, LocalDate end, Long afterId, int size);

    Slice<UserResponseDto> getUsersWithExpiredDocumentsAfter(Long afterId, int size);

    Slice<UserResponseDto> getUsersWithDocumentsExpiringBetweenAfter(LocalDate start, LocalDate end, Long afterId, int size);

    Slice<UserResponseDto> getUsersByDocumentTypeAfter(DocumentType documentType, Long afterId, int size);

    Slice<UserResponseDto> getUsersByGenderAfter(Gender gender, Long afterId, int size);

    Slice<UserResponseDto> getUsersByCitizenshipAfter(String citizenship, Long afterId, int size);

    Slice<UserResponseDto> getUsersByAgeRangeAfter(Integer minAge, Integer maxAge, Long afterId, int size);

    Slice<UserResponseDto> getAliveUsersWithExpiredDocumentsAfter(Long afterId, int size);

    Slice<UserResponseDto> getUsersByGenderAndAgeRangeAfter(Gender gender, Integer minAge, Integer maxAge, Long afterId, int size);
}
