package io.fi0x.languagegenerator.logic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word
{
    private Long languageId;
    private String word;
}
