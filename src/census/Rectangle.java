package census;
/** A class to represent a Rectangle
 *  You do not have to use this, but it's quite convenient
 *  
 *  @author Your Name Here
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
	
	public Rectangle makeOneRec (CensusData data, CensusGroup group) {
		group.getLatitude();
		group.getLongitude();
		
		data.getMinLat();;
		data.getMinLon();
		return new Rectangle
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
