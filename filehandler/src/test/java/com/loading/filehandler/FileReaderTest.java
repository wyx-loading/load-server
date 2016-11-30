package com.loading.filehandler;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileReaderTest {
	
	private FileReader reader;
	
	@Before
	public void init() throws IOException {
		reader = new FileReader(FileWriterTest.FILE_NAME);
	}
	
	@Test
	public void testFileReader() throws IOException {
		String str = null;
		for(int i = 0; i < 100; i++) {
			long skip = reader.skip(986);
			str = reader.read();
			System.out.println(String.format("[skip\t%d] %s", skip, str));
		}
	}
	
	@After
	public void destroy() {
		reader.destroy();
	}

}
