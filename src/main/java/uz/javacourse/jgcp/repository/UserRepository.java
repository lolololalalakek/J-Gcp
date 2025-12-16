package uz.javacourse.jgcp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.javacourse.jgcp.constant.enums.entity.DocumentType;
import uz.javacourse.jgcp.constant.enums.entity.Gender;
import uz.javacourse.jgcp.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // основные методы поиска
    Optional<User> findByPinfl(String pinfl);

    // проверка уникальности
    boolean existsByPinfl(String pinfl);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    // поиск по имени
    List<User> findByFullNameContainingIgnoreCase(String name);

    // живые vs умершие пользователи
    List<User> findByDeathDateIsNull();

    List<User> findByDeathDateIsNotNull();

    long countByDeathDateIsNull();

    long countByDeathDateIsNotNull();

    // поиск по дате смерти
    List<User> findByDeathDateBetween(LocalDate start, LocalDate end);

    // документы
    List<User> findByExpiryDateBefore(LocalDate date);

    List<User> findByExpiryDateBetween(LocalDate start, LocalDate end);

    List<User> findByDocumentType(DocumentType documentType);

    // демографическая статистика
    List<User> findByGender(Gender gender);

    long countByGender(Gender gender);

    List<User> findByCitizenship(String citizenship);

    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    // комбинированные запросы
    List<User> findByDeathDateIsNullAndExpiryDateBefore(LocalDate date);

    List<User> findByGenderAndAgeBetween(Gender gender, Integer minAge, Integer maxAge);
}
