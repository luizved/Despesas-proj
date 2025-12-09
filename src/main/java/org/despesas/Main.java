package org.despesas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(@NotNull Stage primaryStage) {
        try {
            // Carrega o arquivo FXML
            // ATENÇÃO: O caminho do FXML deve ser ajustado para a sua estrutura de projeto
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/org/despesas/hello-view.fxml"));
            AnchorPane rootLayout = loader.load();

            // Cria a Scene
            Scene scene = new Scene(rootLayout);

            // Configura o Stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Registro de Despesas Pessoais - PostgreSQL");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
