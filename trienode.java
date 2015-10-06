//package apriori;
import java.util.LinkedList;

public class trienode{
		 int num;//the number of items passed this node
		 LinkedList<trienode> son; //all the child nodes under this node
		 int itemval;
		trienode(int item){
			this.itemval=item;
			num=0;
			son=new LinkedList<trienode>();
		}
	
	//build the trie tree
	public void insert(int item){
		trienode node =new trienode(item);
		node.itemval=item;
		this.son.add(node);
	//	System.out.println(node.num);
	}
	public trienode search (int item){
		for(trienode s: this.son){
			if(s.itemval==item)
				return s;
		}
		return null;
	}
	public int getnode(){
		return itemval;
	}

}
