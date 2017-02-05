package fr.xebia.tictactoebot.service.impl;

import org.springframework.stereotype.Service;

import fr.xebia.tictactoebot.exception.InvalidPositionException;
import fr.xebia.tictactoebot.service.TicTacToeGameService;
import fr.xebia.tictactoebot.service.impl.game.Position;
import fr.xebia.tictactoebot.service.impl.game.ai.GameIntelligenceAgent;
import fr.xebia.tictactoebot.service.impl.game.ai.MinimaxAgent;
import fr.xebia.tictactoebot.service.impl.game.ai.heuristic.tictactoe.TicTacToeEvaluator;
import fr.xebia.tictactoebot.service.impl.game.game.GameBoard;
import fr.xebia.tictactoebot.service.impl.game.game.TicTacToeGameState;
import fr.xebia.tictactoebot.service.impl.game.game.TicTacToeGameState.Player;

@Service
public class DefaultTicTacToeGameService implements TicTacToeGameService {

	private static final Player humanPlayer = Player.O;
	private static final TicTacToeEvaluator evaluator = new TicTacToeEvaluator(
			humanPlayer);
	private static final GameIntelligenceAgent<TicTacToeGameState> agent = new MinimaxAgent<>(
			evaluator);
	private static final String INSTRUCTION_TEXT = "Enter '<row>,<col>' to play a position. For example, '0,2'.\n";

	@Override
	public TicTacToeGameState createNewGame() {
		return new TicTacToeGameState();
	}

	@Override
	public String getInstructions() {
		return INSTRUCTION_TEXT;
	}

	@Override
	public void continueGame(TicTacToeGameState game, String input)
			throws InvalidPositionException {
		moveHuman(game, input);
		moveComputer(game);
	}

	private void moveComputer(TicTacToeGameState game) {
		TicTacToeGameState nextState = agent.evaluateNextState(game);
		if (nextState == null) {
			return;
		}
		Position nextMove = nextState.getLastMove();
		game.play(nextMove.getRow(), nextMove.getCol());
		game.switchPlayer();

	}

	private void moveHuman(TicTacToeGameState game, String input)
			throws InvalidPositionException {

		Position userPosition = getPosition(input);

		try {
			if (game.play(userPosition.getRow(), userPosition.getCol())) {
				game.switchPlayer();
			} else {
				throw new InvalidPositionException("(" + userPosition.getRow()
						+ "," + userPosition.getCol()
						+ ") has already been taken.\n" + getInstructions());
			}
		} catch (IllegalArgumentException e) {
			throw new InvalidPositionException(
					"(" + userPosition.getRow() + "," + userPosition.getCol()
							+ ") is not on the board.\n" + getInstructions());
		}
	}

	private Position getPosition(String input) throws InvalidPositionException {
		String[] posInput = input.split(",");
		if (posInput.length != 2) {
			throw new InvalidPositionException(getInstructions());
		}
		int row;
		int col;
		try {
			row = Integer.parseInt(posInput[0]);
			col = Integer.parseInt(posInput[1]);
		} catch (NumberFormatException e) {
			throw new InvalidPositionException(getInstructions());
		}
		return new Position(row, col);
	}

	@Override
	public String getGameAsString(TicTacToeGameState game) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			sb.append(getRow(i, game.getGameBoard()));
			sb.append("\n");
		}
		return sb.toString();
	}

	private String getRow(int row, GameBoard gameBoard) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			sb.append(markToString(gameBoard.getMark(row, i)));
		}
		return sb.toString();
	}

	private String markToString(Player player) {
		String s = "";
		if (player == null) {
			s = ":white_large_square:";
		} else {
			switch (player) {
				case O :
					s = ":o:";
					break;
				case X :
					s = ":x:";
					break;
			}
		}
		return s;
	}

}
