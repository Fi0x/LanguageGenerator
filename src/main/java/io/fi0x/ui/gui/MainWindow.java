package io.fi0x.ui.gui;

import io.fi0x.javalogger.logging.Logger;
import io.fi0x.logic.LOG;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

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
            Logger.log("Could not load main fxml", LOG.ERROR);
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
