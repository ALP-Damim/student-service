package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "수강 신청 요청 DTO")
public class EnrollmentRequestDto {
    @Schema(description = "학생 ID", example = "1", required = true)
    private Integer studentId;
    
    @Schema(description = "클래스 ID", example = "1", required = true)
    private Integer classId;
}
