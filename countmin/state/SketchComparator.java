package storm.starter.trident.project.countmin.state;

import java.lang.*;
import java.util.Comparator;

import storm.starter.trident.project.countmin.state.CountMinSketchState;

/***
 *@author: Vijay Sankar Ganesh (vgsankar@ncsu.edu)
***/

//The comparator class which is used to order the elements of the priority queue which stores the top k elements
public class SketchComparator implements Comparator<String>	{
	
	private CountMinSketchState sketch;

	public SketchComparator(CountMinSketchState sketch)	{
		this.sketch = sketch;
	}

	public int compare(String str1, String str2)	{
		return (int) (sketch.estimateCount(str1) - sketch.estimateCount(str2));
	}
	
}
