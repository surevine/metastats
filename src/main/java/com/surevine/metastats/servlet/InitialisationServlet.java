package com.surevine.metastats.servlet;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.surevine.metastats.scm.Repositories;
import com.surevine.metastats.util.ConfigurationProperties;

public class InitialisationServlet extends HttpServlet {

	private static final long serialVersionUID = -5635901816323882787L;

	Timer poll;

	@Override
	public void init() throws ServletException {
		Repositories.find(); // Ensures we build our list of repositories.

		poll = new Timer();

		int pollInterval = Integer.valueOf(ConfigurationProperties
				.get(ConfigurationProperties.REPOSITORY_POLL_PERIOD)) * 1000;

		poll.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Repositories.update();
			}

		}, pollInterval, pollInterval);
	}
}
