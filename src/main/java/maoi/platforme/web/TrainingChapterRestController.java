package maoi.platforme.web;


import maoi.platforme.dtos.TrainingChaptersDTO;
import maoi.platforme.exception.StorageException;
import maoi.platforme.services.TrainingChapterService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class TrainingChapterRestController {
    private final TrainingChapterService trainingChapterService;

    public TrainingChapterRestController(TrainingChapterService trainingChapterService) {
        this.trainingChapterService = trainingChapterService;
    }

    @PostMapping(path = "v1/chapters/save", consumes = {"multipart/form-data"})
    public TrainingChaptersDTO saveChapter(
            @ModelAttribute TrainingChaptersDTO chapter,
            @RequestParam(name = "assets", required = false) List<MultipartFile> assetFiles
    ) throws StorageException {
        return this.trainingChapterService.createChapter(chapter, assetFiles);
    }
}
