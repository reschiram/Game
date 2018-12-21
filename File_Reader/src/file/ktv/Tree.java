package file.ktv;

import java.util.ArrayList;


public class Tree<ContentType> {
	
	private ArrayList<Node<ContentType>> startNodes = new ArrayList<>();
	
	private void add(String key, ArrayList<Node<ContentType>> nodes, ContentType... obj){
		boolean found  = false;
		for(Node<ContentType> node: nodes){
			String kp = getKeyPart(key);
			if(node.key.equals(kp)){
				if(key.equals(kp)){
					for(ContentType o: obj)node.save.add(o);
				}else{
					add(key.substring(kp.length()+1, key.length()), node.follow, obj);
				}
				found = true;
				break;
			}
		}
		if(!found&&!key.contains(".")){
			Node<ContentType> node = new Node<ContentType>(key);
			for(ContentType o: obj)node.save.add(o);
			nodes.add(node);
		}else if(!found){
			String kp = getKeyPart(key);
			Node<ContentType> node = new Node<ContentType>(kp);
			nodes.add(node);
			add(key.substring(kp.length()+1, key.length()), node.follow, obj);
		}
	}
	
	public void add(String key, ContentType... obj){
		this.add(key, startNodes, obj);
	}

	private String getKeyPart(String key) {
		for(int i = 0; i<key.length();i++){
			if(key.charAt(i) == '.')return key.substring(0, i);
		}
		return key;
	}
	
	private ArrayList<ContentType> get(String key, ArrayList<Node<ContentType>> nodes){
		for(Node<ContentType> node: nodes){
			String kp = getKeyPart(key);
			if(node.key.equals(kp)){
				if(!key.contains("."))return node.save;
				else return get(key.substring(kp.length()+1, key.length()), node.follow);
			}
		}
		return null;
	}
	
	public ArrayList<ContentType> get(String key){
		return this.get(key, startNodes);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(String key, ContentType... objs){
		this.remove(key, startNodes, objs);
	}

	private void remove(String key, ArrayList<Node<ContentType>> nodes, ContentType[] objs) {
		for(Node<ContentType> node: nodes){
			String kp = getKeyPart(key);
			if(node.key.equals(kp)){
				if(!key.contains(".")){
					if(objs.length == 0){
						nodes.remove(node);
					}else for(ContentType obj: objs){
						nodes.remove(obj);
					}
				}else remove(key.substring(kp.length()+1,key.length()), node.follow, objs);
				return;
			}
		}
	}

	public ArrayList<Node<ContentType>> getStartNodes() {
		return startNodes;
	}

	public void set(String key, ContentType[] value) {
		set(key, value, startNodes);
	}
	
	private boolean set(String key, ContentType[] value, ArrayList<Node<ContentType>> nodes){
		boolean found = false;
		for(Node<ContentType> node: nodes){
			String kp = getKeyPart(key);
			if(node.key.equals(kp)){
				if(!key.contains(".")){
					node.save.clear();
					for(ContentType obj: value){
						node.save.add(obj);
					}
				}
				else return set(key.substring(kp.length()+1, key.length()), value, node.follow);
				return true;
			}
		}
		if(!found){
			while(key.contains(".")){
				String sKey = key.substring(0,getKeyPart(key).length());
//				System.out.println(sKey);
				add(sKey, nodes);
				for(Node n: nodes){
					if(n.key.equals(sKey)) nodes = n.follow;
				}
				key = key.substring(sKey.length()+1,key.length());
			}
			Node<ContentType> node = new Node<ContentType>(key);
			for(ContentType o: value)node.save.add(o);
			nodes.add(node);
		}
		return false;
	}

	public String[] getSubKey(String key) {
		if(key.equals("")){
			String[] keys = new String[startNodes.size()];
			for(int i = 0; i<startNodes.size();i++)keys[i] = startNodes.get(i).key;
			return keys;
		}
		return getSubKey(key, startNodes);
	}

	private String[] getSubKey(String key, ArrayList<Node<ContentType>> nodes) {
		for(Node<ContentType> node: nodes){
			String kp = getKeyPart(key);
			if(node.key.equals(kp)){
				if(!key.contains(".")){
					String[] keys = new String[node.follow.size()];
					for(int i = 0; i<node.follow.size();i++)keys[i] = node.follow.get(i).key;
					return keys;
				}
				else return getSubKey(key.substring(kp.length()+1, key.length()), node.follow);
			}
		}
		return new String[0];
	}

}
