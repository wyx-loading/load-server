package com.loading.xmlparser;

public class XmlParserLoadConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public XmlParserLoadConfigException() {
		super();
	}

	public XmlParserLoadConfigException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XmlParserLoadConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlParserLoadConfigException(String message) {
		super(message);
	}

	public XmlParserLoadConfigException(Throwable cause) {
		super(cause);
	}

}
