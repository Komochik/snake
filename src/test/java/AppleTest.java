package com.example.snakegame2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования функциональности класса Apple.
 * Проверяет создание яблока, генерацию позиций и взаимодействие со змейкой.
 */
public class AppleTest {
    private Snake snake;
    private Apple apple;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    /**
     * Инициализация тестовых объектов перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        snake = new Snake(100, 100);
        apple = new Apple(WIDTH, HEIGHT);
    }

    /**
     * Тест создания яблока.
     * Проверяет, что яблоко создается с корректной позицией.
     */
    @Test
    void testAppleCreation() {
        assertNotNull(apple, "Яблоко должно быть создано");
        assertNotNull(apple.getPosition(), "Яблоко должно иметь позицию");
    }

    /**
     * Тест генерации новой позиции яблока.
     * Проверяет, что метод generateNewApple работает без ошибок.
     */
    @Test
    void testAppleRegeneration() {
        Point initialPosition = apple.getPosition();

        apple.generateNewApple(WIDTH, HEIGHT, snake);
        Point newPosition = apple.getPosition();

        assertNotNull(newPosition, "Новая позиция яблока не должна быть null");
    }

    /**
     * Тест метода isEaten.
     * Проверяет логику определения съедения яблока.
     */
    @Test
    void testAppleIsEatenMethod() {
        Apple testApple = new Apple(WIDTH, HEIGHT);

        assertFalse(testApple.isEaten(snake),
                "Яблоко не на голове змейки не должно считаться съеденным");
    }

    /**
     * Интеграционный тест яблока и змейки.
     * Проверяет совместную работу двух классов.
     */
    @Test
    void testAppleAndSnakeIntegration() {
        apple.generateNewApple(WIDTH, HEIGHT, snake);

        assertNotNull(apple.getPosition(), "Яблоко должно иметь позицию после генерации");
        assertNotNull(snake.getBody(), "Змейка должна иметь тело");

        snake.setDirection(1, 0);
        boolean gameOver = snake.move(WIDTH, HEIGHT);

        assertFalse(gameOver, "Движение к яблоку не должно завершать игру");
    }
}