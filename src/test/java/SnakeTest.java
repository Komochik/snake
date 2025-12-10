package com.example.snakegame2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования функциональности класса Snake.
 * Проверяет движение, рост, столкновения и отражение змейки.
 */
public class SnakeTest {
    private Snake snake;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int SEGMENT_SIZE = 20;

    /**
     * Инициализация тестового объекта перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        snake = new Snake(100, 100);
    }

    /**
     * Тест инициализации змейки.
     * Проверяет, что змейка создается с корректными параметрами.
     */
    @Test
    void testSnakeInitialization() {
        assertEquals(1, snake.getLength(), "Змейка должна начинаться с длины 1");
        List<Point> body = snake.getBody();
        assertNotNull(body, "Тело змейки не должно быть null");
        assertFalse(body.isEmpty(), "Тело змейки не должно быть пустым");
    }

    /**
     * Тест движения змейки.
     * Проверяет, что змейка корректно перемещается по полю.
     */
    @Test
    void testSnakeMovement() {
        int initialLength = snake.getLength();

        snake.setDirection(1, 0); // Вправо
        boolean gameOver = snake.move(WIDTH, HEIGHT);

        assertFalse(gameOver, "Движение не должно завершать игру");
        assertEquals(initialLength, snake.getLength(), "Длина не должна меняться при движении");
    }

    /**
     * Тест роста змейки.
     * Проверяет, что метод grow() увеличивает длину змейки.
     */
    @Test
    void testSnakeGrow() {
        int initialLength = snake.getLength();

        snake.grow();

        assertEquals(initialLength + 1, snake.getLength(),
                "Длина должна увеличиться на 1 после grow()");
    }

    /**
     * Тест отражения змейки от границ.
     * Проверяет, что змейка не умирает при столкновении с границами поля.
     */
    @Test
    void testSnakeDoesNotDieOnBoundaryReflection() {
        // Проверяем все 4 стены

        // Левая стена
        Snake leftSnake = new Snake(0, 100);
        leftSnake.setDirection(-1, 0);
        assertFalse(leftSnake.move(WIDTH, HEIGHT),
                "Змейка должна отражаться от левой стены, а не умирать");

        // Правая стена
        Snake rightSnake = new Snake(WIDTH - SEGMENT_SIZE, 100);
        rightSnake.setDirection(1, 0);
        assertFalse(rightSnake.move(WIDTH, HEIGHT),
                "Змейка должна отражаться от правой стены, а не умирать");

        // Верхняя стена
        Snake topSnake = new Snake(100, 0);
        topSnake.setDirection(0, -1);
        assertFalse(topSnake.move(WIDTH, HEIGHT),
                "Змейка должна отражаться от верхней стены, а не умирать");

        // Нижняя стена
        Snake bottomSnake = new Snake(100, HEIGHT - SEGMENT_SIZE);
        bottomSnake.setDirection(0, 1);
        assertFalse(bottomSnake.move(WIDTH, HEIGHT),
                "Змейка должна отражаться от нижней стены, а не умирать");
    }

    /**
     * Тест столкновения змейки с собой.
     * Проверяет, что игра завершается при столкновении головы с телом.
     */
    @Test
    void testSnakeDiesOnSelfCollision() {
        // Создаем длинную змейку
        Snake longSnake = new Snake(100, 100);
        for (int i = 0; i < 3; i++) {
            longSnake.grow();
        }

        // Делаем маневр, чтобы голова столкнулась с телом
        longSnake.setDirection(1, 0); // Вправо
        longSnake.move(WIDTH, HEIGHT);

        longSnake.setDirection(0, 1); // Вниз
        longSnake.move(WIDTH, HEIGHT);

        longSnake.setDirection(-1, 0); // Влево
        longSnake.move(WIDTH, HEIGHT);

        longSnake.setDirection(0, -1); // Вверх
        boolean gameOver = longSnake.move(WIDTH, HEIGHT);

        assertTrue(gameOver, "Игра должна завершиться при столкновении с собой");
    }
}