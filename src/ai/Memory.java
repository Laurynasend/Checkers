package ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import chess.board.Move;

public class Memory {

	private Move bestMove;
	private int evaluation;
	private List<Move> bestMoveOrder=new ArrayList<Move>();
	
	Memory (List<Move> possibleMoves) {
		this.bestMoveOrder=possibleMoves;
	}
	
	public final  Move getBestMove() {
		return bestMove;
	}
	public final void setBestMove(Move bestMove) {
		this.bestMove = bestMove;
	}
	public final int getEvaluation() {
		return evaluation;
	}
	public final void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	public final void updateBestMoveOrder(List<Move> bestMoveOrder) {
		this.bestMoveOrder.removeAll(bestMoveOrder);
		this.bestMoveOrder.addAll(0, bestMoveOrder);
	}
	
	public final List<Move> getBestMoveOrderCopy() {
		List<Move> bestMoveOrderCopy=new ArrayList<Move>();
		for(int i=0;i<bestMoveOrder.size();i++) {
			bestMoveOrderCopy.add(i,bestMoveOrder.get(i));
		}
		return bestMoveOrderCopy;
	}
	
	public final List<Move> getBestMoveOrder() {
		return bestMoveOrder;
	}
	
}
