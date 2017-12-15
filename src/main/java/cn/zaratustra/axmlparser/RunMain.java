package cn.zaratustra.axmlparser;

import cn.zaratustra.axmlparser.core.AXMLParser;
import cn.zaratustra.axmlparser.core.XMLParser;
import cn.zaratustra.axmlparser.utils.StringUtils;

/**
 * Created by zaratustra on 2017/12/11.
 */
public class RunMain {

    public static void main(String[] args) {
        String[] args1 = new String[]{
                "", "a2x", "./test-data/AndroidManifest-normal.xml", "./test-data/AndroidManifest_out.xml"
        };
        String[] args2 = new String[]{
                "", "x2a", "./test-data/AndroidManifest_out.xml", "./test-data/AndroidManifest.xml"
        };

        Run(args1);
        Run(args2);
    }

    private static void Run(String[] args) {
        if (args.length < 4) {
            System.out.println("Please enter correct args");
            System.out.println("Ex: java -jar xxx.jar [a2x|x2a] [inputFile] [outputFile]");
            return;
        }

        String operation = args[1];
        String inputFile = args[2];
        String outputFile = args[3];

        boolean isAXML2XML = false;

        if ("a2x".equals(operation)) {
            isAXML2XML = true;
        } else if ("x2a".equals(operation)) {
            isAXML2XML = false;
        } else {
            System.out.println("Please enter operation a2x or x2a");
        }

        if (StringUtils.isEmpty(inputFile)) {
            System.out.println("Please enter input file");
            return;
        }

        if (StringUtils.isEmpty(outputFile)) {
            System.out.println("Please enter output file");
            return;
        }

        if (isAXML2XML) {
            AXMLParser axmlParser = new AXMLParser();
            axmlParser.parseToXML(inputFile, outputFile);
        } else {
            XMLParser xmlParser = new XMLParser();
            xmlParser.parseAXML(inputFile, outputFile);
        }
    }
}
