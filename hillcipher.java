
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

/*
 * S. Faye Strawn
 */

public class hillcipher {
    
    public static int[][] key;
    public static char[] plaintext;
    public static int fileLength = 10000;
    public static int keyLength;
    public static int alphaSize = 26;
    public static int lineWidth = 80;
    public static int arrayLength;

    // Reads key matrix from file into 2D array
    // Echoes to output
    public static void storeKey(File file) throws FileNotFoundException
    {  
        Scanner scanner = new Scanner(new FileInputStream(file));
        keyLength = scanner.nextInt();
        key = new int[keyLength][keyLength];
        System.out.print("\nKey matrix:\n\n");
        for(int i = 0; i < keyLength; i++)
        {
            for(int j = 0; j < keyLength; j++)
            {
                key[i][j] = scanner.nextInt();
                System.out.printf(key[i][j] + " ");
            }
            System.out.printf("\n");
        }
        System.out.print("\n\n");
    }
    
    // Reads plaintext file into array of size 10,000
    // Echoes to output
    public static void storePlaintext(File file) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new FileInputStream(file));
        plaintext = new char[fileLength];
        int j = 0;
        int widthCheck = 0;
        
        System.out.print("Plaintext:\n\n");
        
        while(scanner.hasNext())
        {
            // Scan next token, print to output, convert to lower case
            String temp = scanner.next();          
            temp = temp.toLowerCase();
           
            // Add all letters from this token to the array
            for(int i = 0; i < temp.length(); i++)
            {
                if(Character.isLetter(temp.charAt(i))){
                   plaintext[j] = temp.charAt(i);
                   System.out.print(plaintext[j]);
                   j++;
                   
                   widthCheck++;
                   if(widthCheck%lineWidth == 0)
                       System.out.print("\n");
                }
            }
        }
        // Pad with 'x'
        while(j % keyLength != 0)
        {
            plaintext[j] = 'x';
            System.out.print(plaintext[j]);
            j++;
        }
        
        arrayLength = j;
        
        System.out.print("\n\n\n");

    }
    
    // Splits message into blocks the width of the key, generates their 
    // numerical representations and sends to  be encrypted
    public static void split(String alpha, int n)
    {
        System.out.print("Ciphertext:\n\n");
        int[] blocks = new int[n];
        int widthCheck = 0;
        
        // Iterate through file in sections of block length
        for(int i = 0; i < arrayLength; i+=n)
        {
            // Break if reached end of data in array
            if(!Character.isLetter(plaintext[i]))
                break;
            
            // Fill block with numerical representation of letters at each index
            for(int j = 0; j < n; j++)
            {
                blocks[j] = alpha.indexOf(plaintext[i+j]);
            }
            
            // Encrypt block
            int[] cipher = encrypt(blocks, alpha, n);
            
            // Print encrypted block
            for(int k = 0; k < n; k++)
            {
                System.out.print(alpha.charAt(cipher[k]));
                
                // Ensure 80 characters per line
                widthCheck++;                
                if(widthCheck%lineWidth == 0)
                    System.out.print("\n");
            }

        }
        
        System.out.print("\n\n");
        
    }
    
    // Performs matrix multiplication on blocks and prints to output
    public static int[] encrypt(int[] blocks, String alpha, int n)
    {
        int[] cipher = new int[n];
        
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                cipher[i] += key[i][j] * blocks[j];
            }           
            
            cipher[i] %= alphaSize;
        }
        
        return cipher; 
    }
    
    public static void main(String[] args) throws FileNotFoundException
    {
        // Get file containing the encryption key from command line
        File file1 = new File(args[0]);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file1));
            br.close();
        }catch(Exception e){
        }
        
        // Get file containing plaintext to be encrypted from command line
        File file2 = new File(args[1]);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file2));
            br.close();
        }catch(Exception e){
        }   
        
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        
        storeKey(file1);
        storePlaintext(file2);     
        split(alphabet, keyLength);
    
    }
    
}
