package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(Student student) {
        return studentRepository.save(student);
    }

    public Student get(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(long id, Student student) {
        if (studentRepository.existsById(id)) {
            return studentRepository.save(student);
        }
        return null;
    }

    public Student delete(long id) {
        Student student = this.get(id);
        if (student != null) {
            studentRepository.delete(student);
        }
        return student;
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public List<Student> getStudentsBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long id) {
        if (studentRepository.existsById(id)) {
            return this.get(id).getFaculty();
        }
        return null;
    }

    public Long getCntStudentsByFaculty(Faculty faculty) {
        return studentRepository.countStudentByFaculty(faculty);
    }

    public int getAvgAgeOfStudents() {
        return studentRepository.getAverageAgeStudents();
    }

    public List<Student> getLastStudents() {
        return studentRepository.getLastStudents();
    }
}
