package proj;

import java.io.*;
import java.util.*;

public class Rank_Algorithm {
	PriorityQueue<Page> rank = new PriorityQueue<Page>();
	ArrayList<String> common_words;
	
	Rank_Algorithm() {
		get_common_words();
	}
	
	public void get_common_words(){
		common_words = new ArrayList<String>();
		String str;
		try {
			BufferedReader in = new BufferedReader(new FileReader("common.txt"));
			while((str = in.readLine()) != null) {
				common_words.add(str.toLowerCase());
			}
		} 
		catch (IOException e) {
			System.out.println("Cannot open the file common.txt");
		}
	}
	public void rank_pages(Url arr, String query) {
		Page p;
		p = url_to_page(arr, query);
		rank.add(p);
		//System.out.printf("%s ", p.url);
	}
	
	public Page url_to_page(Url u, String query){
		Page p = new Page();
		
		int t = 0, ur = 0, f = 0, cf;
		
		String title_words[] = u.title.split(" +");
		String content_words[] = u.content.split(" +");
		String query_words[] = query.split(" +");
		
		ArrayList<String> t_words = new ArrayList<String>(Arrays.asList(title_words));
		ArrayList<String> c_words = new ArrayList<String>(Arrays.asList(content_words));
		ArrayList<String> q_words = new ArrayList<String>(Arrays.asList(query_words));
		
		
		for(int i = 0; i < title_words.length; i++){
			String temp = clean1(title_words[i]);
			title_words[i] = temp;
			ArrayList<String> x = clean2(title_words[i]);
			for(int j = 0; j < x.size(); j++){
				t_words.add(x.get(j).toLowerCase());
			}
		}
		
		for(int i = 0; i < content_words.length; i++){
			String temp = clean1(content_words[i]);
			content_words[i] = temp;
			ArrayList<String> x = clean2(content_words[i]);
			for(int j = 0; j < x.size(); j++){
				c_words.add(x.get(j).toLowerCase());
			}
		}
		
		for(int i = 0; i < query_words.length; i++){
			String temp = clean1(query_words[i]);
			query_words[i] = temp;
			ArrayList<String> x = clean2(query_words[i]);
			for(int j = 0; j < x.size(); j++){
				q_words.add(x.get(j).toLowerCase());
			}
		}
		
		AVL_Words main_words = new AVL_Words();
		AVL_Words main_t_words = new AVL_Words();
		AVL_Words com_words = new AVL_Words();

		

/*		for(int i = 0; i < q_words.size(); i++){
			if(!c_words.contains(q_words.get(i))){
				break;
			}
			else return null;
		}
		*/
		for(int i = 0; i < t_words.size(); i++){
			t_words.set(i, t_words.get(i).toLowerCase());
			if(!common_words.contains(t_words.get(i))){
				main_t_words.increase_key(t_words.get(i), main_t_words);
			}
		}
		for(int i = 0; i < c_words.size(); i++){
			c_words.set(i, c_words.get(i).toLowerCase());
			if(c_words.get(i) == "kamakoti"){
				System.out.printf("%s", c_words.get(i));
			}
			if(!common_words.contains(c_words.get(i).toLowerCase())){
				main_words.increase_key(c_words.get(i), main_words);
			}
		}
		for(int i = 0; i < q_words.size(); i++){
			if(!common_words.contains(q_words.get(i))){
				if(main_t_words.root != null ) t += main_t_words.search(q_words.get(i));
				if(main_words.root != null ) {
					f += main_words.search(q_words.get(i));
				}
				if(u.link.contains(q_words.get(i))) ur++;
			}
		}
		
		p.freq = f;
		p.title_freq = t;
		p.url_freq = ur;
		if(t > 0) p.is_in_title = true;
		if(ur > 0) p.is_in_url = true;
		p.url = u.link;
		p.title = u.title;
		if(u.content.contains("Kamakoti")) {
			for(int i = 0; i < c_words.size(); i++){
				//System.out.println(c_words.get(i));
			}
		}

		return p;
	}
	public static String clean1(String string) {
		// TODO Auto-generated method stub
		if(!Character.isLetter(string.charAt(string.length() - 1))){
			String str = "";
			for(int i = 0; i < string.length() - 1; i++){
				str += string.charAt(i);
			}
			return str;
		}
		return string;
	}
	
	public static ArrayList<String> clean2(String string){
		ArrayList<String> arr = new ArrayList<String>();
		String temp = "";
		for(int i = 0; i < string.length(); i++){
			if(Character.isLetter(string.charAt(i))){
				temp += string.charAt(i);
			}
			else{
				arr.add(temp);
				temp = "";
			}
		}
		if(temp != "") arr.add(temp);
		int flag = 0;
		int len = arr.size();
		for(int i = 0; i < len; i++){
			String x = arr.get(i);			
			temp = "";
			for(int j = 0; j < x.length(); j++){
				if(Character.isUpperCase(x.charAt(j))){
					if(flag == 0){
						if(temp != "") arr.add(temp);
						temp = "";
						temp += x.charAt(j);
						flag = 1;
					}
					else if(flag == 1){
						temp += x.charAt(j);
					}
				}
				else{
					if(flag == 0){
						temp += x.charAt(j);
					}
					else{
						if(temp.length() == 1) {
							temp += x.charAt(j);
							flag = 0;
						}
						else {
							arr.add(temp);
							temp = "";
							temp += x.charAt(j);
							flag = 0;
						}
					}
				}
			}
			if(temp != "") arr.add(temp);
		}
		return arr;
	}
}
