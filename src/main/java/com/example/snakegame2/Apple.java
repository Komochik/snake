package com.example.snakegame2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс, представляющий яблоко в игре.
 * Отвечает за генерацию позиции, отрисовку и проверку съедания.
 */
public class Apple {
    private static final Logger logger = LogManager.getLogger(Apple.class);

    private Point position;
    private final int size = 20;

    /**
     * Создает новое яблоко на игровом поле.
     *
     * @param width  ширина игрового поля
     * @param height высота игрового поля
     */
    public Apple(int width, int height) {
        logger.info("Создание нового яблока на поле {}x{}", width, height);
        generateNewApple(width, height, null);
    }

    /**
     * Генерирует новую позицию для яблока.
     * Использует Stream API для поиска свободных позиций.
     *
     * @param width  ширина игрового поля
     * @param height высота игрового поля
     * @param snake  змейка для исключения занятых позиций (может быть null)
     */
    public void generateNewApple(int width, int height, Snake snake) {
        logger.debug("Генерация новой позиции для яблока...");

        Random rand = new Random();
        List<Point> snakeBody = (snake != null) ? snake.getBody() : new ArrayList<>();

        // Генерация всех возможных позиций с использованием Stream API
        List<Point> allPositions = IntStream.range(0, width / size)
                .boxed()
                .flatMap(x -> IntStream.range(0, height / size)
                        .mapToObj(y -> new Point(x * size, y * size)))
                .collect(Collectors.toList());

        logger.debug("Всего возможных позиций: {}", allPositions.size());

        // Фильтрация свободных позиций с использованием Stream API
        List<Point> freePositions = allPositions.stream()
                .filter(point -> snakeBody.stream().noneMatch(snakePoint -> snakePoint.equals(point)))
                .collect(Collectors.toList());

        logger.debug("Свободных позиций: {}", freePositions.size());

        if (!freePositions.isEmpty()) {
            position = freePositions.get(rand.nextInt(freePositions.size()));
            logger.info("Яблоко сгенерировано в позиции: {}", position);
        } else {
            position = new Point(0, 0);
            logger.warn("Нет свободных позиций! Яблоко установлено в (0, 0)");
        }
    }

    /**
     * Отрисовывает яблоко на графическом контексте.
     *
     * @param gc графический контекст для отрисовки
     */
    public void draw(GraphicsContext gc) {
        if (position == null) {
            logger.error("Попытка отрисовать яблоко с null позицией!");
            return;
        }

        gc.setFill(Color.RED);
        gc.fillRect(position.getX(), position.getY(), size, size);

        logger.trace("Яблоко отрисовано в {}", position);
    }

    /**
     * Проверяет, съедено ли яблоко змейкой.
     *
     * @param snake змейка для проверки
     * @return true если яблоко съедено, false в противном случае
     */
    public boolean isEaten(Snake snake) {
        if (position == null || snake == null || snake.getBody().isEmpty()) {
            return false;
        }

        boolean eaten = snake.getBody().get(0).equals(position);

        if (eaten) {
            logger.info("Яблоко съедено в позиции {}", position);
        }

        return eaten;
    }

    /**
     * Возвращает текущую позицию яблока.
     *
     * @return позиция яблока
     */
    public Point getPosition() {
        return position;
    }
}