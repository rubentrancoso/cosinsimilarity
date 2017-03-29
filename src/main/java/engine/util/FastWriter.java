package engine.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FastWriter {

	File file;
	FileWriter writer;

	public FastWriter(String fileName) throws IOException {
		file = new File(fileName);
		writer = new FileWriter(file);
	}

	public void writeRaw(List<String> records) throws IOException {
		writeRecords(records);
	}

	private void writeRecords(List<String> records) throws IOException {
		for (String record : records) {
			writer.write(record);
		}
		writer.flush();
		writer.close();
	}

	public void writeRecord(String string) throws IOException {
		writer.write(string);
	}

	public void finish() throws IOException {
		writer.flush();
		writer.close();	
	}

}
