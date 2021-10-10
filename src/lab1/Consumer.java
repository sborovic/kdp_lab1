package lab1;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer extends Thread {
	private int N;
	private Semaphore consumers;
	private AtomicInteger total;
	private HashMap<Integer, Integer> localMap = new HashMap<>();
	private ConcurrentHashMap<Integer, Integer> combinedMap;
	private boolean finished = false;
	private int consumed = 0;
	BlockingQueue<String> rawData;
	AtomicBoolean finishedProducing;

	public Consumer(int id, int N, Semaphore consumers, AtomicInteger total, BlockingQueue<String> rawData,
			AtomicBoolean finishedProducing, ConcurrentHashMap<Integer, Integer> combinedMap) {
		super("Consumer " + id);
		this.N = N;
		this.consumers = consumers;
		this.total = total;
		this.rawData = rawData;
		this.finishedProducing = finishedProducing;
		this.combinedMap = combinedMap;
	}

	@Override
	public void run() {
		while (!finished) {
			String line = rawData.poll();
			if (line != null)
				parseLine(line);
			else if (finishedProducing.get() == true) {
				mergeMap();
				finished = true;
			}
		}
		//System.out.println(this.getName() + " has consumed " + consumed + " lines!");
		consumers.release();
	}

	private void parseLine(String line) {
		String[] args = line.split("\t");

		try {
			int birthYear = Integer.parseInt(args[2]);
			String deathYear = args[3];
			String primaryProfession = args[4];

			if (isActor(primaryProfession) && isAlive(deathYear)) {
				localMap.compute(getDecade(birthYear), (k, v) -> (v == null) ? 1 : v + 1);
			}
			// System.out.println("Birth year: " + birthYear + ", and decade: " +
			// getDecade(birthYear));
			// System.out.println("Death year: " + args[3] + ", so isAlive returns " +
			// isAlive(deathYear));
			// System.out.println("Primary profession: " + primaryProfession + ", so isActor
			// returns " + isActor(primaryProfession));

		} catch (NumberFormatException e) {
			// System.out.println("Illegal value: " + args[2]);
		}

		if (++consumed % N == 0)
			mergeMap();
	}

	private int getDecade(int year) {
		return year / 10 * 10;
	}

	private boolean isAlive(String year) {
		return "\\N".equals(year);
	}

	private boolean isActor(String primaryProfession) {
		String professions[] = primaryProfession.split(",");
		for (String s : professions) {
			if ("actor".equalsIgnoreCase(s) || "actress".equalsIgnoreCase(s))
				return true;
		}
		return false;
	}

	public void printMap() {
		localMap.forEach(
				(key, value) -> System.out.println(getName() + " - Decade: " + key + ", number of actors: " + value));
	}

	private void mergeMap() {
		localMap.forEach((key, value) -> combinedMap.merge(key, value, (v1, v2) -> v1 + v2));
		localMap.clear();
		total.set(consumed);

	}
}
