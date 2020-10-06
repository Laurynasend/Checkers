package chess.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import ai.MinMax;
import chess.board.Move;
import chess.Player.Player;
import chess.board.Board;
import chess.board.BoardMisc;
import chess.board.Tile;
import chess.pieces.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

public class Table implements PropertyChangeListener {

	private final BoardPanel boardPanel;

	private final JFrame gameFrame;
	
	private final Dimension FRAME_DIMENSION=new Dimension(700,700);
	private final Dimension BOARD_PANEL_DIMENSION = new Dimension(600,600);
	private final Dimension TILE_PANEL_DIMENSION = new Dimension(30,30);
	
	private final Color lightSquare=new Color (240,217,181);
	private final Color darkSquare=new Color (181,136,99);
	private int sourceCoordinate=-1;
	private int destinationCoordinate=-1;
	private Side computerPlayerColour=Side.BLACK;
	private boolean flipBoardEnabled;
	private long thinkTime=500;
	private int depth;

	public Table () {
		Board.startingBoard();
		Board tryBoard=new Board();
		tryBoard.addPropertyChangeListener(this);
		this.gameFrame=new JFrame("LE Checkers");
		
		
		JMenuBar menuBar=new JMenuBar();
		JMenu boardOptions;
		JMenu game;
		
		boardOptions=populateBoardBar();
		game=populateGameBar();
		menuBar.add(boardOptions);
		menuBar.add(game);
		gameFrame.setJMenuBar(menuBar);
		
		this.boardPanel=new BoardPanel();
		
		
		this.gameFrame.add(boardPanel);
		this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.gameFrame.setSize(FRAME_DIMENSION);
		this.gameFrame.setVisible(true);
	}
	
