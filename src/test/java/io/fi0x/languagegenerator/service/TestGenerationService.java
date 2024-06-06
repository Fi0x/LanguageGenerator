package io.fi0x.languagegenerator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

//TODO: Add missing tests
@RunWith(SpringRunner.class)
public class TestGenerationService
{
    @InjectMocks
    private GenerationService service;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Tag("UnitTest")
    void test_generateWords_success()
    {
    }
}
