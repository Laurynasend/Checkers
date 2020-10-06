package chess.board;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import chess.Player.Player;
import chess.pieces.King;
import chess.pieces.Man;
import chess.pieces.Piece;
import chess.pieces.Side;


public class Board{
	
private static Map <Integer,Tile> playingBoard=new HashMap<>();
private static Player playerToMove=new Player(Side.WHITE);
private static int startingCoordinate=-1;
private static Side bottomBoardPlayerColour=Side.WHITE;
private static int movesWithoutCapture=0;

private final static PropertyChangeSupport support = new PropertyChangeSupport(playerToMove);

public void addPropertyChangeListener (PropertyChangeListener listener) {
	support.addPropertyChangeListener(listener);
}

public static final Map<Integer, Tile> initializeEmptyBoard() {
	Map <Integer,Tile> emptyboard=new HashMap<Integer, Tile>();
	for(int i=0;i<64;i++) {
		emptyboard.put(i, new EmptyTile(i));
	}
	playingBoard=emptyboard;
	return emptyboard;
}

public static final void startingBoard () {
	playerToMove=new Player(Side.WHITE);
	movesWithoutCapture=0;
	Map <Integer,Tile> startingBoard=new HashMap<Integer, Tile>();
	for(int i=0;i<64;i++) {
		startingBoard.put(i, new EmptyTile(i));}
	
	startingBoard.replace(1, new OccupiedTile(1, new Man(Side.BLACK)));
	startingBoard.replace(3, new OccupiedTile(3, new Man(Side.BLACK)));
	startingBoard.replace(5, new OccupiedTile(5, new Man(Side.BLACK)));
	startingBoard.replace(7, new OccupiedTile(7, new Man(Side.BLACK)));
	startingBoard.replace(8, new OccupiedTile(8, new Man(Side.BLACK)));
	startingBoard.replace(10, new OccupiedTile(10, new Man(Side.BLACK)));
	startingBoard.replace(12, new OccupiedTile(12, new Man(Side.BLACK)));
	startingBoard.replace(14, new OccupiedTile(14, new Man(Side.BLACK)));
	startingBoard.replace(17, new OccupiedTile(17, new Man(Side.BLACK)));
	startingBoard.replace(19, new OccupiedTile(19, new Man(Side.BLACK)));
	startingBoard.replace(21, new OccupiedTile(21, new Man(Side.BLACK)));
	startingBoard.replace(23, new OccupiedTile(23, new Man(Side.BLACK)));
	startingBoard.replace(40, new OccupiedTile(40, new Man(Side.WHITE)));
	startingBoard.replace(42, new OccupiedTile(42, new Man(Side.WHITE)));
	startingBoard.replace(44, new OccupiedTile(44, new Man(Side.WHITE)));
	startingBoard.replace(46, new OccupiedTile(46, new Man(Side.WHITE)));
	startingBoard.replace(49, new OccupiedTile(49, new Man(Side.WHITE)));
	startingBoard.replace(51, new OccupiedTile(51, new Man(Side.WHITE)));
	startingBoard.replace(53, new OccupiedTile(53, new Man(Side.WHITE)));
	startingBoard.replace(55, new OccupiedTile(55, new Man(Side.WHITE)));
	startingBoard.replace(56, new OccupiedTile(56, new Man(Side.WHITE)));
	startingBoard.replace(58, new OccupiedTile(58, new Man(Side.WHITE)));
	startingBoard.replace(60, new OccupiedTile(60, new Man(Side.WHITE)));
	startingBoard.replace(62, new OccupiedTile(62, new Man(Side.WHITE)));
	
	

	
	//test case
	/*startingBoard.replace(10, new OccupiedTile(10, new Man(Side.BLACK, 10)));
	startingBoard.replace(8, new OccupiedTile(8, new Man(Side.BLACK, 8)));
	startingBoard.replace(1, new OccupiedTile(1, new Man(Side.BLACK, 1)));
	startingBoard.replace(3, new OccupiedTile(3, new Man(Side.BLACK, 3)));
	startingBoard.replace(5, new OccupiedTile(5, new Man(Side.BLACK, 5)));


	startingBoard.replace(35, new OccupiedTile(35, new Man(Side.WHITE, 35)));
	startingBoard.replace(53, new OccupiedTile(53, new Man(Side.WHITE, 53)));
	startingBoard.replace(60, new OccupiedTile(60, new Man(Side.WHITE, 60)));
	startingBoard.replace(55, new OccupiedTile(55, new Man(Side.WHITE, 55)));
	startingBoard.replace(17, new OccupiedTile(17, new Man(Side.WHITE, 17)));*/

	bottomBoardPlayerColour=Side.WHITE;
	playingBoard=startingBoard;
}

public static final void testingPosition () {
	playerToMove=new Player(Side.WHITE);
	Map <Integer,Tile> startingBoard=new HashMap<Integer, Tile>();
	for(int i=0;i<64;i++) {
		startingBoard.put(i, new EmptyTile(i));}

	//test case
	startingBoard.replace(10, new OccupiedTile(10, new Man(Side.BLACK)));
	startingBoard.replace(8, new OccupiedTile(8, new Man(Side.BLACK)));
	startingBoard.replace(1, new OccupiedTile(1, new Man(Side.BLACK)));
	startingBoard.replace(3, new OccupiedTile(3, new Man(Side.BLACK)));
	startingBoard.replace(5, new OccupiedTile(5, new Man(Side.BLACK)));


	startingBoard.replace(35, new OccupiedTile(35, new Man(Side.WHITE)));
	startingBoard.replace(53, new OccupiedTile(53, new Man(Side.WHITE)));
	startingBoard.replace(60, new OccupiedTile(60, new Man(Side.WHITE)));
	startingBoard.replace(55, new OccupiedTile(55, new Man(Side.WHITE)));
	startingBoard.replace(17, new OccupiedTile(17, new Man(Side.WHITE)));

	bottomBoardPlayerColour=Side.WHITE;
	playingBoard=startingBoard;
}


public static final void flipBoard () {
	bottomBoardPlayerColour= bottomBoardPlayerColour==Side.WHITE ? Side.BLACK : Side.WHITE;
	Map <Integer, Tile> tempBoard=Board.getPlayingBoard();
	Map <Integer, Tile> flippedBoard=new HashMap<>();
	for (int i = 0;i<64;i++) {
			flippedBoard.put(i, tempBoard.get(63-i));
			if(flippedBoard.get(i).isTileOccupied()) {
			flippedBoard.get(i).getPiece().updateMoveDirection();}
		
	}
	
	playingBoard=flippedBoard;
}

public static final void updateBoard(final int pieceCoordinate, final int destinationCoordinate){
		
		List <Move> possibleMoves=new ArrayList<Move>();


		possibleMoves.addAll(Board.calculatePossibleMoves(playingBoard, playerToMove.getPlayerColour()));

		/*for (Move legalMove : possibleMoves) {
			System.out.println(legalMove.getStartingCoordinate() + "  " + legalMove.destinationCoordinate);}*/
				
			
			for (Move legalMove : possibleMoves) {
				
				Tile currentTile=getTilePlayingBoard(pieceCoordinate);
				
				if(pieceCoordinate==legalMove.getStartingCoordinate() && legalMove.destinationCoordinate==destinationCoordinate) {
					if(startingCoordinate==-1 || startingCoordinate==pieceCoordinate) {
						
						if(!shouldPromote(currentTile, destinationCoordinate)) {
							movesWithoutCapture++;
					playingBoard.replace(pieceCoordinate, new EmptyTile(pieceCoordinate));
					currentTile.setTileCoordinate(destinationCoordinate);
					playingBoard.replace(destinationCoordinate, currentTile);
					} else {
						movesWithoutCapture++;
					playingBoard.replace(destinationCoordinate, new OccupiedTile(destinationCoordinate, 
					new King(currentTile.getPiece().pieceSide)));
					playingBoard.replace(pieceCoordinate, new EmptyTile(pieceCoordinate));
					}
					
					
					final int coordinateChange=destinationCoordinate-pieceCoordinate;
					if(coordinateChange%2==0) {
						int pieceToDestroyCoordinate=pieceCoordinate+coordinateChange/2;
					playingBoard.replace(pieceToDestroyCoordinate, new EmptyTile(pieceToDestroyCoordinate))	;
					movesWithoutCapture=0;
					}
					
					if(getTilePlayingBoard(destinationCoordinate).getPiece().isPieceAttacking(destinationCoordinate, Board.getPlayingBoard()) && coordinateChange%2==0 ) {
						startingCoordinate=destinationCoordinate;
						
						Player oldValue=new Player();
						oldValue.setPlayerColour(playerToMove.getPlayerColour());
						support.firePropertyChange("playerToMove", oldValue, playerToMove);
					} else {
						startingCoordinate=-1;
						
						
						Player oldValue=new Player();
						oldValue.setPlayerColour(playerToMove.getPlayerColour());
						playerToMove.invertPlayerColour();
						support.firePropertyChange("playerToMove", oldValue, playerToMove);
						
					}
					break;
				}
			}	
		}
			
			for (int i =0; i<64;i++) {
				if (i!=Board.getPlayingBoard().get(i).getCoordinate()) System.out.println(i + "   " + Board.getPlayingBoard().get(i).getCoordinate());
			}
}
			
		

