package com.hk.pimote.packet;

import java.util.ArrayList;
import java.util.List;

import com.hk.pimote.packets.PacketIdentity;
import com.hk.pimote.packets.PacketSetting;
import com.hk.pimote.packets.PacketStatus;
import com.hk.pimote.stream.Stream;

public class PacketHandler
{
	private final Class<? extends Packet>[] packets;

	private final Stream in, out;
	
	public PacketHandler(Stream in, Stream out)
	{
		this.in = in;
		this.out = out;
		packets = registerPackets();
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends Packet>[] registerPackets()
	{
		List<Class<? extends Packet>> lst = new ArrayList<>();
		
		lst.add(PacketIdentity.class);
		lst.add(PacketStatus.class);
		lst.add(PacketSetting.class);
		
		return lst.toArray(new Class[lst.size()]);
	}
	
	public Packet readPacket() throws Exception
	{
		int pktID = in.readInt();
		if(pktID == -1)
		{
			return null;
		}
		else
		{
			Packet pkt = packets[pktID].newInstance();
			pkt.read(in);
			return pkt;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Packet> T readPacket(Class<T> cls) throws Exception
	{
		return (T) readPacket();
	}
	
	public void writePacket(Packet pkt) throws Exception
	{
		int indx = -1;
		
		if(pkt != null)
		{
			for(int i = 0; i < packets.length; i++)
			{
				Class<? extends Packet> cls = packets[i];
				if(cls.equals(pkt.getClass()))
				{
					indx = i;
					break;
				}
			}
		}
		
		out.writeInt(indx);
		if(pkt != null)
		{
			pkt.write(out);
		}
	}
}
