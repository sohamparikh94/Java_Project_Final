import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
public class ReadSerialzedObject {
	static AvlImplementation imp = new AvlImplementation() ; 
	//long serialVersionUID = 1L ; 
	public static void main(String args[]) throws IOException{
		AvlTree avl = null ; 
		try{
			FileInputStream fileIn = new FileInputStream("abs.ser") ;
			System.out.println("Here") ; 
			ObjectInputStream in = new ObjectInputStream(fileIn) ; 
			avl = (AvlTree)in.readObject() ; 
			in.close() ; 
			fileIn.close() ; 
		}
		catch(IOException i ){
			i.printStackTrace() ; 
			return  ;
		}
		catch(ClassNotFoundException c){
			c.printStackTrace() ; 
			return ; 
		}
		File f = new File ("Content.txt") ; 
	    if ( !f.exists())
	        f.createNewFile() ; 
	    FileWriter fw = new FileWriter(f.getAbsoluteFile()) ; 
	    BufferedWriter bw = new BufferedWriter(fw) ; 
	    imp.Inorder(avl.head,bw);
	    bw.close() ; 
	    fw.close() ; 
		System.out.println(imp.Size(avl.head)); 
		return ; 
	}
	
}
