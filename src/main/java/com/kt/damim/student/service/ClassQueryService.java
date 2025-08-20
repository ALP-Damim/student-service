package com.kt.damim.student.service;

import com.kt.damim.student.dto.ClassResponseDto;
import com.kt.damim.student.entity.Class;
import com.kt.damim.student.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassQueryService {

	private final ClassRepository classRepository;

	public List<ClassResponseDto> getClasses(Integer limit, Integer teacherId) {
		List<Class> classes;
		if (teacherId != null) {
			classes = classRepository.findByTeacherId(teacherId);
		} else {
			if (limit != null && limit > 0) {
				classes = classRepository.findAll(PageRequest.of(0, limit)).getContent();
			} else {
				classes = classRepository.findAll();
			}
		}
		
		// teacherId가 지정된 경우에도 limit 적용
		if (teacherId != null && limit != null && limit > 0 && classes.size() > limit) {
			classes = classes.subList(0, limit);
		}
		
		return classes.stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<ClassResponseDto> getClassesWithFilter(Integer limit, String semesterOrder, Integer day, Integer startId, Integer teacherId) {
		List<Class> classes;
		
		// teacherId가 지정된 경우 해당 교사의 수업만 조회
		if (teacherId != null) {
			classes = classRepository.findByTeacherId(teacherId);
		} else {
			if (startId != null) {
				// 시작 ID가 지정되면 번호 순 정렬을 우선 적용
				classes = classRepository.findAll(Sort.by(Sort.Direction.ASC, "classId"));
			} else if (semesterOrder != null && !semesterOrder.isBlank()) {
				Sort.Direction direction = "desc".equalsIgnoreCase(semesterOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
				classes = classRepository.findAll(Sort.by(direction, "semester"));
			} else {
				// 기본 정렬: 번호 오름차순
				classes = classRepository.findAll(Sort.by(Sort.Direction.ASC, "classId"));
			}
		}


		// day 파라미터를 요일 비트마스크(1~127)로 해석하여 필터링

		if (day != null && day >= 1 && day <= 127) {
			int mask = day;
			classes = classes.stream()
					.filter(c -> c != null && c.getHeldDay() != null && (c.getHeldDay() & mask) == mask)
					.collect(Collectors.toList());
		}

		// startId 이상만 반환 (포함)
		if (startId != null) {
			int from = startId;
			classes = classes.stream()
					.filter(c -> c != null && c.getClassId() != null && c.getClassId() >= from)
					.collect(Collectors.toList());
		}

		if (limit != null && limit > 0 && classes.size() > limit) {
			classes = classes.subList(0, limit);
		}

		return classes.stream().map(this::toDto).collect(Collectors.toList());
	}

	private ClassResponseDto toDto(Class c) {
		return ClassResponseDto.builder()
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
				.build();
	}
}


