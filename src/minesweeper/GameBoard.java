/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javafx.scene.input.MouseButton;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class GameBoard extends javax.swing.JFrame {

    /**
     * Creates new form GameBoard
     */
    //The Board class stores the board and all of its functionality and states
    private Board board;
    //The matrix of Buttons in the board
    private JButton buttons[][];

    public GameBoard() {
        initComponents();

        //Initializing the Board
        board = new Board();

        minesCount.setText("Total Mines: " + board.minesCount);
        //Setting the size of cell according to the board size
        int heightRealBoard = jPanel2.getHeight();
        int widthRealBoard = jPanel2.getWidth();

        int cellHeight = heightRealBoard / board.sizeL;
        int cellWidth = widthRealBoard / board.sizeW;

        //Setting the layout of the panel to Grid Layout with the calculated cell size
        jPanel2.setLayout(new GridLayout(board.sizeL, board.sizeW, 0, 0));
        buttons = new JButton[board.sizeL][board.sizeW];

        //Creating the cells in the panel
        for (int i = 0; i < board.sizeL; i++) {
            for (int j = 0; j < board.sizeW; j++) {

                class MouseClickHandle implements MouseListener {

                    final int row;
                    final int col;

                    public MouseClickHandle(int ii, int jj) {
                        this.row = ii;
                        this.col = jj;
                    }

                    @Override
                    public void mouseClicked(MouseEvent me) {
                    }

                    @Override
                    public void mousePressed(MouseEvent me) {

                        if (me.getButton() == MouseEvent.BUTTON1 && !board.getFlagged(row, col)) {

                            int gameState = board.gameState();
                            if (gameState == 0) {
                                boolean didMove = board.makeMove(row, col);
                                if (didMove) {
                                    updateBoard();
                                } else {
                                    displayBoard();
                                }
                            }
                            if (board.gameState() == board.LOST) {
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog(null, "Would You Like to Play Again?", "Game Over! You Lost.", dialogButton);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    newGame();
                                }
                            } else if (board.gameState() == board.WON) {
                                int dialogButton = JOptionPane.YES_NO_OPTION;
                                int dialogResult = JOptionPane.showConfirmDialog(null, "Your score is: " + board.calculateScore()
                                        + "\nWould You Like to Play Again?", "Congratulations! You Win.", dialogButton);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    newGame();
                                }
                            }
                        } else if (me.getButton() == MouseEvent.BUTTON3) {
                            int gameState = board.gameState();
                            if (gameState == 0) {
                                board.setOrRemoveFlag(row, col);
                                if (board.getFlagged(row, col)) {
                                    Icon icon = new ImageIcon("flag.png");
                                    buttons[row][col].setIcon(icon);
                                } else {
                                    buttons[row][col].setIcon(null);
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent me) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent me) {
                    }

                    @Override
                    public void mouseExited(MouseEvent me) {
                    }

                }

                buttons[i][j] = new JButton("");
                buttons[i][j].setPreferredSize(new Dimension(cellWidth, cellHeight));
                buttons[i][j].addMouseListener(new MouseClickHandle(i, j));
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                jPanel2.add(buttons[i][j]);

            }
        }

    }

    public void newGame() {
        this.setVisible(false);
        // TODO code application logic here
        GameBoard game = new GameBoard();
        game.setLocationRelativeTo(null);

        game.setVisible(true);
        game.setMinimumSize(game.getSize());
    }

    public void displayBoard() {

        board.revealAll();
        for (int i = 0; i < board.sizeL; i++) {
            for (int j = 0; j < board.sizeW; j++) {
                int value = board.getValue(i, j);

                if(board.getFlagged(i, j)){
                    buttons[i][j].setIcon(null);
                }
                if (value != 0) {
                    if (value == board.MINE) {
                        Icon icon = new ImageIcon("mine.jpg");
                        buttons[i][j].setIcon(icon);

                    } else {
                        buttons[i][j].setText(Integer.toString(value));
                    }
                }
                if (value == 1) {
                    buttons[i][j].setForeground(Color.BLUE);
                } else if (value == 2) {
                    buttons[i][j].setForeground(Color.GREEN);
                } else if (value == 3) {
                    buttons[i][j].setForeground(Color.black);
                } else if (value == 4) {
                    buttons[i][j].setForeground(Color.RED);
                } else if (value == 5) {
                    buttons[i][j].setForeground(Color.orange);
                } else if (value == 6) {
                    buttons[i][j].setForeground(Color.CYAN);
                } else if (value == 7) {
                    buttons[i][j].setForeground(Color.black);
                } else if (value == 8) {
                    buttons[i][j].setForeground(Color.MAGENTA);
                }
                buttons[i][j].setBackground(Color.lightGray);
            }

        }
        jButton1.setEnabled(false);
        scoreLabel.setText("Score: 0");
    }

    public void updateBoard() {
        for (int i = 0; i < board.sizeL; i++) {
            for (int j = 0; j < board.sizeW; j++) {
                if (board.isRevealed(i, j)) {
                    int value = board.getValue(i, j);

                    if (value != 0) {
                        buttons[i][j].setText(Integer.toString(value));
                    }
                    if (value == 1) {
                        buttons[i][j].setForeground(Color.BLUE);
                    } else if (value == 2) {
                        buttons[i][j].setForeground(Color.GREEN);
                    } else if (value == 3) {
                        buttons[i][j].setForeground(Color.BLACK);
                    } else if (value == 4) {
                        buttons[i][j].setForeground(Color.RED);
                    } else if (value == 5) {
                        buttons[i][j].setForeground(Color.orange);
                    } else if (value == 6) {
                        buttons[i][j].setForeground(Color.CYAN);
                    } else if (value == 7) {
                        buttons[i][j].setForeground(Color.black);
                    } else if (value == 6) {
                        buttons[i][j].setForeground(Color.MAGENTA);
                    }
                    buttons[i][j].setBackground(Color.lightGray);
                }
            }
        }
        int score = board.calculateScore();
        scoreLabel.setText("Score: " + Integer.toString(score));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        scoreLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        minesCount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Minesweeper");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        scoreLabel.setText("Score: 0");

        jButton1.setText("Reveal 50%");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        minesCount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        minesCount.setText("Total Mines: 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minesCount)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(minesCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(scoreLabel))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        board.open50Perc();
        updateBoard();
        jButton1.setEnabled(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel minesCount;
    private javax.swing.JLabel scoreLabel;
    // End of variables declaration//GEN-END:variables
}
