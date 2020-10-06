package chess.board;

import chess.pieces.Piece;

public class EmptyTile extends Tile{
	
public EmptyTile (final int coordinate) {
	super(coordinate);
}
@Override
public boolean isTileOccupied () {
	return false;
}
@Override
public Piece getPiece() {
	return null;
}
}
