package maoi.platforme.mappers;

import maoi.platforme.dtos.EventsDTO;
import maoi.platforme.dtos.ListEventsDTO;
import maoi.platforme.entities.Events;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsMapperImpl {

    public EventsDTO fromEvents(Events events){
        EventsDTO eventsDTO = new EventsDTO();
        BeanUtils.copyProperties(events,eventsDTO);
        return eventsDTO;
    }

    public Events fromEventsDTO(EventsDTO eventsDTO){
        Events events = new Events();
        BeanUtils.copyProperties(eventsDTO,events);
        return events;
    }

    public ListEventsDTO fromPageEventsDto(Page<EventsDTO> eventsDTOPage){
        ListEventsDTO listEventsDTO = new ListEventsDTO();
        listEventsDTO.setListEventsDTO(eventsDTOPage.getContent());
        listEventsDTO.setCurrentPage(eventsDTOPage.getNumber());
        listEventsDTO.setPageSize(eventsDTOPage.getSize());
        listEventsDTO.setTotalPages(eventsDTOPage.getTotalPages());
        return listEventsDTO;
    }
}
