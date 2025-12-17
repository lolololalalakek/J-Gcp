package uz.javacourse.jgcp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.javacourse.jgcp.constant.enums.entity.DocumentType;
import uz.javacourse.jgcp.constant.enums.entity.Gender;
import uz.javacourse.jgcp.dto.request.MarkDeceasedRequestDto;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.MarkDeceasedResponseDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/gcp/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        // ограничение: максимум 25 элементов на страницу
        if (size > 25) {
            size = 25;
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto response = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/by-id/{id}")
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
    public ResponseEntity<List<UserResponseDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchByName(name));
    }

    @GetMapping("/alive")
    public ResponseEntity<List<UserResponseDto>> getAllAliveUsers() {
        return ResponseEntity.ok(userService.getAllAliveUsers());
    }

    @GetMapping("/deceased")
    public ResponseEntity<List<UserResponseDto>> getAllDeceasedUsers() {
        return ResponseEntity.ok(userService.getAllDeceasedUsers());
    }

    @GetMapping("/alive/count")
    public ResponseEntity<Long> getAliveUsersCount() {
        return ResponseEntity.ok(userService.getAliveUsersCount());
    }

    @GetMapping("/deceased/count")
    public ResponseEntity<Long> getDeceasedUsersCount() {
        return ResponseEntity.ok(userService.getDeceasedUsersCount());
    }

    @GetMapping("/deceased/between")
    public ResponseEntity<List<UserResponseDto>> getUsersDeceasedBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(userService.getUsersDeceasedBetween(start, end));
    }

    @GetMapping("/documents/expired")
    public ResponseEntity<List<UserResponseDto>> getUsersWithExpiredDocuments() {
        return ResponseEntity.ok(userService.getUsersWithExpiredDocuments());
    }

    @GetMapping("/documents/expiring-between")
    public ResponseEntity<List<UserResponseDto>> getUsersWithDocumentsExpiringBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(userService.getUsersWithDocumentsExpiringBetween(start, end));
    }

    @GetMapping("/documents/by-type/{documentType}")
    public ResponseEntity<List<UserResponseDto>> getUsersByDocumentType(@PathVariable DocumentType documentType) {
        return ResponseEntity.ok(userService.getUsersByDocumentType(documentType));
    }

    @GetMapping("/by-gender/{gender}")
    public ResponseEntity<List<UserResponseDto>> getUsersByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(userService.getUsersByGender(gender));
    }

    @GetMapping("/by-gender/{gender}/count")
    public ResponseEntity<Long> getUsersCountByGender(@PathVariable Gender gender) {
        return ResponseEntity.ok(userService.getUsersCountByGender(gender));
    }

    @GetMapping("/by-citizenship/{citizenship}")
    public ResponseEntity<List<UserResponseDto>> getUsersByCitizenship(@PathVariable String citizenship) {
        return ResponseEntity.ok(userService.getUsersByCitizenship(citizenship));
    }

    @GetMapping("/by-age-range")
    public ResponseEntity<List<UserResponseDto>> getUsersByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        return ResponseEntity.ok(userService.getUsersByAgeRange(minAge, maxAge));
    }

    @GetMapping("/alive/documents/expired")
    public ResponseEntity<List<UserResponseDto>> getAliveUsersWithExpiredDocuments() {
        return ResponseEntity.ok(userService.getAliveUsersWithExpiredDocuments());
    }

    @GetMapping("/by-gender-and-age")
    public ResponseEntity<List<UserResponseDto>> getUsersByGenderAndAgeRange(
            @RequestParam Gender gender,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        return ResponseEntity.ok(userService.getUsersByGenderAndAgeRange(gender, minAge, maxAge));
    }
}
