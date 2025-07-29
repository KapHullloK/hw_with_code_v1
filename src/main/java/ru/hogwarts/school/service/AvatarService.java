package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar for student with id = {}", studentId);
        Student student = studentRepository.getById(studentId);
        Path filePath = Path.of(avatarDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = getAvatarFromDB(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
        student.setAvatar(avatar);
        studentRepository.save(student);
        logger.debug("Student with id = {} updated with avatar id = {}", studentId, avatar.getId());
    }

    private String getExtensions(String fileName) {
        logger.info("Was invoked method for get extensions by file: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar getAvatarFromDB(Long studentId) {
        logger.info("Was invoked method for get avatar from DB for student with id = {}", studentId);
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        }
        logger.warn("Student:'{}' not found in get avatar from DB", studentId);
        return new Avatar();
    }

    public byte[] getAvatarFromDisk(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar from disk for student with id = {}", studentId);
        Avatar avatar = getAvatarFromDB(studentId);
        String filePath = avatar.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("Avatar file not found on disk at path: {}", filePath);
            throw new IllegalArgumentException("File not found at path: " + filePath);
        }
        return java.nio.file.Files.readAllBytes(file.toPath());
    }

    public List<Avatar> getAllAvatars(Integer page, Integer size) {
        logger.info("Was invoked method for get all avatars. Page: {}, Size: {}", page - 1, size);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
