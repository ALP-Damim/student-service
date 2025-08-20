package com.kt.damim.student.controller;

import com.kt.damim.student.dto.UserProfileResponseDto;
import com.kt.damim.student.dto.UserProfileUpdateRequestDto;
import com.kt.damim.student.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
@Tag(name = "사용자 프로필 관리", description = "사용자 프로필 조회 및 수정 API")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 프로필 조회", description = "특정 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (존재하지 않는 사용자 ID)"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<UserProfileResponseDto> getUserProfileById(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Integer userId) {
        UserProfileResponseDto userProfile = userProfileService.getUserProfileById(userId);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "사용자 프로필 수정", description = "특정 사용자의 프로필 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 ID"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<UserProfileResponseDto> updateUserProfile(
            @Parameter(description = "사용자 ID", example = "1") @PathVariable Integer userId,
            @Parameter(description = "프로필 수정 요청 데이터", required = true) @RequestBody UserProfileUpdateRequestDto requestDto) {
        UserProfileResponseDto updatedProfile = userProfileService.updateUserProfile(userId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
