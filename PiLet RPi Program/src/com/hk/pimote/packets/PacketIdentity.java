package com.hk.pimote.packets;

import com.hk.pimote.packet.Packet;
import com.hk.pimote.stream.Stream;
import com.hk.pimote.stream.StreamException;

public class PacketIdentity extends Packet
{
	public String name;
	
	public PacketIdentity()
	{}
	
	public PacketIdentity(String name)
	{
		this.name = name;
	}

	@Override
	public void read(Stream in) throws StreamException
	{
		name = in.readUTFString();
		rd("Name", name);
	}

	@Override
	public void write(Stream out) throws StreamException
	{
		out.writeUTFString(name);
		wr("Name", name);
	}
}
