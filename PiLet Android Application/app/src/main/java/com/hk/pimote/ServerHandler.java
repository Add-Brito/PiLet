package com.hk.pimote;

import android.os.AsyncTask;

import com.hk.pimote.packet.Packet;
import com.hk.pimote.packet.PacketHandler;
import com.hk.pimote.packets.PacketIdentity;
import com.hk.pimote.packets.PacketSetting;
import com.hk.pimote.packets.PacketStatus;
import com.hk.pimote.stream.InStream;
import com.hk.pimote.stream.OutStream;
import com.hk.pimote.stream.Stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler extends AsyncTask<ServerHandler.BridgeportConnecticut, Boolean, Void>
{
	private boolean shutdown = false;
	private final BridgeportConnecticut cb;
	private ProgressListener listener;
	private String name;
	public boolean connectionEstablished;

	public ServerHandler()
	{
		cb = new BridgeportConnecticut();
	}

	public void toggleSwitch()
	{
		if(!shutdown)
		{
			synchronized (cb)
			{
				cb.requestedStatus = PacketStatus.STATUS_TURN;
			}
		}
	}

	public void shutdown()
	{
		if(!shutdown)
		{
			shutdown = true;
			synchronized (cb)
			{
				cb.requestedStatus = PacketStatus.STATUS_SHUTDOWN;
			}
		}
	}

	public boolean isShutdown()
	{
		return shutdown;
	}

	public String getName()
	{
		return name;
	}

	public void start()
	{
		execute(cb);
	}

	@Override
	protected Void doInBackground(BridgeportConnecticut... callbacks)
	{
		BridgeportConnecticut cb = callbacks[0];
		try
		{
			ServerSocket socket = new ServerSocket(45114);

			Socket client = socket.accept();

			connectionEstablished = true;
			Stream in = new InStream(client.getInputStream());
			Stream out = new OutStream(client.getOutputStream());
			PacketHandler handler = new PacketHandler(in, out);

			PacketIdentity identityPacket = handler.readPacket(PacketIdentity.class);
			name = identityPacket.name;
			handler.writePacket(new PacketStatus(PacketStatus.STATUS_OK));

			while(true)
			{
				synchronized (cb)
				{
					if(cb.requestedStatus != -1)
					{
						if(cb.requestedStatus == PacketStatus.STATUS_TURN)
						{
							handler.writePacket(new PacketStatus(PacketStatus.STATUS_TURN));
							PacketSetting pkt = handler.readPacket(PacketSetting.class);
							publishProgress(pkt.isOn);
						}
						else if(cb.requestedStatus == PacketStatus.STATUS_SHUTDOWN)
						{
							handler.writePacket(new PacketStatus(PacketStatus.STATUS_SHUTDOWN));
							break;
						}
						cb.requestedStatus = -1;
					}
				}
			}

			client.close();

			socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Boolean... values)
	{
		super.onProgressUpdate(values);

		if(listener != null)
		{
			listener.onReceive(values[values.length - 1]);
		}
	}

	public void setListener(ProgressListener listener)
	{
		this.listener = listener;
	}

	protected final class BridgeportConnecticut
	{
		private int requestedStatus = -1;
	}
}
