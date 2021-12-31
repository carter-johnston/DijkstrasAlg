import java.util.ArrayList;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

public class Assignment4V1 {

	public static void main(String[] args) {
		ArrayList<String> a1 = new ArrayList<>();//rm
		ArrayList<String> a2 = new ArrayList<>();//rm
		ArrayList<String> a3 = new ArrayList<>();//rm
		ArrayList<String> a4 = new ArrayList<>();//rm
		a1.add("1");a1.add("2");a1.add("3");//rm 
		a2.add("2");a2.add("4");a2.add("5");//rm 
		a3.add("3");a3.add("5");//rm 
		a4.add("4");a4.add("5");//rm 
		
		Map map = new Map("Test",a1);
		map.insert(a2);
		map.insert(a3);
		map.insert(a4);
		
		System.setProperty("org.graphstream.ui","Swing");
		map.graph.display();
		

	}

	static class Map{
		Graph graph;
		ArrayList<Node> nodes;
		public Map(String name,ArrayList<String> line){
			graph = new SingleGraph(name);
			nodes = new ArrayList<>();
			Node root = new Node(line.remove(0));
			for(String temp:line) {
				Node n = new Node(temp);
				nodes.add(n);
			}
			root.setEdges(line);
			nodes.add(root);
		}
		public void insert(ArrayList<String> line) {
			int address = 0;
			for(Node temp:nodes) {
				if(temp.getId() == line.get(0)) {
					line.remove(0);
					for(String temp2:line) {
						if(!idExists(temp2)) {
							Node n = new Node(temp2);
							nodes.add(n);
						}
					}
					temp.setEdges(line);
					nodes.set(address, temp);
					break;
				}
				address++;
			}
		}
		public boolean idExists(String id) {
			boolean flag = false;
			for(Node temp:nodes) {
				if(temp.getId() == id) {
					flag = true;
				}
			}
			return flag;
		}

		class Node{
			String id;
			ArrayList<String> edges;
			public Node(String id){
				this.id = id;
				graph.addNode(id);
			}
		}
	}
}
