package census;
import java.util.concurrent.*;

/**
 * Class uses parallelism to compute the total population within a give query.
 * Number of threads created depends on the sequential cutoff
 *
 * 
 * @author Joe Newbry and Evan Casey
 * @version 4/7/2013
 *
 */

public class V2Query extends RecursiveAction {
	
	private static final int SEQUENTIAL_CUTOFF = 50;
	int lo; // used when slicing up the data onto different threads
	int hi; 
	
	CensusGroup[] data;  // the array of censusData that we're tying to search through
	
	int totalPop; 
	int queryPop;
	
	float xDim;
	float yDim;
	Rectangle queryRec;
	CensusData censusData;
	
	//Constructor for v2Query class
	public V2Query(int l, int h, CensusGroup[] data, float xDim, float yDim, Rectangle queryRec, CensusData censusData){
		lo = l;
		hi = h;
		totalPop = 0;
		queryPop = 0;
		this.data = data;
		this.xDim = xDim;
		this.yDim = yDim;
		this.queryRec = queryRec;
		this.censusData = censusData;
	}
	
	protected void compute() {
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			this.findPopSeq(lo, hi);
		} else {
			int mid = (hi+lo)/2;
			V2Query left = new V2Query(lo, mid, data, xDim, yDim, queryRec, censusData);
			V2Query right = new V2Query(mid, hi, data, xDim, yDim, queryRec, censusData);
			left.fork();
			right.compute();
			left.join();
		
			totalPop = left.totalPop + right.totalPop;
			queryPop = left.queryPop + right.queryPop;
		}
	}
	
	//used to find the population of the smaller list of censusGroups sequentially
	protected void findPopSeq(int lo, int hi) {
		
		for (int i = lo; i < hi; i++) {
			
			Rectangle currentGroupRect = Rectangle.makeOneRec(censusData, data[i], xDim, yDim);
		
			//Adds the population of census group if it is contained in the census rectangle
			if(currentGroupRect.isContained(queryRec)){
				queryPop = queryPop + data[i].getPopulation();
			}
			
			//Keeps track of all the people
			totalPop = totalPop + data[i].getPopulation();
		}
	}
		
	public int getTotalPop() {
		return totalPop;
	}

	public void setTotalPop(int totalPop) {
		this.totalPop = totalPop;
	}

	public int getQueryPop() {
		return queryPop;
	}

	public void setQueryPop(int queryPop) {
		this.queryPop = queryPop;
	}

}
