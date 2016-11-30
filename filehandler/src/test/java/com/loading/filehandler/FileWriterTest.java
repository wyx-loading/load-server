package com.loading.filehandler;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileWriterTest {
	
	static final String FILE_NAME = "E:\\git\\load-server\\filehandler\\src\\test\\resources\\out1";
	
	static final String CONTENT = "奥沙利文你是哪的你深V领卡萨丁就；你什么女性才女那里说啥卡拉卡拉的弗兰克为首的客服哈里斯肯定会法拉克的回房间阿卡丽东方红龙卡及水电费好可怜玩家还发什么V型初版， \n";
	
	private FileWriter writer;
	
	@Before
	public void init() throws IOException {
		writer = new FileWriter(FILE_NAME);
	}
	
	@Test
	public void testFileWriterPerformance() {
		for(int i = 0; i < 100; i++) {
			writer.write(CONTENT);
		}
	}
	
	@After
	public void destroy() {
		writer.destroy();
	}

}
