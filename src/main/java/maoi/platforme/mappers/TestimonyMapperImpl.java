package maoi.platforme.mappers;

import maoi.platforme.dtos.ListTestimonyDTO;
import maoi.platforme.dtos.TestimonyDTO;
import maoi.platforme.entities.Testimony;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public class TestimonyMapperImpl {

    public TestimonyDTO fromTestimony(Testimony testimony){
        TestimonyDTO testimonyDTO = new TestimonyDTO();
        BeanUtils.copyProperties(testimony, testimonyDTO);
        return testimonyDTO;
    }

    public  Testimony fromTestimonyDTO(TestimonyDTO testimonyDTO){
        Testimony testimony = new Testimony();
        BeanUtils.copyProperties(testimonyDTO, testimony);
        return testimony;
    }

    public ListTestimonyDTO fromListTestimony(Page<TestimonyDTO> pageTestionyDTO){
        ListTestimonyDTO listTestimonyDTO = new ListTestimonyDTO();
        listTestimonyDTO.setListTestimonyDTO(pageTestionyDTO.getContent());
        listTestimonyDTO.setCurrentPage(pageTestionyDTO.getNumber());
        listTestimonyDTO.setPageSize(pageTestionyDTO.getSize());
        listTestimonyDTO.setTotalPages(pageTestionyDTO.getTotalPages());
        return listTestimonyDTO;
    }
}
