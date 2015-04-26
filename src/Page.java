package proj;

import java.util.*;

public class Page implements Comparable<Page>{
	String url;
	String title;
	boolean is_in_title;
	int title_freq;
	boolean is_in_url;
	int url_freq;
	int freq;
	
	Page(){
		url = "";
		is_in_title = false;
		is_in_url = false;
		url_freq = 0;
		freq = 0;
		title_freq = 0;
	}
	@Override
	public int compareTo(Page o1) {
		// TODO Auto-generated method stub
		if(o1.is_in_title == this.is_in_title){
			if(o1.title_freq > this.title_freq) return 1;
			else if(o1.title_freq < this.title_freq) return -1;
			else if(o1.is_in_url == this.is_in_url){
				if(o1.url_freq > this.url_freq) return 1;
				else if(o1.url_freq < this.url_freq) return -1;
				else if(o1.freq > this.freq) return 1;
				else return -1;
			}
			else if(o1.is_in_url == true) return 1;
			else return -1;
		}
		else if(o1.is_in_title == true) return 1;
		else return -1;
	}
}
