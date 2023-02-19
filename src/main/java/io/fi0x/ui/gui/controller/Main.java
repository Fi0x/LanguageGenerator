package io.fi0x.ui.gui.controller;

import io.fi0x.javalogger.logging.Logger;
import io.fi0x.logic.FileLoader;
import io.fi0x.logic.LOG;
import io.fi0x.logic.Randomizer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class Main implements Initializable
{
    @FXML
    private ComboBox<String> languageDropDown;
    @FXML
    public TextArea outputWords;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        languageDropDown.getItems().addAll(FileLoader.getLoadedLanguageNames(false));
        languageDropDown.getSelectionModel().select(FileLoader.getActiveLanguage());
        languageDropDown.setOnAction(actionEvent ->
        {
            FileLoader.loadLanguageFile(languageDropDown.getValue());
            //TODO: Close combobox again
        });
    }

    @FXML
    private void onGenerateNamesClicked()
    {
        int amount = 100;
        Logger.log("Generating " + amount + " names", LOG.SUCCESS);
        Randomizer.generateWords(amount);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        for(String word : Randomizer.getGeneratedWords())
            joiner.add(word);
        outputWords.setText(joiner.toString());

        Logger.log("Names generated", LOG.SUCCESS);
    }
}
