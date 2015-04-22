package storm.starter.trident.project.countmin.state;

import storm.trident.state.StateFactory;
import storm.trident.state.State;
import java.util.Map;
import backtype.storm.task.IMetricsContext;

/**
 *@author: Vijay Sankar Ganesh (vgsankar@ncsu.edu)
 */

public class CountMinSketchStateFactory implements StateFactory {

	protected int depth;
	protected int width;
	protected int seed;
	// The following reference variables are added to hold the reference of the Count Min Sketch object and
	//  the TopList class (which holds the priority queue)
	protected CountMinSketchState sketch;
	protected TopList toplist;


	public CountMinSketchStateFactory( int depth, int width, int seed) {
		this.depth=depth;
		this.width = width;
		this.seed = seed;
		this.sketch = null;
		this.toplist = null;
	}


	@Override
	public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
		this.sketch = new CountMinSketchState(depth,width,seed);
		//The moment a new count min sketch is created, the class which holds the priority queue is also
		// instantiated and is passed the reference to the newly created count min sketch
		this.toplist = new TopList(this.sketch);
		this.sketch.setTopList(toplist);
		return sketch;
		//return new CountMinSketchState(depth,width,seed);
	} 
	public TopList getTopList()	{
		return this.toplist;
	}
}
