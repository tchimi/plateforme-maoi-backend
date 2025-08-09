package maoi.platforme.services;

import jakarta.transaction.Transactional;
import maoi.platforme.dtos.ListServiceDTO;
import maoi.platforme.dtos.ServicesDTO;
import maoi.platforme.entities.Services;
import maoi.platforme.exception.*;
import maoi.platforme.mappers.ServicesMapperImpl;
import maoi.platforme.repositories.ServicesRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ServicesServiceImpl implements ServicesService {

    private ServicesRepository servicesRepository;
    private UploadFileService uploadFileService;
    private ServicesMapperImpl servicesMapper;
    private final Path rootLocation = Paths.get("upload/");
    private final String storageDir = "Services/coverImage";

    public ServicesServiceImpl(ServicesRepository servicesRepository, UploadFileService uploadFileService, ServicesMapperImpl servicesMapper) {
        this.servicesRepository = servicesRepository;
        this.uploadFileService = uploadFileService;
        this.servicesMapper = servicesMapper;
    }

    @Override
    public ServicesDTO save(ServicesDTO servicesDTO, MultipartFile file) throws ServicesSlugInvalidException, StorageException, ServicesSlugUsedException {
        if (servicesDTO.getSlug() == null) {
            throw new ServicesSlugInvalidException("slug not found");
        }
        if (servicesDTO.getSlug().contains(" ")) {
            throw new ServicesSlugInvalidException("Votre slug est invalide");
        }
        Optional servicesOptional = servicesRepository.findBySlug(servicesDTO.getSlug());
        if (servicesOptional.isPresent()) {
            throw new ServicesSlugUsedException("Votre slug est déjà utilisé");
        }
        servicesDTO.setCreatedAt(new Date());
        servicesDTO.setUpdatedAt(servicesDTO.getCreatedAt());
        String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
        servicesDTO.setImageCover(fileName);
        Services services = servicesMapper.fromServiceDTO(servicesDTO);
        Services servicesSave = servicesRepository.save(services);
        return servicesMapper.fromServices(servicesSave);
    }

    @Override
    public ServicesDTO update(Long idServices, ServicesDTO servicesDTO, MultipartFile file) throws ServicesNotFoundException, StorageException, ServicesSlugInvalidException {
        ServicesDTO oldServicesDTO = service(idServices);
        String slug = servicesDTO.getSlug();
        String oldSlug = oldServicesDTO.getSlug();
        if (!oldSlug.equalsIgnoreCase(slug)) {
            if (slug.contains(" ")) {
                throw new ServicesSlugInvalidException("Votre slug est invalide");
            }
        }
        servicesDTO.setIdService(idServices);
        servicesDTO.setUpdatedAt(new Date());
        if (file != null) {

            String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
            servicesDTO.setImageCover(fileName);
        }
        Services services = servicesMapper.fromServiceDTO(servicesDTO);
        Services servicesSave = servicesRepository.save(services);
        return servicesMapper.fromServices(servicesSave);
    }

    @Override
    public void delete(Long idService) throws ServicesNotFoundException, IOException {
        ServicesDTO servicesDTO = service(idService);
        if (servicesDTO != null) {
            String fileName = servicesDTO.getImageCover();
            uploadFileService.deleteFile(fileName, storageDir, rootLocation);
            servicesRepository.deleteById(idService);
        }
    }

    @Override
    public ListServiceDTO services(int page, int size) throws ServicesNotFoundException {
        Sort.Direction direction = Sort.Direction.fromString("DESC");
        String sortBy = "updatedAt";
        Page<Services> servicesPage = servicesRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)));
        if (servicesPage == null) {
            throw new ServicesNotFoundException("service not found");
        }
        Page<ServicesDTO> servicesDTOPage = servicesPage.map(service -> servicesMapper.fromServices(service));
        return servicesMapper.fromServiceDTOPage(servicesDTOPage);
    }

    @Override
    public ServicesDTO service(Long idService) throws ServicesNotFoundException {
        Services services = servicesRepository.findById(idService).orElseThrow(() -> new ServicesNotFoundException("service not found "));
        return servicesMapper.fromServices(services);
    }

    @Override
    public ServicesDTO findServiceBySlug(String slug) throws ServicesNotFoundException {
        Services services = servicesRepository.findBySlug(slug).orElseThrow(() -> new ServicesNotFoundException("service not found with slug " + slug));
        return servicesMapper.fromServices(services);
    }

    @Override
    public Resource getImageCover(String fileName) throws StorageFileNotFoundException {
        return this.uploadFileService.loadAsResource(fileName, storageDir, this.rootLocation);
    }
}
