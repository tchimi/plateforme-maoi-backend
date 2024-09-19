package maoi.platforme.services;

import maoi.platforme.dtos.ListTestimonyDTO;
import maoi.platforme.dtos.TestimonyDTO;
import maoi.platforme.entities.Testimony;
import maoi.platforme.exception.TestimonyNotFoundException;
import maoi.platforme.exception.TestimonySlugInvalideException;
import maoi.platforme.exception.TestimonySlugUsedException;
import maoi.platforme.mappers.TestimonyMapperImpl;
import maoi.platforme.repositories.TestimonyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TestimonyServiceImpl implements TestimonyService {

    private TestimonyRepository testimonyRepository;
    private TestimonyMapperImpl testimonyMapper;

    public TestimonyServiceImpl(TestimonyRepository testimonyRepository, TestimonyMapperImpl testimonyMapper) {
        this.testimonyRepository = testimonyRepository;
        this.testimonyMapper = testimonyMapper;
    }

    @Override
    public TestimonyDTO saveTestimony(TestimonyDTO testimonyDTO) throws TestimonySlugInvalideException, TestimonySlugUsedException {

        if(testimonyDTO.getSlug() == null){
            throw new TestimonySlugInvalideException("slug not found");
        }
        if (testimonyDTO.getSlug().contains(" ")) {
            throw new TestimonySlugInvalideException("Votre slug est invalide");
        }

        Optional TestimonyOptional = this.testimonyRepository.findBySlug(testimonyDTO.getSlug());
        if (TestimonyOptional.isPresent()) {
            throw new TestimonySlugUsedException("Votre slug est déjà utilisé");
        }

        testimonyDTO.setCreatedAt(new Date());
        testimonyDTO.setUpdatedAt(testimonyDTO.getCreatedAt());
        Testimony testimony = testimonyMapper.fromTestimonyDTO(testimonyDTO);
        Testimony testimonySave = testimonyRepository.save(testimony);
        return  testimonyMapper.fromTestimony(testimonySave);
    }

    @Override
    public TestimonyDTO updateTestimony(Long idTestimony, TestimonyDTO testimonyDTO) throws TestimonyNotFoundException, TestimonySlugInvalideException {
        TestimonyDTO oldTestimonyDTO = testimony(idTestimony);
        String slug = testimonyDTO.getSlug();
        String oldSlug = oldTestimonyDTO.getSlug();
        if (!oldSlug.equalsIgnoreCase(slug)){
            if(slug == null){
                throw new TestimonySlugInvalideException("slug not found");
            }
            if (slug.contains(" ")) {
                throw new TestimonySlugInvalideException("Votre slug est invalide");
            }
        }
        testimonyDTO.setId(idTestimony);
        testimonyDTO.setUpdatedAt(new Date());
        Testimony testimony = testimonyMapper.fromTestimonyDTO(testimonyDTO);
        Testimony testimonySave = testimonyRepository.save(testimony);
        return testimonyMapper.fromTestimony(testimonySave);
    }


    @Override
    public void deleteTestimony(Long idTestimony) throws TestimonyNotFoundException {
        Testimony testimony = testimonyRepository.findById(idTestimony).orElseThrow(() -> new TestimonyNotFoundException("testimony not found"));
        if(testimony != null){
            testimonyRepository.deleteById(idTestimony);
        }
    }

    @Override
    public TestimonyDTO findTestimonyBySlug(String slug) throws TestimonyNotFoundException {
        Testimony testimony = testimonyRepository.findBySlug(slug).orElseThrow(()-> new TestimonyNotFoundException("testimony not found"));
        return testimonyMapper.fromTestimony(testimony);
    }

    @Override
    public ListTestimonyDTO getTestimonies(int page, int size) throws TestimonyNotFoundException {
        Sort.Direction direction = Sort.Direction.fromString("DESC");
        String sortBy = "updatedAt";
        Page<Testimony> listTestimonies = testimonyRepository.findAll(PageRequest.of(page, size, Sort.by(direction,sortBy)));
        if (listTestimonies.isEmpty()) {
            throw new TestimonyNotFoundException("list testimonies not found");
        }
        Page<TestimonyDTO> testimonyDTOPage = listTestimonies.map(testimony -> testimonyMapper.fromTestimony(testimony));
        return testimonyMapper.fromListTestimony(testimonyDTOPage);
    }

    @Override
    public TestimonyDTO testimony(Long idTestimony) throws TestimonyNotFoundException {
        Testimony testimony = testimonyRepository.findById(idTestimony).orElseThrow(() -> new TestimonyNotFoundException("testimony not found"));
        return testimonyMapper.fromTestimony(testimony);
    }
}
