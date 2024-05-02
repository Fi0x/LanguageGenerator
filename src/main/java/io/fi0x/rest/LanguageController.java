package io.fi0x.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LanguageController
{
    @GetMapping("/generate-word/{language}")
    public String generateWord(ModelMap model, @PathVariable(value = "language") String language)
    {
        // TODO: Load correct language-file
        //  Use Generator to generate a single word

        List<Word> wordList = new ArrayList<>();
        wordList.add(new Word(language, "Test Word"));
        model.put("words", wordList);
        return "wordView";
    }
}
