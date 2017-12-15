package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by zaratustra on 2017/12/11.
 */
public class StringChunk extends Chunk {

    public final static int CHUNK_TYPE = 0x001C0001;
    private final int UTF8_FLAG = 1 << 8;

    private int mChunkSize;
    private int mStringCount;
    private int mStyleCount;
    private int mStringPoolOffset;
    private int mStylePoolOffset;
    private int mStringOffsets[];
    private int mStyleOffsets[];

    public StringChunk() {
//        mUnknown = 0x00000000;
//        mUnknown = UTF8_FLAG;
    }

    @Override
    public void parseFromAXML(ValuePool valuePool, byte[] data, int offset) {
        mValuePool = valuePool;

        ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, data.length - offset);
        mChunkType = byteBuffer.getInt();

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mChunkSize = byteBuffer.getInt();
        mStringCount = byteBuffer.getInt();
        mStyleCount = byteBuffer.getInt();
        mUnknown = byteBuffer.getInt();
        mStringPoolOffset = byteBuffer.getInt();
        mStylePoolOffset = byteBuffer.getInt();
        mStringOffsets = new int[mStringCount];
        mStyleOffsets = new int[mStyleCount];

        for (int i = 0; i < mStringCount; i++) {
            mStringOffsets[i] = byteBuffer.getInt();
        }

        for (int i = 0; i < mStyleCount; i++) {
            mStyleOffsets[i] = byteBuffer.getInt();
        }

        for (int i = 0; i < mStringCount; i++) {
            byteBuffer.position(mStringPoolOffset + offset + mStringOffsets[i]);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int strLength;
            if (mUnknown == UTF8_FLAG) {
                valuePool.setUTF8(true);
                strLength = byteBuffer.get();
                byteBuffer.get();
            } else {
                valuePool.setUTF8(false);
                strLength = byteBuffer.getShort() * 2;
            }

            byte[] strByte = new byte[strLength];
            byteBuffer.get(strByte, 0, strLength);
            valuePool.addString(filterStringNull(strByte));
        }
    }

    @Override
    public byte[] toByte() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(mChunkSize);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(mChunkType);
        byteBuffer.putInt(mChunkSize);
        byteBuffer.putInt(mStringCount);
        byteBuffer.putInt(mStyleCount);
        byteBuffer.putInt(mUnknown);
        byteBuffer.putInt(mStringPoolOffset);
        byteBuffer.putInt(mStylePoolOffset);
        for (int i = 0; i < mStringOffsets.length; i++) {
            byteBuffer.putInt(mStringOffsets[i]);
        }
        for (int i = 0; i < mValuePool.getStringSize(); i++) {
            String value = mValuePool.getString(i);

            if (mValuePool.isUTF8()) {
                byteBuffer.put((byte) value.length());
                byteBuffer.put((byte) value.length());
                byte[] bytes = value.getBytes(Charset.forName("utf-8"));
                byteBuffer.put(bytes, 0, bytes.length);
                byteBuffer.put(new byte[]{0x00});
                if (i == mValuePool.getStringSize() - 1 && byteBuffer.position() < mChunkSize) {
                    byteBuffer.put(new byte[0x00]);
                }
            } else {
                byteBuffer.putShort((short) value.length());
                byte[] bytes = addZero(value.getBytes(Charset.forName("utf-8")));
                byteBuffer.put(bytes, 0, bytes.length);
                byteBuffer.putChar('\0');
                if (i == mValuePool.getStringSize() - 1 && byteBuffer.position() < mChunkSize) {
                    byteBuffer.putChar('\0');
                }
            }
        }

        return byteBuffer.array();
    }

    @Override
    public String genXML() {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    private String filterStringNull(byte[] strByte) {
        int noZeroIndex = 0;
        int endIndex = 0;
        while (endIndex < strByte.length) {
            if (strByte[endIndex] != 0 && strByte[noZeroIndex] == 0) {
                swap(strByte, noZeroIndex, endIndex);
                endIndex++;
                noZeroIndex++;
            } else {
                if (strByte[noZeroIndex] != 0) {
                    noZeroIndex++;
                    endIndex = noZeroIndex;
                }
                if (endIndex < strByte.length &&
                        strByte[endIndex] == 0) {
                    endIndex++;
                }
            }
        }
        try {
            return new String(Arrays.copyOf(strByte, noZeroIndex), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(Arrays.copyOf(strByte, noZeroIndex));
        }
    }

    private byte[] addZero(byte[] strByte) {
        byte[] targetByte = new byte[strByte.length * 2];
        for (int i = 0; i < strByte.length; i++) {
            targetByte[2 * i] = strByte[i];
            targetByte[2 * i + 1] = '\0';
        }
        return targetByte;
    }

    private void swap(byte[] data, int a, int b) {
        byte temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < mValuePool.getStringSize(); i++) {
            stringBuilder.append("\"" + mValuePool.getString(i) + "\",").append("\n");
        }
        return String.format("ChunkSize:%s\nStringCount:%s\nStyleCount:%s\nUnknown:%s\nStringPoolOffset:%s\nStylePoolOffset:%s\n",
                mChunkSize, mStringCount, mStyleCount, mUnknown, mStringPoolOffset, mStylePoolOffset) + stringBuilder.toString();
    }

    public int getChunkSize() {
        return mChunkSize;
    }

    @Override
    public void setChunkSize(int chunkSize) {
        mChunkSize = chunkSize;
    }

    public int getStringCount() {
        return mStringCount;
    }

    public void setStringCount(int stringCount) {
        mStringCount = stringCount;
    }

    public int getStyleCount() {
        return mStyleCount;
    }

    public void setStyleCount(int styleCount) {
        mStyleCount = styleCount;
    }

    public int getUnknown() {
        return mUnknown;
    }

    public void setUnknown(int unknown) {
        mUnknown = unknown;
    }

    public int getStringPoolOffset() {
        return mStringPoolOffset;
    }

    public void setStringPoolOffset(int stringPoolOffset) {
        mStringPoolOffset = stringPoolOffset;
    }

    public int getStylePoolOffset() {
        return mStylePoolOffset;
    }

    public void setStylePoolOffset(int stylePoolOffset) {
        mStylePoolOffset = stylePoolOffset;
    }

    public int[] getStringOffsets() {
        return mStringOffsets;
    }

    public void setStringOffsets(int[] stringOffsets) {
        mStringOffsets = stringOffsets;
    }

    public int[] getStyleOffsets() {
        return mStyleOffsets;
    }

    public void setStyleOffsets(int[] styleOffsets) {
        mStyleOffsets = styleOffsets;
    }
}