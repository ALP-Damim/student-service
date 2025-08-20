package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Session;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세션 응답 DTO")
public class SessionResponseDto {
    @Schema(description = "세션 ID", example = "1")
    private Integer sessionId;
    
    @Schema(description = "클래스 ID", example = "1")
    private Integer classId;
    
    @Schema(description = "세션명", example = "session1")
    private String sessionName;
    
    @Schema(description = "세션 날짜", example = "2024-11-25T14:00:00Z")
    private OffsetDateTime onDate;
    
    @Schema(description = "생성일시", example = "2024-12-02T10:00:00Z")
    private OffsetDateTime createdAt;
    
    public static SessionResponseDto from(Session session) {
        return SessionResponseDto.builder()
                .sessionId(session.getSessionId())
                .classId(session.getClassId())
                .sessionName(session.getSessionName())
                .onDate(session.getOnDate())
                .build();
    }
}
