//package apriori;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.*;

//import java.util.Map;
//import java.util.LinkedList;
//import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.*;
//import java.util.AbstractCollection;
//import java.util.Iterator;

public class aphli {
	private HashMap<String, Integer> itemdict;//<raw trans, itemval>
	private int itemvalue=0;
	int min_sup;
	int level=0;
	String filename;
	String outname;
    File outfile;
	 List<List<Integer>> transRecord=new ArrayList<List<Integer>>();
	public List<List<Integer>> getTransaction(String filename, int k){
		//convert the raw data to the list of transactions of int
		//remove the transactions whose itemsets are smaller than k
		List<List<Integer>> tran=new ArrayList<List<Integer>>();
		itemdict=new HashMap<>();
		try{
			String encoding= "GBK";
			File file=new File(filename);
			if(file.isFile() && file.exists()){
				FileReader read = new FileReader(filename);
			      BufferedReader bufferedReader = new BufferedReader(read);	      
				//InputStreamReader read = new InputStreamReader(
				//		new FileInputStream(file), encoding);
				//BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT=null;
				while((lineTXT=bufferedReader.readLine())!=null){
					String [] lineString= lineTXT.split(" ");
					if(lineString.length>=k){
					List<Integer> lineList=new ArrayList<Integer>();//store the int trasaction in each line
					for(int i=0;i<lineString.length;i++){
						if(itemdict.containsKey(lineString[i]))
							lineList.add(itemdict.get(lineString[i]));
						else{
                            itemvalue++;
							itemdict.put(lineString[i], itemvalue);
							lineList.add(itemvalue);
						}
							
					}
					//finish one line, then add the list to a whole list
                  Collections.sort(lineList);
					tran.add(lineList);
				}
				}
                read.close();
			}
			
		}catch (Exception e) {
			System.out.println("error: worng in reading files");
			e.printStackTrace();
	}
		return tran;
	}
    public trienode generateFirstCandidate(String filename,int k,trienode root){
    	//nothing to store in the root node, so using dummy node here
    	//trienode root=new trienode(0);
    	//transRecord=getTransaction(filename,k);
    	// the first add all the item to the son list
    	for(int i=0;i<itemvalue;i++){
    		//trienode child=new trienode();
    		//child.itemval=itemvalue;
    		root.insert(i+1);

        }
    	for(int ii=0;ii<transRecord.size();ii++){
    		List<Integer> lineList=new ArrayList<Integer>();	
    		lineList=transRecord.get(ii);
    		//for(int j=0;j<lineList.size();j++){	}
    		supportCount(root,lineList,0);//get the num for each node
    	}
      //  System.out.println("finished supportCount");
        checkRemove(root,1);  
      //  System.out.println("finished checkRemove");
        
    	 
    	return root;
    }
    // count how many times passed each node
    public void supportCount(trienode root, List<Integer>list, int ith){
    	// use the recursive to count support
        if(root.son.isEmpty())
    	root.num++;
       // System.out.println(root.num);
        else {
        	for(int i=ith;i<list.size();i++){
               // System.out.println(list.size());
        		trienode son=root.search(list.get(i));
                        
        		if(son!=null)
                    {//System.out.println(son.num);
        			supportCount(son,list,ith+1); }   			
        	}
        }
    }
   public void countAll(trienode root){
    List <Integer> linet=new ArrayList<>();
    for(int i=0;i<transRecord.size();i++)
       {  
        linet=transRecord.get(i);
        supportCount(root,linet,0);
    }
   }

    //remove the nodes whose support is less than min_suport
    //remove from the leaf node to the inner node
    public void checkRemove(trienode root,int l){
        ListIterator<trienode> listIterator = root.son.listIterator();
        while (listIterator.hasNext()) {
            trienode node = listIterator.next();
            if(!node.son.isEmpty()){
                checkRemove(node,l+1);
            }
            //System.out.println(min_sup+" is the min_sup and level is "+level);
            if((node.son.isEmpty() && node.num<min_sup)) {
                listIterator.remove();
            }
            if(node.son.isEmpty() && l<level)
                listIterator.remove();

          }/*
    	int count=root.son.size();
//System.out.println(count);
    	int i=0;
    	List <trienode>list=root.son;
    	while(i<count){
    		trienode node=list.get(i);
    		//not reach the leaf node continue to go down
    		if(!node.son.isEmpty()){
    			checkRemove(node);
    		}
    		if(node.son.isEmpty() && (node.num<min_sup)){
    			list.remove(node);
    			count=list.size();
    			//i++;
    		}
            i++;

    	}
        */
    	
    }


