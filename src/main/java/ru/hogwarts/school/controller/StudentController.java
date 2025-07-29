package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.add(student);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student upd_student = studentService.update(id, student);
        if (upd_student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(upd_student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student student = studentService.delete(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-age/{age}")
    public ResponseEntity<List<Student>> getStudentsByAge(@PathVariable Integer age) {
        List<Student> students = studentService.getStudentsByAge(age);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/between")
    public ResponseEntity<List<Student>> getStudentsByBetween(@RequestParam Integer min, @RequestParam Integer max) {
        List<Student> students = studentService.getStudentsBetween(min, max);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/faculty")
    public ResponseEntity<Faculty> getStudentsByFaculty(@RequestParam Long id) {
        Faculty faculty = studentService.getStudentFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/cnt-faculty")
    public ResponseEntity<Long> getCntStudentsByFaculty(@RequestParam Faculty faculty) {
        Long cnt = studentService.getCntStudentsByFaculty(faculty);
        return ResponseEntity.ok(cnt);
    }


    @GetMapping("/avg-age")
    public ResponseEntity<Integer> getAvgAgeOfStudents() {
        int avg = studentService.getAvgAgeOfStudents();
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/last")
    public ResponseEntity<List<Student>> getLastStudents() {
        List<Student> students = studentService.getLastStudents();
        if (students == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getNamesStudents() {
        List<String> names = studentService.getAllNamesStudents();
        if (names.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    @GetMapping("/avg-s")
    public ResponseEntity<Double> getAvgAgeStudentsByStream() {
        return ResponseEntity.ok(studentService.getAvgAgeStudentsByStream());
    }

    @GetMapping("/print-parallel")
    public void getNamesStudentsInConsole() {
        studentService.getNameStudentsByThreads();
    }

    @GetMapping("/print-synchronized")
    public void getNamesStudentsInConsoleSynchro() {
        studentService.getNameStudentsByThreadsSynchro();
    }
}
