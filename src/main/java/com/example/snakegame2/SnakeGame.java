package com.example.snakegame2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SnakeGame extends Application {
    private static final Logger logger = LogManager.getLogger(SnakeGame.class);

    // Статический блок для инициализации JavaFX
    static {
        // Для JavaFX на Mac
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("javafx.preloader", "null");
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("=".repeat(50));
        logger.info("ЗАПУСК ИГРЫ 'ЗМЕЙКА'");
        logger.info("=".repeat(50));

        try {
            GamePanel gamePanel = new GamePanel(800, 600);
            Scene scene = new Scene(gamePanel, 800, 600);

            primaryStage.setTitle("Snake");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            gamePanel.requestFocus();
            logger.info("Окно игры создано");

            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= 1_000_000_000 / gamePanel.getSpeed()) {
                        gamePanel.gameLoop();
                        lastUpdate = now;
                    }
                }
            };

            timer.start();
            logger.info("Игровой цикл запущен");

            primaryStage.setOnCloseRequest(event -> {
                logger.info("Закрытие игры");
                timer.stop();
                Platform.exit();
            });

        } catch (Exception e) {
            logger.error("Ошибка запуска: {}", e.getMessage(), e);
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        // Запуск JavaFX приложения
        launch(args);
    }
}