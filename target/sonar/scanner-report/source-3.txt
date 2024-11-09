package com.zetcode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;

    public static final int NUM_IMAGES = 13;
    public static final int CELL_SIZE = 15;

    public static final int COVER_FOR_CELL = 10;
    public static final int MARK_FOR_CELL = 10;
    public static final int EMPTY_CELL = 0;
    public static final int MINE_CELL = 9;
    public static final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public static final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    public static final int DRAW_MINE = 9;
    public static final int DRAW_COVER = 10;
    public static final int DRAW_MARK = 11;
    public static final int DRAW_WRONG_MARK = 12;

    public int[] getField() {
        return field;
    }

    public void setField(int[] field) {
        this.field = field;
    }

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private transient Image[] img;
    private int mines = 40;
    private int rows = 16;
    private int cols = 16;
    private int allCells;
    private JLabel statusbar;

    public Board(JLabel statusbar) {
        this.statusbar = statusbar;
        img = new Image[NUM_IMAGES];
        loadImages();
        setDoubleBuffered(true);
        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void loadImages() {
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = new ImageIcon("C:\\Users\\lilou\\Bureau\\MEL\\TP MEL M2\\Java-Minesweeper-Game\\src\\main\\resources\\" + i + ".gif").getImage();
        }
    }

    public void newGame() {
        inGame = true;
        minesLeft = mines;
        allCells = rows * cols;
        field = new int[allCells];
        initializeField();
        placeMines();
        statusbar.setText(Integer.toString(minesLeft));
    }

    private void initializeField() {
        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }
    }

    private void placeMines() {
        SecureRandom random = new SecureRandom();
        int minesPlaced = 0;
        while (minesPlaced < mines) {
            int position = random.nextInt(allCells);
            if (field[position] != COVERED_MINE_CELL) {
                field[position] = COVERED_MINE_CELL;
                updateAdjacentCells(position);
                minesPlaced++;
            }
        }
    }

    public void updateAdjacentCells(int position) {
        int currentCol = position % cols;
        int[] neighborOffsets = {-cols - 1, -1, cols - 1, -cols, cols, -cols + 1, cols + 1, 1};

        for (int offset : neighborOffsets) {
            int neighbor = position + offset;
            if (isValidNeighbor(neighbor, currentCol)) {
                field[neighbor]++;
            }
        }
    }

    public boolean isValidNeighbor(int neighbor, int currentCol) {
        int neighborCol = neighbor % cols;
        return neighbor >= 0 && neighbor < allCells
                && field[neighbor] != COVERED_MINE_CELL
                && Math.abs(currentCol - neighborCol) <= 1;
    }

    public void findEmptyCells(int index) {
        int[] neighborOffsets = {-cols - 1, -1, cols - 1, -cols, cols, -cols + 1, cols + 1, 1};

        for (int offset : neighborOffsets) {
            int neighbor = index + offset;
            if (isValidEmptyCell(neighbor, index)) {
                field[neighbor] -= COVER_FOR_CELL;
                if (field[neighbor] == EMPTY_CELL) {
                    findEmptyCells(neighbor);
                }
            }
        }
    }

    private boolean isValidEmptyCell(int neighbor, int index) {
        int currentCol = index % cols;
        int neighborCol = neighbor % cols;
        return neighbor >= 0 && neighbor < allCells
                && field[neighbor] > MINE_CELL
                && Math.abs(currentCol - neighborCol) <= 1;
    }

    @Override
    public void paint(Graphics g) {
        int cell;
        int uncover = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell = field[i * cols + j];

                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                cell = getCellToDraw(cell);
                g.drawImage(img[cell], j * CELL_SIZE, i * CELL_SIZE, this);

                if (cell == DRAW_COVER) {
                    uncover++;
                }
            }
        }
        updateStatus(uncover);
    }

    private int getCellToDraw(int cell) {
        if (!inGame) {
            if (cell == COVERED_MINE_CELL) {
                return DRAW_MINE;
            } else if (cell == MARKED_MINE_CELL) {
                return DRAW_MARK;
            } else if (cell > COVERED_MINE_CELL) {
                return DRAW_WRONG_MARK;
            } else if (cell > MINE_CELL) {
                return DRAW_COVER;
            }
        } else {
            if (cell > COVERED_MINE_CELL) {
                return DRAW_MARK;
            } else if (cell > MINE_CELL) {
                return DRAW_COVER;
            }
        }
        return cell;
    }

    private void updateStatus(int uncover) {
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            if (!inGame) {
                newGame();
                repaint();
                return;
            }

            if (isWithinBounds(x, y)) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    handleRightClick(cRow, cCol);
                } else {
                    handleLeftClick(cRow, cCol);
                }
            }
        }

        private boolean isWithinBounds(int x, int y) {
            return x < cols * CELL_SIZE && y < rows * CELL_SIZE;
        }

        private void handleRightClick(int cRow, int cCol) {
            int cellIndex = (cRow * cols) + cCol;
            if (field[cellIndex] > MINE_CELL) {
                if (field[cellIndex] <= COVERED_MINE_CELL) {
                    if (minesLeft > 0) {
                        field[cellIndex] += MARK_FOR_CELL;
                        minesLeft--;
                        statusbar.setText(Integer.toString(minesLeft));
                    } else {
                        statusbar.setText("No marks left");
                    }
                } else {
                    field[cellIndex] -= MARK_FOR_CELL;
                    minesLeft++;
                    statusbar.setText(Integer.toString(minesLeft));
                }
                repaint();
            }
        }

        private void handleLeftClick(int cRow, int cCol) {
            int cellIndex = (cRow * cols) + cCol;

            if (field[cellIndex] > COVERED_MINE_CELL || field[cellIndex] < MINE_CELL) {
                return;
            }

            field[cellIndex] -= COVER_FOR_CELL;
            if (field[cellIndex] == MINE_CELL) {
                inGame = false;
            } else if (field[cellIndex] == EMPTY_CELL) {
                findEmptyCells(cellIndex);
            }
            repaint();
        }
    }
}
