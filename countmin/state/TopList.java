package storm.starter.trident.project.countmin.state;


import java.lang.*;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import storm.starter.trident.project.countmin.state.CountMinSketchState;
import storm.starter.trident.project.countmin.state.SketchComparator;
import storm.starter.trident.project.countmin.state.BloomFilter;

/***
 * @author: Vijay Sankar Ganesh (vgsankar@ncsu.edu)
***/


//The following class holds the Priority Queue to find the Top K words.
// It also stores the list of Stop-Words which are a list of words are
// a list of words which shouldn't be counted for the computation of
// the Top K words
public class TopList	{
	PriorityQueue<String> heap;
	int k;
	CountMinSketchState sketch;
	//The reference to the bloomfilter which is used with the stop words
	BloomFilter<String> stopWordsFilter;
	//The list of Stop Words
	String[] stopWords =	{
								"to", "a", "and", "but",
								"the", "an", "this", "that",
								"or"
							};

	public TopList(CountMinSketchState sketch)	{
		this.sketch = sketch;
		this.k = 10;
		this.heap = new PriorityQueue<String>(k+1, new SketchComparator(this.sketch));
		this.initBloomFilter();
	}

	public TopList(CountMinSketchState sketch, int k)	{
		this.sketch = sketch;
		this.k = k;
		this.heap = new PriorityQueue<String>(k+1, new SketchComparator(this.sketch));
		this.initBloomFilter();
	}

	//Initialize the bloomfilter with the list of stop words
	public void initBloomFilter()	{
		stopWordsFilter = new BloomFilter<String>((double)0.05, stopWords.length);
		for(String stopWord : stopWords)
			stopWordsFilter.add((String)stopWord.toUpperCase());
	}

	//Every time a word is added or updated in the count min sketch, it also updated
	// in the priorit queue
	public void add(String element)	{
		if(stopWordsFilter.contains(element.toUpperCase()))
			return;
		heap.add(element);
		while(heap.size() > k)	heap.remove();
	}

	//Returns the list of words in the priority queue, i.e., the top k words
	public List<String> getList()	{
		return new ArrayList<String>(heap);
	}
	
	//Checks if a certain word is present in the priority queue
	public boolean contains(String val)	{
		return heap.contains(val);
	}

}
