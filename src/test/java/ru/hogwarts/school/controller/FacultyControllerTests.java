package ru.hogwarts.school.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
    }

    @Test
    void testAddFaculty() throws Exception {
        when(facultyService.add(any(Faculty.class))).thenReturn(faculty);

        String jsonRequest = objectMapper.writeValueAsString(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(faculty.getId()))
                .andExpect(jsonPath("name").value(faculty.getName()));
    }

    @Test
    void testGetFacultyById() throws Exception {
        when(facultyService.get(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(faculty.getId()))
                .andExpect(jsonPath("name").value(faculty.getName()));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        Faculty updated = new Faculty();
        updated.setId(1L);
        updated.setName("Slytherin");
        updated.setColor("Green");

        when(facultyService.update(eq(1L), any(Faculty.class))).thenReturn(updated);

        String jsonRequest = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Slytherin"))
                .andExpect(jsonPath("color").value("Green"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        when(facultyService.delete(1L)).thenReturn(faculty);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1));
    }

    @Test
    void testGetFacultyByColor() throws Exception {
        List<Faculty> faculties = List.of(faculty);

        when(facultyService.getByColor("Red")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/by-color/Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));
    }

    @Test
    void testGetFacultiesByNameOrColor() throws Exception {
        List<Faculty> byName = List.of(faculty);
        List<Faculty> byColor = List.of(faculty);

        when(facultyService.getFacultiesByNameOrColor("Gryffindor", null)).thenReturn(byName);
        when(facultyService.getFacultiesByNameOrColor(null, "Red")).thenReturn(byColor);

        // По имени
        mockMvc.perform(get("/faculty")
                        .param("name", "Gryffindor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));

        // По цвету
        mockMvc.perform(get("/faculty")
                        .param("color", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));
    }

    @Test
    void testGetFacultyStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Bob");
        student.setAge(17);

        when(facultyService.getStudents(1L)).thenReturn(List.of(student));

        mockMvc.perform(get("/faculty/students")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1));
    }
}