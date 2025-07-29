package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    @Query("select count(s.id) from Student as s where s.faculty = :faculty")
    Long countStudentByFaculty(@Param("faculty") Faculty faculty);

    @Query("select avg(s.age) from Student as s")
    int getAverageAgeStudents();

    @Query("select s from Student as s order by s.id desc limit 5")
    List<Student> getLastStudents();
}
