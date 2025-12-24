package uz.javacourse.jgcp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;
import uz.javacourse.jgcp.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // основные методы поиска
    @Query("SELECT u FROM User u WHERE u.pinfl = :pinfl")
    Optional<User> findByPinfl(@Param("pinfl") String pinfl);

    // проверка уникальности
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.pinfl = :pinfl")
    boolean existsByPinfl(@Param("pinfl") String pinfl);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.phoneNumber = :phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // поиск по имени с пагинацией
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Slice<User> findByFullNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // живые пользователи с пагинацией
    @Query("SELECT u FROM User u WHERE u.deathDate IS NULL")
    Slice<User> findByDeathDateIsNull(Pageable pageable);

    // умершие пользователи с пагинацией
    @Query("SELECT u FROM User u WHERE u.deathDate IS NOT NULL")
    Slice<User> findByDeathDateIsNotNull(Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.deathDate IS NULL")
    long countByDeathDateIsNull();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deathDate IS NOT NULL")
    long countByDeathDateIsNotNull();

    // поиск по дате смерти с пагинацией
    @Query("SELECT u FROM User u WHERE u.deathDate BETWEEN :start AND :end")
    Slice<User> findByDeathDateBetween(@Param("start") LocalDate start,
                                      @Param("end") LocalDate end,
                                      Pageable pageable);

    // документы
    @Query("SELECT u FROM User u WHERE u.expiryDate < :date")
    Slice<User> findByExpiryDateBefore(@Param("date") LocalDate date, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.expiryDate BETWEEN :start AND :end")
    Slice<User> findByExpiryDateBetween(@Param("start") LocalDate start,
                                       @Param("end") LocalDate end,
                                       Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.documentType = :documentType")
    Slice<User> findByDocumentType(@Param("documentType") DocumentType documentType, Pageable pageable);

    // демографическая статистика
    @Query("SELECT u FROM User u WHERE u.gender = :gender")
    Slice<User> findByGender(@Param("gender") Gender gender, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.gender = :gender")
    long countByGender(@Param("gender") Gender gender);

    @Query("SELECT u FROM User u WHERE u.citizenship = :citizenship")
    Slice<User> findByCitizenship(@Param("citizenship") String citizenship, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.citizenship = :citizenship")
    long countByCitizenship(@Param("citizenship") String citizenship);

    @Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge")
    Slice<User> findByAgeBetween(@Param("minAge") Integer minAge,
                                @Param("maxAge") Integer maxAge,
                                Pageable pageable);

    // комбинированные запросы
    @Query("SELECT u FROM User u WHERE u.deathDate IS NULL AND u.expiryDate < :date")
    Slice<User> findByDeathDateIsNullAndExpiryDateBefore(@Param("date") LocalDate date, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.gender = :gender AND u.age BETWEEN :minAge AND :maxAge")
    Slice<User> findByGenderAndAgeBetween(@Param("gender") Gender gender,
                                         @Param("minAge") Integer minAge,
                                         @Param("maxAge") Integer maxAge,
                                         Pageable pageable);

    // метод для получения всех пользователей с пагинацией
    Slice<User> findAllBy(Pageable pageable);

    //  KEYSET (CURSOR-BASED) ПАГИНАЦИЯ
    // Быстрая пагинация через afterId - не зависит от глубины страницы

    @Query("SELECT u FROM User u WHERE u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findAllByIdGreaterThan(@Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByFullNameContainingIgnoreCaseAndIdGreaterThan(@Param("name") String name, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deathDate IS NULL AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByDeathDateIsNullAndIdGreaterThan(@Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deathDate IS NOT NULL AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByDeathDateIsNotNullAndIdGreaterThan(@Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deathDate BETWEEN :start AND :end AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByDeathDateBetweenAndIdGreaterThan(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.expiryDate < :date AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByExpiryDateBeforeAndIdGreaterThan(@Param("date") LocalDate date, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.expiryDate BETWEEN :start AND :end AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByExpiryDateBetweenAndIdGreaterThan(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.documentType = :documentType AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByDocumentTypeAndIdGreaterThan(@Param("documentType") DocumentType documentType, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.gender = :gender AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByGenderAndIdGreaterThan(@Param("gender") Gender gender, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.citizenship = :citizenship AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByCitizenshipAndIdGreaterThan(@Param("citizenship") String citizenship, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByAgeBetweenAndIdGreaterThan(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deathDate IS NULL AND u.expiryDate < :date AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByDeathDateIsNullAndExpiryDateBeforeAndIdGreaterThan(@Param("date") LocalDate date, @Param("afterId") Long afterId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.gender = :gender AND u.age BETWEEN :minAge AND :maxAge AND u.id > :afterId ORDER BY u.id ASC")
    Slice<User> findByGenderAndAgeBetweenAndIdGreaterThan(@Param("gender") Gender gender, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, @Param("afterId") Long afterId, Pageable pageable);

    // статистика - count методы
    @Query("SELECT COUNT(u) FROM User u WHERE u.expiryDate < :date")
    long countByExpiryDateBefore(@Param("date") LocalDate date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.expiryDate BETWEEN :start AND :end")
    long countByExpiryDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(u) FROM User u WHERE u.documentType = :documentType")
    long countByDocumentType(@Param("documentType") DocumentType documentType);

    @Query("SELECT COUNT(u) FROM User u WHERE u.age BETWEEN :minAge AND :maxAge")
    long countByAgeBetween(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    @Query("SELECT COUNT(u) FROM User u WHERE u.deathDate BETWEEN :start AND :end")
    long countByDeathDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(u) FROM User u WHERE u.deathDate IS NULL AND u.expiryDate < :date")
    long countByDeathDateIsNullAndExpiryDateBefore(@Param("date") LocalDate date);

    @Query("SELECT COUNT(u) FROM User u WHERE u.gender = :gender AND u.age BETWEEN :minAge AND :maxAge")
    long countByGenderAndAgeBetween(@Param("gender") Gender gender, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}
