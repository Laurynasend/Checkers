package chess.board;

import chess.pieces.Piece;

public class OccupiedTile extends Tile {
	private Piece pieceOnTile;
	
public OccupiedTile (final int coordinate,final Piece pieceOnTile) {
	super(coordinate);
	this.pieceOnTile=pieceOnTile;
}
@Override
public boolean isTileOccupied () {
	return true;
}
@Override
public Piece getPiece() {
	
	return this.pieceOnTile;
}


}
