import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class GetUrls{

	static UrlFunc f = new UrlFunc() ; 
	static AvlImplementation imp = new AvlImplementation() ; 
	
	public void Get( Url url  , ArrayList<Url> abs , AvlTree avl_abs ,ExecutorService cachedPool) throws IOException, InterruptedException{	

		AvlTree avl_rel = new AvlTree(null) ; 
		URL u = new URL(url.link) ; 
		InputStream is = u.openStream();
		String line ;
		BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
		
		url.content = new String("") ; 
		while ( (line  = br.readLine()) != null  ){
			f.Urls(url,line, abs, avl_abs, avl_rel,cachedPool) ;
		}
		/*
		for ( int i=0 ; i<rel.size();i++){
			num = rel.size()-i ; 
			al = new ArrayList<Thread>() ; 
			ul = new ArrayList<Url>() ; 
			for(int l = 0 ; l < num ; l++ ){
				a = rel.get(i++) ; 
				t = new Thread(new Relative( url, a, abs , rel, avl_abs, avl_rel, bw,i)) ; 
				t.start() ; 
				al.add(t) ;
				ul.add(a) ; 
			}
			for(int l = 0 ; l < num ; l++ ){
				t = al.get(l) ; 
				t.join() ; 
				System.out.println(abs.size() + " " + rel.size() + " " + imp.Size(avl_rel.head) + " " + imp.Size(avl_abs.head)) ;  
				System.out.println(l + " " + num ) ; 
			}
			System.out.println(abs.size() + " " + rel.size() + " " + imp.Size(avl_rel.head) + " " + imp.Size(avl_abs.head)) ;  
			System.out.println(i) ; 
			
		}*/
	}
}
