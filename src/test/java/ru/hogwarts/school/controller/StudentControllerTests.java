package ru.hogwarts.school.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.Faculty;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getRootUrl() {
        return "http://localhost:" + port + "/student";
    }

    private Long createdStudentId;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setName("Alice");
        student.setAge(20);

        ResponseEntity<Student> response = restTemplate.postForEntity(getRootUrl(), student, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        createdStudentId = response.getBody().getId();
    }

    @Test
    void testAddStudent() {
        assertThat(createdStudentId).isNotNull();
    }

    @Test
    void testGetStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity(getRootUrl() + "/" + createdStudentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(createdStudentId);
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setName("UpdatedName");
        student.setAge(25);

        HttpEntity<Student> requestEntity = new HttpEntity<>(student);
        ResponseEntity<Student> response = restTemplate.exchange(
                getRootUrl() + "/" + createdStudentId,
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("UpdatedName");
    }

    @Test
    void testDeleteStudent() {
        ResponseEntity<Student> response = restTemplate.exchange(
                getRootUrl() + "/" + createdStudentId,
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(createdStudentId);
    }

    @Test
    void testGetStudentsByAge() {
        Integer age = 20;
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/by-age/" + age, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void testGetStudentsBetween() {
        Integer min = 18;
        Integer max = 25;

        URI uri = UriComponentsBuilder.fromUriString(getRootUrl() + "/between")
                .queryParam("min", min)
                .queryParam("max", max)
                .build()
                .toUri();

        ResponseEntity<List> response = restTemplate.getForEntity(uri, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void testGetStudentFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(
                getRootUrl().replace("/student", "/faculty"),
                faculty,
                Faculty.class
        );

        Long facultyId = facultyResponse.getBody().getId();

        Student student = new Student();
        student.setName("Bob");
        student.setAge(17);
        student.setFaculty(new Faculty());
        student.getFaculty().setId(facultyId);

        ResponseEntity<Student> studentResponse = restTemplate.postForEntity(
                getRootUrl(),
                student,
                Student.class
        );

        Long studentId = studentResponse.getBody().getId();

        URI uri = UriComponentsBuilder.fromUriString(getRootUrl() + "/faculty")
                .queryParam("id", studentId)
                .build()
                .toUri();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(uri, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
    }
}