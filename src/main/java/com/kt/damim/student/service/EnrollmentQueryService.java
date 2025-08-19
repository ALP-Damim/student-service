package com.kt.damim.student.service;

import com.kt.damim.student.dto.EnrolledClassResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.entity.Enrollment;
import com.kt.damim.student.repository.ClassRepository;
import com.kt.damim.student.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
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
			dtos.add(mapToDto(c, null));
		}
		return dtos;
	}

	public List<EnrolledClassResponseDto> getNearestThree(Integer studentId) {
		List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
		Set<Integer> classIds = enrollments.stream().map(Enrollment::getClassId).collect(Collectors.toSet());
		List<Class> classes = classIds.isEmpty() ? List.of() : classRepository.findAllById(classIds);

		OffsetDateTime now = OffsetDateTime.now();
		List<EnrolledClassResponseDto> scored = new ArrayList<>();
		for (Class c : classes) {
			OffsetDateTime nextStart = computeNextStart(now, c.getHeldDay(), c.getStartsAt());
			if (nextStart != null) {
				scored.add(mapToDto(c, nextStart));
			}
		}

		return scored.stream()
				.sorted(Comparator.comparing(EnrolledClassResponseDto::getNextStartAt))
				.limit(3)
				.collect(Collectors.toList());
	}

	private boolean isHeldToday(Integer heldDay) {
		if (heldDay == null) return false;
		int bit = 1 << (dayOfWeekToIndex(LocalDate.now().getDayOfWeek()));
		return (heldDay & bit) != 0;
	}

	private boolean isTimeInRange(LocalTime target, LocalTime start, LocalTime end) {
		if (start == null || end == null) return false;
		return !target.isBefore(start) && !target.isAfter(end);
	}

	private OffsetDateTime computeNextStart(OffsetDateTime now, Integer heldDay, LocalTime startTime) {
		if (heldDay == null || startTime == null) return null;
		LocalDate date = now.toLocalDate();
		for (int i = 0; i < 14; i++) {
			LocalDate candidate = date.plusDays(i);
			int bit = 1 << dayOfWeekToIndex(candidate.getDayOfWeek());
			if ((heldDay & bit) != 0) {
				OffsetDateTime candidateStart = candidate.atTime(startTime).atZone(ZoneId.systemDefault()).toOffsetDateTime();
				if (!candidateStart.isBefore(now)) {
					return candidateStart;
				}
			}
		}
		return null;
	}

	private int dayOfWeekToIndex(DayOfWeek dow) {
		switch (dow) {
			case MONDAY: return 0;
			case TUESDAY: return 1;
			case WEDNESDAY: return 2;
			case THURSDAY: return 3;
			case FRIDAY: return 4;
			case SATURDAY: return 5;
			case SUNDAY: return 6;
			default: return 0;
		}
	}

	private EnrolledClassResponseDto mapToDto(Class c, OffsetDateTime nextStart) {
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
				.nextStartAt(nextStart)
				.build();
	}
}
