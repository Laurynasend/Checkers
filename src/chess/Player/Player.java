package chess.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.board.Tile;
import chess.pieces.Piece;
import chess.pieces.Side;

public class Player {

private Side playerColour;
private boolean isAttacking;

public Player(final Side playerColour) {
	this.playerColour=playerColour;
}

public Player() {}

public Side getPlayerColour () {
	return playerColour;
}

public void setPlayerColour (final Side playerColour) {
	this.playerColour=playerColour;
}

public void invertPlayerColour () {
	if(this.playerColour==Side.BLACK) {
		this.playerColour=Side.WHITE;
	} else {
		this.playerColour=Side.BLACK;}
}

public boolean getIsAttacking() {
	return isAttacking;
}

public void setIsAttacking(boolean isAttacking) {
	this.isAttacking = isAttacking;
}


}

