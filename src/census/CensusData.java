package census;
/** 
 *  just a resizing array for holding the input
 *  
 *  note: array may not be full; see data_size field
 *  
 *  @author: Joe Newbry and Evan Casey
 */

public class CensusData {
	private static final int INITIAL_SIZE = 100;
	private CensusGroup[] data;


	private int data_size;
	
	
	
	


	private float minLon;
	private float minLat;
	private float maxLon;
	private float maxLat;
	
	public CensusData() {
		data = new CensusGroup[INITIAL_SIZE];
		data_size = 0;
		minLon = 100;
		minLat = 100;
		maxLon = 0;
		maxLat = 0;
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
		findEdges();
	}
	
	public void findEdges() {
		//set minLat
		minLat = data[0].getLatitude();
		for(int i = 1; i < data_size; i++) {

		}
		
		//set minLon
		minLon = data[0].getLongitude();
		for(int i = 1; i < data_size; i++) {

		}
		
		//set maxLat
		maxLat = data[0].getLatitude();
		for(int i = 1; i < data_size; i++) {

		}
		
		//set maxLon
		maxLon = data[0].getLongitude();
		for(int i = 1; i < data_size; i++) {

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
