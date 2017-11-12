package com.hk.pimote.stream;

import java.io.Closeable;
import java.io.Serializable;

public interface Stream extends Closeable
{
	static final byte TYPE_BOOLEAN = 0;
	static final byte TYPE_BYTE = 1;
	static final byte TYPE_SHORT = 2;
	static final byte TYPE_INT = 3;
	static final byte TYPE_FLOAT = 4;
	static final byte TYPE_CHAR = 5;
	static final byte TYPE_LONG = 6;
	static final byte TYPE_DOUBLE = 7;
	static final byte TYPE_UTF_STRING = 8;
	static final byte TYPE_SERIALIZABLE = 9;
	static final byte TYPE_BYTES = 10;

	public void writeBoolean(boolean o) throws StreamException;

	public void writeByte(byte o) throws StreamException;

	public void writeShort(short o) throws StreamException;

	public void writeInt(int o) throws StreamException;

	public void writeFloat(float o) throws StreamException;

	public void writeCharacter(char o) throws StreamException;

	public void writeLong(long o) throws StreamException;

	public void writeDouble(double o) throws StreamException;

	public void writeUTFString(String o) throws StreamException;

	public void writeSerializable(Serializable o) throws StreamException;

	public void writeBytes(byte[] arr) throws StreamException;

	public void writeBytes(byte[] arr, int off, int len) throws StreamException;

	public boolean readBoolean() throws StreamException;

	public byte readByte() throws StreamException;

	public short readShort() throws StreamException;

	public int readInt() throws StreamException;

	public float readFloat() throws StreamException;

	public char readCharacter() throws StreamException;

	public long readLong() throws StreamException;

	public double readDouble() throws StreamException;

	public String readUTFString() throws StreamException;

	public Serializable readSerializable() throws StreamException;

	public void readBytes(byte[] arr) throws StreamException;

	public void readBytes(byte[] arr, int off, int len) throws StreamException;

	@Override
	public void close() throws StreamException;


}
