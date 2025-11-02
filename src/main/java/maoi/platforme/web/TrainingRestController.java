package maoi.platforme.web;


import maoi.platforme.dtos.ListTrainingsDTO;
import maoi.platforme.dtos.TrainingDTO;
import maoi.platforme.exception.*;
import maoi.platforme.services.TrainingServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class TrainingRestController {
    private TrainingServiceImpl trainingService;

    public TrainingRestController(TrainingServiceImpl trainingService) {
        this.trainingService = trainingService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "v1/training/save", consumes = {"multipart/form-data"})
    public TrainingDTO saveTraining(
            @ModelAttribute TrainingDTO trainingDTO,
            @RequestParam(name = "coverFile", required = false) MultipartFile coverFile,
            @RequestParam(name = "assets", required = false) List<MultipartFile> assetFiles
    ) throws StorageException, TrainingSlugUsedException, TrainingSlugInvalidException {

        return this.trainingService.save(trainingDTO, coverFile, assetFiles);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "v1/training/update/{idTraining}", consumes = {"multipart/form-data"})
    public TrainingDTO updateTraining(
            @PathVariable(name = "idTraining") Long idTraining,
            @ModelAttribute TrainingDTO trainingDTO,
            @RequestParam(name = "coverFile", required = false) MultipartFile coverFile,
            @RequestParam(name = "assets", required = false) List<MultipartFile> assetFiles
    ) throws StorageException, TrainingNotFoundException, TrainingSlugInvalidException, IOException {

        return this.trainingService.update(idTraining, trainingDTO, coverFile, assetFiles);
    }


    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "v1/training/delete/{idTraining}")
    public void deleteServices(@PathVariable(name = "idTraining") Long idTraining) throws IOException, TrainingNotFoundException {
        this.trainingService.delete(idTraining);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/trainings")
    public ListTrainingsDTO getTrainings (@RequestParam(name = "Page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "10") int size) throws TrainingNotFoundException {
        return this.trainingService.trainings(page, size);
    }

    @GetMapping(path = "v1/trainings/{idTraining}")
    public TrainingDTO getService(@PathVariable(name = "idTraining") Long idTraining) throws TrainingNotFoundException {
        return this.trainingService.training(idTraining);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/trainings/findBySlug/{trainingSlug}")
    public TrainingDTO findServiceBySlug(@PathVariable(name = "trainingSlug") String trainingSlug) throws TrainingNotFoundException {
        return this.trainingService.findTrainingBySlug(trainingSlug);
    }

    @GetMapping(path = "v1/trainings/imageCover")
    public Resource getImageCover(@RequestParam(name = "imageCoverName") String imageCoverName) throws StorageFileNotFoundException {
        return this.trainingService.getImageCover(imageCoverName);
    }

}
