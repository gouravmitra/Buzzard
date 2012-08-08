package buzzard.main;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import buzzard.main.GraphDB.RelTypes;
import buzzard.serialize.Serializer;
import buzzard.utility.KeyGenerator;

public class TestBuzzard 
{

	public static void main(String args[])
	{
		GraphDB.setPath("graphdb");

		GraphDatabaseService graphDb = GraphDB.getGraphDB();

		Node refGraphDb = GraphDB.getReferenceNode();

		Transaction tx = graphDb.beginTx();

		Node firstNode = null;
		try
		{
			firstNode = graphDb.createNode();
			System.out.println("Node Created");
			firstNode.setProperty("name", "Gourav Mitra");
			firstNode.setProperty("type", "User");
			firstNode.setProperty("id", KeyGenerator.getKey());
			System.out.println("Node Properties have been set");
			refGraphDb.createRelationshipTo( firstNode, RelTypes.USER );
			System.out.println("Connected to reference node");

			Node secondNode = graphDb.createNode();
			secondNode.setProperty("name", "iPhone");
			secondNode.setProperty("type", "cell phone");
			secondNode.setProperty("id", KeyGenerator.getKey());
			firstNode.createRelationshipTo(secondNode, RelTypes.BOUGHT);

			Node thirdNode = graphDb.createNode();
			thirdNode.setProperty("name", "Altima");
			thirdNode.setProperty("type", "car");
			thirdNode.setProperty("id", KeyGenerator.getKey());
			firstNode.createRelationshipTo(thirdNode, RelTypes.BOUGHT);

			Node fourthNode = graphDb.createNode();
			fourthNode.setProperty("name", "Apple");
			fourthNode.setProperty("type", "Company");
			fourthNode.setProperty("id", KeyGenerator.getKey());
			secondNode.createRelationshipTo(fourthNode, RelTypes.MADEBY);

			Node fifthNode = graphDb.createNode();
			fifthNode.setProperty("name", "Nissan");
			fifthNode.setProperty("type", "Company");
			fifthNode.setProperty("id", KeyGenerator.getKey());
			thirdNode.createRelationshipTo(fifthNode, RelTypes.MADEBY);

			tx.success();
		}
		finally
		{
			tx.finish();
		}


		
		Serializer serializer = Serializer.getSerializer("graph.xml");
		serializer.serializeGraph();
		
	}

}