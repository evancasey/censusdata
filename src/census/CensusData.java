package census;
import java.util.Arrays;
import java.util.concurrent.*;
/** 
 *  just a resizing array for holding the input
 *  
 *  note: array may not be full; see data_size field
 *  
 *  @author: Joe Newbry and Evan Casey
 */

public class CensusData extends RecursiveAction {
	private static final int INITIAL_SIZE = 100;
	private CensusGroup[] data;
	private int data_size;
	private float minLon;
	private float minLat;
	private float maxLon;
	private float maxLat;
	// instance variables (lo, high, left data result, right data result, for finding max and min using
	private int lo;
	private int hi;
	CensusGroup[] left;
	CensusGroup[] right;

	private int cutoffVal = 5;
	
	private static final ForkJoinPool fjPool = new ForkJoinPool();
	
	public CensusData() {
		data = new CensusGroup[INITIAL_SIZE];
		data_size = 0;
		minLat = 100; //minLat should be below this and positive
		minLon = 100; //maxLon should be above this and negative
		maxLat = -100; //maxLon should be above this and positive
		maxLon = -100; //maxLon should be above this and negative
	}
	
	public CensusData(CensusGroup[] group, int l, int h) {
		data = group;
		data_size = group.length;
		minLat = 100; //minLat should be below this and positive
		minLon = 100; //maxLon should be above this and negative
		maxLat = -100; //maxLon should be above this and positive
		maxLon = -100; //maxLon should be above this and negative
		lo = l;
		hi = h;
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
	
	public void findEdgesSeq() {
		for(int i=0; i < data_size; ++i) {
			System.out.println(i);
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
	
	public void findEdgesPar() {
		fjPool.invoke(new CensusData(data, 0, data_size));
	}
	
	@Override
	protected void compute() {
		if ((hi - lo) < cutoffVal) {
			this.findEdgesSeq();
		} else {
			int mid = data_size/2;
			CensusData left = new CensusData(Arrays.copyOfRange(data, lo, mid), lo, mid);
			CensusData right = new CensusData(Arrays.copyOfRange(data, mid, hi), mid, hi);
			left.fork();
			right.compute();
			left.join();
		}
		
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
}
