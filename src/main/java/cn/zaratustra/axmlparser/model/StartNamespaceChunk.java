package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zaratustra on 2017/12/11.
 */
public class StartNamespaceChunk extends Chunk {

    public static final int CHUNK_TYPE = 0x00100100;

    private int mLineNumber;
    private int mPrefix;
    private int mUri;

    public StartNamespaceChunk() {
        mUnknown = -1;
    }

    @Override
    public void parseFromAXML(ValuePool valuePool, byte[] data, int offset) {
        mValuePool = valuePool;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, data.length - offset);
        mChunkType = byteBuffer.getInt();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mChunkSize = byteBuffer.getInt();

        mLineNumber = byteBuffer.getInt();
        mUnknown = byteBuffer.getInt();
        mPrefix = byteBuffer.getInt();
        mUri = byteBuffer.getInt();

        mValuePool.putUriToNamespace(mValuePool.getString(mUri), mValuePool.getString(mPrefix));
    }

    @Override
    public String genXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"" + (mValuePool.isUTF8() ? "no" : "yes") + "\"?>\n";
    }

    public String toString() {
        if (mValuePool == null) {
            return "";
        }
        return String.format("ChunkSize: %s\nLineNumber: %s\nUnknown: %s\nPrefix: %s\nUri: %s\n", mChunkSize, mLineNumber,
                mUnknown, mValuePool.getString(mPrefix), mValuePool.getString(mUri));
    }

    @Override
    public int getLineNumber() {
        return mLineNumber;
    }

    @Override
    public byte[] toByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(mChunkSize);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(mChunkType);
        byteBuffer.putInt(mChunkSize);
        byteBuffer.putInt(mLineNumber);
        byteBuffer.putInt(mUnknown);
        byteBuffer.putInt(mPrefix);
        byteBuffer.putInt(mUri);
        return byteBuffer.array();
    }

    public void setLineNumber(int lineNumber) {
        mLineNumber = lineNumber;
    }

    public int getUnknown() {
        return mUnknown;
    }

    public void setUnknown(int unknown) {
        mUnknown = unknown;
    }

    public int getPrefix() {
        return mPrefix;
    }

    public void setPrefix(int prefix) {
        mPrefix = prefix;
    }

    public int getUri() {
        return mUri;
    }

    public void setUri(int uri) {
        mUri = uri;
    }

    @Override
    public void setChunkSize(int chunkSize) {
        super.setChunkSize(chunkSize);
    }
}
