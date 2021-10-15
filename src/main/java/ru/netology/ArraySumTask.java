package ru.netology;

import java.util.concurrent.RecursiveTask;

public class ArraySumTask extends RecursiveTask<Integer> {
    private final int start;
    private final int end;
    private final int[] array;

    public ArraySumTask(int start, int end, int[] array) {
        this.start = start;
        this.end = end;
        this.array = array;
    }

    @Override
    protected Integer compute() {
        final int diff = end - start;
        switch (diff) {
            case 0: {
                return 0;
            }
            case 1: {
                return array[start];
            }
            case 2: {
                return array[start] + array[start + 1];
            }
            default:
                return forkTasksAndGetResult();
        }

    }

    private int forkTasksAndGetResult() {
        final int middle = (end - start) / 2 + start;
        // Создаем задачу для левой части диапазона
        final ArraySumTask task1 = new ArraySumTask(start, middle, array);
        // Создаем задачу для правой части диапазона
        final ArraySumTask task2 = new ArraySumTask(middle, end, array);
        // Запускаем обе задачи в пуле
        invokeAll(task1, task2);
        // Суммируем результаты выполнения обеих задач
        return task1.join() + task2.join();
    }
}
