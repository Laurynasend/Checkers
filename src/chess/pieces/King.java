package chess.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import chess.board.Board;
import chess.board.BoardMisc;
import chess.board.Move;
import chess.board.Tile;


public class King extends Piece {
	final private int value=pieceValueSign()*200;
	public final int[] CANDIDATE_LEGAL_MOVES= {-9,-7, 7,9};
	
	public King(final Side pieceSide) {
		super(pieceSide);
	}

	@Override
	public List<Move> calculateMoves(final int pieceCoordinate, Map <Integer, Tile> board) {
		List <Move> possibleLegalMoves=new ArrayList<Move>();
		List <Move> possibleMustMoves=new ArrayList<Move>();
		for(final int candidateMove : CANDIDATE_LEGAL_MOVES) {
			final int possibleLegalMove=pieceCoordinate+candidateMove;
			Tile candidateLegalMoveTile=board.get(possibleLegalMove);
			if(BoardMisc.moveIsInBoard(possibleLegalMove) && !firstColumnException(pieceCoordinate,candidateMove) && !eighthColumnException(pieceCoordinate, candidateMove)) {
				if(!candidateLegalMoveTile.isTileOccupied()) {
				possibleLegalMoves.add(new Move.NormalMove (board, pieceCoordinate, possibleLegalMove));
			}   else {
				if(candidateLegalMoveTile.getPiece().pieceSide==this.pieceSide) {
					continue;
				} else {
					final int possibleMustMove=possibleLegalMove+candidateMove;
					if(BoardMisc.moveIsInBoard(possibleMustMove)) {
					Tile candidateMustMoveTile=board.get(possibleMustMove);
					if(!candidateMustMoveTile.isTileOccupied() && !firstColumnException(possibleLegalMove,candidateMove)
							&& !eighthColumnException(possibleLegalMove, candidateMove)) {
						possibleMustMoves.add(new Move.AttackingMove(board, pieceCoordinate, possibleMustMove));
					}
				}
			  }
			}
		}
	}
		if(!possibleMustMoves.isEmpty()) {
			pieceAttacking=true;
			return possibleMustMoves;
		} else {
			pieceAttacking=false;
			return possibleLegalMoves;
		}
	}
	

	public static boolean firstColumnException (final int coordinate, final int candidateMove) {
		return BoardMisc.FIRST_COLUMN[coordinate] && (candidateMove==7 || candidateMove==-9);
	}
	
	public static boolean eighthColumnException (final int coordinate, final int candidateMove) {
		return BoardMisc.EIGHTH_COLUMN[coordinate] && (candidateMove==9 || candidateMove==-7);
	}

	

	@Override
	public String toString() {
		return "King";
	}
	
	@Override
	public boolean isPieceAttacking(final int pieceCoordinate, Map <Integer, Tile> board) {
		for(final int candidateMove : CANDIDATE_LEGAL_MOVES) {
			final int possibleLegalMove=pieceCoordinate+candidateMove;
			Tile candidateLegalMoveTile=board.get(possibleLegalMove);
			if(BoardMisc.moveIsInBoard(possibleLegalMove) && !firstColumnException(pieceCoordinate,candidateMove) && !eighthColumnException(pieceCoordinate, candidateMove)) {
				if(!candidateLegalMoveTile.isTileOccupied()) {
			}   else {
				if(candidateLegalMoveTile.getPiece().pieceSide==this.pieceSide) {
					continue;
				} else {
					final int possibleMustMove=possibleLegalMove+candidateMove;
					if(BoardMisc.moveIsInBoard(possibleMustMove)) {
					Tile candidateMustMoveTile=board.get(possibleMustMove);
					if(!candidateMustMoveTile.isTileOccupied() && !firstColumnException(possibleLegalMove,candidateMove) 
							&& !eighthColumnException(possibleLegalMove, candidateMove)) {
						return true;}
				}
			  }
			}
		}
	}
		return false;
	}
	
	
	

	@Override
	public int getPieceValue() {
		return value;
	}

	@Override
	public void updateMoveDirection() {
		
	}
	
	@Override
	public String getPieceType() {
		return "k";
	}
	
}
