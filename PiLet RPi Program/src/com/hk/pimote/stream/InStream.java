package com.hk.pimote.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class InStream implements Stream {
    private boolean closed;
    private final InputStream in;


    public InStream(InputStream in) {
        this.in = in;
    }

    private byte read() throws StreamException {
        if (closed) {
            throw new StreamException("Stream was Closed");
        }
        int b = -2;

        try {
            b = in.read();
        } catch (IOException e) {
            throw new StreamException(e);
        }
        if (b == -1) {
            throw new StreamException("End of Stream");
        }
        if (b == -2) {
            throw new StreamException();
        }
        return (byte) b;
    }

    @Override
    public boolean readBoolean() throws StreamException {
        byte type = read();
        if (type == TYPE_BOOLEAN) {
            return read() == 1;
        }
        throw new StreamException("Boolean not expected");
    }

    @Override
    public byte readByte() throws StreamException {
        byte type = read();
        if (type == TYPE_BYTE) {
            return read();
        }
        throw new StreamException("Byte not expected");
    }

    @Override
    public int readInt() throws StreamException {
        byte type = read();
        if (type == TYPE_INT) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            return a & 0xFF | (b & 0xFF) << 8 | (c & 0xFF) << 16 | (d & 0xFF) << 24;
        }
        throw new StreamException("Integer not expected");
    }

    @Override
    public void readBytes(byte[] arr) throws StreamException {
        readBytes(arr, 0, arr.length);
    }

    @Override
    public void readBytes(byte[] arr, int off, int len) throws StreamException {
        byte type = read();
        if (type == TYPE_BYTES) {
            for (int i = off; i < off + len; i++) {
                arr[i] = read();
            }
        } else {
            throw new StreamException("Bytes not expected");
        }
    }

    @Override
    public void close() throws StreamException {
        try {
            in.close();
        } catch (IOException e) {
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
    public float readFloat() throws StreamException {
        byte type = read();
        if (type == TYPE_FLOAT) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            return Float.intBitsToFloat(a & 0xFF | (b & 0xFF) << 8 | (c & 0xFF) << 16 | (d & 0xFF) << 24);
        }
        throw new StreamException("Float not expected");
    }

    @Override
    public char readCharacter() throws StreamException {
        byte type = read();
        if (type == TYPE_CHAR) {
            byte a = read();
            byte b = read();
            return (char) (a & 0xFF | (b & 0xFF) << 8);
        }
        throw new StreamException("Character not expected");
    }

    @Override
    public long readLong() throws StreamException {
        byte type = read();
        if (type == TYPE_LONG) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            byte e = read();
            byte f = read();
            byte g = read();
            byte h = read();
            return (a & 0xFFL) << 0 | (b & 0xFFL) << 8 | (c & 0xFFL) << 16 | (d & 0xFFL) << 24 | (e & 0xFFL) << 32 | (f & 0xFFL) << 40 | (g & 0xFFL) << 48 | (h & 0xFFL) << 56;
        }
        throw new StreamException("Long not expected");
    }

    @Override
    public double readDouble() throws StreamException {
        byte type = read();
        if (type == TYPE_DOUBLE) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            byte e = read();
            byte f = read();
            byte g = read();
            byte h = read();
            return Double.longBitsToDouble((a & 0xFFL) << 0 | (b & 0xFFL) << 8 | (c & 0xFFL) << 16 | (d & 0xFFL) << 24 | (e & 0xFFL) << 32 | (f & 0xFFL) << 40 | (g & 0xFFL) << 48 | (h & 0xFFL) << 56);
        }
        throw new StreamException("Double not expected");
    }

    @Override
    public String readUTFString() throws StreamException {
        byte type = read();
        if (type == TYPE_UTF_STRING) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            int length = a & 0xFF | (b & 0xFF) << 8 | (c & 0xFF) << 16 | (d & 0xFF) << 24;
            byte[] arr = new byte[length];
            for (int i = 0; i < length; i++) {
                arr[i] = (byte) ~read();
            }
            return new String(arr, StandardCharsets.UTF_8);
        }
        throw new StreamException("UTF String not expected");
    }

    @Override
    public Serializable readSerializable() throws StreamException {
        byte type = read();
        if (type == TYPE_SERIALIZABLE) {
            byte a = read();
            byte b = read();
            byte c = read();
            byte d = read();
            int length = a & 0xFF | (b & 0xFF) << 8 | (c & 0xFF) << 16 | (d & 0xFF) << 24;
            byte[] arr = new byte[length];
            for (int i = 0; i < length; i++) {
                arr[i] = (byte) ~read();
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(arr);
            Serializable o = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                o = (Serializable) ois.readObject();
                ois.close();
            } catch (Exception e) {
                throw new StreamException(e);
            }
            return o;
        }
        throw new StreamException("Serializable Object not expected");
    }

    @Override
    public short readShort() throws StreamException {
        byte type = read();
        if (type == TYPE_SHORT) {
            byte a = read();
            byte b = read();
            return (short) (a | (b & 0xFF) << 8);
        }
        throw new StreamException("Short not expected");
    }

    @Override
    public void writeBoolean(boolean o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeByte(byte o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeShort(short o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeInt(int o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeFloat(float o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeCharacter(char o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeLong(long o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeDouble(double o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeUTFString(String o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeSerializable(Serializable o) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeBytes(byte[] arr) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

    @Override
    public void writeBytes(byte[] arr, int off, int len) throws StreamException {
        throw new StreamException("Can't write to this stream");
    }

}
