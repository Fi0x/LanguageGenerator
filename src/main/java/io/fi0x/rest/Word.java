package io.fi0x.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Word
{
    private String language;
    private String word;
}
