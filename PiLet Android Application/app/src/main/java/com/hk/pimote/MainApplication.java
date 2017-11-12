package com.hk.pimote;

import android.app.Application;

public class MainApplication extends Application
{
	private ServerHandler handler;

	public ServerHandler getHandler()
	{
		return handler;
	}

	public ServerHandler shutdown()
	{
		handler.shutdown();
		return null;
	}

	public ServerHandler startup()
	{
		handler = new ServerHandler();
		handler.start();
		return handler;
	}
}
