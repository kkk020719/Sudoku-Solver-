package finalproject;

import java.util.*;
import java.io.*;


public class ChessSudoku
{
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
     * a standard Sudoku puzzle, SIZE is 3 and N is 9.
     */
    public int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0.
     */
    public int grid[][];

    /* Booleans indicating whether of not one or more of the chess rules should be
     * applied to this Sudoku.
     */
    public boolean knightRule;
    public boolean kingRule;
    public boolean queenRule;


    // Field that stores the same Sudoku puzzle solved in all possible ways
    public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();

    private boolean check(int[][] a, int i, int j,int num, boolean allSolution){
        for (int x = 0; x < N; x++){ //checks for the current row
            if (a[i][x] == num ){
                return false;
            }
        }
        for (int x = 0; x < N; x++){ //checks for the current column
            if (a[x][j] == num){
                return false;

            }
        }
        int gridNR = i - (i%SIZE);
        int gridNC = j - (j%SIZE);
        for (int x = gridNR; x < gridNR + SIZE; x++){
            for (int y = gridNC; y < gridNC + SIZE; y++){
                if (a[x][y] == num){
                    return false;
                }
            }
        }
        return true;
    }


    private boolean queen(int[][]a, int i, int j){
        for (int x = 1; x < N; x++){
            if (i + x < N && i + x >= 0 && j + x < N && j+x >= 0){
                if (a[i+x][j+x] == N){
                    return false;
                }
            }
            if (i + x < N && i + x >= 0 && j - x < N && j-x >= 0){
                if (a[i+x][j-x] == N){
                    return false;
                }
            }
            if (i - x < N && i - x >= 0 && j - x < N && j-x >= 0){
                if (a[i-x][j-x] == N){
                    return false;
                }
            }
            if (i - x < N && i - x >= 0 && j + x < N && j+x >= 0){
                if (a[i-x][j+x] == N){
                    return false;
                }
            }
        }
        return true;
    }


    private boolean king(int[][] a, int i, int j,int num){
        if (i-1 >= 0 && j - 1 >= 0 && a[i-1][j-1] == num || i - 1 >= 0 && j + 1 < N && a[i-1][j+1] == num || i + 1 < N && j - 1 >= 0 && a[i+1][j-1] == num ||i + 1 < N && j + 1 <N && a[i+1][j+1] == num){
            return false;
        }
        return true;
    }

    private boolean knight(int[][] a, int i, int j,int num){
        if (i - 2 >= 0 && j -1 >= 0 && a[i-2][j-1] == num || i - 2 >= 0 && j + 1 < N && a[i-2][j+1] == num || i -1 >= 0 && j + 2 < N && a[i-1][j+2] == num || i + 1 < N && j + 2 < N && a[i+1][j+2] == num || i + 2 < N && j + 1 < N && a[i+2][j+1] == num || i + 2 < N && j -1 >= 0 && a[i+2][j-1] == num || i + 1 < N && j -2 >= 0 && a[i+1][j-2] == num || i - 1 >= 0 && j-2 >= 0 && a[i-1][j-2]==num){
            return false;
        }
        return true;
    }

