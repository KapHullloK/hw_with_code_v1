package ru.hogwarts.school.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @RequestParam("studentId") Long studentId,
            @RequestParam("file") MultipartFile file) {

        try {
            avatarService.uploadAvatar(studentId, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload avatar: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/db/{id}")
    public ResponseEntity<Resource> getAvatarFromDB(@PathVariable Long id) {
        byte[] avatarData = avatarService.getAvatarFromDB(id).getData();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(avatarData));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getAvatarFromDisk(@PathVariable Long id) throws IOException {
        byte[] data = avatarService.getAvatarFromDisk(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(data));
    }

    @GetMapping()
    public ResponseEntity<List<Avatar>> getAllAvatars(@RequestParam Integer page, @RequestParam Integer size) {
        List<Avatar> avatars = avatarService.getAllAvatars(page, size);
        return ResponseEntity.ok(avatars);
    }
}