    public void generateNextCandidate(trienode root, int kk){
    	int firstlevel=level-2;//the first level I need to reach
    	if(firstlevel==kk){//reach the level I need
    		for(int i=0;i<root.son.size()-1;i++){
    			for(int ii=i+1;ii<root.son.size();ii++){
    			trienode nextnode=root.son.get(i);
    			//creat new node, add larger node at cruurent level as its son
    			nextnode.son.add(new trienode(root.son.get(ii).getnode()));
              //  System.out.println("checking"+i+";ii is"+ii+";"+root.son.get(ii).getnode());
    			}
    		}
    		root.son.remove(root.son.size()-1);   		
    	}
    	else if(firstlevel>kk){//not reach the level I wanted, just go down recursivly
    		for(trienode n: root.son){
    			generateNextCandidate(n,kk+1);
    		}	
    	}    	
    	//int start=0;//starting from here to the firstlevel I need
    	/*trienode node= root;
    	LinkedList<trienode> currlevel=new LinkedList<>();
    	LinkedList<trienode> nextlevel=new LinkedList<>();
    	if(!node.son.isEmpty()){		
    		currlevel=node.son;
    	}*/
    	//not reach the level, go down
    	
    	/*while(start<firstlevel){
    		for(int i=0;i<currlevel.size();i++){
    		trienode temp=currlevel.get(i);
    		for(int j=0;j<temp.son.size();j++){
    	    	//LinkedList<trienode> nextlevel=new LinkedList<>();
    			nextlevel.add(temp.son.get(j));
    		}
    		}
    		currlevel=nextlevel;
    		nextlevel.clear();
    		start++;
    		}*/
    	
    	//generate next from currlevel
    	/*for(int ii=0;ii<currlevel.size()-1;ii++){
    		for(int jj=ii+1;jj<currlevel.size();jj++){
    			currlevel.get(ii).num=0;
    			currlevel.get(ii).son.add(new trienode(currlevel.get(jj).getnode()));
    		}
    	}*/
    	//remove the largest one in the currlevel
    	
    	}
    public void createOutputFile(String outname){
    	File outfile=new File(outname);
    	try{
    		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outfile),"utf-8");
    		BufferedWriter bufferedWriter = new BufferedWriter(write);
    		//bufferWriter.write()
    		}catch (Exception e) {
    			System.out.println("error: worng in writing files");
    			e.printStackTrace();
    	}
    }
    public void printTransaction(String outname,trienode root,String item){
    	//File outfile=new File(outname);
        
        if(root.itemval !=0){//get the raw transaction
    		for(String key:itemdict.keySet()){
    			if(itemdict.get(key).equals(root.itemval))
    				item=item+key+" ";
    		}
    	}
    	if(root.son.isEmpty()){
    		try{
                FileWriter fileWritter = 
                    new FileWriter(outname,true);
    		BufferedWriter bufferedWriter = new BufferedWriter(fileWritter);
    		bufferedWriter.write(item+"("+root.num+")\n");
           // System.out.println("printTransaction:"+item+"("+root.num+")\n");
    		bufferedWriter.close();
    		}catch (Exception e) {
    			System.out.println("error: worng in writing files");
    			e.printStackTrace();
    	}
    	}
    	else {
    		for(trienode s:root.son){
    			printTransaction(outname,s,item);
    		}
    	}
    }
    public static void main(String [] args){
    	try{
    	int minsup=Integer.parseInt(args[0]);
    	int k=Integer.parseInt(args[1]);
    	String file=args[2];
    	String result=args[3];
        
    	//transRecord= getTransactions(file,k);
    	aphli apriori =new aphli();
        apriori.outfile=new File(result);
    	trienode dummy=new trienode(0);
                apriori.level++;
        apriori.transRecord=apriori.getTransaction(file,k);
       // System.out.println(apriori.transRecord.size());
    	apriori.filename=file;
    	apriori.outname=result;
    	apriori.min_sup=minsup;
    	trienode node= apriori.generateFirstCandidate(file,k,dummy);
        //apriori.printTransaction(result,node,"");

      //  System.out.println("TEST"+node.son.size());

                    //apriori.printTransaction(result,node,"");

    	while(!node.son.isEmpty()){
           // System.out.println(node.son.size());
            //System.out.println("level"+apriori.level);
            if(apriori.level>=k)
                apriori.printTransaction(result,node,"");
    		apriori.level++;
            //System.out.println("check 1:"+node.son.size());
    		apriori.generateNextCandidate(node,0);
            //apriori.supportCount()
            //System.out.println("check 2:"+node.son.get(0).son.size());

            apriori.countAll(node);
            apriori.checkRemove(node,1);
    		
            //apriori.generateFirstCandidate(file, k, node);
    	}
        //*/
    
    }catch ( Exception e){
        System.out.println("The format is: java aphli min_sup k input_filename output_filename");
        e.printStackTrace();
      }
        }
}
    


