package com.hk.pimote.packets;

import com.hk.pimote.packet.Packet;
import com.hk.pimote.stream.Stream;
import com.hk.pimote.stream.StreamException;

public class PacketSetting extends Packet
{
	public boolean isOn;
	
	public PacketSetting()
	{
	}
	
	public PacketSetting(boolean isOn)
	{
		this.isOn = isOn;
	}

	@Override
	public void read(Stream in) throws StreamException
	{
		isOn = in.readBoolean();
		rd("Is On", isOn);
	}

	@Override
	public void write(Stream out) throws StreamException
	{
		out.writeBoolean(isOn);
		wr("Is On", isOn);
	}
}
