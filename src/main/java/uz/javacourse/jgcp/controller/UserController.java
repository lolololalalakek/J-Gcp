package uz.javacourse.jgcp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.javacourse.jgcp.dto.request.MarkDeceasedRequestDto;
import uz.javacourse.jgcp.dto.request.UserRequestDto;
import uz.javacourse.jgcp.dto.response.UserResponseDto;
import uz.javacourse.jgcp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/gcp/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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
    public ResponseEntity<UserResponseDto> markUserAsDeceased(
            @PathVariable Long id,
            @Valid @RequestBody MarkDeceasedRequestDto requestDto) {
        return ResponseEntity.ok(userService.markUserAsDeceased(id, requestDto.getDeathDate()));
    }
}
