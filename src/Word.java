package src;
import java.util.ArrayList;

public class Word {
	String word;
	ArrayList<int[]> path;
	
	Word(String word, ArrayList<int[]> path) {
		this.word = word;
		this.path = path;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public ArrayList<int[]> getPath() {
		return path;
	}

	public void setPath(ArrayList<int[]> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return word;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		return word.equals(other.word);
	}
}
