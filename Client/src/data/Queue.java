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
	
	public void add(ContentType Object){
		Node n = new Node(Object);
		if(tail!=null)tail.next = n;
		tail = n;
		if(head==null)head = n;
	}
	
	public ContentType get(){
		return head.Object;
	}
	
	public void remove(){
		if(head!=null){
			head=head.next;
			if(head==null)tail=null;
		}
	}
	
	public boolean isEmpty(){
		return head==null;
	}

}
