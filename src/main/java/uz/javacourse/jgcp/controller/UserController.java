package uz.javacourse.jgcp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;
import uz.javacourse.jgcp.dto.request.MarkDeceasedRequestDto;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.service.UserService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/gcp/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Slice<UserResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto response = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-pinfl/{pinfl}")
    public ResponseEntity<UserResponseDto> getUserByPinfl(@PathVariable String pinfl) {
        return ResponseEntity.ok(userService.getUserByPinfl(pinfl));
    }

    @GetMapping("/{id}/is-alive")
    public ResponseEntity<Boolean> isUserAlive(@PathVariable Long id) {
        return ResponseEntity.ok(userService.isUserAlive(id));
    }

    @PatchMapping("/{id}/mark-deceased")
    public ResponseEntity<MarkDeceasedResponseDto> markUserAsDeceased(
            @PathVariable Long id,
            @Valid @RequestBody MarkDeceasedRequestDto requestDto) {
        return ResponseEntity.ok(userService.markUserAsDeceased(id, requestDto.deathDate()));
    }

    @GetMapping("/search")
    public ResponseEntity<Slice<UserResponseDto>> searchByName(
            @RequestParam String name,
            Pageable pageable) {
        return ResponseEntity.ok(userService.searchByName(name, pageable));
    }

    @GetMapping("/alive")
    public ResponseEntity<Slice<UserResponseDto>> getAllAliveUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllAliveUsers(pageable));
    }

    @GetMapping("/deceased")
    public ResponseEntity<Slice<UserResponseDto>> getAllDeceasedUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllDeceasedUsers(pageable));
    }

    @GetMapping("/alive/count")
    public ResponseEntity<Long> getAliveUsersCount() {
        // просто возвращает количество живых пользователей
        return ResponseEntity.ok(userService.getAliveUsersCount());
    }

    @GetMapping("/deceased/count")
    public ResponseEntity<Long> getDeceasedUsersCount() {
        return ResponseEntity.ok(userService.getDeceasedUsersCount());
    }

    @GetMapping("/deceased/between")
    public ResponseEntity<Slice<UserResponseDto>> getUsersDeceasedBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersDeceasedBetween(start, end, pageable));
    }

    @GetMapping("/documents/expired")
    public ResponseEntity<Slice<UserResponseDto>> getUsersWithExpiredDocuments(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments(pageable));
    }

    @GetMapping("/documents/expiring-between")
    public ResponseEntity<Slice<UserResponseDto>> getUsersWithDocumentsExpiringBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetween(start, end, pageable));
    }

    @GetMapping("/documents/by-type/{documentType}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByDocumentType(
            @PathVariable DocumentType documentType,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByDocumentType(documentType, pageable));
    }

    @GetMapping("/by-gender/{gender}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByGender(
            @PathVariable Gender gender,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByGender(gender, pageable));
    }

    @GetMapping("/by-gender/{gender}/count")
    public ResponseEntity<Long> getUsersCountByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(userService.getUsersCountByGender(gender));
    }

    @GetMapping("/by-citizenship/{citizenship}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByCitizenship(
            @PathVariable String citizenship,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByCitizenship(citizenship, pageable));
    }

    @GetMapping("/by-age-range")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByAgeRange(minAge, maxAge, pageable));
    }

    @GetMapping("/alive/documents/expired")
    public ResponseEntity<Slice<UserResponseDto>> getAliveUsersWithExpiredDocuments(Pageable pageable) {
        return ResponseEntity.ok(userService.getAliveUsersWithExpiredDocuments(pageable));
    }

    @GetMapping("/by-gender-and-age")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByGenderAndAgeRange(
            @RequestParam Gender gender,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByGenderAndAgeRange(gender, minAge, maxAge, pageable));
    }

    // СТАТИСТИКА

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(userService.getTotalUsersCount());
    }

    @GetMapping("/documents/expired/count")
    public ResponseEntity<Long> getUsersWithExpiredDocumentsCount() {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocumentsCount());
    }

    @GetMapping("/documents/expiring-between/count")
    public ResponseEntity<Long> getUsersWithDocumentsExpiringBetweenCount(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetweenCount(start, end));
    }

    @GetMapping("/documents/by-type/{documentType}/count")
    public ResponseEntity<Long> getUsersByDocumentTypeCount(@PathVariable DocumentType documentType) {
        return ResponseEntity.ok(userService.getUsersByDocumentTypeCount(documentType));
    }

    @GetMapping("/by-age-range/count")
    public ResponseEntity<Long> getUsersByAgeRangeCount(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        return ResponseEntity.ok(userService.getUsersByAgeRangeCount(minAge, maxAge));
    }

    @GetMapping("/deceased/between/count")
    public ResponseEntity<Long> getUsersDeceasedBetweenCount(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(userService.getUsersDeceasedBetweenCount(start, end));
    }

    @GetMapping("/alive/documents/expired/count")
    public ResponseEntity<Long> getAliveUsersWithExpiredDocumentsCount() {
        return ResponseEntity.ok(userService.getAliveUsersWithExpiredDocumentsCount());
    }

    @GetMapping("/by-gender-and-age/count")
    public ResponseEntity<Long> getUsersByGenderAndAgeRangeCount(
            @RequestParam Gender gender,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        return ResponseEntity.ok(userService.getUsersByGenderAndAgeRangeCount(gender, minAge, maxAge));
    }

    @GetMapping("/by-citizenship/{citizenship}/count")
    public ResponseEntity<Long> getUsersByCitizenshipCount(@PathVariable String citizenship) {
        return ResponseEntity.ok(userService.getUsersByCitizenshipCount(citizenship));
    }

    // === KEYSET (CURSOR-BASED) ПАГИНАЦИЯ ===
    // Быстрая пагинация через afterId - всегда быстро, независимо от глубины
    // Использование: ?afterId=12345&size=25
    // Для первой загрузки: ?afterId=0&size=25

    @GetMapping("/cursor")
    public ResponseEntity<Slice<UserResponseDto>> getAllUsersCursor(
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getAllUsersAfter(afterId, size));
    }

    @GetMapping("/cursor/search")
    public ResponseEntity<Slice<UserResponseDto>> searchByNameCursor(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.searchByNameAfter(name, afterId, size));
    }

    @GetMapping("/cursor/alive")
    public ResponseEntity<Slice<UserResponseDto>> getAllAliveUsersCursor(
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getAllAliveUsersAfter(afterId, size));
    }

    @GetMapping("/cursor/deceased")
    public ResponseEntity<Slice<UserResponseDto>> getAllDeceasedUsersCursor(
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getAllDeceasedUsersAfter(afterId, size));
    }

    @GetMapping("/cursor/deceased/between")
    public ResponseEntity<Slice<UserResponseDto>> getUsersDeceasedBetweenCursor(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersDeceasedBetweenAfter(start, end, afterId, size));
    }

    @GetMapping("/cursor/documents/expired")
    public ResponseEntity<Slice<UserResponseDto>> getUsersWithExpiredDocumentsCursor(
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocumentsAfter(afterId, size));
    }

    @GetMapping("/cursor/documents/expiring-between")
    public ResponseEntity<Slice<UserResponseDto>> getUsersWithDocumentsExpiringBetweenCursor(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetweenAfter(start, end, afterId, size));
    }

    @GetMapping("/cursor/documents/by-type/{documentType}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByDocumentTypeCursor(
            @PathVariable DocumentType documentType,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersByDocumentTypeAfter(documentType, afterId, size));
    }

    @GetMapping("/cursor/by-gender/{gender}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByGenderCursor(
            @PathVariable Gender gender,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersByGenderAfter(gender, afterId, size));
    }

    @GetMapping("/cursor/by-citizenship/{citizenship}")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByCitizenshipCursor(
            @PathVariable String citizenship,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersByCitizenshipAfter(citizenship, afterId, size));
    }

    @GetMapping("/cursor/by-age-range")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByAgeRangeCursor(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersByAgeRangeAfter(minAge, maxAge, afterId, size));
    }

    @GetMapping("/cursor/alive/documents/expired")
    public ResponseEntity<Slice<UserResponseDto>> getAliveUsersWithExpiredDocumentsCursor(
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getAliveUsersWithExpiredDocumentsAfter(afterId, size));
    }

    @GetMapping("/cursor/by-gender-and-age")
    public ResponseEntity<Slice<UserResponseDto>> getUsersByGenderAndAgeRangeCursor(
            @RequestParam Gender gender,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge,
            @RequestParam(defaultValue = "0") Long afterId,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(userService.getUsersByGenderAndAgeRangeAfter(gender, minAge, maxAge, afterId, size));
    }
}
