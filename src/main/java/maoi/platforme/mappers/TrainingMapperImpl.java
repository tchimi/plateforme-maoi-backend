package maoi.platforme.mappers;

import maoi.platforme.dtos.*;
import maoi.platforme.entities.Training;
import maoi.platforme.entities.TrainingAsset;
import maoi.platforme.entities.TrainingChapters;
import maoi.platforme.entities.TrainingPartie;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TrainingMapperImpl {

    public TrainingDTO fromTraining(Training training) {
        if (training == null) return null;

        TrainingDTO dto = new TrainingDTO();
        BeanUtils.copyProperties(training, dto);

        // Mapper les parties
        if (training.getParties() != null) {
            dto.setParties(training.getParties().stream()
                    .map(this::fromTrainingPartie)
                    .toList());
        }

        return dto;
    }

    public Training fromTrainingDTO(TrainingDTO dto) {
        if (dto == null) return null;

        Training training = new Training();
        BeanUtils.copyProperties(dto, training);

        // Mapper les parties
        if (dto.getParties() != null) {
            training.setParties(dto.getParties().stream()
                    .map(this::fromTrainingPartieDTO)
                    .peek(p -> p.setTraining(training)) // lier la relation inverse
                    .toList());
        }

        return training;
    }

    public TrainingPartieDTO fromTrainingPartie(TrainingPartie partie) {
        if (partie == null) return null;

        TrainingPartieDTO dto = new TrainingPartieDTO();
        BeanUtils.copyProperties(partie, dto);

        // Mapper les chapitres
        if (partie.getChapters() != null) {
            dto.setChapters(partie.getChapters().stream()
                    .map(this::fromTrainingChapter)
                    .toList());
        }

        return dto;
    }

    public TrainingPartie fromTrainingPartieDTO(TrainingPartieDTO dto) {
        if (dto == null) return null;

        TrainingPartie partie = new TrainingPartie();
        BeanUtils.copyProperties(dto, partie);

        // Mapper les chapitres
        if (dto.getChapters() != null) {
            partie.setChapters(dto.getChapters().stream()
                    .map(this::fromTrainingChapterDTO)
                    .peek(c -> c.setPartie(partie)) // relation inverse
                    .toList());
        }

        return partie;
    }

    public TrainingChaptersDTO fromTrainingChapter(TrainingChapters chapter) {
        if (chapter == null) return null;

        TrainingChaptersDTO dto = new TrainingChaptersDTO();
        BeanUtils.copyProperties(chapter, dto);

        // Mapper les assets
        if (chapter.getAssets() != null) {
            dto.setAssets(chapter.getAssets().stream()
                    .map(this::fromTrainingAsset)
                    .toList());
        }

        return dto;
    }

    public TrainingChapters fromTrainingChapterDTO(TrainingChaptersDTO dto) {
        if (dto == null) return null;

        TrainingChapters chapter = new TrainingChapters();
        BeanUtils.copyProperties(dto, chapter);

        // Mapper les assets
        if (dto.getAssets() != null) {
            chapter.setAssets(dto.getAssets().stream()
                    .map(this::fromTrainingAssetDTO)
                    .peek(a -> a.setChapter(chapter)) // relation inverse
                    .toList());
        }

        return chapter;
    }

    public TrainingAssetDTO fromTrainingAsset(TrainingAsset asset) {
        if (asset == null) return null;

        TrainingAssetDTO dto = new TrainingAssetDTO();
        BeanUtils.copyProperties(asset, dto);
        return dto;
    }

    public TrainingAsset fromTrainingAssetDTO(TrainingAssetDTO dto) {
        if (dto == null) return null;

        TrainingAsset asset = new TrainingAsset();
        BeanUtils.copyProperties(dto, asset);
        return asset;
    }

    public ListTrainingsDTO fromTrainingDTOPage(Page<TrainingDTO> trainingDTOPage) {
        ListTrainingsDTO listDTO = new ListTrainingsDTO();
        listDTO.setListTrainingDTO(trainingDTOPage.getContent());
        listDTO.setCurrentPage(trainingDTOPage.getNumber());
        listDTO.setPageSize(trainingDTOPage.getSize());
        listDTO.setTotalPages(trainingDTOPage.getTotalPages());
        return listDTO;
    }
}
