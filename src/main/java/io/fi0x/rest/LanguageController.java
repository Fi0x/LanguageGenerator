package io.fi0x.rest;

import io.fi0x.logic.LanguageTraits;
import io.fi0x.logic.Randomizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LanguageController
{
    @GetMapping("/generate-words/{language}")
    public String generateWord(ModelMap model, @PathVariable(value = "language") String language,
                               @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        LanguageTraits.loadTemplateLanguage();
        // TODO: Load correct language-file

        Randomizer.generateWords(amount);
        List<Word> wordList = Randomizer.getGeneratedWords().stream().map((word) -> convert(language, word)).toList();
        model.put("words", wordList);

        return "wordView";
    }

    private Word convert(String language, String word)
    {
        return new Word(language, word);
    }
}
