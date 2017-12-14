package cn.zaratustra.axmlparser.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by zaratustra on 2017/12/11.
 */
public class AXMLHeader {

    public static final int MAGIC_NUMBER = 0x03000800;
    private int mHeaderLength;
    private int mMagicNumber;
    private int mFileSize;

    public AXMLHeader(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        mMagicNumber = byteBuffer.getInt();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        mFileSize = byteBuffer.getInt();
        mHeaderLength = byteBuffer.position();
        System.out.println("Get file size: " + mFileSize + ", " + Integer.toHexString(mMagicNumber) + ", " + mHeaderLength);
    }


    public AXMLHeader() {

    }

    public byte[] toBytes() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(mMagicNumber);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(mFileSize);
//        byteBuffer.putInt(mHeaderLength);
        return byteBuffer.array();
    }

    public int getHeaderLength() {
        return mHeaderLength;
    }

    public void setHeaderLength(int headerLength) {
        mHeaderLength = headerLength;
    }

    public int getMagicNumber() {
        return mMagicNumber;
    }

    public void setMagicNumber(int magicNumber) {
        mMagicNumber = magicNumber;
    }

    public int getFileSize() {
        return mFileSize;
    }

    public void setFileSize(int fileSize) {
        mFileSize = fileSize;
    }

    @Override
    public String toString() {
        return "file size: " + mFileSize + ", " + Integer.toHexString(mMagicNumber) + ", " + mHeaderLength;
    }
}
