package backend.model.minimaxAB;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Keys;
import common.State;
import backend.model.minimaxAB.dataobjects.Board;
import backend.model.minimaxAB.dataobjects.Direction;

/**
 * The AIsolver class that uses Artificial Intelligence to estimate the next
 * move.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public class MinimaxAB {

	/**
	 * Player vs Computer enum class
	 */
	public enum Player {
		/**
		 * Computer
		 */
		COMPUTER,

		/**
		 * User
		 */
		USER
	}

	/**
	 * Method that finds the best next move.
	 * 
	 * @param theBoard
	 * @param depth
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static int findBestMove(State currentState, int depth) throws CloneNotSupportedException {
		
        Board theBoard = new Board(currentState.getCopyBoard(), currentState.getScore());
		Map<String, Object> result = alphabeta(theBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, Player.USER);
		
		if(!(result.get("Direction")==null))
			return ((Direction) result.get("Direction")).getCode();
		System.out.println("The direction is null");
		return Keys.UP;
	}

	

	/**
	 * Finds the best move bay using the Alpha-Beta pruning algorithm.
	 * 
	 * @param theBoard
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param player
	 * @return
	 * @throws CloneNotSupportedException
	 */
	
	
	private static Map<String, Object> alphabeta(Board theBoard, int depth,
			int alpha, int beta, Player player)
			throws CloneNotSupportedException {
		Map<String, Object> result = new HashMap<>();

		Direction bestDirection = null;
		int bestScore;

		if (theBoard.isGameTerminated()) {
			bestDirection = null;
			if (theBoard.hasWon()) {
				bestScore = Integer.MAX_VALUE; // highest possible score
			} else {
				bestScore = Math.min(theBoard.getScore(), 1); // lowest possible
																// score
			}
		} else if (depth == 0) {
			bestScore = heuristicScore(theBoard.getScore(),
					theBoard.getNumberOfEmptyCells(),
					calculateClusteringScore(theBoard.getBoardArray()));
		} else {
			if (player == Player.USER) {
				for (Direction direction : Direction.values()) {
					Board newBoard = (Board) theBoard.clone();

					int points = newBoard.move(direction);

					if (points == 0
							&& newBoard.isEqual(theBoard.getBoardArray(),
									newBoard.getBoardArray())) {
						continue;
					}

					Map<String, Object> currentResult = alphabeta(newBoard,
							depth - 1, alpha, beta, Player.COMPUTER);
					int currentScore = ((Number) currentResult.get("Score"))
							.intValue();

					if (currentScore > alpha) { // maximize score
						alpha = currentScore;
						bestDirection = direction;
					}

					if (beta <= alpha) {
						break; // beta cutoff
					}
				}

				bestScore = alpha;
			} else {
				List<Integer> moves = theBoard.getEmptyCellIds();
				int[] possibleValues = { 2, 4 };

				int i, j;
				abloop: for (Integer cellId : moves) {
					i = cellId / Board.BOARD_SIZE;
					j = cellId % Board.BOARD_SIZE;

					for (int value : possibleValues) {
						Board newBoard = (Board) theBoard.clone();
						newBoard.setEmptyCell(i, j, value);

						Map<String, Object> currentResult = alphabeta(newBoard,
								depth - 1, alpha, beta, Player.USER);
						int currentScore = ((Number) currentResult.get("Score"))
								.intValue();
						if (currentScore < beta) { // minimize best score
							beta = currentScore;
						}

						if (beta <= alpha) {
							break abloop; // alpha cutoff
						}
					}
				}

				bestScore = beta;

				if (moves.isEmpty()) {
					bestScore = 0;
				}
			}
		}

		result.put("Score", bestScore);
		result.put("Direction", bestDirection);

		return result;
	}

	/**
	 * Estimates a heuristic score by taking into account the real score, the
	 * number of empty cells and the clustering score of the board.
	 * 
	 * @param actualScore
	 * @param numberOfEmptyCells
	 * @param clusteringScore
	 * @return
	 */
	private static int heuristicScore(int actualScore, int numberOfEmptyCells,
			int clusteringScore) {
		int score = (int) (actualScore + Math.log(actualScore)
				* numberOfEmptyCells - clusteringScore);
		return Math.max(score, Math.min(actualScore, 1));
	}

	/**
	 * Calculates a heuristic variance-like score that measures how clustered
	 * the board is.
	 * 
	 * @param boardArray
	 * @return
	 */
	private static int calculateClusteringScore(int[][] boardArray) {
		int clusteringScore = 0;

		int[] neighbors = { -1, 0, 1 };

		for (int i = 0; i < boardArray.length; ++i) {
			for (int j = 0; j < boardArray.length; ++j) {
				if (boardArray[i][j] == 0) {
					continue; // ignore empty cells
				}

				// clusteringScore-=boardArray[i][j];

				// for every pixel find the distance from each neightbors
				int numOfNeighbors = 0;
				int sum = 0;
				for (int k : neighbors) {
					int x = i + k;
					if (x < 0 || x >= boardArray.length) {
						continue;
					}
					for (int l : neighbors) {
						int y = j + l;
						if (y < 0 || y >= boardArray.length) {
							continue;
						}

						if (boardArray[x][y] > 0) {
							++numOfNeighbors;
							sum += Math
									.abs(boardArray[i][j] - boardArray[x][y]);
						}

					}
				}

				clusteringScore += sum / numOfNeighbors;
			}
		}

		return clusteringScore;
	}

}
