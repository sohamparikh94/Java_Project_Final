import java.io.BufferedWriter;

import java.io.Serializable; 
import java.io.IOException;
import java.util.Queue ; 
import java.util.LinkedList ; 

class Url implements Comparable<Url>, Serializable{
	private static final long serialVersionUID = 1L;
	public String title ; 
	public String link ; 
	public String content ; 
	
	public Url(){
		this.title = new String() ; 
		this.link = new String() ;
	}
	public Url(String s , String l ){
		this.title = s ; 
		this.link  = l ; 
	}
	@Override
	public int compareTo(Url that) {
		if ( sameUrl(this.link,that.link) ) 
			return 0 ; 
		return this.link.compareTo(that.link) ; 
	}
	private boolean sameUrl ( String s1 , String s2 ){
		if ( s1.contains(s2) )
			if ( s1.length() != 0 )
				return s1.charAt(s1.length()-1) == '/' ; 
		if ( s2.contains(s1))
			if ( s2.length() != 0 )
			return s2.charAt(s2.length()-1) == '/' ; 
		return false ; 
	}
}

class Bfs{
	Node node ; 
	int level ; 
}

class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	Node lChild  ; 
	Node rChild  ; 
	Node parent ; 
	Url key ; 
	int height ; 
}
class AvlTree implements Serializable {
	private static final long serialVersionUID = 1L;
	Node head  = null ;
	public AvlTree ( Url key ) {
		head = new Node() ; 
		head.parent = null ; 
		head.lChild = null ; 
		head.rChild = null ; 
		head.height = 1 ; 
		head.key = key ; 
	}
}
class Status {
	public Status(){
	}
	boolean present ; 
	Node InsOrDel ;	
}

public class AvlImplementation {	
	void Insert ( Node parent  , Url key , AvlTree avl ) throws IOException
	{
		if ( parent == null ){
			avl.head.key = key ;  
			return ; 
		}
		int order = key.compareTo(parent.key) ; 
		if ( order < 0  ){
			parent.lChild = NewNode ( key , parent ) ;
			Balance ( parent.lChild , avl  ) ; 
		}
		else{
			parent.rChild = NewNode ( key , parent ) ; 
			Balance ( parent.rChild , avl ) ; 
		}
	}
	
	void PrintBfs ( Node node , BufferedWriter bw  ) throws IOException{
		Queue<Bfs> q = new LinkedList<Bfs>() ; 
		Bfs bfs = new Bfs() , b  ; 
		bfs.node = node ; 
		bfs.level = 0 ;  // The level field indicates the distance from the root 
		int level = 0  ; 
		q.add(bfs) ; 
		while ( !q.isEmpty() ){
			b = (Bfs)q.poll() ; 
			if ( b.level == level )
				bw.write(b.node.key.link+"   ") ; 
			else{ // If we enter a different level 
				bw.write("\n#\n") ; 
				bw.write(b.node.key.link+"    ")  ;
				level = bfs.level ; 
			}
			if ( b.node.lChild != null ){
				bfs = new Bfs() ; 
				bfs.node = b.node.lChild ; 
				bfs.level = b.level + 1 ; 
				q.add(bfs) ; 
			}
			if ( b.node.rChild != null ){
				bfs = new Bfs() ; 
				bfs.node = b.node.rChild ; 
				bfs.level = b.level + 1 ;
				q.add(bfs) ; 
			}
		}
		
	}
	
	void Inorder ( Node node) throws IOException{
		if ( node.lChild != null )
			Inorder ( node.lChild) ;
		System.out.println(node.key.link) ; 
		if ( node.rChild != null ) 
			 Inorder ( node.rChild) ;
	}
	void Inorder ( Node node , BufferedWriter bw ) throws IOException{
		if ( node.lChild != null )
			Inorder ( node.lChild,bw) ;
		
		if ( !node.key.link.contains("http://www.iitm.ac.in/") && !node.key.link.contains("https://www.iitm.ac.in/")){
			System.out.println(node.key.link + " " + node.key.content + " " + node.key.title) ;
			bw.write(node.key.link + " " +  node.key.content + " " + node.key.title+"\n") ; 
		}
		if ( node.rChild != null ) 
			 Inorder ( node.rChild,bw) ;
	}
	int Size ( Node node ){
		int l = 0 , r = 0  ; 
		if ( node.lChild != null )
			l = Size ( node.lChild ) ;
		if ( node.rChild != null ) 
			r = Size ( node.rChild) ;
		return r+l+1 ; 
	}
	
