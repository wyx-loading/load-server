package com.loading.xmlparser;

import java.util.concurrent.locks.StampedLock;

public class ConfigWrapper<T> {
	
	private Class<?> clazz;

	private T _instance;
	private final StampedLock lock;

	private String configPath;
	
	public ConfigWrapper(Class<?> clazz) {
		this.clazz = clazz;
		this._instance = null;
		this.lock = new StampedLock();
		this.configPath = null;
	}
	
	public T get() {
		long stamp = lock.tryOptimisticRead();
		T inst = _instance;
		if(!lock.validate(stamp)) {
			stamp = lock.readLock();
			try {
				inst = _instance;
			} finally {
				lock.unlockRead(stamp);
			}
		}
		
		if(inst == null) {
			throw new XmlParserLoadConfigException(clazz.getSimpleName() + " not load yet.");
		}
		
		return inst;
	}
	
	public T load(String configPath) {
		T newInst = loadConfig(configPath);
		setInstance(newInst);
		this.configPath = configPath;
		return newInst;
	}
	
	public T reload() {
		T newInst = loadConfig(configPath);
		setInstance(newInst);
		return newInst;
	}
	
	private void setInstance(T newInst) {
		long stamp = lock.writeLock();
		try {
			_instance = newInst;
		} finally {
			lock.unlockWrite(stamp);
		}
	}
	
	@SuppressWarnings("unchecked")
	private T loadConfig(String configPath) {
		try {
			return (T) XmlConfigHelper.loadXmlConfigByFilePath(clazz, configPath);
		} catch (Throwable t) {
			throw new XmlParserLoadConfigException(configPath, t);
		}
	}

}
