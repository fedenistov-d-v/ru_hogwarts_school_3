package ru.hogwarts.school.third.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.LongStream;

@Service
public class InfoService {

    private static final Logger logger = LoggerFactory.getLogger(InfoService.class);

    /**
     * <code>long sum</code> - Вариант с использованием Stream однопоточный.<p>
     * <code>long sum2</code> - Вариант с многопоточным Stream.<p>
     *      ( На моём ноутбуке однопоточный Stream работает быстрее с NUMBER = 1_000_000,
     *      но при NUMBER = 2_000_000 паралельный стрим в полтора раза быстрее ).<p>
     * <code>long sum3</code> - Вариант без использования Stream - работает совсем быстро :)
     * @return любую sum - результат одинаков, разница только по времени подсчёта.
     */
    public long getSumOfSequence() {
        final int NUMBER = 1_000_000;
        logger.info("Был вызван метод подсчёта суммы последовательности от 0 до {}", NUMBER);

        long startTime = System.currentTimeMillis();
        long sum = LongStream.rangeClosed(0, NUMBER)
                .sum();
        long finishTime = System.currentTimeMillis()-startTime;
        logger.info ("время работы метода LongStream.rangeClosed(0, NUMBER).sum(); - {}", finishTime);
        startTime = System.currentTimeMillis();

        long sum2 = LongStream.rangeClosed(0, NUMBER)
                .parallel()
                .sum();
        finishTime = System.currentTimeMillis()-startTime;
        logger.info ("время работы метода LongStream.rangeClosed(0, NUMBER).parallel().sum(); - {}", finishTime);

        startTime = System.currentTimeMillis();
        long sum3 = (long) NUMBER * (NUMBER + 1) / 2;
        finishTime = System.currentTimeMillis()-startTime;
        logger.info ("время работы метода 5 - {}", finishTime);

        return sum;
    }
}