	void Delete( Node n , Url key , AvlTree avl ) throws IOException{
		Url replace  ;
		boolean left = false ; 
		if ( n.parent != null ){
			if ( n.parent.key.compareTo(n.key) > 0   ) 
				left = true ; 
			else
				left = false ; 
		}
		if ( n.lChild != null ) {
			if ( n.lChild.rChild != null ){
				replace = predecessor ( n.lChild , avl )  ;
				n.key = replace ; // Because the actual deletion of node is done in the method predecessor , Balance is 
				// called there 
			}
			else{  // n.child is the predecessor 
				n.lChild.rChild = n.rChild ;
				if ( n.parent == null ){ // If the root is being deleted make n.lchild the new root 
					avl.head = n.lChild ; 
					n.lChild.parent = null ; 
					Balance ( avl.head , avl ) ; // To check imbalance
				}
				else{ // else depending on whether the node  being deleted is the left or right child of its parent 
					// attach n.lchild to the parent 
					if ( left ){
						n.parent.lChild = n.lChild ; 
						n.lChild.parent = n.parent  ; 
						Balance ( n.lChild , avl ) ; // To check imbalance
	
					}
					else{
						n.parent.rChild = n.lChild ; 
						n.lChild.parent = n.parent  ; 
						Balance ( n.lChild , avl ) ; // To check imbalance  
					}
				}
			}
		}
		else{ // If the node to be deleted doesn't have a left child
			if ( n.rChild != null ){ // Replace the node to be deleted with it's right child 
				if ( n.parent == null ){
					avl.head = n.rChild ; 
					Balance ( avl.head , avl ) ; // To check the imbalance 
					return ; 
				}
				else{
					if ( left ){
						n.parent.lChild = n.rChild ; 
						n.rChild.parent = n.parent ; 
					}
					else{
						n.parent.rChild = n.rChild ; 
						n.rChild.parent = n.parent ; 
					}		
				}
			} // If both children are null 
			else{
				if ( n.parent == null ){
					avl.head = null ; 
					return ; 
				}
				else{
					if ( left )
						n.parent.lChild = null ; 
					else
						n.parent.rChild = null ;	
				}
			}
			Balance ( n.parent , avl ) ; // To check the imbalance caused by the deletion 
		}
	}

