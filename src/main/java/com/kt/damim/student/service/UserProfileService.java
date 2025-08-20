package com.kt.damim.student.service;

import com.kt.damim.student.dto.UserProfileResponseDto;
import com.kt.damim.student.dto.UserProfileUpdateRequestDto;
import com.kt.damim.student.entity.UserProfile;
import com.kt.damim.student.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileResponseDto getUserProfileById(Integer userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 프로필을 찾을 수 없습니다: " + userId));

        return toDto(userProfile);
    }

    @Transactional
    public UserProfileResponseDto updateUserProfile(Integer userId, UserProfileUpdateRequestDto requestDto) {
        // 요청 데이터 검증
        validateUpdateRequest(requestDto);

        // 기존 프로필 조회
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 프로필을 찾을 수 없습니다: " + userId));

        // 프로필 정보 업데이트
        updateProfileFields(userProfile, requestDto);

        // 데이터베이스에 저장
        UserProfile updatedProfile = userProfileRepository.save(userProfile);

        return toDto(updatedProfile);
    }

    private void validateUpdateRequest(UserProfileUpdateRequestDto requestDto) {
        if (requestDto.getName() != null && requestDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (requestDto.getBirthDate() != null && requestDto.getBirthDate().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("생년월일은 미래 날짜일 수 없습니다.");
        }
        if (requestDto.getPhone() != null && !requestDto.getPhone().matches("^010-\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("전화번호는 010-XXXX-XXXX 형식이어야 합니다.");
        }
    }

    private void updateProfileFields(UserProfile userProfile, UserProfileUpdateRequestDto requestDto) {
        if (requestDto.getName() != null) {
            userProfile.setName(requestDto.getName());
        }
        if (requestDto.getDesiredCourse() != null) {
            userProfile.setDesiredCourse(requestDto.getDesiredCourse());
        }
        if (requestDto.getDesiredJob() != null) {
            userProfile.setDesiredJob(requestDto.getDesiredJob());
        }
        if (requestDto.getBirthDate() != null) {
            userProfile.setBirthDate(requestDto.getBirthDate());
        }
        if (requestDto.getSchool() != null) {
            userProfile.setSchool(requestDto.getSchool());
        }
        if (requestDto.getPhone() != null) {
            userProfile.setPhone(requestDto.getPhone());
        }
    }

    private UserProfileResponseDto toDto(UserProfile userProfile) {
        return UserProfileResponseDto.builder()
                .userId(userProfile.getUserId())
                .name(userProfile.getName())
                .desiredCourse(userProfile.getDesiredCourse())
                .desiredJob(userProfile.getDesiredJob())
                .birthDate(userProfile.getBirthDate())
                .school(userProfile.getSchool())
                .phone(userProfile.getPhone())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
}
