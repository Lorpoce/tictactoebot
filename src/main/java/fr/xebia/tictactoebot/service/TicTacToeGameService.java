package fr.xebia.tictactoebot.service;

import fr.xebia.tictactoebot.exception.InvalidPositionException;
import fr.xebia.tictactoebot.service.impl.game.game.TicTacToeGameState;

public interface TicTacToeGameService {

	String getInstructions();

	TicTacToeGameState createNewGame();

	void continueGame(TicTacToeGameState game, String input)
			throws InvalidPositionException;

	String getGameAsString(TicTacToeGameState game);
}
