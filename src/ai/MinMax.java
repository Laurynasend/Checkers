package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import chess.board.Board;
import chess.board.BoardMisc;
import chess.board.CalcBoard;
import chess.board.Move;
import chess.board.Tile;
import chess.pieces.Piece;
import chess.pieces.Side;

public class MinMax {
	
private CalcBoard board=new CalcBoard(Board.getPlayingBoard(),Board.getPlayerToMove(), Board.getStartingCoordinate(), Board.getMovesWithoutCaptureCount());
private int depth;

// for iterative deepening
Map <Long, Memory> trTable=new HashMap<Long,Memory>();
private boolean timeOut;
private long TIME_OUT_MS;
private long startTime;

public final Move IterativeDeepening (final long thinkTime, final int depth) {
	TIME_OUT_MS=thinkTime;
	//filling array with random values
	ZobristValues.fillArray();

	
	int currentDepth=0;
	int i=0;
	timeOut=false;
	startTime=System.currentTimeMillis();
	long endTime=0;
	long laikas=0;
	long computerStartTime=0;
	
	Move bestMoveYet = null;
	Move trueBestMove = null;
		
	if (depth>0) {
		TIME_OUT_MS=20000;
		return ComputerMove(depth);
	} else {
	
	while (!timeOut) {
		if(i>0) {
			trueBestMove=bestMoveYet;
			System.out.println("Current depth  --- " + currentDepth + "    and current Best move ---  " + trueBestMove.getStartingCoordinate() + "  --->  " + trueBestMove.destinationCoordinate);
			System.out.println("Sio gylio skaiciavimo laikas =  " + laikas );
			System.out.println();
			
		}
		

		currentDepth=1+i;
		
		//instantly move if only one move is available
		if(currentDepth==1) {
			List<Move> mustTake=board.calculatePossibleMoves(board.getCurrentBoard(),board.getPlayerColour(), board.getStartingCoordinate());
			if (mustTake.size()==1){
			return mustTake.get(0);}
		}
		
		computerStartTime=System.currentTimeMillis();
		bestMoveYet=ComputerMove(currentDepth);
		endTime=System.currentTimeMillis();
		laikas=endTime-computerStartTime;
		
		i+=1;
	}
	
	return trueBestMove;
	}
}


public final Move ComputerMove (final int depth) {
	int highestValue=Integer.MIN_VALUE;
	int lowestValue=Integer.MAX_VALUE;
	Move bestMove=null;
	//alpha beta pruning
	int alpha=Integer.MIN_VALUE;
	int beta=Integer.MAX_VALUE;
	//transposition table
	List<Move> possibleMoves;
	List<Move> bestMoveOrder=new ArrayList<Move>();
	final long boardZobristValue=ZobristValues.boardZobristValue(board.getCurrentBoard(), board.getPlayerColour(), board.getStartingCoordinate());
	final boolean mapContainsZvalue=trTable.containsKey(boardZobristValue);
	if(mapContainsZvalue) {
	possibleMoves=trTable.get(boardZobristValue).getBestMoveOrderCopy();
	} else {
		possibleMoves=board.calculatePossibleMoves(board.getCurrentBoard(), board.getPlayerColour(), board.getStartingCoordinate());
		trTable.put(boardZobristValue, new Memory (getListCopy(possibleMoves)));
	} 

	for (Move possibleMove : possibleMoves) {
		if (System.currentTimeMillis()-startTime>TIME_OUT_MS) {
			timeOut=true;
			return bestMove;
		}
		CalcBoard possibleBoard=new CalcBoard(board.getCurrentBoardCopy(), board.getPlayerColour(), board.getStartingCoordinate(), Board.getMovesWithoutCaptureCount());		
		possibleBoard.makeMove(possibleMove.getStartingCoordinate(), possibleMove.destinationCoordinate);
		
		if(board.getPlayerColour()==possibleBoard.getPlayerColour()) {
			if (board.getPlayerColour()==Side.BLACK) {
				int currentValue=min(possibleBoard, depth, alpha, beta);
				if(lowestValue>currentValue) {lowestValue=currentValue;bestMove=possibleMove;bestMoveOrder.add(0,bestMove);beta=currentValue; }
			} else {
				int currentValue=max(possibleBoard, depth, alpha, beta);
				if(highestValue<currentValue) {highestValue=currentValue;bestMove=possibleMove;bestMoveOrder.add(0,bestMove);alpha=currentValue;}
			}
			
		} else {
			if (board.getPlayerColour()==Side.BLACK) {
				int currentValue=max(possibleBoard, depth-1, alpha, beta);
				if(lowestValue>currentValue) {
					lowestValue=currentValue;
					bestMove=possibleMove;
					bestMoveOrder.add(0,bestMove);
					beta=currentValue;
					}
				
			} else {
				int currentValue=min(possibleBoard, depth-1, alpha, beta);
				if(highestValue<currentValue) {
					highestValue=currentValue;
					bestMove=possibleMove;
					bestMoveOrder.add(0,bestMove);
					alpha=currentValue;
			}
			}
		}
		
	}

	trTable.get(boardZobristValue).updateBestMoveOrder(bestMoveOrder);
	return bestMove;
}


