/**
 * @Author 		LeDaniel Leung
 * @Filename 	Entry.java
 * @Description Holds the data for an entry into database
 */

package entry_data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entry{
	private String name, booksStr, datesStr;
	private int ID;
	private List<Books> books;
	private List<String> dates;
	
	/**
	 * @function	Entry
	 * @param 		name (String) - (last) name of the family's entry
	 * @param 		ID (int) - family's registered ID
	 * @param 		books (HashMap<String, String>) - collection of the book(s)
	 * 					that the family bought mapped to the dates (format:
	 * 					mm/dd/yyyy) they were bought
	 * @description	Constructor for an entry in the database
	 */
	public Entry(String name, int ID, List<Books> books, List<String> dates){
		this.name  = name;
		this.ID    = ID;
		this.books = books;
		this.dates = dates;
		
		booksStr = getBookList_String();
		datesStr = getDateList_String();
	}
	
	/**
	 * @function	Entry
	 * @param 		name (String) - (last) name of the family's entry
	 * @param 		ID (int) - family's registered ID
	 * @description	Constructor where books + dates have not been specified
	 */
	public Entry(String name, int ID){
		this.name = name;
		this.ID = ID;
		books = new ArrayList<Books>(0);
		dates = new ArrayList<String>(0);
	}
	
	/**
	 * @function	Entry (default constructor)
	 * @param		none
	 * @description	Default constructor for an entry
	 */
	public Entry(){this("DEF_ENTRY", 0);}
	
	/* getter methods */
	public int 			getID()	    {return ID;   	 }
	public String 		getName()   {return name; 	 }
	public List<Books>  getBooks()  {return books;	 }
	public List<String> getDates()  {return dates;	 }
	public String 		getBooksStr(){return booksStr;}
	public String		getDatesStr(){return datesStr;}
	
	/* setter methods */
	public void setBooksAndDates(List<Books> books, List<String> dates){
		this.books = books;
		this.dates = dates;
	}
	
	/**
	 * @function	isEqual
	 * @param 		otherEntry (Entry) - entry to be checked against
	 * @return		true if name and ID are equal
	 * 				false otherwise
	 */
	public boolean isEqual(Entry otherEntry){
		if(name.equals(otherEntry.getName()) && ID == otherEntry.getID())
			return true;
		return false;
	}
	
	/**
	 * @function	getBookList_String
	 * @param		none
	 * @return		A complete String of the entry's books
	 */
	public String getBookList_String(){
		List<String> bookNames = new ArrayList<String>();
		for(Books book: books)bookNames.add(book.toString());
		return bookNames.stream().collect(Collectors.joining("\n"));
	}
	
	/**
	 * @function	getDateList_String()
	 * @param		none
	 * @return		A complete String of the entry's dates
	 */
	public String getDateList_String(){
		return dates.stream().collect(Collectors.joining("\n"));
	}
	
	/**
	 * @function	toString
	 * @param		none
	 * @return		String in the format:	name(6)	ID	books	dates
	 * @description	Formats a string to return
	 */
	public String toString(){
		//restrict name to up to 6 characters when displaying
		String ret = (name.length() > 6 ? name.substring(0, 6) : name) + 
				"\t" + ID + "\t";
		
		//appends the books + dates they were bought
		for(int i = 0; i < books.size(); i++){
			
			//there is more than one book, pad with "\t"s
			if(i != 0) ret += "\n\t\t";
			
			//append the book title
			ret += books.get(i);
			
			//restrict alignment for book titles
			for(int j = books.get(i).toString().length(); 
					j < Books.TBPM1.toString().length(); j++)
				ret += " ";
			
			//append the date
			ret += "\t" + dates.get(i);
		}
		return ret;
	}
}
	/* Test code */
	/*
	public static void main(String args[]){
		Entry test = new Entry();
		System.out.println(test);
		
		String[] nameArray = { "A", "Li", "Lee", "Chia", "Leung", "Chungs", "Bae Lim"};
		List<Books> bookList = new ArrayList<Books>();
		List<String> dateList = new ArrayList<String>();
		int count = 0;
		for(Books book: Books.values()){
			bookList.add(book);
			dateList.add(((count % 12) + 1) + "/" + ((count % 30) + 1) + "/" + 2016);
			count++;
		}

		for(String name: nameArray){
			int ID = name.hashCode() % 10000;
			Entry entry = new Entry(name, ID, bookList, dateList);
			
			System.out.println(entry);
		}
		
	}
	*/
