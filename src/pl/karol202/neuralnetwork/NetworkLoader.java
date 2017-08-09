package pl.karol202.neuralnetwork;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import pl.karol202.neuralnetwork.network.Network;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class NetworkLoader
{
	private Network network;
	
	public NetworkLoader(Network network)
	{
		this.network = network;
	}
	
	public void tryToLoadNetworkData(File file)
	{
		try
		{
			loadNetworkData(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadNetworkData(File file) throws IOException, SAXException, ParserConfigurationException
	{
		if(file.createNewFile()) return;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(file);
		Element elementNetwork = document.getDocumentElement();
		network.parseNetwork(elementNetwork);
	}
	
	public void tryToSaveNetworkData(File file)
	{
		try
		{
			saveNetworkData(file);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void saveNetworkData(File file) throws ParserConfigurationException, TransformerException, IOException
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.newDocument();
		document.appendChild(network.saveNetwork(document));
		saveToFile(document, file);
	}
	
	private void saveToFile(Document document, File file) throws TransformerException, IOException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(file);
		transformer.transform(domSource, streamResult);
	}
}