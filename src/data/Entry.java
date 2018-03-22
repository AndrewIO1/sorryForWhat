package data;

import java.util.Vector;

public class Entry {
	private Vector<String> trackList;
	
	public Entry() {
		trackList = new Vector<String>();
	}
	
	public void addTrack(String track) {
		if(trackList.contains(track)) {
			System.out.println("Error: track already exists");
			return;
		}
		trackList.add(track);
	}
	
	public String get(int position) {
		if(position < 0 || position >= trackList.size()) {
			System.out.println("Error: out of bounds");
			return "Error";
		}
		return trackList.get(position);
	}
	
	public int getPosition(String track) {
		if(!trackList.contains(track)) {
			System.out.println("Error: track with this name doesn't exist");
			return -1;
		}
		
		return trackList.indexOf(track);
	}
}