    private boolean sudokuSolver(ArrayList<int[]> cell, int[][] a, boolean allSolution) {
        int[] curr = min(a, cell, allSolution);
        if (curr == null && !allSolution) {
            return true;
        }
        if (curr == null && allSolution){
            ChessSudoku temp = new ChessSudoku(SIZE);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    temp.grid[i][j] = a[i][j];
                }
            }
            this.solutions.add(temp);
            return false;

        }

            for (int n = 1; n <= N; n++) {

                if (check(a, curr[0], curr[1], n, allSolution)) {
                    if (kingRule && queenRule && knightRule) {
                        if (king(a, curr[0], curr[1], n) && knight(a, curr[0], curr[1], n)) {
                            if (n != N) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);

                                }
                            } else if (queen(a, curr[0], curr[1])) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);
                                }
                            }
                        }
                    } else if (!kingRule && !queenRule && !knightRule) {
                        a[curr[0]][curr[1]] = n;
                        cell.remove(curr);
                        if (sudokuSolver(cell, a, allSolution)) {
                            return true;
                        } else {
                            a[curr[0]][curr[1]] = 0;
                            cell.add(curr);
                        }

                    } else if (kingRule && queenRule) {
                        if (king(a, curr[0], curr[1], n)) {
                            if (n != N) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);
                                }
                            } else if (queen(a, curr[0], curr[1])) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);
                                }
                            }
                        }
                    } else if (kingRule && knightRule) {
                        if (king(a, curr[0], curr[1], n) && knight(a, curr[0], curr[1], n)) {
                            a[curr[0]][curr[1]] = n;
                            cell.remove(curr);
                            if (sudokuSolver(cell, a, allSolution)) {
                                return true;
                            } else {
                                a[curr[0]][curr[1]] = 0;
                                cell.add(curr);
                            }
                        }
                    } else if (queenRule && knightRule) {
                        if (knight(a, curr[0], curr[1], n)) {
                            if (n != N) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);
                                }
                            } else if (queen(a, curr[0], curr[1])) {
                                a[curr[0]][curr[1]] = n;
                                cell.remove(curr);
                                if (sudokuSolver(cell, a, allSolution)) {
                                    return true;
                                } else {
                                    a[curr[0]][curr[1]] = 0;
                                    cell.add(curr);
                                }
                            }
                        }
                    } else if (kingRule) {
                        if (king(a, curr[0], curr[1], n)) {
                            a[curr[0]][curr[1]] = n;
                            cell.remove(curr);
                            if (sudokuSolver(cell, a, allSolution)) {
                                return true;
                            } else {
                                a[curr[0]][curr[1]] = 0;
                                cell.add(curr);
                            }
                        }
                    } else if (knightRule) {
                        if (knight(a, curr[0], curr[1], n)) {
                            a[curr[0]][curr[1]] = n;
                            cell.remove(curr);
                            if (sudokuSolver(cell, a, allSolution)) {
                                return true;
                            } else {
                                a[curr[0]][curr[1]] = 0;
                                cell.add(curr);
                            }
                        }
                    } else {
                        if (n != N) {
                            a[curr[0]][curr[1]] = n;
                            cell.remove(curr);
                            if (sudokuSolver(cell, a, allSolution)) {
                                return true;
                            } else {
                                a[curr[0]][curr[1]] = 0;
                                cell.add(curr);
                            }
                        } else if (queen(a, curr[0], curr[1])) {
                            a[curr[0]][curr[1]] = n;
                            cell.remove(curr);
                            if (sudokuSolver(cell, a, allSolution)) {
                                return true;
                            } else {
                                a[curr[0]][curr[1]] = 0;
                                cell.add(curr);
                            }
                        }
                    }

                }
            }
            return false;
        }

    private int[] min(int[][]a, ArrayList<int[]> cell, boolean allSolution) {

        int counter = 0;
        int[] first = {0,0};
        int biggest = 0;

        if (cell.size() == 0){
            return null;
        }
        for (int i = 0; i < cell.size(); i++) {
            int[] temp = cell.get(i);

            for (int n = 1; n <= N; n++) {
                if (!check(a, temp[0], temp[1], n, allSolution)) {
                    counter++;
                }

                else if (kingRule && queenRule && knightRule) {
                    if (!king(a, temp[0], temp[1], n) || !knight(a, temp[0], temp[1], n)) {
                        counter++;
                    } else if (n == N && !queen(a, temp[0], temp[1])) {
                        counter++;
                    }
                }
                else if (kingRule && queenRule) {
                    if (!king(a, temp[0], temp[1], n)) {
                        counter++;
                    } else if (n == N && !queen(a, temp[0], temp[1])) {
                        counter++;
                    }
                } else if (kingRule && knightRule) {
                    if (!king(a, temp[0], temp[1], n) || !knight(a, temp[0], temp[1], n)) {
                        counter++;
                        //break;
                    }
                } else if (queenRule && knightRule) {
                    if (!knight(a, temp[0], temp[1], n)) {
                        counter++;
                    } else if (n == N && !queen(a, temp[0], temp[1])) {
                        counter++;
                    }
                    //break;
                } else if (kingRule) {
                    if (!king(a, temp[0], temp[1], n)) {
                        counter++;
                        //break;
                    }
                } else if (knightRule) {
                    if (!knight(a, temp[0], temp[1], n)) {
                        counter++;
                        //break;
                    }
                } else {
                    if (n == N && !queen(a, temp[0], temp[1])) {
                        counter++;
                        //break;
                    }
                }
            }

            if (counter > biggest){
                biggest = counter;
                first = temp;
            }

            counter = 0;

        }

        return first;
    }

    /* The solve() method should remove all the unknown characters ('x') in the grid
     * and replace them with the numbers in the correct range that satisfy the constraints
     * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL
     * possible solutions and store them in the field named solutions. */
    public void solve(boolean allSolutions) {
        ArrayList<int[]> emptyCell = new ArrayList<>();
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (grid[i][j] == 0){
                    emptyCell.add(new int[]{i,j});
                }
            }
        }
        if (allSolutions){

            int[][] newGrid = new int[N][N];
            for (int i = 0; i < this.N; i++) {
                for (int j = 0; j < this.N; j++) {
                    newGrid[i][j] = this.grid[i][j];
                }
            }

            sudokuSolver(emptyCell,newGrid,true);
            for (ChessSudoku temp:solutions){
                this.grid = temp.grid;
                break;
            }
        }

        else{
            sudokuSolver(emptyCell,this.grid,false);
        }

    }

    

    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public ChessSudoku( int size ) {
        SIZE = size;
        N = size*size;

        grid = new int[N][N];
        for( int i = 0; i < N; i++ )
            for( int j = 0; j < N; j++ )
                grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger( InputStream in ) throws Exception {
        int result = 0;
        boolean success = false;

        while( !success ) {
            String word = readWord( in );

            try {
                result = Integer.parseInt( word );
                success = true;
            } catch( Exception e ) {
                // Convert 'x' words into 0's
                if( word.compareTo("x") == 0 ) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord( InputStream in ) throws Exception {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while( whiteSpace.indexOf(currentChar) > -1 ) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while( whiteSpace.indexOf(currentChar) == -1 ) {
            result.append( (char) currentChar );
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read( InputStream in ) throws Exception {
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                grid[i][j] = readInteger( in );
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth( String text, int width ) {
        for( int i = 0; i < width - text.length(); i++ )
            System.out.print( " " );
        System.out.print( text );
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print() {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for( int lineInit = 0; lineInit < lineLength; lineInit++ )
            line.append('-');

        // Go through the grid, printing out its values separated by spaces
        for( int i = 0; i < N; i++ ) {
            for( int j = 0; j < N; j++ ) {
                printFixedWidth( String.valueOf( grid[i][j] ), digits );
                // Print the vertical lines between boxes
                if( (j < N-1) && ((j+1) % SIZE == 0) )
                    System.out.print( " |" );
                System.out.print( " " );
            }
            System.out.println();

            // Print the horizontal line between boxes
            if( (i < N-1) && ((i+1) % SIZE == 0) )
                System.out.println( line.toString() );
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input,
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main( String args[] ) throws Exception {
        InputStream in = new FileInputStream("veryEasy3x3.txt");

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger( in );
        if( puzzleSize > 100 || puzzleSize < 1 ) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        ChessSudoku s = new ChessSudoku( puzzleSize );

        // You can modify these to add rules to your sudoku
        s.knightRule = false;
        s.kingRule = false;
        s.queenRule = false;

        // read the rest of the Sudoku puzzle
        s.read( in );

        System.out.println("Before the solve:");
        s.print();
        System.out.println();

        // Solve the puzzle by finding one solution.
        s.solve(false);

        // Print out the (hopefully completed!) puzzle
        System.out.println("After the solve:");
        s.print();
    }
}
