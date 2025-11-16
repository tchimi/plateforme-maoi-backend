package maoi.platforme.services;

import maoi.platforme.dtos.TrainingChaptersDTO;
import maoi.platforme.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainingChapterService {
    TrainingChaptersDTO createChapter(TrainingChaptersDTO dto,
                                      List<MultipartFile> assetFiles) throws StorageException;
}
