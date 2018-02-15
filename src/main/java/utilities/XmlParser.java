package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Created by rajemani on 2/3/2017.
 */
public class XmlParser {

    private Map<String,String> resultMap;
    private final String PARAMETER = "parameter";
    private final String LABEL = "label";

    public XmlParser()
    {

    };

    public Map<String, String> retrieveTemplatesAsMap(String path) {
        String filename = path;
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(new File(filename)));

            String line;

            while((line=br.readLine())!= null){
                sb.append(line.trim());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseXmlToItems (sb.toString());
    }

    public Map<String,String> parseXmlToItems(String xmlContent) {
        try {
            Document document = null;
            if(xmlContent != null)
            {
                document = convertToDocument(xmlContent);
            }
            resultMap = new HashMap<String, String>();

            if (document != null) {
                NodeList nodeList = document.getDocumentElement().getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node instanceof Element) {
                        NodeList childNodes = node.getChildNodes();
                        String parameter =null,label=null;
                        for (int j = 0; j < childNodes.getLength(); j++)
                        {
                            Node cNode = childNodes.item(j);
                            if (cNode instanceof Element)
                            {
                                if (cNode.getNodeName().equals(PARAMETER)) {
                                    parameter = cNode.getLastChild().getTextContent().trim();
                                }
                                else if (cNode.getNodeName().equals(LABEL)) {
                                    label = cNode.getLastChild().getTextContent().trim();
                                }
                            }
                        }
                        resultMap.put(label,parameter);
                    }

                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultMap;
    }

    private Document convertToDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try
        {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

}
