package cn.zaratustra.axmlparser.model;

import cn.zaratustra.axmlparser.core.ValuePool;
import cn.zaratustra.axmlparser.utils.StringUtils;
import cn.zaratustra.axmlparser.utils.TypedValue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by zaratustra on 2017/12/12.
 */
public class StartTagChunk extends Chunk {

    public static final int CHUNK_TYPE = 0x00100102;

    private int mLineNumber;
    private int mNamespaceUri;
    private int mName;
    private int mFlags;
    private int mAttributeCount;
    private int mClassAttribute;
    private Attribute[] mAttributes;

    public static class Attribute {
        int mNamespaceUri;
        int mName;
        int mValueString;
        int mType;
        int mData;

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

        public int getValueString() {
            return mValueString;
        }

        public void setValueString(int valueString) {
            mValueString = valueString;
        }

        public int getType() {
            return mType;
        }

        public void setType(int type) {
            mType = type;
        }

        public int getData() {
            return mData;
        }

        public void setData(int data) {
            mData = data;
        }
    }


    @Override
    public String genXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        if (!StringUtils.isEmpty(mValuePool.getString(mNamespaceUri))) {
            stringBuilder.append(mValuePool.getNamespace(mValuePool.getString(mNamespaceUri)));
            stringBuilder.append(":");
        }

        stringBuilder.append(mValuePool.getString(mName)).append(" ");
        if (mValuePool.getString(mName).equals("manifest")) {
            Set<String> keySet = mValuePool.getUriToNameSpaceMaps().keySet();
            for (String set : keySet) {
                stringBuilder.append("xmlns:").append(mValuePool.getNamespace(set)).append("=\"").append(set).append("\"").append(" ");
            }
        }

        for (int i = 0; i < mAttributes.length; i++) {
            Attribute attribute = mAttributes[i];
            if (!StringUtils.isEmpty(mValuePool.getString(attribute.mNamespaceUri))) {
                stringBuilder.append(mValuePool.getNamespace(mValuePool.getString(attribute.mNamespaceUri)));
                stringBuilder.append(":");
            }
            stringBuilder.append(mValuePool.getString(attribute.mName)).append("=");
            String data = TypedValue.coerceToString(attribute.mType, attribute.mData);
            if (StringUtils.isEmpty(data)) {
                stringBuilder.append("\"").append(mValuePool.getString(attribute.mValueString)).append("\"");
            } else {
                stringBuilder.append("\"").append(data).append("\"");
            }
            stringBuilder.append(" ");
        }
        stringBuilder.append(">\n");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mAttributeCount; i++) {
            if (i < mAttributes.length) {
                stringBuilder.append("Attribute.Namespace:").append(mValuePool.getNamespace(mValuePool.getString(mAttributes[i].mNamespaceUri))).append("\n");
                stringBuilder.append("Attribute.Name:").append(mValuePool.getString(mAttributes[i].mName)).append("\n");
                stringBuilder.append("Attribute.ValueString:").append(mValuePool.getString(mAttributes[i].mValueString)).append("\n");
                stringBuilder.append("Attribute.Data:").append(TypedValue.coerceToString(mAttributes[i].mType, mAttributes[i].mData)).append("\n");
                stringBuilder.append("Attribute.Type Data:").append(mAttributes[i].mType + ", " + mAttributes[i].mData).append("\n").append("\n").append("\n");
            }
        }

        return String.format("ChunkSize:%s\nLineNumber:%s\nUnknown:%s\nNamespaceUri:%s\nName:%s\nFlags:0x%s\nAttribute Count:%s\nClass Attribute:%s\n",
                mChunkSize, mLineNumber, mUnknown, mValuePool.getString(mNamespaceUri), mValuePool.getString(mName), Integer.toHexString(mFlags), mAttributeCount,
                mClassAttribute) + stringBuilder.toString();
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

    public int getFlags() {
        return mFlags;
    }

    public void setFlags(int flags) {
        mFlags = flags;
    }

    public int getAttributeCount() {
        return mAttributeCount;
    }

    public void setAttributeCount(int attributeCount) {
        mAttributeCount = attributeCount;
    }

    public int getClassAttribute() {
        return mClassAttribute;
    }

    public void setClassAttribute(int classAttribute) {
        mClassAttribute = classAttribute;
    }

    public Attribute[] getAttributes() {
        return mAttributes;
    }

    public void setAttributes(Attribute[] attributes) {
        ArrayList<Attribute> list = new ArrayList<>();
        for (Attribute attribute : attributes) {
            if (attribute != null) {
                list.add(attribute);
            }
        }
        mAttributes = list.toArray(new Attribute[list.size()]);
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
        byteBuffer.putInt(mFlags);
        byteBuffer.putInt(mAttributeCount);
        byteBuffer.putInt(mClassAttribute);
        for (int i = 0; i < mAttributes.length; i++) {
            byteBuffer.putInt(mAttributes[i].mNamespaceUri);
            byteBuffer.putInt(mAttributes[i].mName);
            byteBuffer.putInt(mAttributes[i].mValueString);
            int type = ((mAttributes[i].mType) << 24);
            byteBuffer.putInt(type);
            byteBuffer.position(byteBuffer.position() - 4);
            byteBuffer.putShort((short) 0x08);
            byteBuffer.position(byteBuffer.position() + 2);
            byteBuffer.putInt(mAttributes[i].mData);
        }
        return byteBuffer.array();
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
        mNamespaceUri = byteBuffer.getInt();
        mName = byteBuffer.getInt();
        mFlags = byteBuffer.getInt();
        mAttributeCount = byteBuffer.getInt();
        mClassAttribute = byteBuffer.getInt();

        mAttributes = new Attribute[mAttributeCount];

        for (int i = 0; i < mAttributeCount; i++) {
            mAttributes[i] = new Attribute();
            mAttributes[i].mNamespaceUri = byteBuffer.getInt();
            mAttributes[i].mName = byteBuffer.getInt();
            mAttributes[i].mValueString = byteBuffer.getInt();
            int type = byteBuffer.getInt();
            mAttributes[i].mType = type >> 24;
            mAttributes[i].mData = byteBuffer.getInt();
        }
    }
}
