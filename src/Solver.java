
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Solver {
	private JFrame frame;
	private JPanel contentPane;

	private char[][] board;
	private int boardSize;
	private PriorityQueue<Word> words;
	private Trie dict;
	private int[] scores;
	private int score;
	public static int[] row = { -1, -1, -1, 0, 1, 0, 1, 1 };
	public static int[] col = { -1, 1, 0, -1, -1, 1, 0, 1 };

	Solver(String s) {

		words = new PriorityQueue<Word>(new Comparator<Word>() {
			@Override
			public int compare(Word word1, Word word2) {
				String s1 = word1.getWord();
				String s2 = word2.getWord();
				int len = s2.length() - s1.length();
				int i = s2.compareTo(s1);
				return len == 0 ? i : len;
			}
		});

		scores = new int[20];
		scores[0] = 100;
		scores[1] = 400;
		scores[2] = 800;
		scores[3] = 1400;
		for(int i=4; i<20; i++) {
			scores[i] = 1400 + (i-3)*400;
		}

		boardSize = (int) Math.sqrt(s.length());
		score = 0;
		dict = new Trie();
		getAllWords();

		board = new char[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			board[i] = s.substring(boardSize * i, boardSize * i + boardSize).toCharArray();
		}

		findAllWords();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new Panel(board, words);

		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public void getAllWords() {
		File dict = new File("Dictionary.txt");
		try (BufferedReader br = new BufferedReader(new FileReader(dict))) {
			String line;
			while ((line = br.readLine()) != null) {
				this.dict.insert(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean available(int i, int j, boolean[][] visited) {
		return i >= 0 && i < boardSize && j >= 0 && j < boardSize && !visited[i][j];
	}

	public void dfs(int i, int j, boolean[][] visited, String s, TrieNode node, ArrayList<int[]> path) {
		visited[i][j] = true;
		path.add(new int[] { i, j });

		char ch = board[i][j];

		if (node.getChildren().containsKey(ch)) {
			s += ch;
		} else {
			visited[i][j] = false;
			if (path.size() > 0)
				path.remove(path.size() - 1);
			return;
		}

		if (dict.contains(s) && s.length() > 2 && !words.contains(new Word(s, path))) {
			words.add(new Word(s, (ArrayList<int[]>) path.clone()));
			score += scores[s.length() - 3];
		}
		for (int k = 0; k < 8; k++) {
			if (available(i + row[k], j + col[k], visited)) {
				dfs(i + row[k], j + col[k], visited, s, node.getChildren().get(ch), path);
			}
		}

		visited[i][j] = false;
		if (path.size() > 0)
			path.remove(path.size() - 1);
	}

	public int findAllWords() {
		boolean[][] visited = new boolean[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				dfs(i, j, visited, "", dict.getRoot(), new ArrayList<int[]>());
			}
		}
		return score;
	}

	public void printWords() {
		Iterator<Word> it = words.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	public void printBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println("");
		}
	}

	public static void printBoard(String s) {
		int boardSize = (int) Math.sqrt(s.length());
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				System.out.print(s.charAt(boardSize * i + j) + " ");
			}
			System.out.println("");
		}
	}

	public static String randomString(int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			s += (char) ((26 * Math.random()) + 'a');
		}
		return s;
	}

	public int getScore() {
		return score;
	}

	public static void main(String[] args) {
		Solver solver = new Solver(randomString(10*10));
		solver.printWords();
		System.out.println(solver.score);
	}
}
