package maoi.platforme.services;

import maoi.platforme.dtos.ListTrainingsDTO;
import maoi.platforme.dtos.TrainingDTO;
import maoi.platforme.entities.Training;
import maoi.platforme.entities.TrainingAsset;
import maoi.platforme.entities.TrainingChapters;
import maoi.platforme.entities.TrainingPartie;
import maoi.platforme.exception.*;
import maoi.platforme.mappers.TrainingMapperImpl;
import maoi.platforme.repositories.TrainingPartieRepository;
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
import java.util.*;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingsService {

    private TrainingRepository trainingRepository;
    private UploadFileService uploadFileService;
    private TrainingMapperImpl trainingMapper;
    private TrainingPartieRepository partieRepository;
    private final Path rootLocation = Paths.get("upload/");
    private final String storageDir = "training/coverImage";
    private final String coverDir = "training/coverImage";
    private final String assetDir = "training/assets";

    public TrainingServiceImpl(TrainingRepository trainingRepository, UploadFileService uploadFileService,
                               TrainingMapperImpl trainingMapper,TrainingPartieRepository partieRepository ) {
        this.trainingRepository = trainingRepository;
        this.uploadFileService = uploadFileService;
        this.trainingMapper = trainingMapper;
        this.partieRepository = partieRepository;
    }

    @Override
    public TrainingDTO save(TrainingDTO trainingDTO,
                            MultipartFile coverFile,
                            List<MultipartFile> assetFiles)
            throws StorageException, TrainingSlugInvalidException, TrainingSlugUsedException {

        validateSlug(trainingDTO.getSlug());

        if (trainingRepository.findBySlug(trainingDTO.getSlug()).isPresent()) {
            throw new TrainingSlugUsedException("Votre slug est déjà utilisé");
        }

        trainingDTO.setCreatedAt(new Date());
        trainingDTO.setUpdatedAt(trainingDTO.getCreatedAt());

        //Upload cover image
        if (coverFile != null && !coverFile.isEmpty()) {
            String fileName = uploadFileService.uploadFile(coverFile, coverDir, rootLocation);
            trainingDTO.setImageCover(fileName);
        }

        //Convert DTO → Entity
        Training training = trainingMapper.fromTrainingDTO(trainingDTO);

        Training saved = trainingRepository.save(training);
        return trainingMapper.fromTraining(saved);
    }

    // ================================================================
    // UTILITY METHODS
    // ================================================================

    /**
     * Gère la création et l’attachement automatique des fichiers assets.
     * Si aucun chapitre n’existe, les fichiers seront ajoutés à un chapitre par défaut.
     */
    private void createAssetsForTraining(Training training, List<MultipartFile> assetFiles) throws StorageException {
        if (assetFiles == null || assetFiles.isEmpty()) return;

        // Si aucune partie/chapitre n’existe encore, on crée un chapitre par défaut
        if (training.getParties() == null || training.getParties().isEmpty()) {
            TrainingPartie partie = new TrainingPartie();
            partie.setTitle("Default Section");

            TrainingChapters chapter = new TrainingChapters();
            chapter.setTitle("Default Chapter");
            chapter.setAssets(new ArrayList<>());

            partie.setChapters(Collections.singletonList(chapter));
            training.setParties(Collections.singletonList(partie));
        }

        TrainingChapters targetChapter = training.getParties().get(0).getChapters().get(0);

        for (MultipartFile file : assetFiles) {
            if (file.isEmpty()) continue;

            String storedFileName = uploadFileService.uploadFile(file, assetDir, rootLocation);

            TrainingAsset asset = new TrainingAsset();
            asset.setName(file.getOriginalFilename());
            asset.setUrl(storedFileName);
            asset.setType(file.getContentType());
            asset.setSize(file.getSize());
            asset.setChapter(targetChapter);

            if (targetChapter.getAssets() == null)
                targetChapter.setAssets(new ArrayList<>());

            targetChapter.getAssets().add(asset);
        }
    }

    private void validateSlug(String slug) throws TrainingSlugInvalidException {
        if (slug == null || slug.isEmpty())
            throw new TrainingSlugInvalidException("Slug non fourni");
        if (slug.contains(" "))
            throw new TrainingSlugInvalidException("Slug invalide (contient des espaces)");
    }

    @Override
    public TrainingDTO update(Long idTraining,
                              TrainingDTO trainingDTO,
                              MultipartFile coverFile,
                              List<MultipartFile> assetFiles)
            throws StorageException, TrainingNotFoundException, TrainingSlugInvalidException, IOException {

        Training existingTraining = trainingRepository.findById(idTraining)
                .orElseThrow(() -> new TrainingNotFoundException("Formation non trouvée avec l'id " + idTraining));

        if (trainingDTO.getSlug() != null && !trainingDTO.getSlug().equalsIgnoreCase(existingTraining.getSlug())) {
            validateSlug(trainingDTO.getSlug());
        }

        existingTraining.setTitle(Optional.ofNullable(trainingDTO.getTitle()).orElse(existingTraining.getTitle()));
        existingTraining.setSlug(Optional.ofNullable(trainingDTO.getSlug()).orElse(existingTraining.getSlug()));
        existingTraining.setDescription(Optional.ofNullable(trainingDTO.getDescription()).orElse(existingTraining.getDescription()));
        existingTraining.setDuration(Optional.ofNullable(trainingDTO.getDuration()).orElse(existingTraining.getDuration()));
        existingTraining.setNote(Optional.ofNullable(trainingDTO.getNote()).orElse(existingTraining.getNote()));
        existingTraining.setUpdatedAt(new Date());

        //Upload d'une nouvelle image de couverture
        if (coverFile != null && !coverFile.isEmpty()) {
            // Supprimer l’ancien fichier s’il existe
            if (existingTraining.getImageCover() != null) {
                uploadFileService.deleteFile(existingTraining.getImageCover(), coverDir, rootLocation);
            }

            String newFileName = uploadFileService.uploadFile(coverFile, coverDir, rootLocation);
            existingTraining.setImageCover(newFileName);
        }

        //Gérer les nouveaux fichiers assets
        if (assetFiles != null && !assetFiles.isEmpty()) {
            addAssetsToExistingTraining(existingTraining, assetFiles);
        }

        Training savedTraining = trainingRepository.save(existingTraining);
        return trainingMapper.fromTraining(savedTraining);
    }

    private void addAssetsToExistingTraining(Training training, List<MultipartFile> assetFiles) throws StorageException {
        if (assetFiles == null || assetFiles.isEmpty()) return;

        // Si le training n’a pas encore de parties, on crée une par défaut
        if (training.getParties() == null || training.getParties().isEmpty()) {
            TrainingPartie partie = new TrainingPartie();
            partie.setTitle("Default Section");

            TrainingChapters chapter = new TrainingChapters();
            chapter.setTitle("Default Chapter");
            chapter.setAssets(new ArrayList<>());

            partie.setChapters(Collections.singletonList(chapter));
            training.setParties(Collections.singletonList(partie));
        }

        // On ajoute tous les nouveaux assets au premier chapitre
        TrainingChapters targetChapter = training.getParties().get(0).getChapters().get(0);

        for (MultipartFile file : assetFiles) {
            if (file.isEmpty()) continue;

            String storedFileName = uploadFileService.uploadFile(file, assetDir, rootLocation);

            TrainingAsset asset = new TrainingAsset();
            asset.setName(file.getOriginalFilename());
            asset.setUrl(storedFileName);
            asset.setType(file.getContentType());
            asset.setSize(file.getSize());
            asset.setChapter(targetChapter);

            if (targetChapter.getAssets() == null)
                targetChapter.setAssets(new ArrayList<>());

            targetChapter.getAssets().add(asset);
        }
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

    @Override
    public TrainingDTO attachParties(Long trainingId, List<Long> partieIds) throws TrainingNotFoundException {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

        List<TrainingPartie> parties = partieRepository.findAllById(partieIds);

        training.setParties(parties);
        training.setUpdatedAt(new Date());

        Training saved = trainingRepository.save(training);
        return trainingMapper.fromTraining(saved);
    }
}
