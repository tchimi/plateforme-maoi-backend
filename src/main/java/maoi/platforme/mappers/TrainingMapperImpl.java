package maoi.platforme.mappers;

import maoi.platforme.dtos.ListTrainingsDTO;
import maoi.platforme.dtos.TrainingDTO;
import maoi.platforme.entities.Training;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TrainingMapperImpl {

    public TrainingDTO fromTraining(Training training){
        TrainingDTO trainingDTO = new TrainingDTO();
        BeanUtils.copyProperties(training,trainingDTO);
        return trainingDTO;
    }

    public Training fromTrainingDTO (TrainingDTO trainingDTO){
        Training training = new Training();
        BeanUtils.copyProperties(trainingDTO,training);
        return training;
    }

    public ListTrainingsDTO fromTrainingDTOPage(Page<TrainingDTO> trainingDTOPage){
        ListTrainingsDTO listTrainingsDTO = new ListTrainingsDTO();
        listTrainingsDTO.setListTrainingDTO(trainingDTOPage.getContent());
        listTrainingsDTO.setCurrentPage(trainingDTOPage.getNumber());
        listTrainingsDTO.setPageSize(trainingDTOPage.getSize());
        listTrainingsDTO.setTotalPages(trainingDTOPage.getTotalPages());
        return listTrainingsDTO;
    }
}
