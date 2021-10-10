package lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer extends Thread {
	private String fileName;
	//private int produced;
	private AtomicBoolean finishedProducing;
	private BlockingQueue<String> rawData;

	public Producer(String fileName, BlockingQueue<String> rawData, AtomicBoolean finishedProducing) {
		super("Producer");
		this.fileName = fileName;
		this.rawData = rawData;
		this.finishedProducing = finishedProducing;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String currentLine;
			br.readLine(); // First line contains only labels!
			while ((currentLine = br.readLine()) != null) {
				rawData.add(currentLine);
				//produced++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O error: could not read from file!");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					//System.out.println(this.getName() + " has produced " + produced + " lines!");
					finishedProducing.set(true);
				} catch (IOException e) {
					System.out.println("I/O error: file could not be closed!\"");
					e.printStackTrace();
				}

			}
		}

	}

}
