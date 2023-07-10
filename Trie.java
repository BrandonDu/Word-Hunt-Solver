import java.util.HashMap;

public class Trie {
	private TrieNode root;

	Trie() {
		root = new TrieNode();
	}

	public void insert(String word) {
		int length = word.length();
		TrieNode current = root;
		for (int i = 0; i < length; i++) {
			char ch = word.charAt(i);
			HashMap<Character, TrieNode> children = current.getChildren();
			if (children.containsKey(ch)) {
				current = children.get(ch);
			} else {
				TrieNode node = new TrieNode();
				children.put(ch, node);
				current = node;
			}
		}
		current.setWord(true);
	}

	public boolean contains(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			HashMap<Character, TrieNode> children = current.getChildren();
			if (children.containsKey(ch)) {
				current = children.get(ch);
			} else
				return false;
		}
		if (current.isWord())
			return true;
		return false;
	}

	public TrieNode getRoot() {
		return root;
	}
}

class TrieNode {
	private HashMap<Character, TrieNode> children;
	private boolean isWord;

	TrieNode() {
		children = new HashMap<Character, TrieNode>();
		isWord = false;
	}

	public HashMap<Character, TrieNode> getChildren() {
		return children;
	}

	public boolean isWord() {
		return isWord;
	}

	public void setWord(boolean bool) {
		isWord = bool;
	}
}
