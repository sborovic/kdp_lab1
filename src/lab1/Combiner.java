package lab1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Combiner extends Thread {
	private ConcurrentHashMap<Integer, Integer> combinedMap;
	private AtomicBoolean finishedCombining = new AtomicBoolean(false);
	Semaphore consumers;
	StringBuilder sb = new StringBuilder();
	public Combiner(Semaphore consumers, ConcurrentHashMap<Integer, Integer> combinedMap) {
		super("Combiner");
		this.consumers = consumers;
		this.combinedMap = combinedMap;
	}

	@Override
	public void run() {
		consumers.acquireUninterruptibly();
		combinedMap.forEach(
				(key, value) -> sb.append("Decade: " + key + ", number of alive actors: " + value + "\n"));
		//System.out.println("Combiner finished!");
		finishedCombining.set(true);
	}
	
	public String getResults() {
		return sb.toString();
	}
	public boolean hasFinished() {
		return finishedCombining.get();
	}
	

}
