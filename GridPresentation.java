import java.util.ArrayList;
import java.util.Collections;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class Grid_Driver extends JFrame {
      private static final long serialVersionUID = 1L;
      private boolean bombGrid[][];
      private int countGrid[][];
      private int numRows, numColumns, numBombs;
      private int safeCount;
      private JButton buttons[][];
      
      public Grid_Driver() {      
            this(10, 10, 25);
      }
     
      public Grid_Driver(int rows, int cols) {
    	   this(rows, cols, 25);
      }

      public Grid_Driver(int rows, int cols, int numBombs) {
            super("MineSweeper");
            numRows = rows;
            numColumns = cols;
            this.numBombs = numBombs;
            createBombGrid();
            createCountGrid();
            safeCount = (getNumRows() * getNumColumns()) - getNumBombs();
            buttons = new JButton[getNumRows()][getNumColumns()];
            setLayout(new GridLayout(getNumRows(), getNumColumns()));
            for (int i = 0; i < getNumRows(); i++) {
            	for (int j = 0; j < getNumColumns(); j++) {
                        buttons[i][j] = new JButton();
                        buttons[i][j].addActionListener(new CellListener(i, j));
                        add(buttons[i][j]);
                  }
            }
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(700, 700);
            setVisible(true);
      }

      private void createBombGrid() {
            bombGrid = new boolean[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                  for (int j = 0; j < numColumns; j++) {
                        bombGrid[i][j] = false;
                  }
            }

            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 1; i < numRows * numColumns; i++) {
                  list.add( Integer.valueOf(i));
            }

            Collections.shuffle(list);
            for (int i = 0; i < numBombs; i++) {
                  int number = (list.get(i));
                  int row = Integer.valueOf(number / numColumns);
                  int column = Integer.valueOf(number % numColumns);
                  bombGrid[row][column] = true;
            }
      }

      private void createCountGrid() {
            countGrid = new int[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                  for (int j = 0; j < numColumns; j++) {
                        countGrid[i][j] = getCountAtLocation(i, j);
                  }
            }
      }

      public boolean IsBombAtLocation(int row, int column) {
            return (bombGrid[row][column]);
      }

      public int getCountAtLocation(int row, int column) {
            int count = 0;
            if (IsBombAtLocation(row, column)) {
                  count++;
            }

            if (row + 1 < numRows) {
                  if (IsBombAtLocation(row + 1, column))
                        count++;
                  	if (column + 1 < numColumns) {
                        if (IsBombAtLocation(row + 1, column + 1))
                              count++;
                  }

                  if (column - 1 >= 0) { 
                        if (IsBombAtLocation(row + 1, column - 1)) 
                              count++;
                  }
            }

            if (row - 1 >= 0) { 
                  if (IsBombAtLocation(row - 1, column)) 
                        count++;
                  	if (column - 1 >= 0) { 
                        if (IsBombAtLocation(row - 1, column - 1)) 
                              count++;
                  }

                  if (column + 1 < numColumns) { 
                        if (IsBombAtLocation(row - 1, column + 1)) 
                              count++;
                  }
            }

            if (column + 1 < numColumns) {                
                  if (IsBombAtLocation(row, column + 1)) 
                        count++;
            }

            if (column - 1 >= 0) { 
                  if (IsBombAtLocation(row, column - 1))
                        count++;
            }
            return count;
      }

      public int getNumRows() {
            return numRows;
      }

      public int getNumColumns() {
            return numColumns;
      }

      public int getNumBombs() {
            return numBombs;
      }

      public boolean[][] getBombGrid() {
            boolean[][] result = new boolean[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                  System.arraycopy(bombGrid[i], 0, result[i], 0, bombGrid[i].length);
            }
            return result;
      }

      public int[][] getCountGrid() {
            int[][] result = new int[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                  System.arraycopy(countGrid[i], 0, result[i], 0, countGrid[i].length);
            }
            return result;
      }

      public static void main(String args[]) {
           new Grid_Driver();
      }

      private void reset() {
            createBombGrid();
            createCountGrid();
            safeCount = (getNumRows() * getNumColumns()) - getNumBombs();
            for (int i = 0; i < getNumRows(); i++) {
                  for (int j = 0; j < getNumColumns(); j++) {
                        buttons[i][j].setText("");
                        buttons[i][j].setEnabled(true);
                  }
            }
      }

      private class CellListener implements ActionListener {
            int row, col;
            public CellListener(int row, int col) {
                  this.row = row;
                  this.col = col;
            }

            @Override

            public void actionPerformed(ActionEvent e) {
                  if (IsBombAtLocation(row, col)) {
                        int[][] counts = getCountGrid();
                        	for (int i = 0; i < getNumRows(); i++) {
                              for (int j = 0; j < getNumColumns(); j++) {
                                    if (IsBombAtLocation(i, j)) {
                                          buttons[i][j].setText("bomb");
                                    }                                  
                                    else {
                                          buttons[i][j].setText(String.valueOf(counts[i][j]));
                                    }
                                    buttons[i][j].setEnabled(false);
                              }
                        }

                        int status = JOptionPane.showConfirmDialog(null,
                                    "You lost. Do you want to play again?", "Game over",
                                    JOptionPane.YES_NO_OPTION);
                        if (status == JOptionPane.YES_OPTION) {
                              reset();
                        }                       
                        else {
                              System.exit(0);
                        }
                  }                  
                  else {
                        buttons[row][col].setText(String.valueOf(getCountAtLocation(
                                   row, col)));
                        buttons[row][col].setEnabled(false);
                        safeCount--;
                        if (safeCount == 0) {
                              int status = JOptionPane.showConfirmDialog(null,
                                          "You won! Do you want to play again?", "Game over",
                                          JOptionPane.YES_NO_OPTION);
                              if (status == JOptionPane.YES_OPTION) {
                                    reset();                              
                              }
                                else {
                                    System.exit(0);
                              }
                        }
                  }
            }
      }
}