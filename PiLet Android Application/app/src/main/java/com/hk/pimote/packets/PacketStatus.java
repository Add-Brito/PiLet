package com.hk.pimote.packets;

import com.hk.pimote.packet.Packet;
import com.hk.pimote.stream.Stream;
import com.hk.pimote.stream.StreamException;

public class PacketStatus extends Packet
{
	public int status;
	
	public PacketStatus()
	{
	}
	
	public PacketStatus(int status)
	{
		this.status = status;
	}

	@Override
	public void read(Stream in) throws StreamException
	{
		status = in.readInt();
		rd("Status", status);
	}

	@Override
	public void write(Stream out) throws StreamException
	{
		out.writeInt(status);
		wr("Status", status);
	}
	
	public static final int STATUS_UNKNOWN = 0;
	public static final int STATUS_OK = 1;
	public static final int STATUS_READ = 2;
	public static final int STATUS_TURN = 3;
	public static final int STATUS_SHUTDOWN = 4;
}
