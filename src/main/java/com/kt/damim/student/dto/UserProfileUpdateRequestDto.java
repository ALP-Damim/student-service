package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 프로필 수정 요청 DTO")
public class UserProfileUpdateRequestDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    
    @Schema(description = "희망 과정", example = "웹 개발")
    private String desiredCourse;
    
    @Schema(description = "희망 직업", example = "프론트엔드 개발자")
    private String desiredJob;
    
    @Schema(description = "생년월일", example = "1995-01-01")
    private LocalDate birthDate;
    
    @Schema(description = "학교", example = "서울대학교")
    private String school;
    
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;
}
