package chess.board;

public class BoardMisc {
public final static boolean [] FIRST_COLUMN=createColumn(0);
public final static boolean [] EIGHTH_COLUMN=createColumn(7);

public final static boolean [] FIRST_ROW=createRow(0);
public final static boolean [] SECOND_ROW=createRow(8);
public final static boolean [] THIRD_ROW=createRow(16);
public final static boolean [] FOURTH_ROW=createRow(24);
public final static boolean [] FIFTH_ROW=createRow(32);
public final static boolean [] SIXTH_ROW=createRow(40);
public final static boolean [] SEVENTH_ROW=createRow(48);
public final static boolean [] EIGHTH_ROW=createRow(56);
public static final int WM = -100;
public static final int WK = -200;
public static final int BM =  100;
public static final int BK =  200;
public static final int drawGameMoveCount=80;

public BoardMisc() {
	throw new RuntimeException("You can not instantiate me");
}

private static final boolean [] createColumn(int column) {
	boolean[] columnTrue=new boolean[64];
	do {columnTrue[column]=true;
	column=column+8;
	} while (column<64);
return columnTrue;
}

private static final boolean [] createRow(int row) {
	boolean[] rowTrue=new boolean[64];
	for(int i = 0; i<8;i++) {
		rowTrue[row]=true;
		row++;
	}
	return rowTrue;
}

public static boolean moveIsInBoard (final int coordinate) {
	return coordinate>0 && coordinate<64;
}

}
