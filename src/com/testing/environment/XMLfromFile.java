
package com.testing.environment;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLfromFile {
    public static void main(String argv[]) {

        try {
            File file = new File("C:\Users\avishmunjal\Desktop\TestCode\xml parsers\test.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);


            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getDocumentElement().getChildNodes();
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    getChildCustom(nNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getChildCustom(Node n){
        NodeList childNodes = n.getChildNodes();
        int childCount = childNodes.getLength();
        if(childCount>1){
            System.out.println("\nCurrent Element :" + n.getNodeName());
            for (int i = 0; i < childCount; i++) {
                Node tempNode = childNodes.item(i);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    getChildCustom(tempNode);
                }
            }
        }else{
            System.out.println( n.getNodeName() + " <=> " + n.getTextContent());
        }
    }
}
