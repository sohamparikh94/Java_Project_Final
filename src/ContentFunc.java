
public class ContentFunc {
	String Content ( String line  ) 
	{
		int len = line.length() ;
		String s = new String("") ; 
		for ( int i = 0 ; i < line.length() ; i++ ){
			if ( line.charAt(i) == '<'){
				while ( line.charAt(i) != '>'  ){
					i++ ;
					if ( i == len )
						break ; 
				}	 
			}
			if ( i != len )
				if ( line.charAt(i) != '>' )
					s += line.charAt(i) ; 
		}
		return Filtering(s) ; 
	}

	public static boolean isHtmlTag ( String line ){
		//ArrayList<String> tags = new ArrayList<String>() ;
		String tag[] = { "display" , "sidebar" , "#header" , "background" , "padding" , "color" , "font-weight" , "title=" , "href=" , "lazyload=" , "alt=" , 
				"text-decoration" , "font-size" , "margin" , "top" , "width" , "border", "indent" ,"float" , "list-style" ,"data-aria-label-part=" , 
				 "align" , "cellspacing" , "table" , "type" , "<script>" , "@import" , "#contents" , "#wrapper" , "data-time=" , "data-long-form=" , 
			"font-family" , "height" , "position" , "line-height" , "visibility" ,"font-style" , "font-stretch" , "data-user-id=" , "data-test-selector=" , 
			"font-variant" , "#news" , "#event" , "footer" , "font" , "navmenu" , "url" , "animation" ,"directionNav" , "role=" 
			, "controlNav" , "pauseOnHover" , "slideshow" , "pauseTime" ,"boxRows" , "boxCols" , "animSpeed" ,"reverse:"
			, "direction:" , "manualAdvance:" , "effect:","slices:" ,"#site-title" , "UnhideWhenUsed=" , "LatentStyleCount="
			, "QFormat=" , "Name="} ;
		int len = tag.length ; 
		for ( int i = 0 ; i < len ; i++ )
			if (line.contains(tag[i]))
				return true ; 
		return false ; 
	}

	static String Filtering ( String line  )
	{
		String trim = line.trim() ; 
	   	if (!isIllegal(trim) )
	 	   return trim+"\n" ; 	
	   else
		   return "" ; 
	} 

	static boolean isIllegal ( String str )
	{
		if ( str.contains("*") || str.contains("{") || str.contains("}") || str.isEmpty() || str.contains("http") || str.contains(";") || isHtmlTag(str))
			return true ; 
		return false ; 
	}
	boolean isInDomain (String str ){
		String[] a = { "google" , "twitter" , "goo." , "reddit" , "facebook" ,".pdf" , ".docx" , "mailto"} ; 
		for ( String s : a ){
			if ( str.contains(s))
				return false ; 
		}
		return true ; 
	}
}
