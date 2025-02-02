package census;

/**
 * class that uses Mercator project to convert coordinates
 * on a sphere into coordinates onto a flat plane
 * 
 * @author Joe Newbry and Evan Casey
 * @version 4/7/2013
 */

public class CensusGroup {
	private int   population;
	private float realLatitude;
	private float latitude;
	private float longitude;
	
	public CensusGroup(int pop, float lat, float lon) {
		population = pop;
		realLatitude = lat;
		latitude   = mercatorConversion(lat); 
		longitude  = lon;
	}
	
	private float mercatorConversion(float lat){
		float latpi = (float)(lat * Math.PI / 180);
		float x = (float)Math.log(Math.tan(latpi) + 1 / Math.cos(latpi));
		return x;
	}
	
	public int getPopulation() {
	    return population;
	}
	
	public float getRealLatitude() {
	    return realLatitude;
	}
	
	public float getLatitude() {
	    return latitude;
	}
	
	public float getLongitude() {
	    return longitude;
	}
}
