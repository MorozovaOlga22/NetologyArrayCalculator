package ru.netology;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    private static final int MAX_VALUE = 1_000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        makeExperiment(10);
        System.out.println();
        makeExperiment(100);
        System.out.println();
        makeExperiment(1_000);
        System.out.println();
        makeExperiment(10_000);
        System.out.println();
        makeExperiment(100_000);
    }

    private static void makeExperiment(int arraySize) throws ExecutionException, InterruptedException {
        System.out.println("Количество элементов: " + arraySize);
        final int[] array = generateArray(arraySize);
        final long oneThreadTime = calculateInOneThread(array);
        final long manyThreadsTime = calculateInManyThreads(array);
        if (oneThreadTime == manyThreadsTime) {
            System.out.println("Расчет занял одинаковое время");
        } else if (oneThreadTime < manyThreadsTime) {
            System.out.println("В одном потоке быстрее");
        } else {
            System.out.println("В нескольких потоках быстрее");
        }
    }

    private static int[] generateArray(int arraySize) {
        final Random random = new Random();

        final int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = random.nextInt(MAX_VALUE);
        }

        return array;
    }

    private static long calculateInOneThread(int[] array) {
        System.out.println("Один поток");
        final long start = System.currentTimeMillis();

        final int sum = Arrays.stream(array).sum();
        System.out.println("Сумма элементов: " + sum);
        System.out.println("Среднее арифметическое: " + ((double) sum / array.length));

        final long calculationTime = System.currentTimeMillis() - start;
        System.out.println("Время расчета в одном потоке: " + calculationTime);
        return calculationTime;
    }

    private static long calculateInManyThreads(int[] array) throws ExecutionException, InterruptedException {
        System.out.println("Много потоков");
        final long start = System.currentTimeMillis();

        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        final ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(new ArraySumTask(0, array.length, array));
        final Integer sum = forkJoinTask.get();

        System.out.println("Сумма элементов: " + sum);
        System.out.println("Среднее арифметическое: " + ((double) sum / array.length));

        final long calculationTime = System.currentTimeMillis() - start;
        System.out.println("Время расчета в нескольких потоках: " + calculationTime);
        return calculationTime;
    }
}
