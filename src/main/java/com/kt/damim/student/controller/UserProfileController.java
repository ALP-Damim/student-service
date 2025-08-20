package com.kt.damim.student.controller;

import com.kt.damim.student.dto.UserProfileResponseDto;
import com.kt.damim.student.dto.UserProfileUpdateRequestDto;
import com.kt.damim.student.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> getUserProfileById(@PathVariable Integer userId) {
        UserProfileResponseDto userProfile = userProfileService.getUserProfileById(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @PathVariable Integer userId,
            @RequestBody UserProfileUpdateRequestDto requestDto) {
        UserProfileResponseDto updatedProfile = userProfileService.updateUserProfile(userId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
