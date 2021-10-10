package lab1;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

	public static void main(String[] args) {
		Instant startInstant = Instant.now();
		// Parameters
		int consumersNumber = 5;
		int N = 10000;
		String fileName = "test_fajlovi/data_3000000.tsv";
		long printPeriod = 500;

		// Shared variables

		BlockingQueue<String> rawData = new LinkedBlockingQueue<>();
		AtomicBoolean finishedProducing = new AtomicBoolean(false);
		Semaphore consumers = new Semaphore(-consumersNumber + 1);
		AtomicInteger[] totals = new AtomicInteger[consumersNumber];
		ConcurrentHashMap<Integer, Integer> combinedMap = new ConcurrentHashMap<>();

		// Threads

		Producer producer = new Producer(fileName, rawData, finishedProducing);
		producer.start();

		for (int i = 0; i < consumersNumber; i++) {
			totals[i] = new AtomicInteger();
			Consumer consumer = new Consumer(i, N, consumers, totals[i], rawData, finishedProducing, combinedMap);
			consumer.start();
		}

		Combiner combiner = new Combiner(consumers, combinedMap);
		combiner.start();

		Printer printer = new Printer(printPeriod, totals, combiner);
		printer.start();

		try {
			printer.join();
			System.out.println("Total execution time: " + Duration.between(startInstant, Instant.now()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
