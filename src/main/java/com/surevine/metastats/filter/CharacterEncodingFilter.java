package com.surevine.metastats.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
	
	private String encoding;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain filterChain) throws IOException, ServletException {
		response.setCharacterEncoding(encoding);
				
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(final FilterConfig config) throws ServletException {
		setEncoding(config.getInitParameter("encoding"));
	}

	@Override
	public void destroy() {
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
