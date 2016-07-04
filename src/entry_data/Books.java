/**
 * @Author 		LeDaniel Leung
 * @Filename	Books.java
 * @Description	File to hold the library of books that may be bought
 */

package entry_data;

public enum Books{
	TBPM1("Vol. 1 Traditional & Bo-Po-Mo"),
	TPY1 ("Vol. 1 Traditional & Pin-Yin"),
	S1   ("Vol. 1 Simplified"),
	TBPM2("Vol. 2 Traditional & Bo-Po-Mo"),
	TPY2 ("Vol. 2 Traditional & Pin-Yin"),
	S2	 ("Vol. 2 Simplified"),
	TBPM3("Vol. 3 Traditional & Bo-Po-Mo"),
	TPY3 ("Vol. 3 Traditional & Pin-Yin"),
	S3	 ("Vol. 3 Simplified"),
	TPY4 ("Vol. 4 Traditional & Pin-Yin"),
	S4   ("Vol. 4 Simplified"),
	TPY5 ("Vol. 5 Traditional & Pin-Yin"),
	S5	 ("Vol. 5 Simplified"),
	TPY6 ("Vol. 6 Traditional & Pin-Yin"),
	S6	 ("Vol. 6 Simplified"),
	TPY7 ("Vol. 7 Traditional & Pin-Yin"),
	S7	 ("Vol. 7 Simplified"),
	PY	 ("Pin Yin"),
	BPM	 ("Bo-Po-Mo"),
	MCBT1("My Chinese Book 1 (Textbook)"),
	MCBW1("My Chinese Book 1 (Workbook)"),
	MCBT2("My Chinese Book 2 (Textbook)"),
	MCBW2("My Chinese Book 2 (Workbook)"),
	ECT1 ("Everyday Chinese 1 (Textbook)"),
	ECW1 ("Everyday Chinese 1 (Workbook)"),
	ECT2 ("Everyday Chinese 2 (Textbook)"),
	ECW2 ("Everyday Chinese 2 (Workbook)"),
	ECT3 ("Everyday Chinese 3 (Textbook)"),
	ECW3 ("Everyday Chinese 3 (Workbook)"),
	L1P1T("Level 1 Part 1 (Textbook)"),
	L1P1W("Level 1 Part 1 (Workbook)"),
	L1P2T("Level 1 Part 2 (Textbook)"),
	L1P2W("Level 1 Part 2 (Workbook)"),
	L2P1T("Level 2 Part 1 (Textbook)"),
	L2P1W("Level 2 Part 1 (Workbook)"),
	L2P2T("Level 2 Part 2 (Textbook)"),
	L2P2W("Level 2 Part 2 (Workbook)"),
	BTB	 ("Beyond the Basics (AP Class)");
	
	private final String title;
	Books(String title){this.title = title;}
	
	public String toString(){return title;}
	
	public static Books getBook(String title){
		for(Books book: Books.values())
			if(book.title.equals(title))
				return book;
		return null;
	}
}
