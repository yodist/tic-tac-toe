import java.util.Scanner;
/**
 * Tic-Tac-Toe: Two-player console, non-graphics, non-OO version.
 * All variables/methods are declared as static (belong to the class)
 *  in the non-OO version.
 */
public class TTTConsoleNonOO2P {
   // Name-constants to represent the seeds and cell contents
   public static final int EMPTY = 0;
   public static final int CROSS = 1;
   public static final int NOUGHT = 2;
 
   // Name-constants to represent the various states of the game
   public static final int PLAYING = 0;
   public static final int DRAW = 1;
   public static final int CROSS_WON = 2;
   public static final int NOUGHT_WON = 3;
 
   // The game board and the game status
   public static int boardLength = 3; // Scalable game board, rows and cols of the board is not final
   public static int rows = boardLength, cols = boardLength; // number of rows and columns with default 3 x 3
   public static int[][] board = new int[rows][cols]; // game board in 2D array
                                                      //  containing (EMPTY, CROSS, NOUGHT)
   public static int currentState;  // the current state of the game
                                    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
   public static int currentPlayer; // the current player 
                                    // (CROSS or NOUGHT)
   public static int currentRow, currentCol; // current seed's row and column
 
   public static Scanner in = new Scanner(System.in); // the input Scanner
 
   /** The entry main method (the program starts here) */
   public static void main(String[] args) {
      // Initialize the game-board and current status
      initGame();
      // Play the game once
      do {
         playerMove(currentPlayer); // update currentRow and currentCol
         updateGame(currentPlayer, currentRow, currentCol); // update currentState
         printBoard();
         // Print message if game-over
         if (currentState == CROSS_WON) {
            System.out.println("'X' won! Bye!");
         } else if (currentState == NOUGHT_WON) {
            System.out.println("'O' won! Bye!");
         } else if (currentState == DRAW) {
            System.out.println("It's a Draw! Bye!");
         }
         // Switch player
         currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
      } while (currentState == PLAYING); // repeat if not game-over
   }
 
   /** Initialize the game-board size, game-board contents and the current states */
   public static void initGame() {
      boolean validInput = false; // for input validation
      // Ask for board size 
      do {
         System.out.print("Define your board game size (n x n)[3-5]: ");
         rows = cols = boardLength = in.nextInt();
         if (boardLength > 5) {
            System.out.println("The board size is too big. Please, try again...");
         } else if (boardLength < 3) {
            System.out.println("The board size is too small. Please, try again...");
         } else {
            validInput = true;  // input okay, exit loop
            System.out.println("Board size is: " + rows + " x " + cols);
            board = new int[rows][cols];
         }
      } while (!validInput);
      for (int row = 0; row < rows; ++row) {
         for (int col = 0; col < cols; ++col) {
            board[row][col] = EMPTY;  // all cells empty
         }
      }
      currentState = PLAYING; // ready to play
      currentPlayer = CROSS;  // cross plays first
   }
 
   /** Player with the "theSeed" makes one move, with input validation.
       Update global variables "currentRow" and "currentCol". */
   public static void playerMove(int theSeed) {
      boolean validInput = false;  // for input validation
      do {
         if (theSeed == CROSS) {
            System.out.print("Player 'X', enter your move (row[1-" + boardLength + "] column[1-" + boardLength + "]): ");
         } else {
            System.out.print("Player 'O', enter your move (row[1-" + boardLength + "] column[1-" + boardLength + "]): ");
         }
         int row = in.nextInt() - 1;  // array index starts at 0 instead of 1
         int col = in.nextInt() - 1;
         if (row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] == EMPTY) {
            currentRow = row;
            currentCol = col;
            board[currentRow][currentCol] = theSeed;  // update game-board content
            validInput = true;  // input okay, exit loop
         } else {
            System.out.println("This move at (" + (row + 1) + "," + (col + 1)
                  + ") is not valid. Try again...");
         }
      } while (!validInput);  // repeat until input is valid
   }
 
   /** Update the "currentState" after the player with "theSeed" has placed on
       (currentRow, currentCol). */
   public static void updateGame(int theSeed, int currentRow, int currentCol) {
      if (hasWon(theSeed, currentRow, currentCol)) {  // check if winning move
         currentState = (theSeed == CROSS) ? CROSS_WON : NOUGHT_WON;
      } else if (isDraw(theSeed)) {  // check for draw
         currentState = DRAW;
      }
      // Otherwise, no change to currentState (still PLAYING).
   }
 
   /** Return true if it is a draw (no more empty cell) */
   // TODO: Shall declare draw if no player can "possibly" win
   public static boolean isDraw(int theSeed) {
      boolean isDraw = false;
      for (int row = 0; row < rows; ++row) {
         for (int col = 0; col < cols; ++col) {
            if (board[row][col] == EMPTY) {
              // check whether the game will lead to absolute draw or not
              theSeed = (theSeed == CROSS) ? NOUGHT : CROSS;
              board[row][col] = theSeed;
              if (hasWon(theSeed, row, col)) {  // check if winning move
                board[row][col] = EMPTY;
                return false;
              } else {
                isDraw = isDraw(theSeed);
              }
              board[row][col] = EMPTY;
              if (!isDraw)
                return isDraw;
            }
         }
      }
      return true;  // no empty cell, it's a draw
   }
 
   /** Return true if the player with "theSeed" has won after placing at
       (currentRow, currentCol) */
   public static boolean hasWon(int theSeed, int currentRow, int currentCol) {
      boolean rowCheck = true, colCheck = true, diagCheck = true, backDiagCheck = true;
      for (int i = 0; i < boardLength; i++) {
        if (board[currentRow][i] != theSeed) 
          rowCheck = false;        // n-in-the-row 
        if (board[i][currentCol] != theSeed) 
          colCheck = false;        // n-in-the-column
        if (board[i][i] != theSeed)
          diagCheck = false;       // n-in-the-diagonal
        if (board[i][boardLength-i-1] != theSeed) // index is from 0 so "boardLength" subtracted by 1            
          backDiagCheck = false;   // n-in-the-opposite-diagonal
      }
      return rowCheck || colCheck || diagCheck || backDiagCheck;
   }
 
   /** Print the game board */
   public static void printBoard() {
      for (int row = 0; row < rows; ++row) {
         for (int col = 0; col < cols; ++col) {
            printCell(board[row][col]); // print each of the cells
            if (col != cols - 1) {
               System.out.print("|");   // print vertical partition
            }
         }
         System.out.println();
         if (row != rows - 1) {
            for (int col = 0; col < cols; ++col) {
              System.out.print("---"); // print horizontal partition 
              if (col != cols - 1)
                System.out.print("-");
            }
         }
         System.out.println();
      }
      System.out.println();
   }
 
   /** Print a cell with the specified "content" */
   public static void printCell(int content) {
      switch (content) {
         case EMPTY:  System.out.print("   "); break;
         case NOUGHT: System.out.print(" O "); break;
         case CROSS:  System.out.print(" X "); break;
      }
   }
}