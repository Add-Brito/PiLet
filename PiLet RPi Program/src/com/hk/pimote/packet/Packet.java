package com.hk.pimote.packet;

import com.hk.pimote.stream.Stream;
import com.hk.pimote.stream.StreamException;

public abstract class Packet
{
	public abstract void read(Stream in) throws StreamException;
	
	public abstract void write(Stream out) throws StreamException;
	
	protected static void p(boolean in, String name, Object obj)
	{
		System.out.println((in ? "Receiving" : "Sending") + " (" + name + "): [" + obj + "]");
	}
	
	protected static void wr(String name, Object obj)
	{
		p(false, name, obj);
	}
	
	protected static void rd(String name, Object obj)
	{
		p(true, name, obj);
	}
}
