package data;

public class Queue<ContentType> {
	
	private class Node{
		ContentType Object;
		Node next;
		
		public Node(ContentType Object){
			this.Object = Object;
		}
	}
	
	private Node head = null;
	private Node tail = null;
	
	private int length = 0;
	
	public void add(ContentType Object){
		Node n = new Node(Object);
		if(tail!=null)tail.next = n;
		tail = n;
		if(head==null)head = n;
		
		length++;
	}
	
	public ContentType get(){
		return head.Object;
	}
	
	public void remove(){
		if(head!=null){
			head=head.next;
			if(head==null)tail=null;			
			length = (length > 0) ? (length-1) : 0;
		}
	}
	
	public boolean isEmpty(){
		return head==null;
	}
	
	public Queue<ContentType> clone(){
		Queue<ContentType> clone = new Queue<>();
		Node node = head;
		while(node!=null){
			clone.add(node.Object);
			node = node.next;
		}
		return clone;
	}
	
	public int getLength() {
		return length;
	}

}