	private int min (CalcBoard board, int depth, int alpha, int beta) {
		if (System.currentTimeMillis()-startTime>TIME_OUT_MS) {
			timeOut=true;
			return beta;
		}
		
		//transposition table
		List<Move> possibleMoves;
		List<Move> bestMoveOrder=new ArrayList<Move>();
		final long boardZobristValue=ZobristValues.boardZobristValue(board.getCurrentBoard(), board.getPlayerColour(), board.getStartingCoordinate());
		final boolean mapContainsZvalue=trTable.containsKey(boardZobristValue);
		if(mapContainsZvalue) {
		possibleMoves=trTable.get(boardZobristValue).getBestMoveOrderCopy();
		} else {
			possibleMoves=board.calculatePossibleMoves(board.getCurrentBoard(), board.getPlayerColour(), board.getStartingCoordinate());
			trTable.put(boardZobristValue, new Memory (getListCopy(possibleMoves)));
			
		} 
		
		
		if(depth == 0 || board.getNumberPieces()==0 || possibleMoves.size()==0) {
			return boardEvaluation(board, depth, possibleMoves.size());
		} else {
			int lowestEvaluation=Integer.MAX_VALUE;
			for (Move move : possibleMoves) {
				
				CalcBoard possibleBoard=new CalcBoard(board.getCurrentBoardCopy(), board.getPlayerColour(), board.getStartingCoordinate(), Board.getMovesWithoutCaptureCount());
				possibleBoard.makeMove(move.getStartingCoordinate(), move.destinationCoordinate);
				

				//pirmas if tikrina ar baigesi ejimas
				if(board.getPlayerColour()==possibleBoard.getPlayerColour()) {
					int currentValue=min(possibleBoard, depth, alpha, beta);
					if(lowestEvaluation>currentValue) {
						lowestEvaluation=currentValue;
						bestMoveOrder.add(0,move);
					}
					//pruning
					if (beta>currentValue) {beta=currentValue;}
					if(beta<=alpha) {break;}
					
				} else {
				int currentValue=max(possibleBoard, depth-1, alpha, beta);
				if(lowestEvaluation>currentValue) {
					lowestEvaluation=currentValue;
					bestMoveOrder.add(0,move);
				}
				//pruning
				if (beta>currentValue) {beta=currentValue;}
				if(beta<=alpha) {break;}
			}
				
			}
			trTable.get(boardZobristValue).updateBestMoveOrder(bestMoveOrder);
			return lowestEvaluation;
		}
		
		
	}



	private int max(CalcBoard board, int depth, int alpha, int beta) {
		if (System.currentTimeMillis()-startTime>TIME_OUT_MS) {
			timeOut=true;
			return alpha;
		}
	
		//transposition table
		List<Move> possibleMoves;
		List<Move> bestMoveOrder=new ArrayList<Move>();
		long boardZobristValue=ZobristValues.boardZobristValue(board.getCurrentBoard(), board.getPlayerColour(),board.getStartingCoordinate());
		final boolean mapContainsZvalue=trTable.containsKey(boardZobristValue);
		if(mapContainsZvalue) {
		possibleMoves=trTable.get(boardZobristValue).getBestMoveOrderCopy();
		if(possibleMoves==trTable.get(boardZobristValue).getBestMoveOrder()) {System.out.println("rastas kas neturetu buti rastas");}
		} else {
			possibleMoves=board.calculatePossibleMoves(board.getCurrentBoard(), board.getPlayerColour(), board.getStartingCoordinate());
			trTable.put(boardZobristValue, new Memory (getListCopy(possibleMoves)));
		} 
		
		if(depth==0 || board.getNumberPieces()==0 || possibleMoves.size()==0) {
			return boardEvaluation(board, depth, possibleMoves.size());
		} else {
			int highestEvaluation=Integer.MIN_VALUE;
			for (Move move : possibleMoves) {
				CalcBoard possibleBoard=new CalcBoard(board.getCurrentBoardCopy(), board.getPlayerColour(), board.getStartingCoordinate(), Board.getMovesWithoutCaptureCount());
				possibleBoard.makeMove(move.getStartingCoordinate(), move.destinationCoordinate);
				
				//pirmas if tikrina ar baigesi ejimas
				if (board.getPlayerColour()==possibleBoard.getPlayerColour()) {
					int currentValue=max(possibleBoard, depth, alpha, beta);
					if(highestEvaluation<currentValue) {
						highestEvaluation=currentValue;	
						bestMoveOrder.add(0,move);
					}
						//pruning
					if (alpha<currentValue) {alpha=currentValue;}
					if(beta<=alpha) {break;}
					
				} else {
				int currentValue=min(possibleBoard, depth-1, alpha, beta);
				if(highestEvaluation<currentValue) {
					highestEvaluation=currentValue;
					bestMoveOrder.add(0,move);
				}
				
				//pruning
				if(alpha<currentValue) {alpha=currentValue;}
				if(beta<=alpha) {break;}
			}
			
			}
			trTable.get(boardZobristValue).updateBestMoveOrder(bestMoveOrder);
			return highestEvaluation;
		}
	}



