package chess.board;

import java.util.*;

import chess.Player.Player;
import chess.pieces.King;
import chess.pieces.Man;
import chess.pieces.Piece;
import chess.pieces.Side;


public class CalcBoard {

List<Tile> pieces;
private Map <Integer, Tile> board=new HashMap<>();
private Player player=new Player();
private final static Side white=Side.WHITE;
private final static Side black=Side.BLACK;
private int startingCoordinate;
public boolean isMoveDone=false;
private int numberPieces;
private int movesWithoutCapture=0;


public CalcBoard(Map <Integer, Tile> playingBoard,Side side, final int startingCoordinate, final int movesWithoutCapture) {
	this.board=playingBoard;
	this.player.setPlayerColour(side);
	this.startingCoordinate=startingCoordinate;
	this.movesWithoutCapture=movesWithoutCapture;

}
				
public final void makeMove(final int pieceCoordinate, final int destinationCoordinate){
		
			Tile currentTile=board.get(pieceCoordinate);
			
					if(!shouldPromote(currentTile, destinationCoordinate)) {
				board.replace(pieceCoordinate, new EmptyTile(pieceCoordinate));
				currentTile.setTileCoordinate(destinationCoordinate);
				board.replace(destinationCoordinate, currentTile);
				movesWithoutCapture++;
				} else {
					movesWithoutCapture++;
				board.replace(destinationCoordinate, new OccupiedTile(destinationCoordinate, 
				new King(currentTile.getPiece().pieceSide)));
				board.replace(pieceCoordinate, new EmptyTile(pieceCoordinate));
				}
				
				
				final int coordinateChange=destinationCoordinate-pieceCoordinate;
				if(coordinateChange%2==0) {
					movesWithoutCapture=0;
					int pieceToDestroyCoordinate=pieceCoordinate+coordinateChange/2;
				board.replace(pieceToDestroyCoordinate, new EmptyTile(pieceToDestroyCoordinate))	;
				}
				
				if(board.get(destinationCoordinate).getPiece().isPieceAttacking(destinationCoordinate, board) && coordinateChange%2==0 ) {
					startingCoordinate=destinationCoordinate;
					isMoveDone=true;
				} else {
					player.invertPlayerColour();
					startingCoordinate=-1;
					isMoveDone=true;
				}
		}

private boolean shouldPromote(final Tile currentTile, final int destinationCoordinate) {
	if(Board.getBottomBoardPlayerColour()==Side.WHITE) {
		if(currentTile.getPiece().pieceSide==player.getPlayerColour()) {
			if(currentTile.getPiece().pieceSide==Side.WHITE && BoardMisc.FIRST_ROW[destinationCoordinate] ) {return true;}
			else if (currentTile.getPiece().pieceSide==Side.BLACK && BoardMisc.EIGHTH_ROW[destinationCoordinate]) {return true;}
			
					}
	} else { 
		if(currentTile.getPiece().pieceSide==Side.WHITE && BoardMisc.EIGHTH_ROW[destinationCoordinate] ) {return true;}
		else if (currentTile.getPiece().pieceSide==Side.BLACK && BoardMisc.FIRST_ROW[destinationCoordinate]) {return true;}
		
	}

	return false;
}		


public  List<Move> calculatePossibleMoves (final Map <Integer, Tile> board, final Side side, final int startingCoordinate) {
	List <Move> possibleMoves=new ArrayList<Move>();
	List <Move> mustMoves=new ArrayList<Move>();
	List<Tile> pieceList=new ArrayList<>();
	if(startingCoordinate==-1) {
	pieceList=sidePieces(board, side);
	for(Tile currentTile : pieceList ) {
		if(!currentTile.getPiece().isPieceAttacking(currentTile.getCoordinate(), board)) {
		possibleMoves.addAll(currentTile.getPiece().calculateMoves(currentTile.getCoordinate(), board));}
		else {mustMoves.addAll(currentTile.getPiece().calculateMoves(currentTile.getCoordinate(), board));}
	}
	
	if(mustMoves.isEmpty()) {return possibleMoves;}
	else {return mustMoves;}
	
	} else {
		if(board.get(startingCoordinate).getPiece().isPieceAttacking(startingCoordinate, board)) {
			mustMoves.addAll(board.get(startingCoordinate).getPiece().calculateMoves(startingCoordinate, board));
		}
		return mustMoves;
	}
	
	
	
}

public List<Tile> sidePieces (Map <Integer,Tile> playingboard, Side side) {
	List<Tile> pieceList=new ArrayList<>();
	for (int i =0;i<64;i++) {
		if(playingboard.get(i).isTileOccupied()) {
		if(playingboard.get(i).getPiece().pieceSide==side) {
			pieceList.add(playingboard.get(i));
		}
	}
	}
	return pieceList;
}


public void invertPlayerColour () {
	if(player.getPlayerColour()==Side.BLACK) {
		player.setPlayerColour(Side.WHITE);
	} else {
		player.setPlayerColour(Side.BLACK);}
}

public final Map<Integer, Tile> getCurrentBoard() {
	return board;
}

public final Map<Integer, Tile> getCurrentBoardCopy() {
	Map<Integer, Tile> copy=new HashMap<>();
	for (int i=0; i<64;i++) {
		// kuriu nauja tile ir perduodu esama figura
		if(board.get(i).isTileOccupied()) {
		copy.put(i, new OccupiedTile(i, board.get(i).getPiece()));}
		else {copy.put(i, new EmptyTile(i));}
	}
	
	return copy;
}
	

public Side getPlayerColour() {
	return player.getPlayerColour();
}



public int getNumberPieces() {
	return sidePieces(this.board, this.getPlayerColour()).size();
}

public Tile getBoardTile(final int coordinate) {
	return board.get(coordinate);
}


public final static void drawBoard(Map<Integer, Tile> board) {

	for(int i=0;i<64;i++) {
		if(!board.get(i).isTileOccupied()) {
			System.out.print("-   ");
		} else {
			if(board.get(i).getPiece().getPieceValue()== 100) {System.out.print("B   ");}
			if(board.get(i).getPiece().getPieceValue()== -100) {System.out.print("W   ");}
			if(board.get(i).getPiece().getPieceValue()== 200) {System.out.print("BK   ");}
			if(board.get(i).getPiece().getPieceValue()== -200) {System.out.print("WK   ");}


		}
		if ((i+1)%8==0) {
			System.out.println();}
			}
	System.out.println();
	System.out.println();
}

public void setBoard(Map<Integer,Tile> board) {
	this.board=board;
}

public void setPlayerColour(Side side) {
	this.player.setPlayerColour(side);
}



public final int getStartingCoordinate() {
	return startingCoordinate;
}



public void setStartingCoordinate(final int startingCoordinate) {
	this.startingCoordinate=startingCoordinate;
}

public int getMovesWithoutCaptureCount() {
	return movesWithoutCapture;
}

}

