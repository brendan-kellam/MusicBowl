package com.main.util.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ManipXML {
	
	/**
	 * This class is used to manipulate the master xml file.
	 * @author Brendan Kellam
	 * @version 1.2
	 */

	
	//xml interface initialization
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder; 
	private Document doc;
	
	private String path;
	private boolean local;
	
	public void open(String path, boolean local){ //open the master xml document. Accepts: path of the xml file, if the file is located locally in the project file
		this.path = path;
		this.local = local;
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			if(local) doc = docBuilder.parse(ManipXML.class.getResourceAsStream(path));
			else doc = docBuilder.parse(path);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refresh(){ //refreshes the xml file by creating a new instance with the same credentials
		open(path, local);
	}
	
	public NodeList returnChildNodes(String root){ //return a list of nodes under a specific family tag
		Node rootNode = doc.getElementsByTagName(root).item(0);
		NodeList list = rootNode.getChildNodes();
		return list;
	}
	
	public Node getNode(String root, String name){ //get a specific node from a family tag
		NodeList userData = returnChildNodes(root);
		
		for(int i = 0; i < userData.getLength(); i++){ //cycle through the family and find the node that equals to the node parameter
			Node node = userData.item(i);
			if(name.equals(node.getNodeName())){
				return node;
			}
		}
		return null;
	}
	
	public void createChildNode(String root, String name, String contents, String[] keys, String[] values){ //create a new child node under a specified family
		Node rootNode = doc.getElementsByTagName(root).item(0);
	
		Element child = doc.createElement(name); 
		
		for(int i = 0; i < keys.length; i++){ //loops through the keys, and set a new attribute to the node with these keys and values
			child.setAttribute(keys[i], values[i]);
		}
		
		child.appendChild(doc.createTextNode(contents));
		rootNode.appendChild(child);
		write(); //write to the file
	}
	
	public void addKey(Node target, String key, String value){ //append a new key to a specified node
		Element child = (Element) target;
		child.setAttribute(key, value);
		write();
	}
	
	public void delKey(Node target, String key, String value){ //delete a key from a specified node
		Element child = (Element) target;
		String result = child.getAttribute(key);
		result = result.replaceFirst(value, "");
		child.setAttribute(key, result);
		write();
	}
	
	public String getKey(Node target, String key){ //get a value from a node with a specified key
		Element child = (Element) target;
		return child.getAttribute(key);
	}
	
	public void appendKey(Node target, String key, String append){ //append to a already existing key (does not delete value of that key)
		Element child = (Element) target;
		child.setAttribute(key, child.getAttribute(key) + append);
		write();
	}
	
	public boolean checkKeyExistance(Node target, String key){ //checks if a key already exists in a specified family
		Element child = (Element) target;
		String result = child.getAttribute(key);
		if(result.equals("")) return false; //return if no key exists
		return true;
	}
	
	public int getNodePosition(Node target){ //returns the specified node's position in the "node neighborhood"
		int index = 0;
	    Node sibling;

	    //loops through all previous siblings, adding to the index positions
	    while ((sibling = target.getPreviousSibling()) != null) {
        	target = sibling;

	    	if(sibling.getNodeName().equals(target.getNodeName())){
	        	++index;
	    	}
	        	
	    }

	    //returns the index position of the node
	    return index/2;
	}
	
	private void write(){ //write to the document
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			String pathSub = ManipXML.class.getResource(path).toString();
			pathSub = pathSub.substring(5, pathSub.length());
			StreamResult result = new StreamResult(pathSub);
			
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