	private final int boardEvaluation(CalcBoard board, final int depth, final int possibleMovesSize) {
		// saskiu verciu skirtumas bei galimu ejimu skaiciaus skirtumas
		//List<Piece> whitePieceList=new ArrayList<>();
		//List<Piece> blackPieceList=new ArrayList<>();
		//int endGamePieceDistanceScore = 0;
		
		int whitePieceScore=0;
		int blackPieceScore=0;
		final int blackPossibleMovesCount;
		final int whitePossibleMovesCount;
		
		if(board.getPlayerColour()==Side.BLACK) {
			 blackPossibleMovesCount=possibleMovesSize;
			 whitePossibleMovesCount=board.calculatePossibleMoves(board.getCurrentBoard(), Side.WHITE, board.getStartingCoordinate()).size();
			} else {
				whitePossibleMovesCount=possibleMovesSize;
				blackPossibleMovesCount=board.calculatePossibleMoves(board.getCurrentBoard(), Side.WHITE, board.getStartingCoordinate()).size();
			}
		
		
		
		
		for (int i =0;i<64;i++) {
			
			if(board.getCurrentBoard().get(i).isTileOccupied()) {
			if(board.getCurrentBoard().get(i).getPiece().pieceSide==Side.BLACK) {
				//blackPieceList.add(board.getCurrentBoard().get(i).getPiece()); // is esmes nereikalingas
				blackPieceScore+=board.getBoardTile(i).getPiece().getPieceValue();
			}
			else {
				//whitePieceList.add(board.getCurrentBoard().get(i).getPiece()); //is esmes nereikalingas
				whitePieceScore+=board.getBoardTile(i).getPiece().getPieceValue();

			}
		}
		}
		
		
		if (board.getPlayerColour()==Side.WHITE  && (whitePossibleMovesCount==0 || whitePieceScore==0)) {
			return -10000-depth*100;
		} else if (board.getPlayerColour()==Side.BLACK && (blackPossibleMovesCount==0 || blackPieceScore==0)) {
			return 10000+depth*100;
		} else {
		int pieceScore=-1*(whitePieceScore+blackPieceScore);
		int activeMovesScore=3*(whitePossibleMovesCount-blackPossibleMovesCount);
		
		/*
		 *for endgame
		 * if(blackPieceList.size()<4 || whitePieceList.size()<4) {
			if(pieceScore>=100) {
				
				for (int i=0;i<64;i++) {
					
					if(board.getBoardTile(i).isTileOccupied()) {
						
						if(board.getBoardTile(i).getPiece().getPieceValue()>0) {
							
							for ( int o=i+1;o<64;o++) {
								if(board.getBoardTile(o).isTileOccupied()) {if(board.getBoardTile(o).getPiece().getPieceValue()<0) {blackPieceDistanceToWhitePieces+=o-i;}}
							}
							
						}
					}
				}
			} else if (pieceScore<=-100){	
				for (int i=0;i<64;i++) {
				
				if(board.getBoardTile(i).isTileOccupied()) {
					
					if(board.getBoardTile(i).getPiece().getPieceValue()>0) {
						
						for ( int o=i+1;o<64;o++) {
							
							if(board.getBoardTile(o).isTileOccupied()) {if(board.getBoardTile(o).getPiece().getPieceValue()<0) {blackPieceDistanceToWhitePieces+=i-o;}}
						
					}
				}
			}
		}
	}		
			endGamePieceDistanceScore=blackPieceDistanceToWhitePieces/(blackPieceList.size()+whitePieceList.size()+11);
		}*/
		
		if(board.getMovesWithoutCaptureCount()>=BoardMisc.drawGameMoveCount) {return 0;}
		
		return pieceScore+activeMovesScore+randomNumber(0,3);
		}
	}
		
	
	private int randomNumber(final int min, final int max) {
		if(min>=max) {
			throw new IllegalArgumentException("Bad random number generator input");
		}
		Random randomInteger=new Random();
		
		return randomInteger.nextInt(max-min+1)+min;
	}


	public void updateBoard (Map <Integer, Tile> board, Side side, final int startingCoordinate) {
		this.board.setBoard(board);
		this.board.setPlayerColour(side);
		this.board.setStartingCoordinate(startingCoordinate);
	}
	
	public void setDepth(final int depth) {
		this.depth=depth;
	}
	
	public final List<Move> getListCopy(final List<Move> possibleMoves) {
		List<Move> possibleMovesCopy=new ArrayList<Move>();
		for(int i=0;i<possibleMoves.size();i++) {
			possibleMovesCopy.add(i,possibleMoves.get(i));
		}
		return possibleMovesCopy;
	}
	
}