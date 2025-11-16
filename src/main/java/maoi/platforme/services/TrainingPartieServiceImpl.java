package maoi.platforme.services;

import maoi.platforme.dtos.CreatePartieRequest;
import maoi.platforme.dtos.TrainingPartieDTO;
import maoi.platforme.entities.TrainingChapters;
import maoi.platforme.entities.TrainingPartie;
import maoi.platforme.mappers.TrainingMapperImpl;
import maoi.platforme.repositories.TrainingChapterRepository;
import maoi.platforme.repositories.TrainingPartieRepository;

import java.util.List;

public class TrainingPartieServiceImpl implements TrainingPartieService {

    private final TrainingPartieRepository partieRepository;
    private final TrainingChapterRepository chapterRepository;
    private TrainingMapperImpl trainingMapper;

    public TrainingPartieServiceImpl(TrainingPartieRepository partieRepository, TrainingChapterRepository chapterRepository) {
        this.partieRepository = partieRepository;
        this.chapterRepository = chapterRepository;
    }

    @Override
    public TrainingPartieDTO createPartie(CreatePartieRequest request) {
        TrainingPartie partie = new TrainingPartie();
        partie.setTitle(request.getTitle());
        //partie.setOrderIndex(request.getOrderIndex());

        List<TrainingChapters> chapters = chapterRepository.findAllById(request.getChapterIds());
        partie.setChapters(chapters);

        TrainingPartie saved = partieRepository.save(partie);

        return trainingMapper.fromTrainingPartie(saved);
    }
}
