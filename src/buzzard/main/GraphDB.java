package buzzard.main;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Node;

import buzzard.utility.KeyGenerator;

public class GraphDB {
	
	private static GraphDatabaseService graphDB = null;
    private static String DB_PATH = null;
    private static Node dbReferenceNode = null;
    
    public static enum RelTypes implements RelationshipType
    {
        USER, BOUGHT, MADEBY
    }
    
    private GraphDB()
    {
    	
    }
    
    public static void setPath(final String PATH )
    {
    	DB_PATH = PATH;    	
    }
	
	public static GraphDatabaseService getGraphDB()
	{
		if(graphDB==null)
		{
			
			
			graphDB = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	        registerShutdownHook( graphDB );
	        System.out.println("Created New Graph DB");
		}
		else
		{
			System.out.println("Getting Existing Graph DB");
		}
		
		return graphDB;
	}
	
	public static Node getReferenceNode()
	{
		
		if(dbReferenceNode == null)
		{
			Transaction tx = graphDB.beginTx();
			try
			{
				dbReferenceNode = graphDB.createNode();
				dbReferenceNode.setProperty("name", "DB Reference");
				dbReferenceNode.setProperty("id", KeyGenerator.getKey());
				dbReferenceNode.setProperty("type", "Reference Node");
				
				tx.success();
				System.out.println("Created New Graph DB Reference");
			
			}
			finally
			{
				tx.finish();
			}
		}
		else
		{
			System.out.println("Getting Existing Graph DB Reference");
		}
		
		return dbReferenceNode;
		
		
		
	}
	
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
	
	public static void shutdown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDB.shutdown();
        
        // END SNIPPET: shutdownServer
    }

}
