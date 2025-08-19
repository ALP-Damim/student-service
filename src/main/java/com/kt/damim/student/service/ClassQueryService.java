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

	public List<ClassResponseDto> getClasses(Integer limit) {
		List<Class> classes;
		if (limit != null && limit > 0) {
			classes = classRepository.findAll(PageRequest.of(0, limit)).getContent();
		} else {
			classes = classRepository.findAll();
		}
		return classes.stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<ClassResponseDto> getClassesWithFilter(Integer limit, String semesterOrder, Integer day) {
		List<Class> classes;
		if (semesterOrder != null && !semesterOrder.isBlank()) {
			Sort.Direction direction = "desc".equalsIgnoreCase(semesterOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
			classes = classRepository.findAll(Sort.by(direction, "semester"));
		} else {
			classes = classRepository.findAll();
		}


		// day 파라미터를 요일 비트마스크(1~127)로 해석하여 필터링
		if (day != null && day >= 1 && day <= 127) {
			int mask = day;
			classes = classes.stream()
					.filter(c -> c != null && c.getHeldDay() != null && (c.getHeldDay() & mask) != 0)
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


