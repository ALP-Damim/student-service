package com.kt.damim.student.dto;

import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassCreateResponseDto {
    
    private Integer classId;
    private Integer teacherId;
    private String teacherName;
    private String className;
    private String semester;
    private String schoolYear;
    private String subject;
    private String zoomUrl;
    private Integer heldDay;
    private String heldDaysString;
    private LocalTime startsAt;
    private LocalTime endsAt;
    private Integer capacity;
    private List<SessionInfo> generatedSessions;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SessionInfo {
        private Integer sessionId;
        private String sessionName;
        private OffsetDateTime onDate;
    }
    
    public static ClassCreateResponseDto from(Class classEntity, List<Session> sessions) {
        List<SessionInfo> sessionInfos = sessions.stream()
            .map(session -> SessionInfo.builder()
                .sessionId(session.getSessionId())
                .sessionName(session.getSessionName())
                .onDate(session.getOnDate())
                .build())
            .collect(Collectors.toList());
        
        return ClassCreateResponseDto.builder()
            .classId(classEntity.getClassId())
            .teacherId(classEntity.getTeacherId())
            .teacherName(classEntity.getTeacherName())
            .className(classEntity.getClassName())
            .semester(classEntity.getSemester())
            .schoolYear(classEntity.getSchoolYear())
            .subject(classEntity.getSubject())
            .zoomUrl(classEntity.getZoomUrl())
            .heldDay(classEntity.getHeldDay())
            .heldDaysString(classEntity.getHeldDaysString())
            .startsAt(classEntity.getStartsAt())
            .endsAt(classEntity.getEndsAt())
            .capacity(classEntity.getCapacity())
            .generatedSessions(sessionInfos)
            .build();
    }
}
