package com.kt.damim.student.repository;

import com.kt.damim.student.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.session.id = :sessionId")
    Optional<Attendance> findByStudentIdAndSessionId(@Param("studentId") Long studentId, @Param("sessionId") Long sessionId);
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.session.classEntity.id = :classId")
    List<Attendance> findByStudentIdAndClassId(@Param("studentId") Long studentId, @Param("classId") Long classId);
}
