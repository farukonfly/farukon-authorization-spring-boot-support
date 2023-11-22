package org.farukon.authorization;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSecurityResult {

	private static final Logger log = LoggerFactory.getLogger(DefaultSecurityResult.class);

	private LocalDateTime timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;

	public DefaultSecurityResult(Integer status, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.message = message;
		this.path = path;
		this.error = "";
	}

	public DefaultSecurityResult(Integer status, String error, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public String toJson() {
		StringBuilder json = new StringBuilder();
		json.append("{");
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (!field.getName().equals("log")) {
				json.append("\"").append(field.getName()).append("\"");
				json.append(":");
				try {

					Object value = new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod()
							.invoke(this);
					if (value instanceof Integer) {
						json.append(new PropertyDescriptor(field.getName(), this.getClass()).getReadMethod()
								.invoke(this).toString());
					} else {
						json.append("\"").append(new PropertyDescriptor(field.getName(), this.getClass())
								.getReadMethod().invoke(this).toString()).append("\"");
					}

					if (i < fields.length - 1) {
						json.append(",");

					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IntrospectionException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		json.append("}");
		return json.toString();
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public Integer getStatus() {
		return this.status;
	}

	public String getError() {
		return this.error;
	}

	public String getMessage() {
		return this.message;
	}

	public String getPath() {
		return this.path;
	}

	public static Logger getLog() {
		return log;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