			private static boolean shouldPromote(final Tile currentTile, final int destinationCoordinate) {
				if(bottomBoardPlayerColour==Side.WHITE) {
					if(currentTile.getPiece().pieceSide==playerToMove.getPlayerColour()) {
						if(currentTile.getPiece().pieceSide==Side.WHITE && BoardMisc.FIRST_ROW[destinationCoordinate] ) {return true;}
						else if (currentTile.getPiece().pieceSide==Side.BLACK && BoardMisc.EIGHTH_ROW[destinationCoordinate]) {return true;}
						}
					
				} else { 
					if(currentTile.getPiece().pieceSide==playerToMove.getPlayerColour()) {
					if(currentTile.getPiece().pieceSide==Side.WHITE && BoardMisc.EIGHTH_ROW[destinationCoordinate] ) {return true;}
					else if (currentTile.getPiece().pieceSide==Side.BLACK && BoardMisc.FIRST_ROW[destinationCoordinate]) {return true;}
					}
				}

				return false;
}

			
				


public static Collection<Move> calculatePossibleMoves (Map <Integer,Tile> playingboard,Side side) {
	List <Move> possibleMoves=new ArrayList<Move>();
	List <Move> mustMoves=new ArrayList<Move>();
	List<Tile> pieceList=new ArrayList<>();
	pieceList=sidePieces(getPlayingBoard(),playerToMove.getPlayerColour());
	for(Tile currentTile : pieceList ) {
		if(!currentTile.getPiece().isPieceAttacking(currentTile.getCoordinate(), playingboard)) {
		possibleMoves.addAll(currentTile.getPiece().calculateMoves(currentTile.getCoordinate(), playingboard));}
		else {mustMoves.addAll(currentTile.getPiece().calculateMoves(currentTile.getCoordinate(), playingboard));}
	}
	
	
	if(mustMoves.isEmpty()) {
	playerToMove.setIsAttacking(false);
	return possibleMoves;} 
	else {
	playerToMove.setIsAttacking(true);
		return mustMoves; }
}

public static List<Tile> sidePieces (Map <Integer,Tile> playingboard, Side side) {
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


public static final void drawBoard() {
	for(int i=0;i<64;i++) {
		if(!playingBoard.get(i).isTileOccupied()) {
			System.out.print("-   ");
		} else {
			if(playingBoard.get(i).getPiece().getPieceValue()== 100) {System.out.print("B   ");}
			if(playingBoard.get(i).getPiece().getPieceValue()== -100) {System.out.print("W   ");}
			if(playingBoard.get(i).getPiece().getPieceValue()== 200) {System.out.print("BK   ");}
			if(playingBoard.get(i).getPiece().getPieceValue()== -200) {System.out.print("WK   ");}


		}
		if ((i+1)%8==0) {
			System.out.println();}
			}
}


public static final Map<Integer, Tile> getPlayingBoard() {
	Map<Integer, Tile> copy=new HashMap<>();
	for (int i=0; i<64;i++) {
		// kuriu nauja tile ir perduodu esama figura
		if(playingBoard.get(i).isTileOccupied()) {
		copy.put(i, new OccupiedTile(i, playingBoard.get(i).getPiece()));}
		else {copy.put(i, new EmptyTile(i));}
	}
	
	return copy;
}

public static final Tile getTilePlayingBoard (final int coordinate) {
	return playingBoard.get(coordinate);
}



public static void invertPlayerColour () {
	if(playerToMove.getPlayerColour()==Side.BLACK) {
		playerToMove.setPlayerColour(Side.WHITE);
	} else {
		playerToMove.setPlayerColour(Side.BLACK);}
}

public static boolean mustMoveList(boolean mustMoveList) {
	return mustMoveList;
	}

public static Side getPlayerToMove() {
	return playerToMove.getPlayerColour();
	
}

public static boolean getIsPlayerAttacking() {
	return playerToMove.getIsAttacking();
	
}

public static int getStartingCoordinate() {
	return startingCoordinate;
}

public static Side getBottomBoardPlayerColour() {
	return bottomBoardPlayerColour;
}

public static int getMovesWithoutCaptureCount() {
	return movesWithoutCapture;
}

public static void setMovesWithoutCaptureCount(int i) {
	movesWithoutCapture=i;
	
}



}

