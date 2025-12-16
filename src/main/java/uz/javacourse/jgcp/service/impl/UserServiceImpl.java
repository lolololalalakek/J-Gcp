package uz.javacourse.jgcp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.javacourse.jgcp.constant.enums.entity.DocumentType;
import uz.javacourse.jgcp.constant.enums.entity.Gender;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.entity.User;
import uz.javacourse.jgcp.exception.BusinessException;
import uz.javacourse.jgcp.exception.ConflictException;
import uz.javacourse.jgcp.exception.ResourceNotFoundException;
import uz.javacourse.jgcp.exception.UserNotFoundException;
import uz.javacourse.jgcp.mapper.UserMapper;
import uz.javacourse.jgcp.repository.UserRepository;
import uz.javacourse.jgcp.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // создает нового пользователя в системе после проверки уникальности email и pinfl
    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        validateUniqueness(requestDto);

        User user = userMapper.toEntity(requestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    // возвращает список всех пользователей из базы данных
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // находит и возвращает пользователя по его уникальному идентификатору
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponseDto(user);
    }

    // находит и возвращает пользователя по его персональному идентификационному номеру (pinfl)
    @Override
    public UserResponseDto getUserByPinfl(String pinfl) {
        User user = userRepository.findByPinfl(pinfl)
                .orElseThrow(() -> new UserNotFoundException("pinfl", pinfl));
        return userMapper.toResponseDto(user);
    }

    // проверяет жив ли пользователь (deathDate == null означает что пользователь жив)
    @Override
    public boolean isUserAlive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return user.getDeathDate() == null;
    }

    // отмечает пользователя как умершего, устанавливая дату смерти
    @Override
    public MarkDeceasedResponseDto markUserAsDeceased(Long id, LocalDate deathDate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getDeathDate() != null) {
            throw new BusinessException("User already marked as deceased");
        }

        user.setDeathDate(deathDate);
        User updatedUser = userRepository.save(user);
        return userMapper.toMarkDeceasedResponseDto(updatedUser);
    }

    // ищет пользователей по части имени (без учета регистра)
    @Override
    public List<UserResponseDto> searchByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает список всех живых пользователей
    @Override
    public List<UserResponseDto> getAllAliveUsers() {
        return userRepository.findByDeathDateIsNull().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает список всех умерших пользователей
    @Override
    public List<UserResponseDto> getAllDeceasedUsers() {
        return userRepository.findByDeathDateIsNotNull().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает количество живых пользователей
    @Override
    public long getAliveUsersCount() {
        return userRepository.countByDeathDateIsNull();
    }

    // возвращает количество умерших пользователей
    @Override
    public long getDeceasedUsersCount() {
        return userRepository.countByDeathDateIsNotNull();
    }

    // возвращает пользователей умерших в указанном периоде
    @Override
    public List<UserResponseDto> getUsersDeceasedBetween(LocalDate start, LocalDate end) {
        return userRepository.findByDeathDateBetween(start, end).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей с истекшими документами
    @Override
    public List<UserResponseDto> getUsersWithExpiredDocuments() {
        return userRepository.findByExpiryDateBefore(LocalDate.now()).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей чьи документы истекают в указанном периоде
    @Override
    public List<UserResponseDto> getUsersWithDocumentsExpiringBetween(LocalDate start, LocalDate end) {
        return userRepository.findByExpiryDateBetween(start, end).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей по типу документа
    @Override
    public List<UserResponseDto> getUsersByDocumentType(DocumentType documentType) {
        return userRepository.findByDocumentType(documentType).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей по полу
    @Override
    public List<UserResponseDto> getUsersByGender(Gender gender) {
        return userRepository.findByGender(gender).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает количество пользователей по полу
    @Override
    public long getUsersCountByGender(Gender gender) {
        return userRepository.countByGender(gender);
    }

    // возвращает пользователей по гражданству
    @Override
    public List<UserResponseDto> getUsersByCitizenship(String citizenship) {
        return userRepository.findByCitizenship(citizenship).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей в указанном возрастном диапазоне
    @Override
    public List<UserResponseDto> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает живых пользователей с истекшими документами
    @Override
    public List<UserResponseDto> getAliveUsersWithExpiredDocuments() {
        return userRepository.findByDeathDateIsNullAndExpiryDateBefore(LocalDate.now()).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // возвращает пользователей по полу и возрастному диапазону
    @Override
    public List<UserResponseDto> getUsersByGenderAndAgeRange(Gender gender, Integer minAge, Integer maxAge) {
        return userRepository.findByGenderAndAgeBetween(gender, minAge, maxAge).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // проверяет уникальность email, pinfl и номера телефона перед созданием нового пользователя
    private void validateUniqueness(UserRequestDto requestDto) {
        if (userRepository.existsByPinfl(requestDto.pinfl())) {
            throw new ConflictException("User with this PINFL already exists");
        }

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new ConflictException("User with this email already exists");
        }

        if (userRepository.existsByPhoneNumber(requestDto.phoneNumber())) {
            throw new ConflictException("User with this phone number already exists");
        }
    }
}
