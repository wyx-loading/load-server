package com.loading.xmlparser;

import java.io.File;
import java.io.InputStream;

import com.loading.xmlparser.converters.reflection.FieldBySetReflectionProvider;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XmlConfigHelper {
	@SuppressWarnings("rawtypes")
	public static Object loadXmlConfigByFilePath(Class clazz, String filePath) {
		XStream xStream = new XStream(new FieldBySetReflectionProvider(), new DomDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						return definedIn != Object.class ? super.shouldSerializeMember(definedIn, fieldName) : false;
					}
				};
			}
		};
		xStream.processAnnotations(clazz);
		File configFile = new File(filePath);
		Object instance = xStream.fromXML(configFile);
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object loadXmlConfigByContent(Class clazz, String content) {
		XStream xStream = new XStream(new FieldBySetReflectionProvider(), new DomDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						return definedIn != Object.class ? super.shouldSerializeMember(definedIn, fieldName) : false;
					}
				};
			}
		};
		xStream.processAnnotations(clazz);
		Object instance = xStream.fromXML(content);
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object loadXmlConfig(Class clazz, InputStream input) {
		XStream xStream = new XStream(new FieldBySetReflectionProvider(), new DomDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						return definedIn != Object.class ? super.shouldSerializeMember(definedIn, fieldName) : false;
					}
				};
			}
		};
		xStream.processAnnotations(clazz);
		Object instance = xStream.fromXML(input);
		return instance;
	}
}
