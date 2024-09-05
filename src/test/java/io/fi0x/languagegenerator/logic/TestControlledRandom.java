package io.fi0x.languagegenerator.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TestControlledRandom
{
    private final ControlledRandom random = new ControlledRandom();

    @Test
    @Tag("UnitTest")
    void test_randomDouble()
    {
        double result = random.randomDouble();

        Assertions.assertTrue(result < 1);
        Assertions.assertTrue(result >= 0);
    }

    @Tag("UnitTest")
    @ParameterizedTest
    @MethodSource("validIntParameters")
    void test_randomInt_success(int min, int max)
    {
        for (int i = 0; i < 10; i++)
        {
            int result = random.randomInt(min, max);
            Assertions.assertTrue(result <= max);
            Assertions.assertTrue(result >= min);
        }
    }

    @Test
    @Tag("UnitTest")
    void test_randomInt_weird()
    {
        for (int i = 0; i < 10; i++)
        {
            int result = random.randomInt(5, 0);
            Assertions.assertTrue(result <= 5);
            Assertions.assertTrue(result >= 2);
        }
    }

    private static Stream<Arguments> validIntParameters()
    {
        return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(0, 0),
                Arguments.of(1, 1),
                Arguments.of(-1, -1),
                Arguments.of(-1, 1),
                Arguments.of(0, 7),
                Arguments.of(7, 7),
                Arguments.of(-7, -7),
                Arguments.of(-7, 7)
        );
    }
}
