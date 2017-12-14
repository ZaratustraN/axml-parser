package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zaratustra on 2017/12/11.
 */
public class ResourceChunk extends Chunk {
    public final static int CHUNK_TYPE = 0x00080180;

    @Override
    public void parseFromAXML(ValuePool valuePool, byte[] data, int offset) {
        mValuePool = valuePool;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, data.length - offset);

        mChunkType = byteBuffer.getInt();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mChunkSize = byteBuffer.getInt();

        while (byteBuffer.position() - offset < mChunkSize) {
            int resId = byteBuffer.getInt();
            mValuePool.addResourceId(resId);
        }
    }

    @Override
    public String genXML() {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public byte[] toByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(mChunkSize);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(mChunkType);
        byteBuffer.putInt(mChunkSize);
//        mValuePool.getResourceIds().clear();
//        mValuePool.getResourceIds().add(Integer.decode("0x101021b"));
//        mValuePool.getResourceIds().add(Integer.decode("0x101021c"));
//        mValuePool.getResourceIds().add(Integer.decode("0x101020c"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010270"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010003"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010024"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010280"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010002"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010001"));
//        mValuePool.getResourceIds().add(Integer.decode("0x101052c"));
//        mValuePool.getResourceIds().add(Integer.decode("0x10103af"));
//        mValuePool.getResourceIds().add(Integer.decode("0x1010000"));
        for (int i = 0; i < mValuePool.getResourceIdSize(); i++) {
            byteBuffer.putInt(mValuePool.getResourceId(i));
        }
        return byteBuffer.array();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer : mValuePool.getResourceIds()) {
            stringBuilder.append("ResId: 0x" + Integer.toHexString(integer)).append("\n");
        }
        return String.format("ChunkType:0x%s\nChunkSize:%d\n", Integer.toHexString(mChunkType), mChunkSize) + stringBuilder.toString();
    }

    public int getChunkSize() {
        return mChunkSize;
    }
}
