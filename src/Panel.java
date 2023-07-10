import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Panel extends JPanel {
	private Word word;
	private JPanel panel;
	private JLabel wordLabel;
	private int boardSize;

	Panel(char[][] board, PriorityQueue<Word> wordList) {
		word = wordList.poll();
		boardSize = board.length;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		wordLabel = new JLabel(word.getWord(), SwingConstants.CENTER);
		wordLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		wordLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		wordLabel.setFont(new Font("Serif", Font.BOLD, 50));
		this.add(wordLabel);

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				String s = word.getWord();
				ArrayList<int[]> path = word.getPath();
				for (int i = 0; i < s.length() - 1; i++) {
					int[] loc1 = findCenter(path.get(i));
					int[] loc2 = findCenter(path.get(i + 1));
					drawArrowLine(g, loc1[0], loc1[1], loc2[0], loc2[1], 25, 10);
				}
			}
		};

		panel.setLayout(new GridLayout(boardSize, boardSize, 0, 0));
		panel.setPreferredSize(new Dimension(500, 500));
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				JLabel letter = new JLabel(Character.toString(board[i][j]), SwingConstants.CENTER);
				letter.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				letter.setFont(new Font("Serif", Font.BOLD, 50));
				letter.setBackground(new Color(248, 223, 161));
				letter.setOpaque(false);
				panel.add(letter);
			}
		}
		this.add(panel);	

		JPanel info = new JPanel();
		info.setLayout(new GridLayout(1, 2));

		JLabel wordsRemaining = new JLabel("Words remaining: " + wordList.size(), SwingConstants.CENTER);
		wordsRemaining.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		wordsRemaining.setAlignmentY(JLabel.CENTER_ALIGNMENT);

		JButton next = new JButton("Next Word");
		next.setAlignmentX(JButton.CENTER_ALIGNMENT);
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (wordList.size() > 0) {
					word = wordList.poll();
					wordLabel.setText(word.getWord());
					wordsRemaining.setText("Words remaining: " + wordList.size());
					repaint();
				} else {
					remove(next);
					repaint();
				}
			}

		});
		info.add(next);
		info.add(wordsRemaining);
		this.add(info);
	}

	public int[] findCenter(int[] loc) {
		int i = loc[0];
		int j = loc[1];
		int xSize = panel.getWidth();
		int ySize = panel.getHeight();
		int x = j * xSize / boardSize + xSize / (2 * boardSize);
		int y = i * ySize / boardSize + ySize / (2 * boardSize);
		return new int[] { x, y };
	}

	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - d, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(Color.RED);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.draw(new Line2D.Float(x1, y1, x2, y2));
		g.fillPolygon(xpoints, ypoints, 3);
	}
}
