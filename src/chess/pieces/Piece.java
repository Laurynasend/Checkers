package chess.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import chess.board.Board;
import chess.board.Move;
import chess.board.Tile;

public abstract class Piece {
public final Side pieceSide;

protected int value;
public int [] CANDIDATE_LEGAL_MOVES;
protected boolean pieceAttacking;



public Piece (final Side pieceSide) {
	this.pieceSide=pieceSide;
	
}
public abstract List<Move> calculateMoves (final int pieceCoordinate, Map <Integer, Tile> board);

public final int setDirection() {
	if(Board.getBottomBoardPlayerColour()==Side.WHITE) {
	if(pieceSide==Side.BLACK) {return 1;}
	else {return -1;}
	} else {
		if(pieceSide==Side.BLACK) {return -1;}
		else {return 1;}
	}
}

public final int pieceValueSign() {
	if (pieceSide==Side.BLACK) {return 1;}
	else {return -1;}
}

public abstract void updateMoveDirection ();

public abstract int getPieceValue();

public abstract String getPieceType();



public abstract boolean isPieceAttacking(final int pieceCoordinate, Map <Integer, Tile> board);

public abstract String toString();

}

