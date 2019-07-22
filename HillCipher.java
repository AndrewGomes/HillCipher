import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads the contents of a text file and enciphers it using Hill.
 * @author Andrew Gomes (NID: an289864)
 * @date 6/5/2017
 * [CIS 3360 | Summer 2017 | Professor: Joshua Lazar]
 */
public class HillCipher {

	/**
	 * The main method of the application.
	 * @param args - the arguments read in.
	 */
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Not enough arguments entered!\nFirst argument must be the name of the keyfile, second argument must be the name of the plaintext file.");
			return;
		}
		
		File keyFile = new File("./" + args[0]);
		File plaintextFile = new File("./" + args[1]);
		System.out.println("[Key file: '" + args[0] + "' | Plaintext file: '" + args[1] + "']");
		
		try {

			BufferedReader key = new BufferedReader(new FileReader(keyFile));
			BufferedReader plaintext = new BufferedReader(new FileReader(plaintextFile));

			//Reading and setting up the matrix
			int i = 0;
			int[][] matrix = new int[9][9];
			int blockSize = 9;
			System.out.println("\nKey matrix:\n");
			for (Object s : key.lines().toArray()) {
				String line = (String) s;
				System.out.println(line);
				if (i == 0) {
					 //Create the matrix using the first line's sizing value
					 blockSize = Integer.parseInt(line);
					 matrix = new int[blockSize][blockSize];
				} else {
					//Populate the matrix with the values
					String[] split = line.split(" ");
					for (int j = 0; j < split.length; j++) {
						matrix[i - 1][j] = Integer.parseInt(split[j]);
					}
				}
				i++;
			}

			//Reading and outputting plaintext
			ArrayList<Character> list = new ArrayList<Character>(10_000);
			System.out.println("\nPlaintext:\n");
			for (Object s : plaintext.lines().toArray()) {
				String line = (String) s;
				System.out.println(line);
				//Only add the valid chars to the list.
				for (Character c : line.toCharArray()) {
					if (Character.isLetter(c)) {
						list.add(Character.toLowerCase(c));
					}
				}
			}
			System.out.println("\n");
			
			//Padding with the letter X to ensure our
			//list of characters is a multiple of our blockSize
			while ((list.size() % blockSize) != 0) {
				list.add('X');
			}

			encipher(matrix, list);
			key.close();
			plaintext.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Enciphers the text using Hill and then prints it to the console.
	 * @param matrix - the matrix to use as an integer array.
	 * @param input - the inputted character list.
	 */
	private static void encipher(int[][] matrix, ArrayList<Character> input) {
		int blockSize = matrix.length;
		String resultString = "";

		System.out.println("Ciphertext:\n");
		
		//Fill character 'blocks' of the specified size
		int totalBlocks = input.size() / blockSize;
		for (int i = 0; i < totalBlocks; i++) {
			char[] block = new char[blockSize];
			for (int j = 0; j < blockSize; j++) {
				block[j] = input.get(0);
				input.remove(0);
			}
			
			//Print each block after it is built
			//System.out.println(Arrays.toString(block));
			
			//Multiply each row with each individual key letter
			for (int col = 0; col < blockSize; col++) {
				int multiResult = 0;
				for (int row = 0; row < blockSize; row++) {
					int keyChar = matrix[col][row];
					int inputChar = Character.getNumericValue(block[row]) - 10;
					multiResult += keyChar * inputChar;
					//Print to show multiplication
					//System.out.println(keyChar + " * " + inputChar + " (char: " + ( block[row]) + ")");
					
				}
				//Convert to a position in the alphabet
				int charNum = multiResult % 26;
				String alpha = "abcdefghijklmnopqrstuvwxyz";
				//Print to show resulting ciphered character
				//System.out.println("= " + (charNum) + " aka letter: " + (alpha.charAt(charNum)) + " \n");
				resultString += alpha.charAt(charNum);
			}
		}
		
		//80 characters printed per line
		int end = 80;
		for (int i = 0; i < (resultString.length() / 80) + 1; i++) {
			int start = end - 80;
			if (end > resultString.length()) {
				end = resultString.length();
			}
			if (resultString.length() < 80) {
				start = 0;
			}
			String sub = resultString.substring(start, end);
			System.out.println(sub);
			end += 80;
		}
	}
}