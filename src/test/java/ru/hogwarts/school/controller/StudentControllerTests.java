package ru.hogwarts.school.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Student student;

    @BeforeEach
    void setUp() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        student = new Student();
        student.setId(1L);
        student.setName("Alice");
        student.setAge(20);
        student.setFaculty(faculty);
    }

    @Test
    void testAddStudent() throws Exception {
        when(studentService.add(any(Student.class))).thenReturn(student);

        String jsonRequest = objectMapper.writeValueAsString(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(student.getId()))
                .andExpect(jsonPath("name").value(student.getName()));
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.get(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(student.getId()))
                .andExpect(jsonPath("name").value(student.getName()));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updated = new Student();
        updated.setId(1L);
        updated.setName("UpdatedName");
        updated.setAge(25);
        updated.setFaculty(student.getFaculty());

        when(studentService.update(eq(1L), any(Student.class))).thenReturn(updated);

        String jsonRequest = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("UpdatedName"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.delete(1L)).thenReturn(student);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1));
    }

    @Test
    void testGetStudentsByAge() throws Exception {
        List<Student> students = List.of(student);

        when(studentService.getStudentsByAge(20)).thenReturn(students);

        mockMvc.perform(get("/student/by-age/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));
    }

    @Test
    void testGetStudentsBetween() throws Exception {
        List<Student> students = List.of(student);

        when(studentService.getStudentsBetween(18, 25)).thenReturn(students);

        mockMvc.perform(get("/student/between")
                        .param("min", "18")
                        .param("max", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));
    }

    @Test
    void testGetStudentFaculty() throws Exception {
        when(studentService.getStudentFaculty(1L)).thenReturn(student.getFaculty());

        mockMvc.perform(get("/student/faculty")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Gryffindor"));
    }
}