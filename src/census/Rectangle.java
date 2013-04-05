package census;
/** A class to represent a Rectangle
 *  You do not have to use this, but it's quite convenient
 *  
 *  @author Joe Newbry and Evan Casey
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
	
	public static Rectangle makeOneRec (CensusData data, CensusGroup group, float xBucket, float yBucket) {
		//Find the span of the latitudes and longitudes
	
		float xLength = data.getMaxLat() - data.getMinLat();
		float yLength = data.getMinLon() - data.getMinLon();
		
		//Find latitude and longitude lengths of each rectangle partition
		float xRecDim = xLength/xBucket;
		float yRecDim = yLength/yBucket;
		
		//Calculate the rectangle coordinates of our CensusGroup
		float xLoc = (group.getLatitude()/xRecDim) + 1;
		float yLoc = (group.getLongitude()/yRecDim) + 1;
		
		System.out.print(xLoc + ", " + xLoc + 1 + ", " + yLoc + ", " + yLoc + 1);
		return new Rectangle(xLoc, xLoc + 1, yLoc, yLoc + 1);
	}
	
	// a functional operation: returns a new Rectangle that is the smallest rectangle
	// containing this and that
	public Rectangle encompass(Rectangle that) {
		return new Rectangle(Math.min(this.left,   that.left),
						     Math.max(this.right,  that.right),             
						     Math.max(this.top,    that.top),
				             Math.min(this.bottom, that.bottom));
	}
	
	public String toString() {
		return "[left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + "]";
	}
}
