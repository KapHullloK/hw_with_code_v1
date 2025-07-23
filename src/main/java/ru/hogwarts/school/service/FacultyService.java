package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;


@Service
public class FacultyService {

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty add(Faculty faculty) {
        logger.info("Was invoked method for add faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty get(Long id) {
        logger.info("Was invoked method for get faculty with id = {}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty update(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with id = {}", id);
        if (facultyRepository.existsById(id)) {
            return facultyRepository.save(faculty);
        }
        logger.warn("Cannot update faculty: faculty with id = '{}' not found", id);
        return null;
    }

    public Faculty delete(Long id) {
        logger.info("Was invoked method for delete faculty with id = {}", id);
        Faculty faculty = this.get(id);
        if (faculty != null) {
            facultyRepository.delete(faculty);
            return faculty;
        }
        logger.warn("Cannot delete faculty: faculty with id = {} not found", id);
        return null;
    }

    public List<Faculty> getByColor(String color) {
        logger.info("Was invoked method for get faculties by color: '{}'", color);
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> getFacultiesByNameOrColor(String name, String color) {
        logger.info("Was invoked method for get faculties by name or color: name='{}', color='{}'", name, color);
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudents(Long id) {
        logger.info("Was invoked method for get students of faculty with id = {}", id);
        if (facultyRepository.existsById(id)) {
            return this.get(id).getStudents();
        }
        logger.warn("Cannot get students: faculty with id = {} not found", id);
        return null;
    }

}
