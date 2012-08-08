package buzzard.main;


import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;


import buzzard.utility.FileService;

public class EmbeddedNeo4j
{
    private static final String DB_PATH_Local = "C://MyStorage//RIT//MsProject//AWS//graphdb";
    private static final String DB_PATH_EC2 = "/home/ec2-user/graphdb";
    private static final String DB_PATH = DB_PATH_Local;
    
    String greeting;
    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        BOUGHT
    }
    // END SNIPPET: createReltype

    public static void main( final String[] args )
    {
        EmbeddedNeo4j hello = new EmbeddedNeo4j();
        hello.createDb();
        hello.removeData();
        hello.shutdown();
    }

    void createDb()
    {
        FileService.clearDb(DB_PATH);
        
        GraphDB.setPath(DB_PATH);
        
        graphDb = GraphDB.getGraphDB();
       

        // START SNIPPET: transaction
        Transaction tx = graphDb.beginTx();
        try
        {
            // Mutating operations go here
            // END SNIPPET: transaction
            // START SNIPPET: addData
            firstNode = graphDb.createNode();
            firstNode.setProperty( "Name", "Gourav Mitra" );
            secondNode = graphDb.createNode();
            secondNode.setProperty( "item", "iPhone 5" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.BOUGHT );
            relationship.setProperty( "message", "has bought" );
            // END SNIPPET: addData

            // START SNIPPET: readData
            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );
            // END SNIPPET: readData

            greeting = ( (String) firstNode.getProperty( "message" ) )
                       + " " + ( (String) relationship.getProperty( "message" ) )
                       + " " + ( (String) secondNode.getProperty( "message" ) );

            // START SNIPPET: transaction
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        // END SNIPPET: transaction
    }


    void removeData()
    {
        Transaction tx = graphDb.beginTx();
        try
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.BOUGHT, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
        finally
        {
            tx.finish();
        }
    }

    void shutdown()
    {
    	 GraphDB.shutdown();
    }
    

}
