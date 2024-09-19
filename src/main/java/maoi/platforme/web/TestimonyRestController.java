package maoi.platforme.web;

import maoi.platforme.dtos.ListTestimonyDTO;
import maoi.platforme.dtos.TestimonyDTO;
import maoi.platforme.exception.TestimonyNotFoundException;
import maoi.platforme.exception.TestimonySlugInvalideException;
import maoi.platforme.exception.TestimonySlugUsedException;
import maoi.platforme.services.TestimonyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestimonyRestController {
    private TestimonyServiceImpl testimonyService;

    public TestimonyRestController(TestimonyServiceImpl testimonyService) {
        this.testimonyService = testimonyService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/v1/testimony/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestimonyDTO saveTestimony(@RequestBody TestimonyDTO testimonyDTO) throws TestimonySlugInvalideException, TestimonySlugUsedException {
        return testimonyService.saveTestimony(testimonyDTO);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "v1/testimony/update/{idTestimony}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestimonyDTO updateTestimony(@PathVariable(name = "idTestimony") Long idTestimony, @RequestBody TestimonyDTO testimonyDTO) throws TestimonyNotFoundException, TestimonySlugInvalideException {
        return testimonyService.updateTestimony(idTestimony, testimonyDTO);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "v1/testimony/delete/{idTestimony}")
    public void deleteTestimony(@PathVariable(name = "idTestimony") Long idTestimony) throws TestimonyNotFoundException {
        testimonyService.deleteTestimony(idTestimony);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/testimony/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListTestimonyDTO getAllTestimony(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size) throws TestimonyNotFoundException {
        return testimonyService.getTestimonies(page, size);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/testimony/{idTestimony}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestimonyDTO getTestimonyDTO(@PathVariable(name = "idTestimony") Long idTestimony) throws TestimonyNotFoundException {
        return testimonyService.testimony(idTestimony);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "v1/testimony/findBySlug/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestimonyDTO getTestimonyBySlug(@PathVariable(name = "slug") String slug) throws TestimonyNotFoundException {
        return testimonyService.findTestimonyBySlug(slug);
    }
}
