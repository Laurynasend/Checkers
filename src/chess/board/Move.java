package chess.board;

import java.util.Map;

import chess.pieces.Piece;

public class Move {

	  private Map<Integer, Tile> playingboard;
	  public final Piece movedPiece;
	  public int destinationCoordinate;
	  private int startingCoordinate;
	  
	  
	  public Move (final Map<Integer, Tile> playingboard, final int startingCoordinate, final int destinationCoordinate){
		  this.playingboard=playingboard;
		  this.destinationCoordinate=destinationCoordinate;
		  this.startingCoordinate=startingCoordinate;
		  this.movedPiece=playingboard.get(startingCoordinate).getPiece();
	  }
	  
	  public int getStartingCoordinate() {
		return startingCoordinate;
	}

	public void setStartingCoordinate(int startingCoordinate) {
		this.startingCoordinate = startingCoordinate;
	}


	public static class AttackingMove extends Move {

		public AttackingMove(Map<Integer, Tile> playingboard, final int startingCoordinate, int destinationCoordinate) {
			super(playingboard, startingCoordinate, destinationCoordinate);
		}
	}
	  
	  public static class NormalMove extends Move {
			public NormalMove(Map<Integer, Tile> playingboard, final int startingCoordinate, int destinationCoordinate) {
			super(playingboard, startingCoordinate, destinationCoordinate);
		}
	}
	  
	  @Override
	  public String toString() {
		 return getStartingCoordinate() + "  --->  " + destinationCoordinate;
	  }


}
