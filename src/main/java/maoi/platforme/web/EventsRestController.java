package maoi.platforme.web;

import lombok.extern.slf4j.Slf4j;
import maoi.platforme.dtos.EventsDTO;
import maoi.platforme.dtos.ListEventsDTO;
import maoi.platforme.exception.*;
import maoi.platforme.services.EventsService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class EventsRestController {
    private EventsService eventsService;

    public EventsRestController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path="v1/events/save")
    public EventsDTO saveEvents(@ModelAttribute EventsDTO eventsDTO,
                                @RequestParam(name = "file") MultipartFile file) throws EventsSlugUsedException, StorageException, EventsSlugInvalidException, UsersAdminException {
        return this.eventsService.saveEvents(eventsDTO, file);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(path = "v1/events/update/{idEvents}")
    public  EventsDTO updateEvewnt(@PathVariable(name = "idEvents") Long idEvent,
                                   @ModelAttribute EventsDTO eventsDTO,
                                   @RequestParam(name = "file") MultipartFile file) throws EventNotFoundException, StorageException, EventsSlugInvalidException, UsersAdminException {
        return this.eventsService.updateEvents(idEvent,eventsDTO,file);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "v1/events/delete/{idEvents}")
    public void deleteEvents(@PathVariable(name = "idEvents") Long idEvents) throws IOException, EventNotFoundException, UsersAdminException {
        this.eventsService.deleteEvents(idEvents);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path="v1/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListEventsDTO getEvents(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size) throws EventNotFoundException,UsersAdminException {
        ListEventsDTO listEventsDTO = this.eventsService.events(page,size);
      return listEventsDTO;
    }

    @GetMapping(path="v1/events/{idEvents}")
    public EventsDTO getEvent(@PathVariable(name = "idEvents") Long idEvents) throws EventNotFoundException {
        return this.eventsService.event(idEvents);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/events/findBySlug/{eventsSlug}")
    public EventsDTO findEventsBySlug(@PathVariable(name = "eventsSlug") String eventsSlug) throws EventNotFoundException {
        return this.eventsService.findEventBySlug(eventsSlug);
    }

    @GetMapping(path = "v1/events/imageCover")
    public Resource getImageCover(@RequestParam(name = "imageCoverName") String imageCoverName) throws StorageFileNotFoundException {
        return this.eventsService.getImageCover(imageCoverName);
    }

}
