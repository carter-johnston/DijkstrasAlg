import java.util.ArrayList;
import java.util.PriorityQueue;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class graphstreamTest {
	public static void main(String[] args) {
		//System.setProperty("org.graphstream.ui","");
//		Graph graph = new SingleGraph("Tutorial 1");
//		System.setProperty("org.graphstream.ui","swing");
//		graph.display();
		
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		ArrayList<Integer> a = new ArrayList<>();
		a.add(1);
		a.add(5);
		a.add(3);
		a.add(2);
		a.add(4);
		for(int temp:a) {
			System.out.print(temp);
		}
		ArrayList<Integer> sorted = bubbleSort(a);
		System.out.println();
		for(int temp:a) {
			System.out.print(temp);
		}
		
	}
	public static ArrayList<Integer> bubbleSort(ArrayList<Integer> list) {
		boolean flag = true;
		while(flag) {
			flag = false;
			for(int i = 0;i<list.size()-1;i++) {
				int var1 = list.get(i);
				int var2 = list.get(i+1);
				if(var1 > var2) {
					list.set(i, var2);
					list.set(i+1, var1);
					flag = true;
				}
			}
		}
		return list;
	}
}
