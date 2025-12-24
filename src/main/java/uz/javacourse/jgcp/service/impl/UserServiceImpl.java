package uz.javacourse.jgcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;
import uz.javacourse.jgcp.exception.BusinessException;
import uz.javacourse.jgcp.exception.ResourceNotFoundException;
import uz.javacourse.jgcp.exception.UserNotFoundException;
import uz.javacourse.jgcp.mapper.UserMapper;
import uz.javacourse.jgcp.repository.UserRepository;
import uz.javacourse.jgcp.service.UserService;
import uz.javacourse.jgcp.service.UserValidationService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private final TransactionTemplate transactionTemplate;

    // создает нового пользователя в системе после проверки уникальности email и pinfl
    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        userValidationService.validateUniqueness(requestDto);
        User user = userMapper.toEntity(requestDto);

        User savedUser = transactionTemplate.execute(status -> userRepository.save(user));
        return userMapper.toResponseDto(savedUser);
    }

    // возвращает список всех пользователей из базы данных с пагинацией
    @Override
    public Slice<UserResponseDto> getAllUsers(Pageable pageable) {
        // выполняем запрос в базу данных
        Slice<User> users = userRepository.findAllBy(pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // находит и возвращает пользователя по его уникальному идентификатору
    @Override
    public UserResponseDto getUserById(Long id) {
        // выполняем запрос в базу данных
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        // преобразуем user в dto и возвращаем
        return userMapper.toResponseDto(user);
    }

    // находит и возвращает пользователя по его персональному идентификационному номеру (pinfl)
    @Override
    public UserResponseDto getUserByPinfl(String pinfl) {
        // выполняем запрос в базу данных
        User user = userRepository.findByPinfl(pinfl)
                .orElseThrow(() -> new UserNotFoundException("pinfl", pinfl));
        // преобразуем user в dto и возвращаем
        return userMapper.toResponseDto(user);
    }

    // проверяет жив ли пользователь (deathDate == null означает что пользователь жив)
    @Override
    public boolean isUserAlive(Long id) {
        // выполняем запрос в базу данных
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        // проверяем дату смерти и возвращаем результат
        return user.getDeathDate() == null;
    }

    // отмечает пользователя как умершего, устанавливая дату смерти
    @Override
    @Transactional
    public MarkDeceasedResponseDto markUserAsDeceased(Long id, LocalDate deathDate) {
        User updatedUser = transactionTemplate.execute(status -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            if (user.getDeathDate() != null) {
                throw new BusinessException("User already marked as deceased");
            }

            user.setDeathDate(deathDate);
            return userRepository.save(user);
        });

        return userMapper.toMarkDeceasedResponseDto(updatedUser);
    }

    // ищет пользователей по части имени (без учета регистра) с пагинацией
    @Override
    public Slice<UserResponseDto> searchByName(String name, Pageable pageable) {
        // выполняем запрос в базу данных
        Slice<User> users = userRepository.findByFullNameContainingIgnoreCase(name, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает список всех живых пользователей с пагинацией
    @Override
    public Slice<UserResponseDto> getAllAliveUsers(Pageable pageable) {
        // выполняем запрос в базу данных
        Slice<User> users = userRepository.findByDeathDateIsNull(pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает список всех умерших пользователей с пагинацией
    @Override
    public Slice<UserResponseDto> getAllDeceasedUsers(Pageable pageable) {
        // выполняем запрос в базу данных
        Slice<User> users = userRepository.findByDeathDateIsNotNull(pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает количество живых пользователей
    @Override
    public long getAliveUsersCount() {
        // просто возвращаем количество из репозитория
        return userRepository.countByDeathDateIsNull();
    }

    // возвращает количество умерших пользователей
    @Override
    public long getDeceasedUsersCount() {
        // просто возвращаем количество из репозитория
        return userRepository.countByDeathDateIsNotNull();
    }

    // возвращает пользователей умерших в указанном периоде с пагинацией
    @Override
    public Slice<UserResponseDto> getUsersDeceasedBetween(LocalDate start, LocalDate end, Pageable pageable) {
        // выполняем запрос в базу данных
        Slice<User> users = userRepository.findByDeathDateBetween(start, end, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей с истекшими документами
    @Override
    public Slice<UserResponseDto> getUsersWithExpiredDocuments(Pageable pageable) {
        Slice<User> users = userRepository.findByExpiryDateBefore(LocalDate.now(),  pageable);
        return users.map(userMapper::toResponseDto);

    }

    // возвращает пользователей чьи документы истекают в указанном периоде
    @Override
    public Slice<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start,
                                                                      LocalDate end,
                                                                      Pageable pageable) {
        Slice<User> users = userRepository.findByExpiryDateBetween(start, end, pageable);
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей по типу документа
    @Override
    public Slice<UserResponseDto> getUsersByDocumentType(DocumentType documentType, Pageable pageable) {
        Slice<User> users = userRepository.findByDocumentType(documentType, pageable);
        return users.map(userMapper::toResponseDto);

    }

    // возвращает пользователей по полу
    @Override
    public Slice<UserResponseDto> getUsersByGender(Gender gender, Pageable pageable) {
        Slice<User> users = userRepository.findByGender(gender, pageable);
        return users.map(userMapper::toResponseDto);
    }

    // возвращает количество пользователей по полу
    @Override
    public long getUsersCountByGender(Gender gender) {
        return userRepository.countByGender(gender);
    }

    // возвращает пользователей по гражданству
    @Override
    public Slice<UserResponseDto> getUsersByCitizenship(String citizenship, Pageable pageable) {
        Slice<User> users = userRepository.findByCitizenship(citizenship, pageable);
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей в указанном возрастном диапазоне
    @Override
    public Slice<UserResponseDto> getUsersByAgeRange(Integer minAge, Integer maxAge, Pageable pageable) {
        Slice<User> users = userRepository.findByAgeBetween(minAge, maxAge, pageable);
        return users.map(userMapper::toResponseDto);
    }

    // возвращает живых пользователей с истекшими документами
    @Override
    public Slice<UserResponseDto> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        Slice<User> users = userRepository.findByDeathDateIsNullAndExpiryDateBefore(LocalDate.now(), pageable);
        return users.map(userMapper::toResponseDto);

    }

    // возвращает пользователей по полу и возрастному диапазону
    @Override
    public Slice<UserResponseDto> getUsersByGenderAndAgeRange(Gender gender, Integer minAge, Integer maxAge, Pageable pageable) {
        Slice<User> users = userRepository.findByGenderAndAgeBetween(gender, minAge, maxAge, pageable);
        return users.map(userMapper::toResponseDto);
    }

    // статистика - count методы
    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    public long getUsersWithExpiredDocumentsCount() {
        return userRepository.countByExpiryDateBefore(LocalDate.now());
    }

    @Override
    public long getUsersWithDocumentsExpiringBetweenCount(LocalDate start, LocalDate end) {
        return userRepository.countByExpiryDateBetween(start, end);
    }

    @Override
    public long getUsersByDocumentTypeCount(DocumentType documentType) {
        return userRepository.countByDocumentType(documentType);
    }

    @Override
    public long getUsersByAgeRangeCount(Integer minAge, Integer maxAge) {
        return userRepository.countByAgeBetween(minAge, maxAge);
    }

    @Override
    public long getUsersDeceasedBetweenCount(LocalDate start, LocalDate end) {
        return userRepository.countByDeathDateBetween(start, end);
    }

    @Override
    public long getAliveUsersWithExpiredDocumentsCount() {
        return userRepository.countByDeathDateIsNullAndExpiryDateBefore(LocalDate.now());
    }

    @Override
    public long getUsersByGenderAndAgeRangeCount(Gender gender, Integer minAge, Integer maxAge) {
        return userRepository.countByGenderAndAgeBetween(gender, minAge, maxAge);
    }

    @Override
    public long getUsersByCitizenshipCount(String citizenship) {
        return userRepository.countByCitizenship(citizenship);
    }

    // === KEYSET (CURSOR-BASED) ПАГИНАЦИЯ ===
    // Быстрая пагинация через afterId - всегда быстро, независимо от глубины

    // возвращает всех пользователей после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getAllUsersAfter(Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос используя id > afterId для быстрой пагинации
        Slice<User> users = userRepository.findAllByIdGreaterThan(afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // ищет пользователей по имени после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> searchByNameAfter(String name, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем поиск по имени используя id > afterId
        Slice<User> users = userRepository.findByFullNameContainingIgnoreCaseAndIdGreaterThan(name, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает живых пользователей после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getAllAliveUsersAfter(Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос для живых пользователей используя id > afterId
        Slice<User> users = userRepository.findByDeathDateIsNullAndIdGreaterThan(afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает умерших пользователей после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getAllDeceasedUsersAfter(Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос для умерших пользователей используя id > afterId
        Slice<User> users = userRepository.findByDeathDateIsNotNullAndIdGreaterThan(afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей умерших в периоде после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersDeceasedBetweenAfter(LocalDate start, LocalDate end, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по периоду смерти используя id > afterId
        Slice<User> users = userRepository.findByDeathDateBetweenAndIdGreaterThan(start, end, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей с истекшими документами после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersWithExpiredDocumentsAfter(Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос для истекших документов используя id > afterId
        Slice<User> users = userRepository.findByExpiryDateBeforeAndIdGreaterThan(LocalDate.now(), afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей с документами истекающими в периоде после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersWithDocumentsExpiringBetweenAfter(LocalDate start, LocalDate end, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по периоду истечения документов используя id > afterId
        Slice<User> users = userRepository.findByExpiryDateBetweenAndIdGreaterThan(start, end, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей по типу документа после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersByDocumentTypeAfter(DocumentType documentType, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по типу документа используя id > afterId
        Slice<User> users = userRepository.findByDocumentTypeAndIdGreaterThan(documentType, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей по полу после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersByGenderAfter(Gender gender, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по полу используя id > afterId
        Slice<User> users = userRepository.findByGenderAndIdGreaterThan(gender, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей по гражданству после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersByCitizenshipAfter(String citizenship, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по гражданству используя id > afterId
        Slice<User> users = userRepository.findByCitizenshipAndIdGreaterThan(citizenship, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей в возрастном диапазоне после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersByAgeRangeAfter(Integer minAge, Integer maxAge, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем запрос по возрасту используя id > afterId
        Slice<User> users = userRepository.findByAgeBetweenAndIdGreaterThan(minAge, maxAge, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает живых пользователей с истекшими документами после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getAliveUsersWithExpiredDocumentsAfter(Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем комбинированный запрос для живых с истекшими документами используя id > afterId
        Slice<User> users = userRepository.findByDeathDateIsNullAndExpiryDateBeforeAndIdGreaterThan(LocalDate.now(), afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }

    // возвращает пользователей по полу и возрасту после указанного id (для keyset пагинации)
    @Override
    public Slice<UserResponseDto> getUsersByGenderAndAgeRangeAfter(Gender gender, Integer minAge, Integer maxAge, Long afterId, int size) {
        // создаем pageable с нужным размером страницы
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, size);
        // выполняем комбинированный запрос по полу и возрасту используя id > afterId
        Slice<User> users = userRepository.findByGenderAndAgeBetweenAndIdGreaterThan(gender, minAge, maxAge, afterId, pageable);
        // преобразуем каждого user в dto и возвращаем slice
        return users.map(userMapper::toResponseDto);
    }
}
