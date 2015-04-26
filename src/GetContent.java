import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

public class GetContent {
	public static void main(String[] args) throws IOException{
		System.setProperty("jsse.enableSNIExtension", "false");
		ContentFunc func = new ContentFunc() ; 
		AvlImplementation imp = new AvlImplementation() ; 
		File f = new File ("2722_rel.txt") ; 
	    if (!f.exists())
	        f.createNewFile() ; 
	    FileReader fr = new FileReader(f.getAbsoluteFile()) ; 
	    BufferedReader br = new BufferedReader(fr) ; 
	    BufferedReader b ; 
	    AvlTree avl = new AvlTree(null) ; 
	    Status s ; 
	    String line , l  ; 
	    Url u ; 
	    String[] array ; 
	    ExecutorService cachedPool = Executors.newCachedThreadPool();
	    ThreadPoolExecutor pool = (ThreadPoolExecutor) cachedPool;
	    URL url  ; 
	    int i = 0 ; 
	    while ( (line = br.readLine()) != null ){
	    	if ( line.contains("http") || line.contains("https")){
	    		//System.out.println(line) ; 
	    		u = new Url() ; 
	    		u.content = new String() ; 
	    		array = line.split(" +") ; 
	    		if ( array.length == 2 ){
	    			u.link = array[0] ; 
	    			u.title = array[1] ; 
	    		}
	    		if ( array.length >= 3 ){
	    			u.link = array[0] ; 
	    			u.title = array[2] ; 
	    		}
	    		try { 
	    			if ( func.isInDomain(u.link)){
			    		url = new URL(u.link) ;
						InputStream is = url.openStream();
						b = new BufferedReader(new InputStreamReader(is)); 
						u.content = new String("") ; 
						System.out.println(u.link + " " + i++ ) ; 
						while ( (l = b.readLine())!= null){
							u.content += func.Content(l) ; 
	 					}
						//System.out.println(u.content + " 1 ") ; 
						s = imp.findElement(avl.head, u , true ) ; 
						if ( !s.present){
							imp.Insert(s.InsOrDel, u, avl) ; 
						}
						
		    		}
	    		}
	    		catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//return ; 
				} catch (SocketException e) {
	                e.printStackTrace();
	                //return ; 
				}catch (SSLHandshakeException e ){
					e.printStackTrace() ; 
					//return ; 
				}catch (SSLException g){
					g.printStackTrace() ; 
					//return  ;
				}
			    catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    			
	    	}
	    }
	    FileOutputStream fileOut  ; ObjectOutputStream ObOut ; 
	  //  long time = System.currentTimeMillis();  
		 /*  while (!(pool.getActiveCount() == 0 )) {
			  if ( ( System.currentTimeMillis() - time  )/ 1000 == 200 ){
		    		fileOut = new FileOutputStream("abs.ser") ; 
		 	        ObOut = new ObjectOutputStream(fileOut) ; 
		 	        ObOut.writeObject(avl) ; 
		 	        ObOut.close() ; 
		 	        fileOut.close() ; 
		 	        break ; 	
		    	}
		        System.out.println(pool.getActiveCount() + " " + pool.getLargestPoolSize() + " " + pool.getPoolSize() + " " +  ((( System.currentTimeMillis() - time))/ 1000)) ;
		    }*/
		    fileOut = new FileOutputStream("rel.ser") ; 
	        ObOut = new ObjectOutputStream(fileOut) ; 
	        ObOut.writeObject(avl) ; 
	        ObOut.close() ; 
	        fileOut.close() ;
	        br.close() ; fr.close(); 
		return ; 
	}
	
	
}
