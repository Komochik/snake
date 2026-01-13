package com.example.snakegame2;

/**
 * Класс, представляющий точку на игровом поле.
 * Используется для хранения координат объектов игры.
 * Реализует методы equals() и hashCode() для корректного сравнения.
 */
public class Point {
    private final int x;
    private final int y;

    /**
     * Создает новую точку с указанными координатами.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату X точки.
     *
     * @return координата X
     */
    public int getX() { return x; }

    /**
     * Возвращает координату Y точки.
     *
     * @return координата Y
     */
    public int getY() { return y; }

    /**
     * Сравнивает две точки на равенство.
     * Точки равны, если их координаты X и Y совпадают.
     *
     * @param obj объект для сравнения
     * @return true если точки равны, false в противном случае
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    /**
     * Возвращает хэш-код точки.
     * Используется для корректной работы в коллекциях.
     *
     * @return хэш-код точки
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * Возвращает строковое представление точки.
     *
     * @return строка в формате "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}