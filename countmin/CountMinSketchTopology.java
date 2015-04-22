package storm.starter.trident.project.countmin; 

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.FilterNull;
import storm.trident.operation.builtin.MapGet;
import storm.trident.operation.builtin.Sum;
import storm.trident.state.StateFactory;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.tuple.Values;


import storm.trident.operation.builtin.Count;

import storm.starter.trident.project.countmin.state.CountMinSketchStateFactory;
import storm.starter.trident.project.countmin.state.CountMinQuery;
import storm.starter.trident.project.countmin.state.CountMinSketchUpdater;
import storm.starter.trident.tutorial.functions.SplitFunction;


import storm.starter.trident.project.countmin.state.KQuery;
import storm.starter.trident.project.countmin.state.TopList;


/**
 *@author: Vijay Sankar Ganesh (vgsankar@ncsu.edu)
 */


public class CountMinSketchTopology {

	 public static StormTopology buildTopology( LocalDRPC drpc ) {

		TridentTopology topology = new TridentTopology();

		//The dimensions for the Count-Min Sketch
		int width = 10;
		int depth = 15;
		int seed = 10;


		//The reference to object which is used to create the Count-Min Sketch object
		CountMinSketchStateFactory sketch_factory = new CountMinSketchStateFactory(depth, width, seed);

		
		FixedBatchSpout spoutFixedBatch = new FixedBatchSpout(new Fields("sentence"), 3,
			new Values("the cow jumped over the moon"),
			new Values("the man went to the store and bought some candy"),
			new Values("four score and seven years ago"),
			new Values("how many apples can you eat"),
			new Values("to be or not to be the person"))
			;
		spoutFixedBatch.setCycle(false);

		//The countMinDBMS object is the state object which is used with the DRPC
		TridentState countMinDBMS = topology.newStream("tweets", spoutFixedBatch)
			.each(new Fields("sentence"), new Split(), new Fields("words"))
			.partitionPersist( sketch_factory, new Fields("words"), new CountMinSketchUpdater())
		;

		//The reference to the function class which performs the required retrival of the TopK values from a priority queue
		KQuery func = new KQuery(sketch_factory);
		topology.newDRPCStream("get_count", drpc)
			.stateQuery(countMinDBMS, new Fields("args"), func, new Fields("topk"))
			.project(new Fields("topk"))
		;

		return topology.build();

	}


	public static void main(String[] args) throws Exception {
		Config conf = new Config();
			conf.setDebug( false );
			conf.setMaxSpoutPending( 10 );


			LocalCluster cluster = new LocalCluster();
			LocalDRPC drpc = new LocalDRPC();
			cluster.submitTopology("get_count",conf,buildTopology( drpc));

			for (int i = 0; i < 5; i++) {

					System.out.println("DRPC RESULT:"+ drpc.execute("get_count","to the apples cow"));
					Thread.sleep( 1000 );
			}

		System.out.println("STATUS: OK");
		//cluster.shutdown();
		//drpc.shutdown();
	}
}
