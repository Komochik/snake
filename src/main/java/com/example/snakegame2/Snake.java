package com.example.snakegame2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, представляющий змейку в игре.
 * Управляет движением, ростом и отображением змейки.
 * Реализует механизм отражения от границ игрового поля.
 */
public class Snake {
    private static final Logger logger = LogManager.getLogger(Snake.class);

    private final LinkedList<Point> body;
    private int directionX, directionY;
    private final int size = 20;
    private boolean invulnerable = false;
    private int invulnerabilityFrames = 0;

    /**
     * Создает новую змейку в указанной позиции.
     *
     * @param startX начальная X координата головы змейки
     * @param startY начальная Y координата головы змейки
     */
    public Snake(int startX, int startY) {
        body = new LinkedList<>();
        body.add(new Point(startX, startY));
        directionX = 1;
        directionY = 0;
        logger.info("Змейка создана в ({}, {})", startX, startY);
    }

    /**
     * Перемещает змейку на один шаг.
     * Проверяет столкновения с границами и с собой.
     *
     * @param width  ширина игрового поля
     * @param height высота игрового поля
     * @return true если игра окончена (столкновение с собой), false в противном случае
     */
    public boolean move(int width, int height) {
        if (body.isEmpty()) {
            logger.error("Тело змейки пустое!");
            return false;
        }

        Point head = body.getFirst();
        Point newHead = new Point(
                head.getX() + directionX * size,
                head.getY() + directionY * size
        );

        if (isCollidingWithBoundary(newHead, width, height)) {
            logger.info("Столкновение с границей в {}. Отражение...", newHead);
            newHead = reflectOffBoundary(head, width, height);
        }

        if (!invulnerable && selfCollision(newHead)) {
            logger.error("Змейка столкнулась с собой в {}! Конец игры.", newHead);
            return true;
        }

        body.addFirst(newHead);
        body.removeLast();

        if (invulnerabilityFrames > 0) {
            invulnerabilityFrames--;
            if (invulnerabilityFrames == 0) {
                invulnerable = false;
                logger.debug("Неуязвимость закончилась");
            }
        }

        logger.trace("Змейка перемещена в {}", newHead);
        return false;
    }

    /**
     * Проверяет, столкнется ли новая голова с телом змейки.
     *
     * @param newHead позиция новой головы
     * @return true если будет столкновение, false в противном случае
     */
    private boolean selfCollision(Point newHead) {
        boolean collision = body.stream()
                .skip(1)
                .anyMatch(segment -> segment.equals(newHead));

        if (collision) {
            logger.warn("Обнаружено столкновение с собой в {}", newHead);
        }

        return collision;
    }

    /**
     * Проверяет, выходит ли точка за границы игрового поля.
     *
     * @param point  проверяемая точка
     * @param width  ширина поля
     * @param height высота поля
     * @return true если точка за границами, false в противном случае
     */
    private boolean isCollidingWithBoundary(Point point, int width, int height) {
        return point.getX() < 0 || point.getX() >= width ||
                point.getY() < 0 || point.getY() >= height;
    }

    /**
     * Отражает змейку от границы игрового поля.
     * Меняет направление движения и активирует неуязвимость.
     *
     * @param head   текущая позиция головы
     * @param width  ширина поля
     * @param height высота поля
     * @return новая позиция головы после отражения
     */
    private Point reflectOffBoundary(Point head, int width, int height) {
        logger.info("Отражаем змейку от границы...");

        if (head.getX() < 0 || head.getX() >= width) {
            directionX = -directionX;
            logger.debug("Направление X изменено на {}", directionX);
        }

        if (head.getY() < 0 || head.getY() >= height) {
            directionY = -directionY;
            logger.debug("Направление Y изменено на {}", directionY);
        }

        invulnerable = true;
        invulnerabilityFrames = 3;
        logger.info("Активирована неуязвимость на 3 кадра");

        return new Point(
                head.getX() + directionX * size,
                head.getY() + directionY * size
        );
    }

    /**
     * Устанавливает направление движения змейки.
     * Игнорирует попытки разворота на 180 градусов.
     *
     * @param dx направление по оси X (-1, 0, 1)
     * @param dy направление по оси Y (-1, 0, 1)
     */
    public void setDirection(int dx, int dy) {
        if (dx != -directionX || dy != -directionY) {
            directionX = dx;
            directionY = dy;
            logger.info("Направление изменено: X={}, Y={}", dx, dy);
        } else {
            logger.debug("Попытка разворота на 180 градусов игнорирована");
        }
    }

    /**
     * Увеличивает длину змейки на один сегмент.
     */
    public void grow() {
        if (body.isEmpty()) {
            logger.warn("Попытка увеличить пустую змейку");
            return;
        }

        Point tail = body.getLast();
        body.addLast(new Point(tail.getX(), tail.getY()));

        logger.info("Змейка выросла! Новая длина: {}", body.size());
    }

    /**
     * Отрисовывает змейку на графическом контексте.
     *
     * @param gc графический контекст для отрисовки
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        body.forEach(segment -> {
            gc.fillRect(segment.getX(), segment.getY(), size, size);
        });
        logger.trace("Змейка отрисована ({} сегментов)", body.size());
    }

    /**
     * Возвращает текущую длину змейки.
     *
     * @return количество сегментов змейки
     */
    public int getLength() {
        return body.size();
    }

    /**
     * Возвращает копию тела змейки.
     *
     * @return список точек, представляющих тело змейки
     */
    public List<Point> getBody() {
        return new LinkedList<>(body);
    }
}