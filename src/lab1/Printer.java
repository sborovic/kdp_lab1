package lab1;

import java.util.concurrent.atomic.AtomicInteger;

public class Printer extends Thread {
	private Combiner combiner;
	private AtomicInteger[] totals;
	long printPeriod;

	public Printer(long printPeriod, AtomicInteger[] totals, Combiner combiner) {
		super("Printer");
		this.combiner = combiner;
		this.printPeriod = printPeriod;
		this.totals = totals;
	}

	@Override
	public void run() {
		System.out.println("Printer started...");
		while (!combiner.hasFinished()) {
			try {
				sleep(printPeriod);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < totals.length; i++) {
				System.out.println("Consumer " + i + " has processed " + totals[i] + " entries!");
			}

		}
		System.out.println("Results:");
		System.out.println(combiner.getResults());

	}

}
