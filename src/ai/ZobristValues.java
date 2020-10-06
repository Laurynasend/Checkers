package ai;

import java.security.*;
import java.util.Map;

import chess.board.BoardMisc;
import chess.board.Tile;
import chess.pieces.Side;

public class ZobristValues {
static  long array[][][]=new long[2][2][64]; // 0 yra baltas 1 yra juodas, kiti skliaustai 0 yra Man 1 yra King
static long whiteMove;
static long startingCoordinateArray[]=new long[65];

public static void fillArray() {
	for(int colour =0;colour<2;colour++ ) {
		for ( int piece=0;piece<2;piece++) {
			for (int coordinate=0;coordinate<64;coordinate++) {
				array[colour][piece][coordinate]=randomLong();
				startingCoordinateArray[coordinate]=randomLong();
			}
		}
	} 
	whiteMove=randomLong();
}

public static long boardZobristValue (final Map<Integer, Tile> board, final Side playerToMove, final int startingCoordinate) {
	long zobristValue=0;
	for(int i = 0;i<64;i++) {
		
	if(board.get(i).isTileOccupied()) {
		
		if(board.get(i).getPiece().getPieceValue()==BoardMisc.WM) {
			zobristValue=zobristValue^array[0][0][i];
		} 
		
		else if(board.get(i).getPiece().getPieceValue()==BoardMisc.BM) {
			zobristValue=zobristValue^array[1][0][i];
		} 
		
		else if(board.get(i).getPiece().getPieceValue()==BoardMisc.WK) {
			zobristValue=zobristValue^array[0][1][i];
		} 
		
		else if(board.get(i).getPiece().getPieceValue()==BoardMisc.BK) {
			zobristValue=zobristValue^array[1][1][i];
		} 
	
		}
	}
	
	if(playerToMove==Side.WHITE) {
		zobristValue=zobristValue^whiteMove;
	}
	
	if(startingCoordinate!=-1) {
		zobristValue=zobristValue^startingCoordinateArray[startingCoordinate];
	}
	
	zobristValue=zobristValue^startingCoordinate;
	
	return zobristValue;
}


public static long randomLong() {
	SecureRandom random=new SecureRandom();
	return random.nextLong();
}



}
