package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zaratustra on 2017/12/13.
 */
public class TextChunk extends Chunk {

    public static final int CHUNK_TYPE = 0x00100104;

    private int mLineNumber;
    private int mName;
    private int mUnknown2;
    private int mUnknown3;


    @Override
    public void parseFromAXML(ValuePool valuePool, byte[] data, int offset) {
        mValuePool = valuePool;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, data.length - offset);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        mChunkType = byteBuffer.getInt();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mChunkSize = byteBuffer.getInt();
        mLineNumber = byteBuffer.getInt();
        mUnknown = byteBuffer.getInt();
        mName = byteBuffer.getInt();
        mUnknown2 = byteBuffer.getInt();
        mUnknown3 = byteBuffer.getInt();
    }

    @Override
    public String genXML() {
        return mValuePool.getString(mName);
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
        byteBuffer.putInt(mName);
        byteBuffer.putInt(mUnknown2);
        byteBuffer.putInt(mUnknown3);
        return byteBuffer.array();
    }
}
