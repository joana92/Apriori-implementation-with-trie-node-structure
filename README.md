## This implementation uses the trie node structure
### The dataset is the Amazon transaction dataset
* The output is just frequent items set. There is no associate rule output. 
### Pre-data:
    converting the raw transaction string to integer with hash map
#### In every travelsal, I try my best to decrease the space costing.

* To compile the program:
	javac Apriori.java TrieNode.java

* To run the program:
	java Apriori -Xmx6g min_sup k input_filename output_filename
	Example: java Apriori -Xmx6g 3 8 transactionDB.txt out_s=3_k=8+.txt
	Most test case run fine with the default memory configuration. 
	However, some may require a memory heap of 6g. 


