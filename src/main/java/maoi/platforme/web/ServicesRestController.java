package maoi.platforme.web;

import maoi.platforme.dtos.ListServiceDTO;
import maoi.platforme.dtos.ServicesDTO;
import maoi.platforme.exception.*;
import maoi.platforme.services.ServicesServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ServicesRestController {
    private ServicesServiceImpl services;

    public ServicesRestController(ServicesServiceImpl services) {
        this.services = services;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "v1/services/save")
    public ServicesDTO saveServices(@ModelAttribute ServicesDTO servicesDTO,
                                    @RequestParam(name = "file") MultipartFile file) throws StorageException, ServicesSlugUsedException, ServicesSlugInvalidException {
        return this.services.save(servicesDTO, file);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(path = "v1/services/update/{idServices}")
    public ServicesDTO updateServices(@PathVariable(name = "idServices") Long idServices,
                                      @ModelAttribute ServicesDTO servicesDTO,
                                      @RequestParam(name = "file") MultipartFile file) throws ServicesNotFoundException, StorageException, ServicesSlugInvalidException {
        return this.services.update(idServices, servicesDTO, file);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "v1/services/delete/{idServices}")
    public void deleteServices(@PathVariable(name = "idServices") Long idServices) throws ServicesNotFoundException, IOException {
        this.services.delete(idServices);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/services")
    public ListServiceDTO getServices(@RequestParam(name = "Page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size) throws ServicesNotFoundException {
        return this.services.services(page, size);
    }

    @GetMapping(path = "v1/services/{idServices}")
    public ServicesDTO getService(@PathVariable(name = "idServices") Long idServices) throws ServicesNotFoundException {
        return this.services.service(idServices);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/services/findBySlug/{servicesSlug}")
    public ServicesDTO findServiceBySlug(@PathVariable(name = "servicesSlug") String servicesSlug) throws ServicesNotFoundException {
        return this.services.findServiceBySlug(servicesSlug);
    }

    @GetMapping(path = "v1/services/imageCover")
    public Resource getImageCover(@RequestParam(name = "imageCoverName") String imageCoverName) throws StorageFileNotFoundException {
        return this.services.getImageCover(imageCoverName);
    }
}
