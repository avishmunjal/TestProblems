
package com.testing.environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLmerge {
    public static void main(String argv[]) {

        try {
            File file1 = new File("C:\Users\avishmunjal\Desktop\TestCode\xml parsers\test.xml");
            File file2 = new File("C:\Users\avishmunjal\Desktop\TestCode\xml parsers\test2.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc1 = dBuilder.parse(file1);
            Document doc2 = dBuilder.parse(file2);

            doc1.getDocumentElement().normalize();
            doc2.getDocumentElement().normalize();
            NodeList nList1 = doc1.getDocumentElement().getChildNodes();
            NodeList nList2 = doc2.getDocumentElement().getChildNodes();
            for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {

                Node nNode1 = nList1.item(temp1);
                if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                    for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        NodeList childNodes2 = nNode2.getChildNodes();
                        if ((nNode2.getNodeType() == Node.ELEMENT_NODE) && 
                                (nNode2.getNodeName().equals(nNode1.getNodeName()))){
                            String tagName1 = nNode1.getNodeName();
                            NodeList ndListFirstFile = doc1.getElementsByTagName(tagName1);
                            int childCount = childNodes2.getLength();
                            for (int i = 0; i < childCount; i++) {
                                Node tempNode = childNodes2.item(i);
                                String tagName = tempNode.getNodeName();
                                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Node newNode = doc1.importNode(doc2.getElementsByTagName(tagName).item(0),true);
                                    ndListFirstFile.item(0).appendChild(newNode);
                                }
                            }  
                        }
                    }
                }
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 

            DOMSource source = new DOMSource(doc1);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result); 

            Writer output = new BufferedWriter(new FileWriter("C:\Users\avishmunjal\Desktop\TestCode\xml parsers\testOutput.xml"));
            String xmlOutput = result.getWriter().toString();  
            output.write(xmlOutput);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
