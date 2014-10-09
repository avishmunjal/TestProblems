package com.testing.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProducerConsumerforXML {

    static ArrayList<String> producerInputList;

    public static void main(String args[]) {
        parseInputXmlFile();
        Vector<String> sharedQueue = new Vector<String>();
        int size = 3;
        Thread prodThread = new Thread(new Producer(sharedQueue, size), "Producer");
        Thread consThread = new Thread(new Consumer(sharedQueue), "Consumer");
        prodThread.start();
        consThread.start();
    }

    private static void parseInputXmlFile() {
        producerInputList = new ArrayList<String>();
        try{
            File file = new File("C:\Users\avishmunjal\Desktop\TestCode\xml parsers\test.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getChildNodes();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    getChildCustom(nNode);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getChildCustom(Node n){
        NodeList childNodes = n.getChildNodes();
        int childCount = childNodes.getLength();
        if(childCount>1){
            for (int i = 0; i < childCount; i++) {
                Node tempNode = childNodes.item(i);
                if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                    getChildCustom(tempNode);
                }
            }
        }else{
            producerInputList.add(n.getTextContent());
        }
    }
}

class Producer implements Runnable {

    private final Vector<String> sharedQueue;
    private final int SIZE;

    public Producer(Vector<String> sharedQueue, int size) {
        this.sharedQueue = sharedQueue;
        this.SIZE = size;
    }

    @Override
    public void run() {
        for (int i = 0; i <ProducerConsumerforXML.producerInputList.size() ; i++) {
            System.out.println("Produced: " + ProducerConsumerforXML.producerInputList.get(i));
            try {
                produce(ProducerConsumerforXML.producerInputList.get(i));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void produce(String i) throws InterruptedException {

        //wait if queue is full
        while (sharedQueue.size() == SIZE) {
            synchronized (sharedQueue) {
                System.out.println("Queue is full " + Thread.currentThread().getName()
                        + " is waiting , size: " + sharedQueue.size());
                sharedQueue.wait();
            }
        }

        //producing element and notify consumers
        synchronized (sharedQueue) {
            sharedQueue.add(i);
            sharedQueue.notifyAll();
        }
    }
}

class Consumer implements Runnable {

    private final Vector<String> sharedQueue;

    public Consumer(Vector<String> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Consumed: " + consume());
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String consume() throws InterruptedException {
        //wait if queue is empty
        while (sharedQueue.isEmpty()) {
            synchronized (sharedQueue) {
                System.out.println("Queue is empty " + Thread.currentThread().getName()
                        + " is waiting , size: " + sharedQueue.size());

                sharedQueue.wait();
            }
        }

        // consume element and notify waiting producer
        synchronized (sharedQueue) {
            sharedQueue.notifyAll();
            return (String) sharedQueue.remove(sharedQueue.size()-1);
        }
    }
}

