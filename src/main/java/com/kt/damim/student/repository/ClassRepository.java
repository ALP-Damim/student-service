package com.kt.damim.student.repository;

import com.kt.damim.student.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    List<Class> findByTeacherId(Integer teacherId);
    List<Class> findBySemester(String semester);
}
