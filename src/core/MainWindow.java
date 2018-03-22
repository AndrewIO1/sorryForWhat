package core;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Entry;
import data.Pair;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private static MainWindow window;
	private static Vector<String> tracks;
	private static Vector<String> pair;
	private static Vector<String> result;
	private static ArrayList<Entry> entries;
	private static final String windowTitle = "TRACK LIST"; 
	private static JList<String> resultList;
	private static JList<String> pairList;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window = new MainWindow(windowTitle);
			}
		});
	}

	public MainWindow(String title) {
		super(title);
		initComponents();
	}

	private void initComponents() {
		this.setSize(700, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainWindow.loadEntries("list.txt");
		calculatePairRating();

		JPanel grid = new JPanel();
		this.getContentPane().add(grid);

		GridLayout layout = new GridLayout(1, 2, 5, 5);
		grid.setLayout(layout);
		
		pairList = new JList<String>(pair);
		JScrollPane scroll1 = new JScrollPane();
		scroll1.setViewportView(pairList);
		scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		grid.add(scroll1);

		result = new Vector<String>();

		resultList = new JList<String>(result);
		resultList.setPreferredSize(new Dimension(200,300));
		JScrollPane scroll2 = new JScrollPane();
		scroll2.setViewportView(resultList);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		grid.add(scroll2);

		JList<String> trackList = new JList<String>(tracks);
		trackList.setPreferredSize(new Dimension(200,300));
		trackList.addListSelectionListener(new ListSelectionListener() {
			private String selected = "";
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String selected = ((JList<String>)e.getSource()).getSelectedValue();
				if(this.selected.equals(selected)) {
					return;
				}
				this.selected = selected;
				MainWindow.calculateTrackInfo(selected);
			}

		});
		JScrollPane scroll3 = new JScrollPane();
		scroll3.setViewportView(trackList);
		scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		grid.add(scroll3);

	}

	protected static void loadEntries(String fileName) {
		entries = new ArrayList<Entry>();
		tracks = new Vector<String>();
		try
		{
			BufferedReader file= new BufferedReader(new FileReader(fileName));
			String temp;
			int listNum = 0;
			Entry newEntry = new Entry();
			while((temp = file.readLine()) != null){
				temp = temp.trim();
				if(temp.equals("#&#")) {
					listNum++;
					entries.add(newEntry);
					newEntry = new Entry();
					continue;
				}
				if(listNum == 0) {
					tracks.add(temp);
				}else {
					if(!tracks.contains(temp)) {
						System.out.println("Error: wrong input, check tracklists in list.txt");
						tracks.clear();
						entries.clear();
						break;
					}
				}
				newEntry.addTrack(temp);
			}
			listNum++;
			entries.add(newEntry);
			file.close();
		}
		catch(Exception e)
		{
			System.out.println("Файл с таким именем не существует!");
		}
	}

	protected static void calculateTrackInfo(String track) {
		if(entries == null || entries.size() == 0) {
			System.out.println("Error: entry list is empty");
			return;
		}
		result.clear();
		int[] votes = new int[tracks.size()];
		for(int i = 0; i < tracks.size(); i++) {
			votes[i] = 0;
		}
		for(Entry e: entries) {
			votes[e.getPosition(track)]++;
		}
		for(int i = 0; i < tracks.size(); i++) {
			double percent = ((int)(((double)votes[i])/entries.size()*10000))/100.;
			result.add(String.valueOf(i+1) + " место: " + percent + "%");
		}
		resultList.setListData(result);
	}
	
	protected static void calculatePairRating() {
		if(entries == null || entries.size() == 0) {
			System.out.println("Error: entry list is empty");
			return;
		}
		pair = new Vector<String>();
		int pairAmount = factorial(tracks.size())/(2*factorial(tracks.size()-2));
		int[] votes = new int[pairAmount];
		Pair[] pairs = new Pair[pairAmount];
		int num = 0;
		for(int i = 0; i < tracks.size()-1; i++) {
			for(int j = i+1; j < tracks.size(); j++) {
				Pair temp = new Pair(entries.get(0).get(i), entries.get(0).get(j));
				pairs[num++] = temp;
			}
		}
		
		for(int i = 0; i < entries.size(); i++) {
			for(int j = 0; j < tracks.size()-1; j++) {
				for(int k = 0; k < pairAmount; k++) {
					if(pairs[k].equalsPair(entries.get(i).get(j), entries.get(i).get(j+1))) {
						votes[k]++;
					}
				}
			}
		}
		
		for(int i = 0; i < pairAmount-1; i++) {
			for(int j = i+1; j < pairAmount; j++) {
				if(votes[j] > votes[i]) {
					int tempVotes = votes[j];
					Pair tempPair = pairs[j];
					votes[j] = votes[i];
					pairs[j] = pairs[i];
					votes[i] = tempVotes;
					pairs[i] = tempPair;
					
				}
			}
		}
		
		for(int i = 0; i < pairAmount; i++) {
			pair.add(pairs[i].getPair() + ": " + votes[i]);
		}
	}
	
	private static int factorial(int n)
    {
		if(n <= 0) return 0;
        int ret = 1;
        for (int i = 1; i <= n; ++i) ret *= i;
        return ret;
    }

}
