package com.kt.damim.student.service;

import com.kt.damim.student.dto.EnrolledClassResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Enrollment;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentQueryService {

	private final EnrollmentRepository enrollmentRepository;
	private final ClassRepository classRepository;

	public List<EnrolledClassResponseDto> getEnrolledClasses(Integer studentId, String semester, Boolean active) {
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
		Set<Integer> classIds = enrollments.stream().map(Enrollment::getClassId).collect(Collectors.toSet());
		List<Class> classes = new ArrayList<>();
		if (!classIds.isEmpty()) {
			classes = classRepository.findAllById(classIds);
		}

		OffsetDateTime now = OffsetDateTime.now();
		List<EnrolledClassResponseDto> dtos = new ArrayList<>();
		for (Class c : classes) {
			if (semester != null && !semester.equals(c.getSemester())) continue;
			if (active != null && active) {
				if (!isHeldToday(c.getHeldDay())) continue;
				if (!isTimeInRange(now.toLocalTime(), c.getStartsAt(), c.getEndsAt())) continue;
			}
			dtos.add(mapToDto(c));
		}
		return dtos;
	}



	private boolean isHeldToday(Integer heldDay) {
		if (heldDay == null) return false;
		int dayOfWeek = LocalDate.now().getDayOfWeek().getValue() - 1; // 0-based index
		int bit = 1 << dayOfWeek;
		return (heldDay & bit) != 0;
	}

	private boolean isTimeInRange(LocalTime target, LocalTime start, LocalTime end) {
		if (start == null || end == null) return false;
		return !target.isBefore(start) && !target.isAfter(end);
	}



	private EnrolledClassResponseDto mapToDto(Class c) {
		return EnrolledClassResponseDto.builder()
				.classId(c.getClassId())
				.teacherId(c.getTeacherId())
				.teacherName(c.getTeacherName())
				.className(c.getClassName())
				.semester(c.getSemester())
				.zoomUrl(c.getZoomUrl())
				.heldDay(c.getHeldDay())
				.heldDaysString(c.getHeldDaysString())
				.startsAt(c.getStartsAt())
				.endsAt(c.getEndsAt())
				.nextStartAt(null)
				.build();
	}
}
