package data;

public class Pair {
	String first;
	String second;
	
	public Pair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public boolean equalsPair(String first, String second) {
		if((first.equals(this.first) && second.equals(this.second)) ||
			(first.equals(this.second) && second.equals(this.first))) {
			return true;
		}
		return false;
	}
	
	public String getPair() {
		return first + " + " + second;
	}
}
