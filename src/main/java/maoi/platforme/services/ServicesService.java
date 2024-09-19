package maoi.platforme.services;

import maoi.platforme.dtos.ListServiceDTO;
import maoi.platforme.dtos.ServicesDTO;
import maoi.platforme.exception.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicesService {

    ServicesDTO save(ServicesDTO servicesDTO, MultipartFile file) throws ServicesSlugInvalidException, StorageException, ServicesSlugUsedException;

    ServicesDTO update(Long idServices, ServicesDTO servicesDTO, MultipartFile file) throws ServicesNotFoundException, StorageException, ServicesSlugInvalidException;

    void delete(Long idService) throws ServicesNotFoundException, IOException;

    ListServiceDTO services(int page, int size) throws ServicesNotFoundException;

    ServicesDTO service(Long idService) throws ServicesNotFoundException;

    ServicesDTO findServiceBySlug(String slug) throws ServicesNotFoundException;

    Resource getImageCover(String fileName) throws StorageFileNotFoundException;

}
