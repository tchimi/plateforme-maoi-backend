package maoi.platforme.services;

import maoi.platforme.dtos.ListTestimonyDTO;
import maoi.platforme.dtos.TestimonyDTO;
import maoi.platforme.exception.TestimonyNotFoundException;
import maoi.platforme.exception.TestimonySlugInvalideException;
import maoi.platforme.exception.TestimonySlugUsedException;

public interface TestimonyService {
    TestimonyDTO saveTestimony(TestimonyDTO testimonyDTO) throws TestimonySlugInvalideException, TestimonySlugUsedException;

    TestimonyDTO updateTestimony(Long idTestimony,  TestimonyDTO testimonyDTO) throws TestimonyNotFoundException, TestimonySlugInvalideException;

    void deleteTestimony(Long idTestimony) throws TestimonyNotFoundException;

    TestimonyDTO findTestimonyBySlug(String slug) throws TestimonyNotFoundException;

    ListTestimonyDTO getTestimonies(int page, int size) throws TestimonyNotFoundException;

    TestimonyDTO testimony(Long idTestimony) throws TestimonyNotFoundException;
}
