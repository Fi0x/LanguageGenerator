package io.fi0x.languagegenerator.logic;

import org.springframework.stereotype.Component;

//TODO: Add tests for this class
@Component
public class ControlledRandom
{
    public double randomDouble()
    {
        return Math.random();
    }

    public int randomInt(int min, int max)
    {
        return ((int) (Math.random() * (max - min + 1))) + min;
    }
}
