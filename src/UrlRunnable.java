import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.concurrent.ThreadPoolExecutor;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

public class UrlRunnable implements Runnable {
	//private static Object lock = new Object()  ; 
	final Url rel ; 
	final Url url ; 
	final ArrayList<Url> absolute ; 
	final AvlTree avl_abs ; 
	final AvlTree avl_rel ; 
	final ExecutorService cachedPool ;  
	UrlRunnable (Url url , Url rel , ArrayList<Url> absolute , AvlTree avl_abs , AvlTree avl_rel ,
			ExecutorService cachedPool){
			this.rel = rel ; 
			this.url = url ; 
			this.absolute = absolute ; 
			this.avl_rel = avl_rel ; 
			this.avl_abs = avl_abs; 
			this.cachedPool = cachedPool ; 
	}	
		static UrlFunc f = new UrlFunc() ; 
		static AvlImplementation imp = new AvlImplementation() ; 
		@Override
		public void run() {
			// TODO Auto-generated method stub	
		try {
			   System.setProperty("jsse.enableSNIExtension", "false"); 
			   ThreadPoolExecutor pool = (ThreadPoolExecutor) cachedPool;
				BufferedReader br ;
				Url ur = new Url(new String(rel.title) , new String(rel.link));
				String r = ur.link ;
				r = f.appendlink ( url.link , r) ; 
				ur.link = r ; 
				Status status ;
				URL u ; 
				String line ;
				synchronized(avl_abs){
					status = imp.findElement(avl_abs.head, ur , true ) ; 
					if ( !status.present )
						imp.Insert(status.InsOrDel, ur, avl_abs) ; 
				}
				if (f.isValidURL(r) && !r.contains("/hi/")&& !r.contains("Border") && f.isInDomain(r)){	
					if ( !r.contains(".pdf") && !r.contains(".docx") ){
						System.out.println(ur.link + " " +  " \n");
						u = new URL(r) ; 
						InputStream is = u.openStream();
						br = new BufferedReader(new InputStreamReader(is)); 
						ur.content = new String("") ; 
						while ( (line  = br.readLine()) != null  ){
							//System.out.println("Work going on") ; 
							f.Urls(url,line,absolute,avl_abs,avl_rel,cachedPool) ;
							//ur.content += f.Content(line) ; 
					}
						//System.out.println(ur.content+"\n") ; 
				}
			}
		}
		catch ( SocketException e ){
				//e.printStackTrace() ; 
				return ; 
			}
		catch (SSLHandshakeException e ){
			//e.printStackTrace() ; 
			return ; 
		}
		catch (SSLException s){
			//s.printStackTrace() ; 
			return  ;
		}
		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 		
		}
	}