	private void computerToMove()  {
		SwingWorker<Move,Void> computerThinks=new SwingWorker<Move,Void>(){
			@Override
			protected Move doInBackground() throws Exception {
				MinMax AI=new MinMax();
				Move bestMove=AI.IterativeDeepening(thinkTime, depth);
				return bestMove;
				
		}
			@Override
			protected void done() {
			try {
				Move bestComputerMove=get();
				Board.updateBoard(bestComputerMove.getStartingCoordinate(), bestComputerMove.destinationCoordinate);
				boardPanel.updateGui();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			}	};
			computerThinks.execute();
	}

		private JMenu populateGameBar() {
		JMenu game=new JMenu("Game");
		JMenu chooseSide= new JMenu("Choose Side");
		JMenuItem wantsBlack=new JMenuItem("Play Black Pieces");
		JMenuItem wantsRed=new JMenuItem("Play Red Pieces");
		//
		wantsBlack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				computerPlayerColour=Side.WHITE;
				if(Board.getPlayerToMove().equals(computerPlayerColour)) {
					computerToMove();
				}
				if(Board.getBottomBoardPlayerColour()==Side.WHITE) {Board.flipBoard();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						boardPanel.updateGui();
						
					}
					
				});}
				
			}
			
		});
		
		//
		
		wantsRed.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				computerPlayerColour=Side.BLACK;
				if(Board.getPlayerToMove().equals(computerPlayerColour)) {
					computerToMove();
				}
				if(Board.getBottomBoardPlayerColour()==Side.BLACK) {Board.flipBoard();
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						boardPanel.updateGui();
						
					}
					
				});}
				
			}
			
		});
		//
		
		chooseSide.add(wantsRed);
		chooseSide.add(wantsBlack);
		game.add(chooseSide);
		
		JMenu difficulty=new JMenu("Difficulty");
		JMenuItem basic=new JMenuItem("First Time Playing  (~ 0 sec)");
		JMenuItem easy=new JMenuItem("Easy               (~ 0 sec)");
		JMenuItem medium=new JMenuItem("Medium         ( ~ 0.1 sec)");
		JMenuItem hard=new JMenuItem("Hard               ( ~ 0.5 sec)");
		JMenuItem hardest=new JMenuItem("Very Hard      ( ~ 2.2 sec)");
		JMenuItem extreme=new JMenuItem("You Crazy?   ( ~ 10 sec)");
		basic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=0;
				depth=1;
				
			}
			
		});
		
		easy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=0;
				depth=2;
				
			}
			
		});
		
		medium.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=100;
				depth=0;
			}
			
		});
		
		hard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=500;
				depth=0;
			}
			
		});
		
		hardest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=2200;
				depth=0;
			}
			
		});
		
		extreme.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				thinkTime=10000;
				depth=0;
			}
			
		});
		
		difficulty.add(basic);
		difficulty.add(easy);
		difficulty.add(medium);
		difficulty.add(hard);
		difficulty.add(hardest);
		difficulty.add(extreme);
		
		JMenu playAgainst= new JMenu("Play Against ...");
		JMenuItem playAgainstHuman=new JMenuItem("Human");
		JMenuItem playAgainstComputer=new JMenuItem("Computer");
		playAgainstHuman.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				computerPlayerColour=null;
			}
			
		});
		
		playAgainstComputer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				computerPlayerColour=Side.BLACK;
				
			}
			
		});
		
		playAgainst.add(playAgainstHuman);
		playAgainst.add(playAgainstComputer);
		
		game.add(playAgainst);
		game.add(difficulty);
		
		return game;
		
	}

	private JMenu populateBoardBar() {
		JMenu boardOptions=new JMenu("Board");
		
		JMenuItem newBoard=new JMenuItem("New board");
		newBoard.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Board.startingBoard();
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						boardPanel.updateGui();	
					}});
				}});
		boardOptions.add(newBoard);
		
		
		JMenuItem flipBoard=new JMenuItem("Flip Board");
		flipBoard.addActionListener(new ActionListener ()
				{

					@Override
					public void actionPerformed(ActionEvent e) {
						Board.flipBoard();
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								boardPanel.updateGui();
							}
						});
					}
			
				});
		boardOptions.add(flipBoard);
		
		
		return boardOptions;
	}
	

	private class BoardPanel extends JPanel {
		List <TilePanel> boardTiles;
		
		BoardPanel () {
			super(new GridLayout(8,8));
			this.boardTiles=new ArrayList<>();
			
			if(flipBoardEnabled==true) {
				for(int i =63;i>=0;i--) {
					
				TilePanel tilePanel=new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}}
			else {
			for(int i =0;i<64;i++) {
				TilePanel tilePanel=new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
		}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void updateGui() {
			removeAll();
			for(final TilePanel tilePanel:boardTiles) {
				tilePanel.drawTile();
				add(tilePanel);
			}
			validate();
			repaint();
		}
		
		
	}
	
	private class TilePanel extends JPanel {
		private final int tileId;
	
		TilePanel(final BoardPanel boardPanel, final int id) {
			super(new GridBagLayout());
			this.tileId=id;
			MousePanel mousePanel=new MousePanel();
			addMouseListener(mousePanel);
			setPreferredSize(TILE_PANEL_DIMENSION);
			AssignColor();
			tilePieceIcon();
			validate();
			
		}
		
		public void drawTile () {
			AssignColor();
			tilePieceIcon();
			highlightTile();
			validate ();
			repaint();
		}

		private void AssignColor() {
			if(BoardMisc.FIRST_ROW[this.tileId] || BoardMisc.THIRD_ROW[this.tileId] || BoardMisc.FIFTH_ROW[this.tileId] || BoardMisc.SEVENTH_ROW[this.tileId]) {
				if (this.tileId%2==0) {setBackground(lightSquare);
				} else {setBackground(darkSquare);}
		}
			 else if ( BoardMisc.SECOND_ROW[this.tileId] || BoardMisc.FOURTH_ROW[this.tileId] || BoardMisc.SIXTH_ROW[this.tileId] || BoardMisc.EIGHTH_ROW[this.tileId]) {
				if (this.tileId%2==0) {setBackground(darkSquare);
				} else {setBackground(lightSquare);}
		}
	}
		
		private void tilePieceIcon () {
			this.removeAll();
			if(Board.getTilePlayingBoard(this.tileId).isTileOccupied()) {
				try {
				int pieceValue=Board.getTilePlayingBoard(tileId).getPiece().getPieceValue();
				final BufferedImage image;
				if (pieceValue==100) {image=ImageIO.read(getClass().getResource("BM.png"));}
				else if (pieceValue==-100) {image=ImageIO.read(getClass().getResource("RM.png"));}
				else if(pieceValue==200) {image=ImageIO.read(getClass().getResource("BK.png"));}
				else {image=ImageIO.read(getClass().getResource("RK.png"));}
				add(new JLabel(new ImageIcon(image)));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void highlightTile () {
			//Hailaitinu laukelius
			
			if(sourceCoordinate !=-1) {
			if(Board.getTilePlayingBoard(sourceCoordinate).getPiece()!=null) {
				Collection<Move> possibleDestinations=Board.calculatePossibleMoves(Board.getPlayingBoard(), Board.getPlayerToMove());
				Collection<Move> possibleSelectedPieceDestinations=Board.getTilePlayingBoard(sourceCoordinate).getPiece().calculateMoves(sourceCoordinate, Board.getPlayingBoard());
				
			for(Move pieceMove: possibleSelectedPieceDestinations) {
				
				for(Move moveAll : possibleDestinations) {
					if(pieceMove.destinationCoordinate==tileId && pieceMove.destinationCoordinate==moveAll.destinationCoordinate 
							&& pieceMove.movedPiece.isPieceAttacking(pieceMove.getStartingCoordinate(), Board.getPlayingBoard())==Board.getIsPlayerAttacking()) {
						
						try{
							add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("highlight.png")))));
							break;
							}
						catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
			}
		}
	}
			
		}
		
		public int getTileId () {
			return tileId;
		}
				
		private class MousePanel implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*if(!Board.getPlayerToMove().equals(computerPlayerColour)) {
				final boolean isTileOccupied=Board.getTilePlayingBoard(tileId).isTileOccupied();
				if(sourceCoordinate==-1) {
					if(isTileOccupied && Board.getTilePlayingBoard(tileId).getPiece().pieceSide==Board.getPlayerToMove()) {
						sourceCoordinate=tileId;
						
					}
					
				}
				else if (sourceCoordinate !=-1 && isTileOccupied){
					if(Board.getTilePlayingBoard(tileId).getPiece().pieceSide==Board.getPlayerToMove()) {
						sourceCoordinate=tileId;
					}
					else {sourceCoordinate=-1;}
				} 
				else {
					destinationCoordinate=tileId;
					Board.updateBoard(sourceCoordinate, destinationCoordinate);
					sourceCoordinate=-1;
					destinationCoordinate=-1;
					
				
				}

			}*/
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				final boolean isTileOccupied=Board.getTilePlayingBoard(tileId).isTileOccupied();
				if(!Board.getPlayerToMove().equals(computerPlayerColour)) {
				if(sourceCoordinate==-1) {
					if(isTileOccupied && Board.getTilePlayingBoard(tileId).getPiece().pieceSide==Board.getPlayerToMove()) {
						sourceCoordinate=tileId;
						
					}
					
				}
				else if (sourceCoordinate !=-1 && isTileOccupied){
					if(Board.getTilePlayingBoard(tileId).getPiece().pieceSide==Board.getPlayerToMove()) {
						sourceCoordinate=tileId;
					}
					else {sourceCoordinate=-1;}
				} 
				else {
					destinationCoordinate=tileId;
					Board.updateBoard(sourceCoordinate, destinationCoordinate);
					sourceCoordinate=-1;
					destinationCoordinate=-1;

				}
				
				SwingUtilities.invokeLater(new Runnable () {

					@Override
					public void run() {
						boardPanel.updateGui();
						
					}
					
				});
			}
			}

			@Override
			public void mouseReleased(MouseEvent e) {			
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		
		
		if(Board.getPlayerToMove().equals(Side.WHITE) && (Board.calculatePossibleMoves(Board.getPlayingBoard(), Side.WHITE).size()==0 || Board.sidePieces(Board.getPlayingBoard(), Side.WHITE).size()==0)) {
			int response = JOptionPane.showConfirmDialog(null, "Black won !   Do you want to start a new game ?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(response==JOptionPane.YES_OPTION) {
				Board.startingBoard();
			}

		}
		else if (Board.getPlayerToMove().equals(Side.BLACK) && (Board.calculatePossibleMoves(Board.getPlayingBoard(), Side.BLACK).size()==0 || Board.sidePieces(Board.getPlayingBoard(), Side.BLACK).size()==0)) {
			int response = JOptionPane.showConfirmDialog(null, "Red won !   Do you want to start a new game ?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(response==JOptionPane.YES_OPTION) {
				Board.startingBoard();
			}
	
		}
		else if (Board.getMovesWithoutCaptureCount()>=BoardMisc.drawGameMoveCount) {
			int response = JOptionPane.showConfirmDialog(null, "It is a draw !   Do you want to start a new game ?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(response==JOptionPane.YES_OPTION) {
				Board.startingBoard();
			} else {Board.setMovesWithoutCaptureCount(0);}
		}
		
		else {
			if(Board.getPlayerToMove().equals(computerPlayerColour)) {
			computerToMove();}
			}
	}
}
