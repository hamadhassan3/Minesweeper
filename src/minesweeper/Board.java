/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.Random;
import java.util.Vector;


public class Board {
    
    public final int sizeL = 30;
    public final int sizeW = 30;
    public final int minesCount = 30;
    private boolean boardOpened[][];
    private int boardMines[][];
    private boolean boardFlagged[][];
    
    public final int MINE = -1;
    
    public final int notMineCount = sizeL * sizeW - minesCount;
    
    boolean lifelineUsed = false;
    
    public final int WON = 1;
    public final int LOST = -1;
    
    private boolean mineClicked = false;
    
    public Board() {
        boardOpened = new boolean[sizeL][sizeW];
        boardMines = new int[sizeL][sizeW];     //initialized with zero
        boardFlagged = new boolean[sizeL][sizeW];
        for(int i = 0; i < sizeL; i++)
            for(int j = 0; j < sizeW; j++){
                boardOpened[i][j] = false;
                boardMines[i][j] = 0;
                boardFlagged[i][j] = false;
            }
        
        //Generating the mines randomly on the board
        Random rand = new Random();
        int minesPlaced = 0;

        while(minesPlaced != minesCount){
            int i = rand.nextInt(sizeL);   //0 to less than sizeL
            int j = rand.nextInt(sizeW);   //0 to less than sizeW    
            
            //Placing mine only if there is not already a mine there
            if(boardMines[i][j] != MINE){
                //Placing the mine on board
                boardMines[i][j] = MINE;
                //Now adjusting the numbers around the mine
                if(i + 1 < sizeL && boardMines[i+1][j] != MINE)
                    boardMines[i+1][j]++;
                if(i - 1 >= 0 && boardMines[i-1][j] != MINE)
                    boardMines[i-1][j]++;
                if(j + 1 < sizeW && boardMines[i][j + 1] != MINE)
                    boardMines[i][j+1]++;
                if(j - 1 >= 0 && boardMines[i][j - 1] != MINE)
                    boardMines[i][j-1]++;
                if(i + 1 < sizeL && j + 1 < sizeW && boardMines[i+1][j + 1] != MINE)
                    boardMines[i+1][j+1]++;
                if(i + 1 < sizeL && j - 1 >= 0 && boardMines[i+1][j - 1] != MINE)
                    boardMines[i+1][j-1]++;
                if(i - 1 >= 0 && j + 1 < sizeW && boardMines[i-1][j + 1] != MINE)
                    boardMines[i-1][j+1]++;
                if(i - 1 >= 0 && j - 1 >= 0 && boardMines[i-1][j - 1] != MINE)
                    boardMines[i-1][j-1]++;
                
                minesPlaced++;
            }
            
        }
        
    }
    
    
    public void revealAll(){
        for(int i = 0; i<sizeL;i++){
            for(int j = 0; j <sizeL; j++){
                this.boardOpened[i][j] = true;
            }
        }
    }
    
    public boolean mineIsClicked(){
        return mineClicked;
    }
    
    public void setOrRemoveFlag(int row, int col){
        if(this.boardOpened[row][col])
            return;
        else{
            if(this.boardFlagged[row][col]){
                this.boardFlagged[row][col] = false;
            }
            else{
                this.boardFlagged[row][col] = true;
            }
        }
    }
    
    public boolean getFlagged(int row, int col){
        return boardFlagged[row][col];
    }

    boolean isRevealed(int row, int col){
        return this.boardOpened[row][col];
    }
    
    int getValue(int row, int col){
        return this.boardMines[row][col];
    }
    
    boolean makeMove(int r, int c){
        
        //If user opens a mine return false as move was unsuccessful
        if(boardMines[r][c] == MINE){
            mineClicked = true;
            return false;
        }
        //No need to call the function if the selected cell is already open
        if(!this.boardOpened[r][c])
            openAll(r, c);
        
        return true;
    }
    
        // Recursive helping function of step method
    public void openAll(int r, int c)
    {
        if( (r > this.sizeL-1  || r < 0 ) || (c > this.sizeW-1 || c < 0 )){
            //If the indeces go out of bound of the board simply return
            return;
        }
        else if(this.boardMines[r][c] == 0 && !this.boardOpened[r][c] && !this.boardFlagged[r][c])
        {
            //If the current index can be opened reveal it and its neighbours
            this.boardOpened[r][c] = true;
            openAll(r-1,c-1);
            openAll(r-1,c);
            openAll(r-1,c+1);
            openAll(r,c-1);
            openAll(r,c+1);
            openAll(r+1 , c+1);
            openAll(r+1 , c);
            openAll(r+1,c-1);
        }
        else if(this.boardMines[r][c] >= 1 && !this.boardFlagged[r][c])
        {
            //If a number is found simply reveal it and donot do anything else
            this.boardOpened[r][c] = true;
        }
    }
    
    public int calculateScore(){
        int score = 0;
        for(int i = 0; i<sizeL;i++){
            for(int j = 0; j < sizeW; j++){
                if(this.boardOpened[i][j]){
                    score += this.boardMines[i][j] + 1;
                }
            }
        }
        if(this.lifelineUsed){
            score -= 10;
        }
        return score;
    }
    
    
    public int gameState(){
        if(getNotOpenedCoordinates().size() == 0 && !mineClicked){
            return WON;
        }
        else if(mineClicked){
            return LOST;
        }
        else{
            return 0;
        }
    }
    
    
    public void open50Perc(){
        if(!lifelineUsed){
            lifelineUsed = true;

            //Getting the coordinates of cells which are not opened and are not mines
            Vector<Coordinates> notOpened = getNotOpenedCoordinates();
            int needToOpenCount = notOpened.size()/2;
            int openedCount = 0;
            
            //Randomly opening 50% of not open and not mine cells
            Random rand = new Random();
            while(openedCount != needToOpenCount){
                int index = rand.nextInt(notOpened.size());
                Coordinates coord = notOpened.get(index);
                this.boardOpened[coord.i][coord.j] = true;
                notOpened.remove(coord);
                openedCount++;
            }
        }
    }
    
    public int getTotalOpened(){
        int count = 0;
        for(int i= 0; i<sizeL; i++){
            for(int j = 0; j < sizeW; j++){
                if(this.boardOpened[i][j]){
                    count++;
                }
            }
        }
        return count;
    }
    
    public Vector<Coordinates> getNotOpenedCoordinates(){
        
        Vector<Coordinates> v = new Vector<Coordinates>();
        for(int i =0; i<sizeL; i++){
            for(int j = 0; j < sizeW; j++){
                if(this.boardMines[i][j] != MINE && !this.boardOpened[i][j]){
                    v.add(new Coordinates(i, j));
                }
            }
        }
        
        return v;
    }

    
    public boolean[][] getBoardOpened() {
        return boardOpened;
    }

    public int[][] getBoardMines() {
        return boardMines;
    }

    private class Coordinates{
        public int i;
        public int j;

        public Coordinates(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
    }

    
}
