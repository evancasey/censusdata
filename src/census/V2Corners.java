package census;
import java.util.concurrent.*;

/**
 * Class uses parallelism to compute the min and max of latitude and longitudes for an
 * array with each object in the array containing a CensusGroup which has a lat and long.
 *
 * In addition the population contained in each CensusGroup point in the array 
 * contained in the search querry can be found using efficient threading.
 * 
 * @author Joe Newbry and Evan Cases
 * @version 4/7/2012
 *
 */
public class V2Corners extends RecursiveAction {
	
	private static final int SEQUENTIAL_CUTOFF = 50;
	int lo; // used when slicing up the data onto different threads
	int hi; 
	
	float minLat = 100; //minLat should be below this and positive
	float minLon = 100; //maxLon should be above this and negative
	float maxLat = -100; //maxLon should be above this and positive
	float maxLon = -100; //maxLon should be above this and negative
	CensusGroup[] data; // the array of censusData that we're tying to search through
	
	
	// constructor for V2Corners 
	public V2Corners(int l, int h, CensusGroup[] data){
		lo = l;
		hi = h;
		this.data = data;
	}
	
	// recursively splits the data points until SEQUENTIAL_CUTOFF is hit
	// then find the min and max latitudes and longitudes for each portion
	// build from the smallers sections by comparing the min and maxes for each part
	protected void compute() {
		if ((hi - lo) < SEQUENTIAL_CUTOFF) {
			this.findEdgesSeq(lo, hi);
		} else {
			int mid = (hi+lo)/2;
			V2Corners left = new V2Corners(lo, mid,data);
			V2Corners right = new V2Corners(mid, hi, data);
			left.fork();
			right.compute();
			left.join();
			minLat = Math.min(left.minLat, right.minLat);
			minLon = Math.min(left.minLon, right.minLon);
			maxLat = Math.max(left.maxLat, right.maxLat);
			maxLon = Math.max(left.maxLon, right.maxLon);
		}
	}
	
	// basic sequential comparison to determine min and maxes
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
		System.out.println("changing values" + minLat + minLon);
	}
	
	public float getMinLat() {
		return minLat;
	}

	public void setMinLat(float minLat) {
		this.minLat = minLat;
	}

	public float getMinLon() {
		return minLon;
	}

	public void setMinLon(float minLon) {
		this.minLon = minLon;
	}

	public float getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(float maxLat) {
		this.maxLat = maxLat;
	}

	public float getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(float maxLon) {
		this.maxLon = maxLon;
	}
	
	
	
	
	
	
	
}
