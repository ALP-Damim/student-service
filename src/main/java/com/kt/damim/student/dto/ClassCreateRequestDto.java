package com.kt.damim.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "강좌 생성 요청 DTO")
public class ClassCreateRequestDto {
    @Schema(description = "교사 ID", example = "1", required = true)
    private Integer teacherId;
    
    @Schema(description = "교사 이름", example = "김교수", required = true)
    private String teacherName;
    
    @Schema(description = "강좌명", example = "자바 프로그래밍", required = true)
    private String className;
    
    @Schema(description = "학기", example = "2024-2", required = true)
    private String semester;
    
    @Schema(description = "줌 URL", example = "https://zoom.us/j/123456789")
    private String zoomUrl;
    
    @Schema(description = "요일 비트마스크 (1~127)", example = "7", required = true)
    private Integer heldDay;
    
    @Schema(description = "시작 시간", example = "10:00:00", required = true)
    private LocalTime startsAt;
    
    @Schema(description = "종료 시간", example = "12:00:00", required = true)
    private LocalTime endsAt;
    
    @Schema(description = "수용 인원", example = "30")
    private Integer capacity;
}
