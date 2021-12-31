import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Assignment4 {
	static Map map;
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
			
		ArrayList<String> set = fileRead("graph.txt");
		boolean flag = true;
		for(String temp:set) {
			String[] tokens = temp.split(" ");
			ArrayList<String> list = new ArrayList<>();
			for(String token:tokens) {
				list.add(token);
			}
			if (flag == true){
				map = new Map(list,"Map");
				flag = false;
			}
			else {
				map.insert(list);
			}		
		}
		System.setProperty("org.graphstream.ui","swing");
		map.graph.display();
		boolean flag2 = true;
		do {
			System.out.println("Enter two Node IDs to find the shortest path between them");
			System.out.print("First Node: ");
			String first = scan.next();
			System.out.println();
			System.out.print("Second Node: ");
			String second = scan.next();
			System.out.println();
			int firstAddy = map.locateVertex(first);
			int secondAddy = map.locateVertex(second);
			if(firstAddy == -1 || secondAddy == -1) {
				System.out.println("Please enter a Node that exists in on the graph.");
				continue;
			}
			if(flag2 == true) {
				map.dijkstras(first,second);
				flag2 = false;
			}
			else {
				
			}
			System.out.print("Would you like to run again?(y,n): ");
			String cont = scan.next();
			if(cont.toLowerCase().equals("y")) {
				map.visualReset();
				continue;
			}
			else {
				System.out.println("-Terminating Program-");
				break;
			}
		}while(true);
		scan.close();
	}
 	public static ArrayList<String> fileRead(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String data;
		ArrayList<String> set = new ArrayList<String>();
		while((data = br.readLine()) != null) {
			set.add(data);
		}
		br.close();
		return set;
	}
	static class Map{
		public Graph graph;
		public ArrayList<Vertex> vertices;
		public ArrayList<Edge> edges;
		public ArrayList<Vertex> visited;
		
		 String styleSheet = 
		        "node {"+
	                "fill-color: black;"+
	        		"size: 20px;"+
	                "text-size: 30;"+"text-alignment: at-right;"+"text-color: black;"+
	        		"text-visibility-mode: normal;"+
                "}"+
                "node.marked {"+
                	"fill-color: blue;"+
                "}"+
		        "edge.marked {"+
		        	"fill-color: blue;"+
		        	"size: 5px;"+
		        "}"+
			    "edge {"+
		        	"fill-color: black;"+
		        	"size: 3px;"+
        		"}";

		public Map(ArrayList<String> line,String name){
			graph = new SingleGraph("Graph "+name);
			vertices = new ArrayList<>();
			edges = new ArrayList<>();

			Node base = graph.addNode(line.get(0));
			base.setAttribute("ui.label", base.getId());
			
			Vertex v = new Vertex(line.remove(0),base);
			
			for(String temp:line) {
				Node node = graph.addNode(temp);
				node.setAttribute("ui.label", temp);
				
				Vertex u = new Vertex(temp,node);
				u.addEdge(v);
				v.addEdge(u);
				vertices.add(u);
			}
			vertices.add(v);
			
			for(String temp:line) {//visual edges
				Edge e = graph.addEdge(base.getId()+temp, base.getId(), temp);
				edges.add(e);
			}
			graph.setAttribute("ui.stylesheet",styleSheet);
		}
		public void visualPath(ArrayList<Vertex> path) {
			for(int i = 0;i<path.size();++i) {
				Node n = graph.getNode(path.get(i).name);
				n.setAttribute("ui.class","marked");
				if(i+1<path.size()) {
					if(containsEdge(path.get(i).name+path.get(i+1).name)) {
						Edge e = graph.getEdge(path.get(i).name+path.get(i+1).name);
						e.setAttribute("ui.class","marked");
					}
					else if (containsEdge(path.get(i+1).name+path.get(i).name)){
						Edge e = graph.getEdge(path.get(i+1).name+path.get(i).name);
						e.setAttribute("ui.class","marked");
					}
				}
			}
		}
		public boolean containsEdge(String name) {
			for(Edge e:edges) {
				if(e.getId().equals(name)) {
					return true;
				}
			}
			return false;
		}
		public void visualReset() {
			for(Vertex v:vertices) {
				Node n = graph.getNode(v.name);
				n.setAttribute("ui.class","");
			}
			for(Edge e:edges) {
				e.setAttribute("ui.class","");
			}
		}
		public Vertex retVertex(String element) {
			int address = -1;
			for(int i = 0;i<vertices.size();++i) {
				if(vertices.get(i).name.equals(element)) {
					address = i;
					break;
				}
			}
			if(address == -1) {
				System.out.println("ERR: retVertex did not find your vertex and returned null");
				return null;
			}
			return vertices.get(address);
		}
		public int locateVertex(String element) {
			int address = -1;
			for(int i = 0;i<vertices.size();++i) {
				if(vertices.get(i).name.equals(element)) {
					address = i;
					break;
				}
			}
			return address;
		}
		public int locateVertex(String element,ArrayList<Vertex> list) {
			int address = -1;
			for(int i = 0;i<list.size();++i) {
				if(list.get(i).name.equals(element)) {
					address = i;
					break;
				}
			}
			return address;
		}
		
		public void dijkstras(String s,String f) {
			visited = new ArrayList<>();
			VertexPriorityQueue pq = new VertexPriorityQueue(vertices);
			Vertex start = retVertex(s);
			Vertex end = retVertex(f);
			int startAddress = locateVertex(start.name);
			if(startAddress == -1) {
				System.out.println("ERR: improper start node. Please ensure the inputed node exists in graph.");
				return;
			}
			start.distance = 0;
			pq.pq.set(startAddress,start);
			pq.bubbleSort();
			
			Vertex current = null;
			while(visited.size() <= vertices.size()) {
				
				for(Vertex v:pq.pq) {
					if(!v.visited) {
						current = v;
					}
				}
				boolean flag = false;
				do {
					for(Vertex neighbor:current.neighbors) {
						if(neighbor.distance >= current.distance+1) {
							neighbor.distance = current.distance + 1;
							neighbor.route = current;
							pq.swap(neighbor);
						}
					}
					current.visited = true;
					visited.add(current);
					System.out.println(current.name);
				
					if(!current.neighbors.isEmpty()) {//pick a new neighbor.
						for(Vertex neighbor:current.neighbors) {
							if(!neighbor.visited) {
								current = neighbor;
								flag = true;
								break;
							}
						}
					}
				}while(flag);
			}

			ArrayList<Vertex> path = new ArrayList<>();
			int finishIndex = locateVertex(end.name,visited);
			Vertex position = visited.get(finishIndex);

			while(position.route != null){
				path.add(position);
				position = position.route;
			}
			path.add(position);
			visualPath(path);
		}
		public void insert(ArrayList<String> line){
			String element = line.remove(0);
			int baseAddy = locateVertex(element);
			if(baseAddy == -1) {
				System.out.println(" ERR: base is not located in vertices.");//troubleshoot
				return;
			}
			Vertex base = vertices.get(baseAddy);
			
			for(String temp:line) {
				int vertAddy = locateVertex(temp);
				if(vertAddy == -1) {
					Node node = graph.addNode(temp);
					node.setAttribute("ui.label",temp);
					
					Vertex v = new Vertex(temp,node);
					base.addEdge(v);
					v.addEdge(base);
					vertices.add(v);
					Edge e = graph.addEdge(base.node.getId()+v.node.getId(), base.node.getId(),v.node.getId());
					edges.add(e);
				}
				else {
					Vertex v = vertices.get(vertAddy);
					base.addEdge(v);
					v.addEdge(base);
					vertices.set(vertAddy, v);
					Edge e = graph.addEdge(base.node.getId()+v.node.getId(), base.node.getId(),v.node.getId());
					edges.add(e);
				}
			}
			vertices.set(baseAddy, base);
		}
		public ArrayList<Vertex> bubbleSort(ArrayList<Vertex> list) {
			boolean flag = true;
			while(flag) {
				flag = false;
				for(int i = 0;i<list.size()-1;i++) {
					Vertex vert1 = list.get(i);
					Vertex vert2 = list.get(i+1);
					if(vert1.distance > vert2.distance) {
						list.set(i, vert2);
						list.set(i+1, vert1);
						flag = true;
					}
				}
			}
			return list;
		}
		
		class VertexPriorityQueue{
			ArrayList<Vertex> pq;
			
			public VertexPriorityQueue(ArrayList<Vertex> vertices) {
				pq = new ArrayList<>();
				for(Vertex v:vertices) {
					pq.add(v);
				}
			}
			public void swap(Vertex vertex) {
				for(int i = 0;i<pq.size();++i) {
					if(pq.get(i).equals(vertex)) {
						pq.set(i, vertex);
					}
				}
			}
			public void remove(Vertex vertex) {
				for(int i = 0;i<pq.size();++i) {
					if(pq.get(i).equals(vertex)) {
						pq.remove(i);
					}
				}
			}
			public boolean exists(String element) {
				for(Vertex v:pq) {
					if(v.name.equals(element)) {
						return true;
					}
				}
				return false;
			}
			public boolean isEmpty() {
				if(pq.isEmpty()) {
					return true;
				}
				else {
					return false;
				}
			}
			public void bubbleSort() {
				boolean flag = true;
				while(flag) {
					flag = false;
					if(pq.size()>1) {
						for(int i = 0;i<pq.size()-1;i++) {
							Vertex vert1 = pq.get(i);
							Vertex vert2 = pq.get(i+1);
							if(vert1.distance > vert2.distance) {
								pq.set(i, vert2);
								pq.set(i+1, vert1);
								flag = true;
							}
						}
					}
				}
			}
			public void changeValue(Vertex vertex,int value) {
				int address = locateV(vertex.name);
				Vertex v = pq.get(address);
				v.distance = value;
				pq.set(address, v);
				bubbleSort();
			}
			public int locateV(String element) {
				int address = -1;
				for(int i = 0;i<pq.size();++i) {
					if(pq.get(i).name.equals(element)) {
						address = i;
						break;
					}
				}
				return address;
			}
			public void enqueue(Vertex vertex) {
				pq.add(vertex);
				bubbleSort();
			}
			public Vertex dequeue() {
				return pq.remove(0);
			}
			public Vertex peek() {
				return pq.get(0);
			}
		}
		
		class Vertex{
			int distance;
			String name;
			Vertex route;
			ArrayList<Vertex> neighbors;
			Node node;
			boolean visited;
			
			public Vertex(String element,Node node) {
				this.name = element;
				this.node = node;
				this.distance = Integer.MAX_VALUE;
				neighbors = new ArrayList<>();
				visited = false;
			}
			public void addEdge(Vertex neighbor) {
				neighbors.add(neighbor);
			}
		}//Class Vertex
	}//Class Map
}