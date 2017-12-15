package cn.zaratustra.axmlparser.core;

import cn.zaratustra.axmlparser.model.*;
import cn.zaratustra.axmlparser.utils.TypedValue;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static cn.zaratustra.axmlparser.utils.TypedValue.TYPE_INT_DEC;
import static cn.zaratustra.axmlparser.utils.TypedValue.TYPE_STRING;

/**
 * Created by zaratustra on 2017/12/12.
 */
public class XMLParser {

    private ArrayList<Chunk> mChunkList;
    private HashMap<String, Integer> mPublicNameToResId;
    public static final String PUBLIC_XML = "./libs/public.xml";

    public XMLParser() {
        mChunkList = new ArrayList<>();
        mPublicNameToResId = new HashMap<>();
        try {
            initPublicNameToResId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPublicNameToResId() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new InputStreamReader(new FileInputStream(PUBLIC_XML)));
        Document document = db.parse(is);
        NodeList nodeList = document.getElementsByTagName("public");
        for (int i = 0; i < nodeList.getLength(); i++) {
            try {
                Node node = nodeList.item(i);
                NamedNodeMap attribute = node.getAttributes();
                if (attribute.getNamedItem("type").getNodeValue().equals("attr")) {
                    mPublicNameToResId.put(attribute.getNamedItem("name").getNodeValue(),
                            Integer.decode(attribute.getNamedItem("id").getNodeValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void parseAXML(String inputFile, String outputFile) {
        try {
            ValuePool valuePool = new ValuePool();
            initValuePullString(inputFile, valuePool);
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = pullParserFactory.newPullParser();
            pullParser.setInput(new FileInputStream(new File(inputFile)), "utf-8");
            int event = pullParser.getEventType();

            int lineIndex = 2;
            valuePool.putUriToNamespace("http://schemas.android.com/apk/res/android", "android");
            valuePool.putNamespaceToUri("http://schemas.android.com/apk/res/android", "android");
            StartNamespaceChunk startNamespaceChunk = new StartNamespaceChunk();
            startNamespaceChunk.setChunkType(StartNamespaceChunk.CHUNK_TYPE);
            startNamespaceChunk.setLineNumber(lineIndex++);
            valuePool.putInteger(startNamespaceChunk.getLineNumber());
            startNamespaceChunk.setPrefix(valuePool.checkAndAddString("android"));
            startNamespaceChunk.setUri(valuePool.checkAndAddString("http://schemas.android.com/apk/res/android"));
            startNamespaceChunk.setValuePool(valuePool);
            startNamespaceChunk.setChunkSize(4 * 6);

            mChunkList.add(startNamespaceChunk);

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        lineIndex = parseStartTagChunk(pullParser, lineIndex, valuePool);
                        break;
                    case XmlPullParser.END_TAG:
                        lineIndex = parseEndTagChunk(pullParser, lineIndex, valuePool);
                        break;
                }
                try {
                    event = pullParser.next();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            addStringChunkAndResourceChunk(valuePool);

            EndNamespaceChunk endNamespaceChunk = new EndNamespaceChunk();
            endNamespaceChunk.setChunkType(EndNamespaceChunk.CHUNK_TYPE);

            if (valuePool.isUTF8()) {
                endNamespaceChunk.setLineNumber(valuePool.pullInteger());
            } else {
                endNamespaceChunk.setLineNumber(lineIndex++);
            }

            endNamespaceChunk.setPrefix(valuePool.checkAndAddString("android"));
            endNamespaceChunk.setUri(valuePool.checkAndAddString("http://schemas.android.com/apk/res/android"));
            endNamespaceChunk.setValuePool(valuePool);
            endNamespaceChunk.setChunkSize(4 * 6);
            mChunkList.add(endNamespaceChunk);

            AXMLHeader axmlHeader = new AXMLHeader();
            axmlHeader.setMagicNumber(AXMLHeader.MAGIC_NUMBER);
            axmlHeader.setHeaderLength(8);
            int totalSize = 0;
            totalSize += 8;
            for (int i = 0; i < mChunkList.size(); i++) {
                totalSize += mChunkList.get(i).getChunkSize();
            }
            axmlHeader.setFileSize(totalSize);

            System.out.println(axmlHeader.toString());

            ByteBuffer byteBuffer = ByteBuffer.allocate(axmlHeader.getFileSize());
            byteBuffer.put(axmlHeader.toBytes());

            for (int i = 0; i < mChunkList.size(); i++) {
                byteBuffer.put(mChunkList.get(i).toByte());
                System.out.println(mChunkList.get(i).toString());
            }

            writeToFile(byteBuffer.array(), outputFile);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initValuePullString(String inputFile, ValuePool valuePool) throws Exception {

        valuePool.setUTF8(readFile(new File(inputFile), "utf-8").contains("standalone=\"no\""));

        XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser pullParser = pullParserFactory.newPullParser();
        pullParser.setInput(new FileInputStream(new File(inputFile)), "utf-8");
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    int attributeCount = pullParser.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        if (pullParser.getAttributeName(i).equals("xmlns:android")) {
                            continue;
                        }
                        String namespaceAndName = pullParser.getAttributeName(i);
                        String[] splitStr = namespaceAndName.split(":");
                        if (splitStr.length >= 2) {
                            valuePool.checkAndAddString(splitStr[1]);
                            if (splitStr[0].equals("android") && !valuePool.getResourceIds().contains(mPublicNameToResId.get(splitStr[1]))) {
                                valuePool.addResourceId(mPublicNameToResId.get(splitStr[1]));
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            try {
                event = pullParser.next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addStringChunkAndResourceChunk(ValuePool valuePool) {
        ResourceChunk resourceChunk = new ResourceChunk();
        resourceChunk.setValuePool(valuePool);
        resourceChunk.setChunkType(ResourceChunk.CHUNK_TYPE);
        resourceChunk.setChunkSize(valuePool.getResourceIdSize() * 4 + 2 * 4);
        mChunkList.add(0, resourceChunk);

        StringChunk stringChunk = new StringChunk();
        stringChunk.setUnknown(valuePool.isUTF8() ? 256 : 0x00000000);
        stringChunk.setValuePool(valuePool);
        stringChunk.setChunkType(StringChunk.CHUNK_TYPE);
        stringChunk.setStringCount(valuePool.getStringSize());
        stringChunk.setStyleCount(0);
        stringChunk.setStringPoolOffset(stringChunk.getStringCount() * 4 + 7 * 4);
        stringChunk.setStylePoolOffset(0);
        stringChunk.setStyleOffsets(new int[]{0});

        int[] stringOffsets = new int[valuePool.getStringSize()];
        int index = 0;
        for (int i = 0; i < valuePool.getStringSize(); i++) {
            stringOffsets[i] = index;
            if (valuePool.isUTF8()) {
                index += (valuePool.getString(i).length() + 3);
            } else {
                index += (valuePool.getString(i).length() * 2 + 4);
            }
        }
        stringChunk.setStringOffsets(stringOffsets);
        if (valuePool.isUTF8()) {
            int size = index + stringChunk.getStringCount() * 4 + 7 * 4;
            if (size % 2 != 0) {
                size += 1;
            }
            stringChunk.setChunkSize(size);
        } else {
            int size = index + stringChunk.getStringCount() * 4 + 7 * 4;
            if (size % 4 != 0) {
                size += 2;
            }
            stringChunk.setChunkSize(size);
        }
        mChunkList.add(0, stringChunk);
    }

    private int parseEndTagChunk(XmlPullParser pullParser, int lineIndex, ValuePool valuePool) {
        int chunkType = EndTagChunk.CHUNK_TYPE;
        int nameSpaceUri = valuePool.getUri(pullParser.getNamespace());
        int name = valuePool.checkAndAddString(pullParser.getName());
        int chunkSize = 6 * 4;

        EndTagChunk endTagChunk = new EndTagChunk();
        endTagChunk.setChunkType(EndTagChunk.CHUNK_TYPE);
        endTagChunk.setValuePool(valuePool);
        endTagChunk.setChunkType(chunkType);

        if (valuePool.isUTF8()) {
            endTagChunk.setLineNumber(valuePool.pullInteger());
        } else {
            int lineNumber = lineIndex++;
            endTagChunk.setLineNumber(lineNumber);
        }
        endTagChunk.setName(name);
        endTagChunk.setNamespaceUri(nameSpaceUri);
        endTagChunk.setChunkSize(chunkSize);
        mChunkList.add(endTagChunk);
        return lineIndex;
    }

    private int parseStartTagChunk(XmlPullParser pullParser, int lineIndex, ValuePool valuePool) {
        int chunkType = StartTagChunk.CHUNK_TYPE;
        int tagName = valuePool.checkAndAddString(pullParser.getName());
        int nameSpaceUri = valuePool.getUri(pullParser.getNamespace());
        int lineNumber = lineIndex;
        int flags = 0x140014;
        int attributeCount = pullParser.getAttributeCount();
        int classAttribute = 0;
        StartTagChunk.Attribute[] attributes = new StartTagChunk.Attribute[attributeCount];
        for (int i = 0; i < attributes.length; i++) {
            if (pullParser.getAttributeName(i).equals("xmlns:android")) {
                attributes[i] = null;
                attributeCount--;
                continue;
            }
            attributes[i] = new StartTagChunk.Attribute();

            int name;
            int namespace = -1;

            String namespaceAndName = pullParser.getAttributeName(i);
            String[] splitStr = namespaceAndName.split(":");
            if (splitStr.length >= 2) {
                namespace = valuePool.getUri(splitStr[0]);
                name = valuePool.checkAndAddString(splitStr[1]);
                if (splitStr[0].equals("android") && !valuePool.getResourceIds().contains(mPublicNameToResId.get(splitStr[1]))) {
                    valuePool.addResourceId(mPublicNameToResId.get(splitStr[1]));
                }
            } else {
                name = valuePool.checkAndAddString(namespaceAndName);
            }

            attributes[i].setNamespaceUri(namespace);
            attributes[i].setName(name);
            String valueString = pullParser.getAttributeValue(i);
            try {
                if ("versionName".equals(valuePool.getString(attributes[i].getName()))) {
                    attributes[i].setType(TYPE_STRING);
                    attributes[i].setValueString(valuePool.checkAndAddString(valueString));
                    attributes[i].setData(valuePool.getStringIndex(valueString));
                } else if ("platformBuildVersionCode".equals(valuePool.getString(attributes[i].getName()))) {
                    attributes[i].setType(TYPE_INT_DEC);
                    attributes[i].setValueString(valuePool.checkAndAddString(valueString));
                    attributes[i].setData(Integer.parseInt(valueString));
                } else {
                    TypedValue.initAttribute(attributes[i], valueString);
                }
            } catch (Exception e) {
                if (e.getMessage().equals("nothing get in attribute")) {
                    attributes[i].setType(TYPE_STRING);
                    attributes[i].setValueString(valuePool.checkAndAddString(valueString));
                    attributes[i].setData(valuePool.getStringIndex(valueString));
                } else {
                    e.printStackTrace();
                }
            }
        }
        int chunkSize = 9 * 4 + attributeCount * 5 * 4;
        StartTagChunk startTagChunk = new StartTagChunk();
        startTagChunk.setValuePool(valuePool);
        startTagChunk.setChunkType(chunkType);
        startTagChunk.setChunkSize(chunkSize);
        startTagChunk.setName(tagName);
        startTagChunk.setNamespaceUri(nameSpaceUri);
        startTagChunk.setLineNumber(lineNumber);
        valuePool.putInteger(lineNumber);
        startTagChunk.setFlags(flags);
        startTagChunk.setAttributeCount(attributeCount);
        startTagChunk.setClassAttribute(classAttribute);
        startTagChunk.setAttributes(attributes);
        mChunkList.add(startTagChunk);
        lineIndex += (attributeCount);
        return lineIndex;
    }

    private void writeToFile(byte[] bytes, String outputFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = byteArrayInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, length);
        }
    }

    public static String readFile(File file, String charset) {
        if (file == null || !file.exists()) {
            return "";
        }

        StringWriter sw = new StringWriter();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(file), charset == null ? "utf-8" : charset);
            char[] buf = new char[1024];
            int len;
            while ((len = isr.read(buf)) != -1) {
                sw.write(buf, 0, len);
            }

            return sw.toString();

        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

}
