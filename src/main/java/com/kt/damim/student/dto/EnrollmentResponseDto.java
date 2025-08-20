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
@Schema(description = "수강 신청 응답 DTO")
public class EnrollmentResponseDto {
    @Schema(description = "학생 ID", example = "1")
    private Integer studentId;
    
    @Schema(description = "클래스 ID", example = "1")
    private Integer classId;
    
    @Schema(description = "수강 상태", example = "ENROLLED")
    private String status;
    
    @Schema(description = "생성일시", example = "2024-01-20T15:30:00Z")
    private OffsetDateTime createdAt;
}
