package specification;

public class LocationBox {
	
	public Location startLocation;
	public int w,h;
	
	public LocationBox(Location startLocation, int h, int w)
	{
		this.startLocation = startLocation;
		this.h= h;
		this.w = w;
	}
	
	public boolean isLocationInBox(Location testLocation)
	{
		return testLocation.x >= this.startLocation.x 
			&& testLocation.x <= this.startLocation.x + this.w
			&& testLocation.y >= this.startLocation.y 
			&& testLocation.y <= this.startLocation.y + this.h;
	}
	

	@Override
	public String toString() {
		return this.startLocation + ", h = " + h + ", w = " + w;
	}

}
