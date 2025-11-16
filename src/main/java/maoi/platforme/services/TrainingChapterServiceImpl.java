package maoi.platforme.services;

import maoi.platforme.dtos.TrainingChaptersDTO;
import maoi.platforme.entities.TrainingAsset;
import maoi.platforme.entities.TrainingChapters;
import maoi.platforme.exception.StorageException;
import maoi.platforme.mappers.TrainingMapperImpl;
import maoi.platforme.repositories.TrainingAssetRepository;
import maoi.platforme.repositories.TrainingChapterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TrainingChapterServiceImpl implements TrainingChapterService {

    private final TrainingChapterRepository chapterRepository;
    private final TrainingAssetRepository assetRepository;
    private final UploadFileService uploadFileService;

    private final Path rootLocation = Paths.get("upload/");
    private final String assetDir = "training/assets";
    private TrainingMapperImpl trainingMapper;

    public TrainingChapterServiceImpl(TrainingChapterRepository chapterRepository, TrainingAssetRepository assetRepository, UploadFileService uploadFileService) {
        this.chapterRepository = chapterRepository;
        this.assetRepository = assetRepository;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public TrainingChaptersDTO createChapter(TrainingChaptersDTO dto, List<MultipartFile> assetFiles) throws StorageException {
        TrainingChapters chapter = new TrainingChapters();
        chapter.setTitle(dto.getTitle());
        chapter.setDescription(dto.getDescription());
        chapter.setDescriptionHtml(dto.getDescriptionHtml());
        chapter.setDuration(dto.getDuration());

        TrainingChapters savedChapter = chapterRepository.save(chapter);

        if (assetFiles != null) {
            for (MultipartFile file : assetFiles) {
                String name = uploadFileService.uploadFile(file, assetDir, rootLocation);

                TrainingAsset asset = new TrainingAsset();
                asset.setName(file.getOriginalFilename());
                asset.setUrl(name);
                asset.setSize(file.getSize());
                asset.setType(file.getContentType());
                asset.setChapter(savedChapter);

                assetRepository.save(asset);
            }
        }

        return trainingMapper.fromTrainingChapter(savedChapter);
    }
}
