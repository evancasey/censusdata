package census;

/** A class to represent a Rectangle
 *  You do not have to use this, but it's quite convenient
 *  
 *  
 *  @author Joe Newbry and Evan Casey
 *  @version 4/7/2013
 */
 
public class Rectangle {
    // invariant: right >= left and top >= bottom (i.e., numbers get bigger as you move up/right)
    // note in our census data longitude "West" is a negative number which nicely matches bigger-to-the-right
	private float left;
	private float right;
	private float top;
	private float bottom;
	
	public Rectangle(float l, float r, float t, float b) {
		left   = l;
		right  = r;
		top    = t;
		bottom = b;
	}
	
	//Find the span of the latitudes and longitudes
	public static Rectangle makeOneRec (CensusData data, CensusGroup group, float xBucket, float yBucket) {
		float xLength = data.getMaxLat() - data.getMinLat();
		float yLength = data.getMaxLon() - data.getMinLon();
		
		//Find latitude and longitude lengths of each rectangle partition
		float xRecDim = (float) ((float) (xLength/xBucket) + .0001);
		
		float yRecDim = (float) ((float) (yLength/yBucket) + .0001);
		
		//Calculate the rectangle coordinates of our CensusGroup
		int xLoc = 1;
		int yLoc = 1;	
		if (xRecDim > 0.0) {
			xLoc = (int) ((group.getLatitude()-data.getMinLat())/xRecDim + 1);
			
		}
		if (yRecDim > 0.0) {
			yLoc = (int) ((group.getLongitude()-data.getMinLon())/yRecDim + 1);
		}
		
		return new Rectangle(xLoc, xLoc + 1, yLoc, yLoc + 1);
	}
	
	// a functional operation: returns a new Rectangle that is the largeest rectangle
	// containing this and that
	public Rectangle encompass(Rectangle that) {
		return new Rectangle(Math.min(this.left,   that.left),
						     Math.max(this.right,  that.right),             
						     Math.max(this.top,    that.top),
				             Math.min(this.bottom, that.bottom));
	}
	
	
	// returns whether or not a rectangle is contained in anthor rectangle (largeRec)
	public Boolean isContained(Rectangle largeRec) {
		if (largeRec.getLeft() <= this.getLeft() && largeRec.getRight() >= this.getRight()
				&& largeRec.getTop() <= this.getTop() && largeRec.getBottom() >= this.getBottom())
		{
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "[left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + "]";
	}
	
	public float getLeft() {
		return left;
	}
	
	public float getRight() {
		return right;
	}
	
	public float getTop() {
		return top;
	}
	
	public float getBottom() {
		return bottom;
	}
}
