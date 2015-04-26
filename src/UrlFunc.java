
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.util.Iterator ; 
public class UrlFunc {
	
	static AvlImplementation imp = new AvlImplementation() ; 
	private Object lock = new Object() ; 
	boolean isValidURL(String pUrl) {
        URL u = null;
        try {
            u = new URL(pUrl);
        } catch (MalformedURLException e) {
        	//e.printStackTrace() ; 
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
        	//e.printStackTrace() ; 
            return false;
        }
       
        if ( isUrlKnown(u))
        	return true;
        return false ;
 }
   boolean isInDomain(String str){
		String s[] = { "google" , "twitter" , "reddit" , "facebook" , "linkedin" } ; 
		for ( int i = 0 ; i < s.length ; i++ ) 
			if ( str.toLowerCase().contains(s[i]))
				return false  ;
		return true ; 
	}

	static boolean isUrlKnown ( URL u ){
		System.setProperty("jsse.enableSNIExtension", "false");
		HttpURLConnection con;
		int status ; 
		try{
			con = (HttpURLConnection) u.openConnection() ; 
			status = con.getResponseCode() ;
			if ( status == 404 )
				return false ; 
			else return true ; 
		}
		catch (java.net.UnknownHostException e) {
		     return false ; 
		}
		catch (Exception e) {
			return false ; 
		}
		
	}
	
	void Urls ( Url url , String line , ArrayList<Url> abs , AvlTree avl_abs , AvlTree avl_rel ,ExecutorService cachedPool) throws IOException 
	{
		Future f ; 
		String a , b = null;
		boolean titleEmpty; 
		int p , q = 0 , num ;
		Status status ; 
		while ( line.contains("href")){ 
			titleEmpty = false ; 
			Url u = new Url() ; 
			p = line.indexOf("href") ; 
			a = line.substring(p) ; 
			p = a.indexOf('"') + 1  ; 
			while ( p < a.length() -1 && a.charAt(p) != '"')
				u.link += a.charAt(p++); 
			if ( line.contains("title=")){
				q = line.indexOf("title=") ; 
				b = line.substring(q) ;
				q = b.indexOf('"') + 1 ; 
				while ( q < b.length() && b.charAt(q) != '"' )
					u.title += b.charAt(q++) ;
				if ( u.title.isEmpty())
					titleEmpty = true ; 
			}
			if ( !line.contains("title=") || titleEmpty ){
				q = a.indexOf('>') + 1 ;
				if ( q < a.length() ){
					if ( a.contains("<blink>"))
						q = ignore ( a , "<blink>" ) ; 
					if ( a.contains("<b>"))
						q = ignore ( a , "<b>" ) ; 
					if ( q < a.length() )
						while ( a.charAt(q) != '<' && q < a.length() -1 )
							u.title += a.charAt(q++) ;
				}
				b = line ; 
			}
			if ( q < b.length() )
				line = b.substring(q) ; 
			else
				line = " " ;  
			if ( u.link.length() >= 1 && u.link.charAt(0)=='h' && !u.title.isEmpty() && !u.link.contains("/hi") ) {
				if ( isValidURL(u.link)){
					synchronized(avl_abs){
						status = imp.findElement( avl_abs.head, u, true ) ;
					if ( !status.present ){
						imp.Insert(status.InsOrDel,u,avl_abs);
						abs.add(u) ; 
					}	
				}
				}	
			}
			else{
				if ( !u.title.isEmpty() && !u.link.contains("/hi") ){
					synchronized(avl_abs){
						status = imp.findElement( avl_rel.head, u, true ) ;	
					if ( !status.present  ){
						imp.Insert(status.InsOrDel,u,avl_rel);
						cachedPool.execute(new UrlRunnable(url, u, abs, avl_abs, avl_rel,cachedPool) ) ; 
					}
				}
				}
			}
	}
}
	
	static int ignore ( String a , String sub ){
		int p ; 
		p = a.indexOf(sub) ;
		a = a.substring(p) ; 
		p = a.indexOf('>') + 1 ; 
		return p  ; 
	}
		
	static String appendlink ( String parent , String link ){
		if ( ! link.isEmpty() ){
			if ( link.charAt(0) != 'h' ){
				if ( parent.charAt(parent.length()-1) == '/'){
					if ( link.charAt(0) == '/'){
						link = link.substring(1) ;
						link = parent.concat(link) ;
						return link ; 
					}
					else{
						link = parent.concat(link) ; 
						return link ; 
					}
				}	
				else
					return parent.concat(link) ; 
			}
			else
				return link ; 
		}
		else
			return link ; 
	}
	void printArray ( ArrayList<Url> al){
		for(int i = 0 ; i < al.size() ; i++ )
			System.out.println(al.get(i).link) ; 
	}	
}
