package chess.board;

import java.util.Map;

import chess.pieces.Piece;

public abstract class Tile {

	private int coordinate;
	public Tile(final int coordinate) {
		this.coordinate=coordinate;
	}
	public abstract boolean isTileOccupied ();
	public abstract Piece getPiece();
	protected final  int getCoordinate() {
		return coordinate;
	}
	
	public void setTileCoordinate (final int coordinate) {
		this.coordinate=coordinate;
	}
	
	public int getTileCoordinate () {
		return coordinate;
	}
	
}
