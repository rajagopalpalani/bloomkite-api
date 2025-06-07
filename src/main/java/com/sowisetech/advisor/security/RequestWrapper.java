package com.sowisetech.advisor.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sowisetech.admin.controller.AdminController;

public class RequestWrapper extends HttpServletRequestWrapper {
	private final String body;
	private byte[] rawData;
	private HttpServletRequest request;
	private ResettableServletInputStream servletStream;

	private static final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);

	public RequestWrapper(HttpServletRequest request) throws IOException {
		// So that other request method behave just like before
		super(request);
		this.servletStream = new ResettableServletInputStream();
		this.request = request;

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		// Store request pody content in 'body' variable
		body = stringBuilder.toString();
		rawData = stringBuilder.toString().getBytes();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawData);
		ServletInputStream servletInputStream = new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
		return servletInputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	// Use this method to read the request body N times
	public String getBody() {
		return this.body;
	}

	public void resetInputStream(byte[] newRawData) {
		rawData = newRawData;
		servletStream.stream = new ByteArrayInputStream(newRawData);
	}

	private class ResettableServletInputStream extends ServletInputStream {

		private InputStream stream;

		@Override
		public int read() throws IOException {
			return stream.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}
	}

}