package maoi.platforme.services;

import maoi.platforme.dtos.CreatePartieRequest;
import maoi.platforme.dtos.TrainingPartieDTO;

public interface TrainingPartieService {
    TrainingPartieDTO createPartie(CreatePartieRequest request);
}
