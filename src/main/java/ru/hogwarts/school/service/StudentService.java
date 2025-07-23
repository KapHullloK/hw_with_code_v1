package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(Student student) {
        logger.info("Was invoked method for add student: {}", student);
        return studentRepository.save(student);
    }

    public Student get(long id) {
        logger.info("Was invoked method for get student with id = {}", id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(long id, Student student) {
        logger.info("Was invoked method for update student with id = {}", id);
        if (studentRepository.existsById(id)) {
            return studentRepository.save(student);
        }
        logger.warn("Cannot update student: student with id = {} not found", id);
        return null;
    }

    public Student delete(long id) {
        logger.info("Was invoked method for delete student with id = {}", id);
        Student student = this.get(id);
        if (student != null) {
            studentRepository.delete(student);
            return student;
        }
        logger.warn("Cannot delete student: student with id = {} not found", id);
        return null;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age = {}", age);
        return studentRepository.findAllByAge(age);
    }

    public List<Student> getStudentsBetween(int min, int max) {
        logger.info("Was invoked method for get students between ages {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long id) {
        logger.info("Was invoked method for get faculty of student with id = {}", id);
        if (studentRepository.existsById(id)) {
            return this.get(id).getFaculty();
        }
        logger.warn("Student with id = {} not found", id);
        return null;
    }

    public Long getCntStudentsByFaculty(Faculty faculty) {
        logger.info("Was invoked method for count students in faculty: {}", faculty);
        return studentRepository.countStudentByFaculty(faculty);
    }

    public int getAvgAgeOfStudents() {
        logger.info("Was invoked method for get average age of all students");
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> getLastStudents() {
        logger.info("Was invoked method for get last students");
        return studentRepository.getLastStudents();
    }
}
