import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public class SearchEngine {
    static String par = "https://www.iitm.ac.in/" ; 
    static Url ul = new Url (new String("Academics") , new String(par) ) ; 
    static AvlTree avl = new AvlTree(ul) ;
    static AvlImplementation imp = new AvlImplementation() ; 
    static GetUrls get = new GetUrls() ; 
	public static synchronized void main(String[] args) throws MalformedURLException, InterruptedException, ExecutionException {
	    InputStream is = null;
	    System.setProperty("jsse.enableSNIExtension", "false");
	    try {
	        ArrayList<Url> urls = new ArrayList<Url>() ;
	        urls.add(ul) ;
	       /* 
	        File f3 = new File ("Urls.txt") ; 
		    if ( !f3.exists())
		        f3.createNewFile() ; 
		    FileWriter fw3 = new FileWriter(f3.getAbsoluteFile()) ; 
		    */
	       ExecutorService cachedPool = Executors.newCachedThreadPool();
	       ThreadPoolExecutor pool = (ThreadPoolExecutor) cachedPool;
	       // BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(5);
	       //MyThreadPoolExecutor pool = new MyThreadPoolExecutor( 4 , 3000 , 5000, TimeUnit.MILLISECONDS, blockingQueue); 
	       //MyThreadPoolExecutor myPool = (MyThreadPoolExecutor) cachedPool ;     
	       //ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 5000, TimeUnit.MILLISECONDS, null) ; 
	       get.Get(urls.get(0), urls, avl,cachedPool) ; 
	      // pool.setKeepAliveTime(5000,TimeUnit.MILLISECONDS) ; 
	       FileOutputStream fileOut  ; ObjectOutputStream ObOut ; 
	       //pool.setCorePoolSize(3); 
	       //int prev = 0 , current = 0 ; 
	       long time = System.currentTimeMillis();  
		   while (!(pool.getActiveCount() == 0 )) {
		    	/*current = pool.getActiveCount() ; 
		    	 if ( current != prev ){
		    		time = System.currentTimeMillis(); 
		    		prev = current ; 
		    	 }*/
			  if ( ( System.currentTimeMillis() - time  )/ 1000 == 350 ){
		    		fileOut = new FileOutputStream("x.ser") ; 
		 	        ObOut = new ObjectOutputStream(fileOut) ; 
		 	        ObOut.writeObject(avl) ; 
		 	        ObOut.close() ; 
		 	        fileOut.close() ; 
		 	        break ; 	
		    	}
		        System.out.println(pool.getActiveCount() + " " + pool.getLargestPoolSize() + " " + pool.getPoolSize() + " " +  ((( System.currentTimeMillis() - time))/ 1000)) ;
		    }
		    System.out.println("") ; 
		    cachedPool.shutdownNow();
		    cachedPool.awaitTermination(10,TimeUnit.SECONDS) ; 
		    
 	       //Serialization of the avl tree ;  	
		   //int atomic = pool.getexecuting().get() ; 
		   //int prev = 0 , current = 0 ; 
	       //while( atomic != 0 ){
	       //System.out.println("Waiting for threads to complete " + atomic  ) ; 
	       // atomic = pool.getexecuting().get() ; 
	       //}
		    fileOut = new FileOutputStream("AvlTree.ser") ; 
	        ObOut = new ObjectOutputStream(fileOut) ; 
	        ObOut.writeObject(avl) ; 
	        ObOut.close() ; 
	        fileOut.close() ; 
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            return ; 
	        }
	    }
	}	
}