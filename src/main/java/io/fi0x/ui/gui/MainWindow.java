package io.fi0x.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MainWindow extends Application
{
    @Override
    public void start(Stage stage)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root;
        try
        {
            root = loader.load();
        } catch(IOException e)
        {
            log.error("Could not load main fxml", e);
            throw new RuntimeException(e);
        }

        stage.setTitle("Language Generator");
        stage.getIcons().add(new Image("images/icon.png"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public static void launchGUI(String[] args)
    {
        Application.launch(args);
    }
}
