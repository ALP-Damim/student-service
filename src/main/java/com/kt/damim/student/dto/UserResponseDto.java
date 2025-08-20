package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 응답 DTO")
public class UserResponseDto {
    @Schema(description = "사용자 ID", example = "1")
    private Integer userId;
    
    @Schema(description = "이메일", example = "user@example.com")
    private String email;
    
    @Schema(description = "사용자 역할", example = "STUDENT")
    private String role;
    
    @Schema(description = "활성화 여부", example = "true")
    private Boolean isActive;
    
    @Schema(description = "생성일시", example = "2024-01-01T00:00:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2024-01-01T00:00:00Z")
    private OffsetDateTime updatedAt;
}
