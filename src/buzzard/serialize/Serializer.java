package buzzard.serialize;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import buzzard.main.GraphDB;

public class Serializer
{

	private HashMap<Integer, GNode> graphNodesMemomry = new HashMap<Integer, GNode>();

	private PrintWriter  graphFile;
	private static Serializer serializer;

	private Serializer(String xmlFile)
	{
		try {
			graphFile = new PrintWriter(new FileWriter(xmlFile));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static Serializer getSerializer(String xmlFile)
	{
		if(serializer==null)
			serializer = new Serializer(xmlFile);

		return serializer;
	}


	private void loadGraphNodeMemory()
	{

		Node refNode = GraphDB.getReferenceNode();

		LinkedList<Node> queue = new LinkedList<Node>();

		queue.offer(refNode);

		int key = (Integer)refNode.getProperty("id");
		GNode gNode = new GNode(refNode, null, null);
		graphNodesMemomry.put(key, gNode );

		System.out.println(key +" "+ gNode.getNode().getProperty("name") );

		while(!queue.isEmpty())
		{
			Node node = queue.poll();

			GNode gSourceNode = graphNodesMemomry.get(node.getProperty("id"));

			for(Relationship rel: node.getRelationships(Direction.OUTGOING))
			{
				Node endNode = rel.getEndNode();
				queue.offer(endNode);
				GNode gEndNode = new GNode(endNode, null, rel.getType());

				key = (Integer)endNode.getProperty("id");

				if(graphNodesMemomry.get(key)==null)
				{	
					gNode = new GNode(endNode, null, null);
					graphNodesMemomry.put(key, gNode );
				}

				while(gSourceNode.getConnectedTo()!=null)
				{
					gSourceNode = gSourceNode.getConnectedTo();
				}

				gSourceNode.setConnectedTo(gEndNode);

				System.out.println(node.getProperty("name")+" "+rel.getType()+" "+endNode.getProperty("name"));
			}
		}

	}




	public void serializeGraph()
	{
		loadGraphNodeMemory();
		graphFile.println("<graph-db>");
		serializeNode();
		serializegNode();
		graphFile.println("</graph-db>");
		graphFile.flush();

	}

	private void serializeNode()
	{
		for(Map.Entry<Integer, GNode> entry: graphNodesMemomry.entrySet())
		{
			int key = entry.getKey();
			GNode gNode = entry.getValue();
			Node node = gNode.getNode();
			String name = (String)node.getProperty("name");
			String type = (String)node.getProperty("type");

			graphFile.println("<node>");
			graphFile.println("<node-id>"+key+"</node-id>");
			graphFile.println("<node-name>"+name+"</node-name>");
			graphFile.println("<node-type>"+type+"</node-type>");
			graphFile.println("</node>");

		}
	}

	private void serializegNode()
	{
		for(Map.Entry<Integer, GNode> entry: graphNodesMemomry.entrySet())
		{
			int key = entry.getKey();
			GNode gNode = entry.getValue();

			graphFile.println("<gnode>");
			graphFile.println("<root-node>"+key+"</root-node>");
			graphFile.println("<connected-nodes>");

			GNode nextNode = gNode.getConnectedTo();

			while(nextNode!=null)
			{
				graphFile.println("<connected-node>");
				graphFile.println("<connected-node-id>"+nextNode.getNode().getProperty("id") +"</connected-node-id>");
				graphFile.println("<relation>"+ nextNode.getConnectedBy()+"</relation>");
				graphFile.println("</connected-node>");
				nextNode = nextNode.getConnectedTo();
			}

			graphFile.println("</connected-nodes>");
			graphFile.println("</gnode>");

		}
	}

	public void deserializeGraph(String path)
	{

		

	}


	private void createNewDBGraph()
	{

	}

	private void domGraphParser(String path)
	{
		Document dom=null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(path);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}



}
