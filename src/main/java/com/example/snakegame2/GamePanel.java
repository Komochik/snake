package com.example.snakegame2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GamePanel extends StackPane {
    private static final Logger logger = LogManager.getLogger(GamePanel.class);

    private final Canvas canvas;
    private final GraphicsContext gc;
    private Snake snake;
    private Apple apple;
    private final int width, height;
    private boolean gameOver = false;

    private static final int SLOW_SPEED = 5;
    private static final int MEDIUM_SPEED = 10;
    private static final int FAST_SPEED = 15;
    private int speed = MEDIUM_SPEED;
    private int currentSpeedMode = 1;

    public GamePanel(int width, int height) {
        this.width = width;
        this.height = height;

        logger.info("Инициализация GamePanel {}x{}", width, height);

        // Создаем змейку и яблоко
        this.snake = new Snake(width / 2, height / 2);
        this.apple = new Apple(width, height);

        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();

        getChildren().add(canvas);
        setFocusTraversable(true);

        setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            handleKeyPress(keyCode);
        });

        setOnMouseClicked(event -> requestFocus());
    }

    private void handleKeyPress(KeyCode keyCode) {
        if (gameOver) {
            return;
        }

        switch (keyCode) {
            case W -> snake.setDirection(0, -1);
            case S -> snake.setDirection(0, 1);
            case A -> snake.setDirection(-1, 0);
            case D -> snake.setDirection(1, 0);
            case UP -> switchSpeedMode(true);
            case DOWN -> switchSpeedMode(false);
        }
    }

    private void switchSpeedMode(boolean increase) {
        if (increase) {
            currentSpeedMode = (currentSpeedMode + 1) % 3;
        } else {
            currentSpeedMode = (currentSpeedMode + 2) % 3;
        }
        updateSpeed();
    }

    private void updateSpeed() {
        switch (currentSpeedMode) {
            case 0 -> speed = SLOW_SPEED;
            case 1 -> speed = MEDIUM_SPEED;
            case 2 -> speed = FAST_SPEED;
        }
    }

    public void gameLoop() {
        if (gameOver) {
            return;
        }

        gameOver = snake.move(width, height);

        if (gameOver) {
            drawGameOver();
            return;
        }

        if (apple.isEaten(snake)) {
            snake.grow();
            apple.generateNewApple(width, height, snake);
        }

        draw();
    }

    private void draw() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        snake.draw(gc);
        apple.draw(gc);

        // Информация
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 14));
        gc.fillText("Длина: " + snake.getLength(), 10, 20);
        gc.fillText("Скорость: " +
                switch(currentSpeedMode) {
                    case 0 -> "Медленно";
                    case 1 -> "Средне";
                    case 2 -> "Быстро";
                    default -> "?";
                }, 10, 40);
    }

    private void drawGameOver() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", 50));
        gc.fillText("GAME OVER", width / 2 - 150, height / 2);

        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", 30));
        gc.fillText("Длина: " + snake.getLength(), width / 2 - 80, height / 2 + 60);
    }

    public int getSpeed() {
        return speed;
    }
}