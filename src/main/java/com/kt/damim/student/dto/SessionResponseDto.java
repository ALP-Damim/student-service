package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDto {
    private Integer sessionId;
    private Integer classId;
    private String sessionName;
    private OffsetDateTime onDate;
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
