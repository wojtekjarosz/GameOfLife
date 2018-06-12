package sample;

public class Board {

    private Cell[][] cells;
    private int width;
    private int height;
    private boolean period;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        resetAll();
    }

    // reset calej planszy
    private void resetAll() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(false);
            }
        }
    }

    // ustawienie wartosci komorki
    public void setCellValue(int x, int y, boolean isAlive) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            cells[x][y].setAlive(isAlive);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // pobranie wartosci komorki
    public boolean getCellValue(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return cells[x][y].isAlive();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean isPeriod() {
        return period;
    }

    public void setPeriod(boolean period) {
        this.period = period;
    }

    // wykonanie aktualnej tury
    public void nextCycle() {

        // kopiowanie aktualnego stanu
        Cell[][] newBoard = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newBoard[i][j] = cells[i][j].clone();
            }
        }

        // wykonanie akcji dla danej komorki
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int neighboursCount = countAliveNeighbours(i, j);
                newBoard[i][j].changeState(neighboursCount);
            }
        }

        cells = newBoard;
    }

    // liczenie zywych sasiadow
    public int countAliveNeighbours(int i, int j) {

        int aliveNeighbours = 0;

        if(period){
            //periodycznie
            int startX = i-1;
            int startY = j-1;
            int endX = i+1;
            int endY = j+1;

            int tmpX, tmpY;
            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    tmpX=x; tmpY=y;
                    if(x==-1) tmpX=width-1;
                    if(x==width) tmpX=0;
                    if(y==-1) tmpY=height-1;
                    if(y==height) tmpY=0;

                    if (cells[tmpX][tmpY].isAlive()) {
                        aliveNeighbours++;
                    }
                }
            }
        }else {
            int startX = Math.max(i - 1, 0);
            int startY = Math.max(j - 1, 0);
            int endX = Math.min(i + 1, width - 1);
            int endY = Math.min(j + 1, height - 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {

                    if (cells[x][y].isAlive()) {
                        aliveNeighbours++;
                    }
                }
            }
        }
        // nie liczymy komorki ktorej sasiadow sprawdzamy
        if (cells[i][j].isAlive()) {
            aliveNeighbours--;
        }
        return aliveNeighbours;
    }
    public void printBoard(){
        for(int i=0; i <height;i++){
            for (int j=0;j<width;j++){
                System.out.print(getCellValue(j,i)+ "\t");
            }
            System.out.println("");
        }
    }

}
