package buzzard.serialize;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;


public class GNode {
	
	public GNode(Node node, GNode connectedTo, RelationshipType connectedBy )
	{
		this.node = node;
		this.connectedTo = connectedTo;
		this.connectedBy = connectedBy;
	}
	
	private Node node;
	private GNode connectedTo;	
	private RelationshipType connectedBy;
	
	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}
	/**
	 * @return the connectedTo
	 */
	public GNode getConnectedTo() {
		return connectedTo;
	}
	/**
	 * @param connectedTo the connectedTo to set
	 */
	public void setConnectedTo(GNode connectedTo) {
		this.connectedTo = connectedTo;
	}
	/**
	 * @return the connectedBy
	 */
	public RelationshipType getConnectedBy() {
		return connectedBy;
	}
	/**
	 * @param connectedBy the connectedBy to set
	 */
	public void setConnectedBy(RelationshipType connectedBy) {
		this.connectedBy = connectedBy;
	}
	
	

}
