//import CountMinSketchState;
//Vijay
package storm.starter.trident.project.countmin.state;

import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;
import storm.trident.operation.TridentCollector;
import java.util.List;
import java.util.ArrayList;
import backtype.storm.tuple.Values;



import storm.starter.trident.project.countmin.state.TopList;
import storm.starter.trident.project.countmin.state.CountMinSketchStateFactory;
/**
 *@author: Vijay Sankar Ganesh (vgsankar@ncsu.edu)
 */


public class KQuery extends BaseQueryFunction<CountMinSketchState, String> {

	private CountMinSketchStateFactory skfact;

	//Constructor to initialize the reference variable for the Count Min Sketch Factory
	public KQuery(CountMinSketchStateFactory skfact)	{
		this.skfact = skfact;
	}

	public List<String> batchRetrieve(CountMinSketchState state, List<TridentTuple> inputs) {
        List<String> ret = new ArrayList();
        for(TridentTuple input: inputs) {
            ret.add(String.valueOf(state.estimateCount(input.getString(0))));
        }
        return ret;
    }

    public void execute(TridentTuple tuple, String count, TridentCollector collector) {
		//Returns the top k words if the appropriate priority queue and count min sketches are initialized
		if(skfact.getTopList() == null)
			collector.emit(null);
		else	{
			List<String> topwords = skfact.getTopList().getList();
			for(String word : topwords)
				collector.emit(new Values(word));
		}
    }    

}
