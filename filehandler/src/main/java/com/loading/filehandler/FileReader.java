package com.loading.filehandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;

public class FileReader {

	protected String fileName;
	
	private String encoding;
	
	private BufferedReader reader;
	
	public FileReader(String fileName) throws IOException {
		this.setFile(fileName);
	}
	
	public synchronized long skip(int bytes) throws IOException {
		try {
			return reader.skip(bytes);
		} catch (IOException e) {
			if(e instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}
			
			throw e;
		}
	}
	
	public synchronized String read() throws IOException {
		try {
			return reader.readLine();
		} catch (IOException e) {
			if(e instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}
			
			throw e;
		}
	}
	
	public synchronized void setFile(String fileName) throws IOException {
		reset();
		
		FileInputStream istream = new FileInputStream(fileName);
		
		Reader fr = createReader(istream);
		BufferedReader br = new BufferedReader(fr);
		this.setReader(br);
		this.fileName = fileName;
	}
	
	private InputStreamReader createReader(InputStream is) {
		InputStreamReader retval = null;
		
		String enc = getEncoding();
		if(enc != null) {
			try {
				retval = new InputStreamReader(is, enc);
			} catch (IOException e) {
				if(e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
				
				// Unsupport encoding
			}
		}
		if(retval == null) {
			retval = new InputStreamReader(is);
		}
		return retval;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncoding(String value) {
		this.encoding = value;
	}
	
	private void setReader(BufferedReader fr) {
		this.reader = fr;
	}
	
	private void reset() {
		closeFile();
		this.fileName = null;
	}
	
	private void closeFile() {
		if(reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				if(e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}

				// Could not close
			}
		}
	}
	
	public void destroy() {
		reset();
	}
}
