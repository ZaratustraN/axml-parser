package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;
import cn.zaratustra.axmlparser.utils.StringUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zaratustra on 2017/12/12.
 */
public class EndTagChunk extends Chunk {

    public static final int CHUNK_TYPE = 0x00100103;

    private int mLineNumber;
    private int mNamespaceUri;
    private int mName;

    @Override
    public void parseFromAXML(ValuePool valuePool, byte[] data, int offset) {
        mValuePool = valuePool;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, data.length - offset);
        mChunkType = byteBuffer.getInt();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mChunkSize = byteBuffer.getInt();

        mLineNumber = byteBuffer.getInt();
        mUnknown = byteBuffer.getInt();
        mNamespaceUri = byteBuffer.getInt();
        mName = byteBuffer.getInt();
    }

    @Override
    public String genXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("</");
        if (!StringUtils.isEmpty(mValuePool.getString(mNamespaceUri))) {
            stringBuilder.append(mValuePool.getNamespace(mValuePool.getString(mNamespaceUri)));
            stringBuilder.append(":");
        }
        stringBuilder.append(mValuePool.getString(mName)).append(">").append("\n");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return String.format("ChunkSize:%s\nLineNumber:%s\nUnknown:%s\nNamespaceUri:%s\nName:%s\n",
                mChunkSize, mLineNumber, mUnknown, mValuePool.getString(mNamespaceUri), mValuePool.getString(mName));
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
        byteBuffer.putInt(mNamespaceUri);
        byteBuffer.putInt(mName);

        return byteBuffer.array();
    }

    public void setLineNumber(int lineNumber) {
        mLineNumber = lineNumber;
    }

    public int getUnKnown() {
        return mUnknown;
    }

    public void setUnKnown(int unKnown) {
        mUnknown = unKnown;
    }

    public int getNamespaceUri() {
        return mNamespaceUri;
    }

    public void setNamespaceUri(int namespaceUri) {
        mNamespaceUri = namespaceUri;
    }

    public int getName() {
        return mName;
    }

    public void setName(int name) {
        mName = name;
    }
}
