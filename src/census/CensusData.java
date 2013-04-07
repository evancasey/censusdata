package census;
import structure5.*;
import java.util.concurrent.*;
import java.util.*;
/** 
 *  just a resizing array for holding the input
 *  
 *  note: array may not be full; see data_size field
 *  
 *  @author: Joe Newbry and Evan Casey
 */

public class CensusData{
	private static final int INITIAL_SIZE = 100;
	private CensusGroup[] data;
	private int data_size;
	
	// fields to store 
	private float minLon;
	private float minLat;
	private float maxLon;
	private float maxLat;
	
	// instance variables (lo, high, left data result, right data result, for finding max and min using
	private int lo;
	private int hi;
	
	// fields to store population values when processing a query
	private int queryPop;

	private int totalPop;

	private static int SEQUENTIAL_CUTOFF = 100;
	
	private static final ForkJoinPool fjPool = new ForkJoinPool();
	
	// processed matrix
	private int[][] beforeProcess;
	private int[][] afterProcess;
	
	public CensusData() {
		data = new CensusGroup[INITIAL_SIZE];
		data_size = 0;
		minLat = 100; //minLat should be below this and positive
		minLon = 100; //maxLon should be above this and negative
		maxLat = -100; //maxLon should be above this and positive
		maxLon = -100; //maxLon should be above this and negative
	}
	
	
	// find edges by invoking fjpool on V2Corners
	public void findEdgesPar() {
		V2Corners corners = new V2Corners(0, data_size, data);
		fjPool.invoke(corners);
		minLat = corners.minLat;
		minLon = corners.minLon;
		maxLon = corners.maxLon;
		maxLat = corners.maxLat;
	}
	
	public void add(int population, float latitude, float longitude) {
		if(data_size == data.length) { // resize
			CensusGroup[] new_data = new CensusGroup[data.length*2];
			for(int i=0; i < data.length; ++i)
				new_data[i] = data[i];
			data = new_data;
		}
		CensusGroup g = new CensusGroup(population,latitude,longitude); 
		data[data_size++] = g;
	}
	
	public void findEdgesSeq(int lo, int hi) {
		for(int i=lo; i < hi; ++i) {
		    if(data[i].getLatitude() < minLat) {
		    	minLat = data[i].getLatitude();
		    }
		    if(data[i].getLongitude() < minLon) {
		    	minLon = data[i].getLongitude();
		    }
		    if(data[i].getLatitude() > maxLat) {
		    	maxLat = data[i].getLatitude();
		    }
		    if(data[i].getLongitude() > maxLon) {
		    	maxLon = data[i].getLongitude();
		    }
	    }
	}
	
	public void processQuery(float xDim, float yDim, Rectangle queryRec) {
		V2Query query = new V2Query(0, data_size, data, xDim, yDim, queryRec, this);
		fjPool.invoke(query);
		totalPop = query.totalPop;
		queryPop = query.queryPop;
	}
	
	public void preProcess(float xDim, float yDim) {
		System.out.println((int)xDim + (int)yDim);
		beforeProcess = new int[(int) (xDim+1)][(int) (yDim+1)];
		afterProcess = new int[(int) (xDim+1)][(int) (yDim+1)];
		// putting in zero values for all of these (0 value excluded because it won't be used
		// decided to leave 0 values empty rather than subtracting 1 from each query value
		for (int i = 1; i <= xDim; i++) {
			for (int j = 1; j <= yDim; j++) {
				System.out.println("x,y " + i + " " + j);
				beforeProcess[i][j] = 0;
				afterProcess[i][j] = 0;
			}
		}
		
		// update beforeProcess with the actual populations
		for (int i = 0; i < data_size; i++) {
			Rectangle currentGroupRect = Rectangle.makeOneRec(this, data[i], xDim, yDim);
			beforeProcess[(int)currentGroupRect.getLeft()][(int)currentGroupRect.getTop()] = beforeProcess[(int)currentGroupRect.getLeft()][(int)currentGroupRect.getTop()] 
																								+ data[i].getPopulation();
		}
		
		for (int i = 1; i<= xDim; i++){
			for (int j = 1; j<=yDim; j ++){
				int sumOfPop = 0;
				// subtract double counted square
				if (j-1 > 0 && i-1> 0){
					sumOfPop = sumOfPop - afterProcess[i-1][j-1];
				}
				
				// add squares to the left and above (y-1)
				if (i -1 > 0){
					sumOfPop = sumOfPop + afterProcess[i-1][j];
				}
				if  (j -1 > 0){
					sumOfPop = sumOfPop + afterProcess[i][j-1];
				}
				sumOfPop = sumOfPop + beforeProcess[i][j];
				afterProcess[i][j] = sumOfPop;
			}
		}
	}
	
	public int queryProcess(int left, int right, int top, int bottom){
		return afterProcess[right][bottom] - afterProcess[left-1][bottom] - afterProcess[right][top-1] + afterProcess[left-1][top-1];
	}
	
	//get minLat
	public float getMinLat() {
		return minLat;
	}
	
	//get minLon
	public float getMinLon() {
		return minLon;
	}
	
	//get maxLat
	public float getMaxLat() {
		return maxLat;
	}
	
	//get minLon
	public float getMaxLon() {
		return maxLon;
	}
	
	// get data size
	public int getData_size() {
		return data_size;
	}
	
	// get the data array
	public CensusGroup[] getData() {
		return data;
	}
	
	public int getQueryPop() {
		return queryPop;
	}


	public void setQueryPop(int queryPop) {
		this.queryPop = queryPop;
	}


	public int getTotalPop() {
		return totalPop;
	}


	public void setTotalPop(int totalPop) {
		this.totalPop = totalPop;
	}
}
