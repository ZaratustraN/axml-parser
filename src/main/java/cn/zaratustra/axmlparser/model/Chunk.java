package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;

/**
 * Created by zaratustra on 2017/12/11.
 */
public abstract class Chunk {

    protected int mChunkType;
    protected int mChunkSize;
    protected int mUnknown = -1;

    protected ValuePool mValuePool;

    public Chunk() {
    }

    public int getChunkSize() {
        return mChunkSize;
    }

    public void setChunkSize(int chunkSize) {
        mChunkSize = chunkSize;
    }

    public void setChunkType(int chunkType) {
        mChunkType = chunkType;
    }

    public abstract void parseFromAXML(ValuePool valuePool, byte[] data, int offset);

    public abstract String genXML();

    public abstract int getLineNumber();

    public void setValuePool(ValuePool valuePool) {
        mValuePool = valuePool;
    }

    public abstract byte[] toByte();
}
