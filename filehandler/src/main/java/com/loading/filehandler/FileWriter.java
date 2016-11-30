package com.loading.filehandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileWriter {
	
	private String encoding = null;
	
	protected boolean fileAppend = true;
	protected String fileName = null;
	protected boolean bufferedIO = true;
	protected int bufferSize = 8 * 1024;
	protected boolean immediateFlush = true;

	private Writer writer = null;
	
	public FileWriter(String fileName) throws IOException {
		this(fileName, true);
	}
	
	public FileWriter(String fileName, boolean fileAppend) throws IOException {
		this(fileName, fileAppend, true, 8 * 1024);
	}
	
	public FileWriter(String fileName, boolean bufferedIO, int bufferSize) throws IOException {
		this(fileName, true, bufferedIO, bufferSize);
	}
	
	public FileWriter(String fileName, boolean fileAppend, boolean bufferedIO, int bufferSize) throws IOException {
		this.setFile(fileName, fileAppend, bufferedIO, bufferSize);
	}
	
	public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
		reset();
		
		FileOutputStream ostream = null;
		try {
			ostream = new FileOutputStream(fileName, append);
		} catch (FileNotFoundException e) {
			String parentName = new File(fileName).getParent();
			if(parentName != null) {
				File parentDir = new File(parentName);
				if(!parentDir.exists() && parentDir.mkdirs()) {
					ostream = new FileOutputStream(fileName, append);
				} else {
					throw e;
				}
			} else {
				throw e;
			}
		}
		
		Writer fw = createWriter(ostream);
		if(bufferedIO) {
			fw = new BufferedWriter(fw, bufferSize);
		}
		this.setWriter(fw);
		this.fileName = fileName;
		this.fileAppend = append;
		this.bufferedIO = bufferedIO;
		this.bufferSize = bufferSize;
	}
	
	public synchronized void write(String msg) {
		if(msg != null) {
			try {
				writer.write(msg);
				if(immediateFlush) {
					writer.flush();
				}
			} catch (Exception e) {
				// Write error
			}
		}
	}
	
	private OutputStreamWriter createWriter(OutputStream os) {
		OutputStreamWriter retval = null;
		
		String enc = getEncoding();
		if(enc != null) {
			try {
				retval = new OutputStreamWriter(os, enc);
			} catch (IOException e) {
				if(e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
				
				// Unsupport encoding
			}
		}
		if(retval == null) {
			retval = new OutputStreamWriter(os);
		}
		return retval;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncoding(String value) {
		this.encoding = value;
	}
	
	public void setImmediateFlush(boolean value) {
		this.immediateFlush = value;
	}
	
	private void setWriter(Writer fw) {
		this.writer = fw;
	}
	
	private void reset() {
		closeFile();
		this.fileName = null;
	}
	
	public void destroy() {
		this.reset();
	}
	
	private void closeFile() {
		if(this.writer != null) {
			try {
				this.writer.close();
			} catch (IOException e) {
				if(e instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
				
				// Could not close
			}
		}
	}

}
