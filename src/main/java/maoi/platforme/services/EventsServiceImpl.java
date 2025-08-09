package maoi.platforme.services;

import maoi.platforme.dtos.EventsDTO;
import maoi.platforme.dtos.ListEventsDTO;
import maoi.platforme.entities.Events;
import maoi.platforme.exception.*;
import maoi.platforme.mappers.EventsMapperImpl;
import maoi.platforme.repositories.EventsRepository;
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
public class EventsServiceImpl implements EventsService {

    private EventsRepository eventsRepository;
    private EventsMapperImpl eventsMapper;
    private UploadFileService uploadFileService;
    private final Path rootLocation = Paths.get("upload/");
    private final String storageDir = "events/coverImage";

    public EventsServiceImpl(EventsRepository eventsRepository, EventsMapperImpl eventsMapper, UploadFileService uploadFileService) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.uploadFileService = uploadFileService;
    }

    @Override
    public EventsDTO saveEvents(EventsDTO eventsDTO, MultipartFile file) throws EventsSlugInvalidException, EventsSlugUsedException, StorageException {

        if (eventsDTO.getSlug() == null) {
            throw new EventsSlugInvalidException("slug not found");
        }
        if (eventsDTO.getSlug().contains(" ")) {
            throw new EventsSlugInvalidException("Votre slug est invalide");
        }
        Optional TestimonyOptional = this.eventsRepository.findBySlug(eventsDTO.getSlug());
        if (TestimonyOptional.isPresent()) {
            throw new EventsSlugUsedException("Votre slug est déjà utilisé");
        }
        eventsDTO.setCreatedAt(new Date());
        eventsDTO.setUpdatedAt(eventsDTO.getCreatedAt());
        String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
        eventsDTO.setImageCover(fileName);
        Events events = eventsMapper.fromEventsDTO(eventsDTO);
        Events saveEvents = eventsRepository.save(events);
        return eventsMapper.fromEvents(saveEvents);
    }

    @Override
    public EventsDTO updateEvents(Long idEvent, EventsDTO eventsDTO, MultipartFile file) throws EventNotFoundException, EventsSlugInvalidException, StorageException {
        EventsDTO oldEventsDTO = event(idEvent);
        String slug = eventsDTO.getSlug();
        String oldSlug = oldEventsDTO.getSlug();
        if (!oldSlug.equalsIgnoreCase(slug)) {
            if (slug.contains(" ")) {
                throw new EventsSlugInvalidException("Votre slug est invalide");
            }
        }
        eventsDTO.setIdEvent(idEvent);
        eventsDTO.setUpdatedAt(new Date());
        if (file != null) {

            String fileName = this.uploadFileService.uploadFile(file, storageDir, rootLocation);
            eventsDTO.setImageCover(fileName);
        }
        Events events = eventsMapper.fromEventsDTO(eventsDTO);
        Events eventsSave = eventsRepository.save(events);
        return eventsMapper.fromEvents(eventsSave);
    }

    @Override
    public void deleteEvents(Long idEvent) throws EventNotFoundException, IOException {
        EventsDTO eventsDTO = event(idEvent);
        if (eventsDTO != null) {
            eventsRepository.deleteById(idEvent);
            String filename = eventsDTO.getImageCover();
            uploadFileService.deleteFile(filename, storageDir, rootLocation);
        }
    }

    @Override
    public EventsDTO findEventBySlug(String slug) throws EventNotFoundException {
        Events events = eventsRepository.findBySlug(slug).orElseThrow(() -> new EventNotFoundException("event not found with slug " + slug));
        return eventsMapper.fromEvents(events);
    }

    @Override
    public ListEventsDTO events(int page, int size) throws EventNotFoundException {
        Sort.Direction direction = Sort.Direction.fromString("DESC");
        String sortBy = "updatedAt";
        Page<Events> eventsPage = eventsRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)));
        if (eventsPage == null) {
            throw new EventNotFoundException("events not found");
        }
        Page<EventsDTO> eventsDTOPage = eventsPage.map(event -> eventsMapper.fromEvents(event));
        return eventsMapper.fromPageEventsDto(eventsDTOPage);
    }

    @Override
    public EventsDTO event(Long idEvent) throws EventNotFoundException {
        Events events = eventsRepository.findById(idEvent).orElseThrow(() -> new EventNotFoundException("event not found"));
        return eventsMapper.fromEvents(events);
    }

    @Override
    public Resource getImageCover(String fileName) throws StorageFileNotFoundException {
        return this.uploadFileService.loadAsResource(fileName, storageDir, this.rootLocation);
    }
}
