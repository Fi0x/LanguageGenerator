package io.fi0x.languagegenerator.client.gui.controller;

import io.fi0x.languagegenerator.client.logic.FileLoader;
import io.fi0x.languagegenerator.client.logic.Randomizer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

@Slf4j
@Deprecated
public class Main implements Initializable
{
    @FXML
    private ComboBox<String> languageDropDown;
    @FXML
    public TextArea outputWords;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        languageDropDown.getItems().addAll(FileLoader.getLoadedLanguageNames(io.fi0x.languagegenerator.client.Main.languageFolder, false));
        languageDropDown.getSelectionModel().select(FileLoader.getActiveLanguage());
        languageDropDown.setOnAction(actionEvent -> FileLoader.loadLanguageFile(io.fi0x.languagegenerator.client.Main.languageFolder, languageDropDown.getValue()));
    }

    @FXML
    private void onGenerateNamesClicked()
    {
        int amount = 100;
        log.trace("Generating {} names", amount);
        Randomizer.generateWords(amount);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        for(String word : Randomizer.getGeneratedWords())
            joiner.add(word);
        outputWords.setText(joiner.toString());

        log.info("Names generated");
    }
}
