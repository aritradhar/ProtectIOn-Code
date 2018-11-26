package specification;

public class Location {
	
	public int x, y;

	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Location addWidth(int w)
	{
		return new Location(this.x + w, this.y);
	}
	
	public Location addHeight(int h)
	{
		return new Location(this.x, this.y + h);
	}
}
