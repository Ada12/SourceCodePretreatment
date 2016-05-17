package pers.yangchen.SCP;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangchen on 16-4-25.
 */
public class GetRTCNames {
    public static List<String> getNames(String path) throws ParserConfigurationException, IOException, SAXException {
        List<String> names = new ArrayList<String>();
        File inputFile = new File(path);
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dFactory.newDocumentBuilder();
        Document doc = builder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :"
                + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("topic");
        System.out.println("----------------------------");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            String name = eElement.getAttribute("titles");
            String[] n = name.split("\\,| |\\.");
            String nName = "";
            for(int j = 0; j <  n.length; j++){
                if(!n[j].equals("")){
                    nName = nName + n[j].trim() + ".";
                }
            }
            names.add(nName+"txt");
        }
        return names;
    }
}
