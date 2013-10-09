package ch.furthermore.poorman.templateengine;

import static org.junit.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleTest {

	@Test
	public void test() throws IOException {
		String expecteed = readFile("testdata/expected.txt");
		Main.replacePlaceholders("testdata/properties.txt", "testdata/testtemplate.txt", "testdata/result.txt");
		String result = readFile("testdata/result.txt");
		
		assertEquals(expecteed, result);
	}

	private String readFile(String name) throws IOException {
		File f = new File(name);
		byte[] data = new byte[(int) f.length()];
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		try {
			in.readFully(data);
			return new String(data);
		}
		finally {
			in.close();
		}
	}
}
