package com.chronos_0812.user.controller;

import com.chronos_0812.user.dto.get.UserGetAllResponse;
import com.chronos_0812.user.dto.get.UserGetOneResponse;
import com.chronos_0812.user.dto.save.UserSaveRequest;
import com.chronos_0812.user.dto.update.UserUpdateRequest;
import com.chronos_0812.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 유저 CRUD API
 * - POST   /users
 * - GET    /users
 * - GET    /users/{userId}
 * - PUT    /users/{userId}
 * - DELETE /users/{userId}
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> save(
            @RequestBody UserSaveRequest userSaveRequestreq
    ) {
        return ResponseEntity.ok(userService.save(userSaveRequestreq));
    }

    @GetMapping
    public ResponseEntity<List<UserGetAllResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetOneResponse> findOne(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        userService.update(id, userUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
