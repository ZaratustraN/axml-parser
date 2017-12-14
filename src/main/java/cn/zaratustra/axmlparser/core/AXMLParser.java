package cn.zaratustra.axmlparser.core;

import cn.zaratustra.axmlparser.model.*;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


/**
 * Created by zaratustra on 2017/12/12.
 */
public class AXMLParser {

    private ArrayList<Chunk> mChunkList;

    public AXMLParser() {
        mChunkList = new ArrayList<>();
    }

    public void parseToXML(String inputFile, String outputFile) {
        try {
            byte[] byteData = readFileData(inputFile);

            ByteBuffer byteBuffer = ByteBuffer.wrap(byteData);
            //解析头部
            AXMLHeader axmlHeader = new AXMLHeader(byteData);
            byteBuffer.position(axmlHeader.getHeaderLength());

            ValuePool valuePool = new ValuePool();
            //读取Chunk
            while (byteBuffer.position() < byteData.length) {
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                int chunkType = byteBuffer.getInt();
                byteBuffer.position(byteBuffer.position() - 4);
                Chunk chunk = null;
                switch (chunkType) {
                    case StringChunk.CHUNK_TYPE:
                        chunk = new StringChunk();
                        break;
                    case ResourceChunk.CHUNK_TYPE:
                        chunk = new ResourceChunk();
                        break;
                    case StartNamespaceChunk.CHUNK_TYPE:
                        chunk = new StartNamespaceChunk();
                        break;
                    case StartTagChunk.CHUNK_TYPE:
                        chunk = new StartTagChunk();
                        break;
                    case EndTagChunk.CHUNK_TYPE:
                        chunk = new EndTagChunk();
                        break;
                    case EndNamespaceChunk.CHUNK_TYPE:
                        chunk = new EndNamespaceChunk();
                        break;
                    case TextChunk.CHUNK_TYPE:
                        chunk = new TextChunk();
                        break;
                    default:
                        byteBuffer.position(byteData.length);
                        break;
                }
                if (chunk != null) {
                    chunk.parseFromAXML(valuePool, byteData, byteBuffer.position());
                    byteBuffer.position(byteBuffer.position() + chunk.getChunkSize());
                    System.out.println(chunk.toString());
                    mChunkList.add(chunk);
                }
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < mChunkList.size(); i++) {
                Chunk chunk = mChunkList.get(i);
                if (chunk instanceof EndNamespaceChunk || chunk instanceof StartNamespaceChunk
                        || chunk instanceof StartTagChunk || chunk instanceof EndTagChunk || chunk instanceof TextChunk) {
                    stringBuilder.append(chunk.genXML());
                }
            }
            writeToFile(formatXML(stringBuilder.toString()), outputFile);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private String formatXML(String in) {
        return in;
//        try {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            InputSource is = new InputSource(new StringReader(in));
//            Document document = db.parse(is);
//            OutputFormat format = new OutputFormat(document);
//
//            format.setIndenting(true);
//            format.setStandalone(true);
//            format.setLineWidth(30);
//            format.setAllowJavaNames();
//            format.setEncoding("utf-8");
//            format.setOmitComments(true);
//            format.setOmitDocumentType(true);
//            format.setOmitXMLDeclaration(false);
//
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//            return out.toString();
//        } catch (Exception e) {
//            return in;
//        }
    }

    private void writeToFile(String s, String outputFile) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(s.getBytes());

        byte[] buffer = new byte[1024];
        int length;
        while ((length = byteArrayInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, length);
        }
    }

    private byte[] readFileData(String inputFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(inputFile));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }


}
