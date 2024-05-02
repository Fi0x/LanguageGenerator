package io.fi0x.rest;

import io.fi0x.logic.LanguageTraits;
import io.fi0x.logic.Randomizer;
import io.fi0x.logic.dto.Word;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LanguageController
{
    /**
     * Generates random words in the provided language
     *
     * @param model    Internal use to display the webpage
     * @param language The desired language
     * @param amount   The amount of words that should get generated
     * @return A webpage that displays the requested words
     */
    @GetMapping("/generate/{language}")
    public String generateWord(ModelMap model, @PathVariable(value = "language") String language,
                               @RequestParam(value = "amount", defaultValue = "1", required = false) int amount)
    {
        LanguageTraits.loadTemplateLanguage();
        // TODO: Load correct language-file

        Randomizer.generateWords(amount);
        List<Word> wordList = Randomizer.getGeneratedWords().stream().map((word) -> new Word(language, word)).toList();
        model.put("words", wordList);

        return "wordView";
    }

    // TODO: Add user accounts that can be created for free and are stored in a local db
    // TODO: Add a mapping to show all loaded languages
    // TODO: Add a mapping to set a specific language as default for the current user
    // TODO: Add a mapping to show a page where a user can create a new language or edit an existing one
}
