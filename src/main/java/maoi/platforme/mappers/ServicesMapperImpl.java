package maoi.platforme.mappers;

import maoi.platforme.dtos.ListServiceDTO;
import maoi.platforme.dtos.ServicesDTO;
import maoi.platforme.entities.Services;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ServicesMapperImpl {

    public ServicesDTO fromServices(Services services){
        ServicesDTO servicesDTO = new ServicesDTO();
        BeanUtils.copyProperties(services,servicesDTO);
        return servicesDTO;
    }

    public Services fromServiceDTO (ServicesDTO servicesDTO){
        Services services = new Services();
        BeanUtils.copyProperties(servicesDTO,services);
        return services;
    }

    public ListServiceDTO fromServiceDTOPage(Page<ServicesDTO> servicesDTOPage){
        ListServiceDTO listServiceDTO = new ListServiceDTO();
        listServiceDTO.setListServicesDTO(servicesDTOPage.getContent());
        listServiceDTO.setCurrentPage(servicesDTOPage.getNumber());
        listServiceDTO.setPageSize(servicesDTOPage.getSize());
        listServiceDTO.setTotalPages(servicesDTOPage.getTotalPages());
        return listServiceDTO;
    }
}
