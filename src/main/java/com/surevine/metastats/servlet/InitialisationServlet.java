package com.surevine.metastats.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.surevine.metastats.scm.Repositories;

public class InitialisationServlet extends HttpServlet {

	private static final long serialVersionUID = -5635901816323882787L;

	@Override
	public void init() throws ServletException {
		Repositories.find(); // Ensures we build our list of repositories.
	}
}