	// It returns an instance of the class Status where it's two fields present and parent 
	// If the node is present then InsOrDel references to the node with the same key 
	// Else it references to the node where it has to be inserted 
	Status findElement ( Node n , Url key  , boolean root )
	{
		Status st = new Status() ; 
		if ( root ) // If it the root 
		{
			if ( n.key == null ){
				st.InsOrDel = null ; 
				st.present = false ; 
				return st ; 
			}
			if ( n.key.compareTo(key) ==0  ){
				st.InsOrDel = n  ; 
				st.present = true ; 
				return st ; 
			}		
		}
		
		if ( n.lChild != null ){ // If the key matches with the left child 
			if (n.lChild.key.compareTo(key) == 0   ){
				st.InsOrDel = n.lChild ; 
				st.present = true ; 
				return st ; 
			}	
		}
		if ( n.rChild != null ){ // If the key matches with the right child
			if ( n.rChild.key.compareTo(key) == 0  ){
				st.InsOrDel = n.rChild ; 
				st.present = true ; 
				return st ; 
			}
		}
		if ( key.compareTo(n.key) > 0   ){ 
			if ( n.rChild != null )  // If going further down the tree is possible 
				return findElement ( n.rChild , key , false ) ; 
			else{ // Else return 
				st.InsOrDel = n ; 
				st.present = false ; 
				return st ; 
			}
		}
		if ( key.compareTo(n.key) < 0 ){ // If going further down the tree is possible
			if ( n.lChild != null )
				return findElement ( n.lChild , key , false ) ; 
			else{
				st.InsOrDel = n ; 
				st.present = false ; 
				return st ; 
			}	
		}
		return st;
	}
	void Balance ( Node z , AvlTree avl ) throws IOException{
		Node p = z.parent , y ;
		int lh , rh  , yrh , ylh ; 
		if ( z.lChild == null )
			lh = 0 ; 
		else
			lh = z.lChild.height ; 
		if ( z.rChild == null )
			rh = 0 ; 
		else
			rh = z.rChild.height ;
		// lh and rh contain the heights of the left and right subtrees respectively 
		z.height = Math.max(lh, rh) + 1 ; 
		if ( lh - rh == 2 || rh - lh == 2 ){ // Since the difference between the heights of the left and right subtrees can be at most 1 
			//  when an imbalance occurs this difference cannot be more than 2 
			if ( lh - rh == 2 ){
				y = z.lChild ;  // y is the child of z with greater height 
				if ( y.lChild != null )
					ylh = y.lChild.height ; 
				else
					ylh = 0 ; 
				if ( y.rChild != null )
					yrh = y.rChild.height ; 
				else
					yrh = 0 ; // yrh and ylh are the heights of the right and left subtrees of y respectively 
				if ( ylh > yrh || ( ylh == yrh )  ){
					if ( p != null ){
						if ( p.key.compareTo(z.key) > 0  )
							p.lChild = ClkRot ( z , p  ) ; 
						else
							p.rChild = ClkRot ( z , p  ) ; 
					}
					else
						avl.head = ClkRot ( z , p ) ;
				}
				if ( yrh > ylh  ){ // If z , y  are on the same side , we require two rotations
					z.lChild = AntiClkRot ( y , z ) ;
					if ( p != null ){
						if ( p.key.compareTo(z.key) > 0)
							p.lChild = ClkRot ( z , p  ) ; 
						else
							p.rChild = ClkRot ( z , p  ) ; 
					}
					else
						avl.head = ClkRot ( z , p ) ; 
				}			
			}
			else{
				y = z.rChild ;
				if ( y.lChild != null )
					ylh = y.lChild.height ; 
				else
					ylh = 0 ; 
				if ( y.rChild != null )
					yrh = y.rChild.height ; 
				else
					yrh = 0 ; 
				if ( ylh > yrh  ){
					z.rChild = ClkRot ( y , z  ) ; 
					if (p != null ){
						if ( p.key.compareTo(z.key) > 0  )
							p.lChild = AntiClkRot ( z , p  ) ; 
						else
							p.rChild = AntiClkRot ( z , p  ) ; 
					}
					else
						avl.head = AntiClkRot ( z , p ) ; 
				}	
				if ( yrh > ylh || yrh == ylh  ){
					if ( p != null ){
						if ( p.key.compareTo(z.key) > 0  ) 
							p.lChild = AntiClkRot ( z , p  ) ; 
						else
							p.rChild = AntiClkRot ( z , p  ) ; 
					}
					else
						avl.head = AntiClkRot ( z , p ) ; 				
				}
			}
			if ( p != null ) // Recursively go up till root is reached
				Balance ( p , avl ) ; 
		}
		else
			if ( z.parent != null )
				Balance( z.parent , avl ) ; 
	}
	
	Node ClkRot ( Node node , Node p  ){ // Clockwise rotation at 'node' where 'p' is it's parent 
		Node y = node.lChild ; 
		node.lChild = y.rChild ; 
		if ( y.rChild != null )
			y.rChild.parent = node ; 
		y.rChild = node ; 
		node.parent = y ; 
		node.height = Height ( node ) ; 
		y.height = Height ( y ) ; 
		y.parent = p ; 
		return y ; 
	}
	Node AntiClkRot ( Node node , Node p ){  // AntiClockwise rotation at 'node' where 'p' is it's parent 
		Node y = node.rChild  ;
		node.rChild = y.lChild ;
		if ( y.lChild != null )
			y.lChild.parent = node ; 
		y.lChild  = node ; 
		node.parent = y ; 
		node.height = Height ( node ) ; 
		y.height = Height ( y ) ; 
		y.parent = p ; 
		return y ;  
	}
	int Height ( Node n ){ // Returns the height of Node 'n'
		int lh , rh ; 
		if ( n.lChild != null )
			lh = n.lChild.height ; 
		else
			lh = 0 ; 
		if ( n.rChild != null )
			rh = n.rChild.height ; 
		else
			rh = 0 ; 
		return 1 + Math.max(lh, rh)  ; 
	}
	// Creates a new node and sets 'n' to be the parent of the newly created node
	
	Node NewNode ( Url key , Node parent ){
		Node node = new Node() ; 
		node.lChild = null ; 
		node.rChild = null ; 
		node.key = key ; 
		node.height = 1 ;
		node.parent = parent ; 
		return node ; 
	}
	// Returns the predecessor 
	// This method is called only if a predecessor is present 
	// So it always returns a predecessor 
	
	Url predecessor ( Node n , AvlTree avl ) throws IOException{
		if ( n.rChild.rChild == null ){
			Node r = n.rChild ; 
			n.rChild = r.lChild ;
			n.height = Height ( n ) ; 
			Balance ( n , avl ) ; // Since we are deleting a node , check for imbalance  
			return  r.key ; 
		}
		else
			return predecessor ( n.rChild , avl ) ; 
	}
}