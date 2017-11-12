package com.hk.pimote.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class OutStream implements Stream
{
	private boolean closed;
	private final OutputStream out;

	public OutStream(OutputStream out)
	{
		this.out = out;
	}


	private void write(byte b) throws StreamException
	{
		if (closed)
		{
			throw new StreamException("Stream was Closed");
		}
		try
		{
			out.write(b);
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
	}

	@Override
	public void writeBoolean(boolean o) throws StreamException
	{
		byte b = (byte) (o ? 1 : 0);

		write(TYPE_BOOLEAN);
		write(b);
	}

	@Override
	public void writeByte(byte o) throws StreamException
	{
		write(TYPE_BYTE);
		write(o);
	}


	@Override
	public void writeInt(int o) throws StreamException
	{
		write(TYPE_INT);
		write((byte) (o & 0xFF));
		write((byte) (o >> 8 & 0xFF));
		write((byte) (o >> 16 & 0xFF));
		write((byte) (o >> 24 & 0xFF));
	}

	@Override
	public void writeBytes(byte[] arr) throws StreamException
	{
		writeBytes(arr, 0, arr.length);
	}

	@Override
	public void writeBytes(byte[] arr, int off, int len) throws StreamException
	{
		write(TYPE_BYTES);
		for (int i = off; i < off + len; i++)
		{
			write(arr[i]);
		}
	}

	@Override
	public void close() throws StreamException
	{
		try
		{
			out.close();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
		closed = true;
	}

	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////

	@Override
	public void writeShort(short o) throws StreamException
	{
		write(TYPE_SHORT);
		write((byte) (o & 0xFF));
		write((byte) (o >> 8 & 0xFF));
	}

	@Override
	public void writeFloat(float o) throws StreamException
	{
		write(TYPE_FLOAT);
		int i = Float.floatToIntBits(o);
		write((byte) (i & 0xFF));
		write((byte) (i >> 8 & 0xFF));
		write((byte) (i >> 16 & 0xFF));
		write((byte) (i >> 24 & 0xFF));
	}

	@Override
	public void writeCharacter(char o) throws StreamException
	{
		write(TYPE_CHAR);
		write((byte) (o & 0xFF));
		write((byte) (o >> 8 & 0xFF));
	}

	@Override
	public void writeLong(long o) throws StreamException
	{
		write(TYPE_LONG);
		write((byte) (o & 0xFF));
		write((byte) (o >> 8 & 0xFF));
		write((byte) (o >> 16 & 0xFF));
		write((byte) (o >> 24 & 0xFF));
		write((byte) (o >> 32 & 0xFF));
		write((byte) (o >> 40 & 0xFF));
		write((byte) (o >> 48 & 0xFF));
		write((byte) (o >> 56 & 0xFF));
	}

	@Override
	public void writeDouble(double o) throws StreamException
	{
		write(TYPE_DOUBLE);
		long l = Double.doubleToLongBits(o);
		write((byte) (l & 0xFF));
		write((byte) (l >> 8 & 0xFF));
		write((byte) (l >> 16 & 0xFF));
		write((byte) (l >> 24 & 0xFF));
		write((byte) (l >> 32 & 0xFF));
		write((byte) (l >> 40 & 0xFF));
		write((byte) (l >> 48 & 0xFF));
		write((byte) (l >> 56 & 0xFF));
	}

	@Override
	public void writeUTFString(String o) throws StreamException
	{
		write(TYPE_UTF_STRING);
		int length = o.length();
		write((byte) (length & 0xFF));
		write((byte) (length >> 8 & 0xFF));
		write((byte) (length >> 16 & 0xFF));
		write((byte) (length >> 24 & 0xFF));
		byte[] bs = o.getBytes(StandardCharsets.UTF_8);
		for (int i = 0; i < length; i++)
		{
			write((byte) ~bs[i]);
		}
	}

	@Override
	public void writeSerializable(Serializable o) throws StreamException
	{
		write(TYPE_SERIALIZABLE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}

		byte[] bs = baos.toByteArray();
		int length = bs.length;
		write((byte) (length & 0xFF));
		write((byte) (length >> 8 & 0xFF));
		write((byte) (length >> 16 & 0xFF));
		write((byte) (length >> 24 & 0xFF));
		for (int i = 0; i < length; i++)
		{
			write((byte) ~bs[i]);
		}
	}

	@Override
	public boolean readBoolean() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public byte readByte() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public short readShort() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public int readInt() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public float readFloat() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public char readCharacter() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public long readLong() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public double readDouble() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public String readUTFString() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public Serializable readSerializable() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public void readBytes(byte[] arr) throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public void readBytes(byte[] arr, int off, int len) throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}


}
