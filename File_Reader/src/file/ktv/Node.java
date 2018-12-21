package file.ktv;

import java.util.ArrayList;

public class Node<ContentType> {
	
	String key;
	ArrayList<Node<ContentType>> follow = new ArrayList<>();
	ArrayList<ContentType> save = new ArrayList<>();
	
	Node(String key){
		//System.out.println(key);
		this.key = key;
	}
}
