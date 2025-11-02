package maoi.platforme.services;

import maoi.platforme.dtos.ListTrainingsDTO;
import maoi.platforme.dtos.TrainingDTO;
import maoi.platforme.entities.Training;
import maoi.platforme.exception.*;
import maoi.platforme.mappers.TrainingMapperImpl;
import maoi.platforme.repositories.TrainingRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingsService {

    private TrainingRepository trainingRepository;
    private UploadFileService uploadFileService;
    private TrainingMapperImpl trainingMapper;
    private final Path rootLocation = Paths.get("upload/");
    private final String storageDir = "training/coverImage";

    public TrainingServiceImpl(TrainingRepository trainingRepository, UploadFileService uploadFileService,
                               TrainingMapperImpl trainingMapper) {
        this.trainingRepository = trainingRepository;
        this.uploadFileService = uploadFileService;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public TrainingDTO save(TrainingDTO trainingDTO, MultipartFile file) throws StorageException, TrainingSlugInvalidException, TrainingSlugUsedException {

        if (trainingDTO.getSlug() == null) {
            throw new TrainingSlugInvalidException("slug not found");
        }
        if (trainingDTO.getSlug().contains(" ")) {
            throw new TrainingSlugInvalidException("Votre slug est invalide");
    }
        Optional<Training> existing = trainingRepository.findBySlug(trainingDTO.getSlug());
        if (existing.isPresent()) {
            throw new TrainingSlugUsedException("Votre slug est déjà utilisé");
        }

        // Image cover
        if (file != null && !file.isEmpty()) {
            String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
            trainingDTO.setImageCover(fileName);
        }

        trainingDTO.setCreatedAt(new Date());
        trainingDTO.setUpdatedAt(new Date());

        // Conversion DTO → Entity
        Training training = trainingMapper.fromTrainingDTO(trainingDTO);

        // Sauvegarde
        Training saved = trainingRepository.save(training);

        return trainingMapper.fromTraining(saved);
    }

    @Override
    public TrainingDTO update(Long idTraining, TrainingDTO trainingDTO, MultipartFile file)
            throws StorageException, TrainingNotFoundException, TrainingSlugInvalidException {

        TrainingDTO oldTrainingDTO = training(idTraining);
        String slug = trainingDTO.getSlug();

        if (slug != null && !oldTrainingDTO.getSlug().equalsIgnoreCase(slug)) {
            if (slug.contains(" ")) {
                throw new TrainingSlugInvalidException("Votre slug est invalide");
            }
        }

        trainingDTO.setId(idTraining);
        trainingDTO.setUpdatedAt(new Date());

        if (file != null && !file.isEmpty()) {
            String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
            trainingDTO.setImageCover(fileName);
        } else {
            trainingDTO.setImageCover(oldTrainingDTO.getImageCover());
        }

        Training training = trainingMapper.fromTrainingDTO(trainingDTO);
        Training updated = trainingRepository.save(training);

        return trainingMapper.fromTraining(updated);
    }

    @Override
    public void delete(Long idTraining) throws IOException, TrainingNotFoundException {
        TrainingDTO trainingDTO = this.training(idTraining);

        if (trainingDTO != null) {
            String fileName = trainingDTO.getImageCover();
            uploadFileService.deleteFile(fileName, storageDir, rootLocation);
            trainingRepository.deleteById(idTraining);
        }
    }

    @Override
    public ListTrainingsDTO trainings(int page, int size) throws TrainingNotFoundException {
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy = "updatedAt";
        Page<Training> trainingPage = trainingRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)));

        if (trainingPage.isEmpty()) {
            throw new TrainingNotFoundException("Aucune formation trouvée");
        }

        Page<TrainingDTO> trainingDTOPage = trainingPage.map(trainingMapper::fromTraining);
        return trainingMapper.fromTrainingDTOPage(trainingDTOPage);
    }

    @Override
    public TrainingDTO training(Long idTraining) throws TrainingNotFoundException {
        Training training = this.trainingRepository.findById(idTraining)
                .orElseThrow(() -> new TrainingNotFoundException("training not found "));
        return trainingMapper.fromTraining(training);
    }

    @Override
    public TrainingDTO findTrainingBySlug(String slug) throws TrainingNotFoundException {
        Training training = this.trainingRepository.findBySlug(slug)
                .orElseThrow(() -> new TrainingNotFoundException("training not found with slug " + slug));
        return trainingMapper.fromTraining(training);
    }

    @Override
    public Resource getImageCover(String fileName) throws StorageFileNotFoundException {
        return this.uploadFileService.loadAsResource(fileName, storageDir, this.rootLocation);
    }
}
