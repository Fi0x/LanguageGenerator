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

        // TODO: Fix jsp-file with correct word usage
        List<String> wordList = new ArrayList<>();
        wordList.add(language);
        model.put("words", wordList);
        return "wordView";
    }
}
