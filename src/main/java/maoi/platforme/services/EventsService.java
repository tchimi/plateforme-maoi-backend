package maoi.platforme.services;

import maoi.platforme.dtos.EventsDTO;
import maoi.platforme.dtos.ListEventsDTO;
import maoi.platforme.exception.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface EventsService {
    EventsDTO saveEvents(EventsDTO eventsDTO, MultipartFile file) throws EventsSlugInvalidException, EventsSlugUsedException, StorageException;

    EventsDTO updateEvents(Long idEvent, EventsDTO eventsDTO, MultipartFile file) throws EventNotFoundException, EventsSlugInvalidException, StorageException;

    void deleteEvents(Long idEvent) throws EventNotFoundException, IOException;

    EventsDTO findEventBySlug(String slug) throws EventNotFoundException;

    ListEventsDTO events(int page, int size) throws EventNotFoundException;

    EventsDTO event(Long idEvent) throws EventNotFoundException;

    Resource getImageCover(String filename) throws StorageFileNotFoundException;
}
