package maoi.platforme.services;

import maoi.platforme.dtos.ListTrainingsDTO;
import maoi.platforme.dtos.TrainingDTO;
import maoi.platforme.exception.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TrainingsService {

    TrainingDTO save(TrainingDTO trainingDTO, MultipartFile file) throws StorageException, TrainingSlugInvalidException, TrainingSlugUsedException;

    TrainingDTO update(Long idTraining, TrainingDTO trainingDTO, MultipartFile file) throws StorageException, TrainingNotFoundException, TrainingSlugInvalidException;

    void delete(Long idTraining) throws IOException, TrainingNotFoundException;

    ListTrainingsDTO trainings(int page, int size) throws TrainingNotFoundException;

    TrainingDTO training(Long idTraining) throws TrainingNotFoundException;

    TrainingDTO findTrainingBySlug(String slug) throws TrainingNotFoundException;

    Resource getImageCover(String fileName) throws StorageFileNotFoundException;
}
