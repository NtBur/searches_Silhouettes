package com.shpp.p2p.cs.nburianovata.assignment12;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import acm.graphics.*;
import static javax.imageio.ImageIO.read;

/* The program searches for silhouettes in black and white pictures and displays their number*/
public class Assignment12Part1 {

    static int  FOR_ALMOST_WHITE = 165;//for clean the picture

    public static void main(String[] args) {

        try {
            File file = new File(args[0]);

            GImage cleanPicture = new GImage(cleaningPictures(file));

            Color colorBackRound = new Color(cleaningPictures(file).getRGB(0, 0));

            int[][] graf = createGraf(cleanPicture, colorBackRound);

            findSilhouettes(graf);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds light tones in a picture and replaces
     * them with white and dark tones with black
     * @param file contains picture
     * @return
     * @throws IOException occurs on unsuccessful reading of the file
     */
    public static BufferedImage cleaningPictures(File file) throws IOException {
        BufferedImage source = read(file);
        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                Color color = new Color(source.getRGB(x, y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();
                Color newColor = (red > FOR_ALMOST_WHITE || green > FOR_ALMOST_WHITE || blue > FOR_ALMOST_WHITE) ? Color.WHITE : Color.BLACK;
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        return result;
    }

    /**
     * Creates an array: 0 is the background color, 1 is the silhouette color
      * @param image picture where we are looking for pixel colors
     * @param color background color
     * @return array with 0 and 1
     */
    public static int[][] createGraf(GImage image, Color color) {
        int[][] pixels = image.getPixelArray();
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        int[][] result = new int[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Color colorPixel = new Color(pixels[row][col]);
                result[row][col] = (colorPixel.equals(color)) ? 0 : 1;
            }
        }
        return result;
    }

    /**
     * Recursively searches for 1 and replaces with 2
     * @param arr an array containing 1
     * @param x position
     * @param y position
     * @param res the number of calls to the recursive function is equal to 1 (silhouette size)
     * @return if the search is successful
     */
    public static boolean DFS(int[][] arr, int x, int y, long[] res) {
        res[0]++;
        if (x < arr.length - 1 && y < arr[0].length - 1) {
            if (arr[x][y] == 2) {
                return true;
            } else {
                arr[x][y] = 2;
                if (arr[x][y - 1] == 1 && y > 1)   DFS(arr, x, y - 1, res);
                if (arr[x][y + 1] == 1 && y < arr[0].length - 1) DFS(arr, x, y + 1, res);
                if (arr[x - 1][y] == 1 && x > 1) DFS(arr, x - 1, y, res);
                if (arr[x + 1][y] == 1 && x < arr.length - 1) DFS(arr, x + 1, y, res);
                }
        }
        return true;
    }

    /**
     * Looking for the number of silhouettes
     * @param graf an array in which he searches for silhouettes
     */
   public static void findSilhouettes(int[][] graf){
        ArrayList<Long> arr = new ArrayList<>();
        long[] counter = new long[1];
        for (int i = 1; i < graf.length-1; i++) {
            for (int j = 1; j < graf[0].length-1; j++) {
                if (graf[i][j] == 1) { //if it's not a background
                    DFS(graf, i, j, counter);// count the volume of the silhouette
                    arr.add(counter[0]);// here are all the silhouettes found
                }
            }
        }
        // size array - count silhouettes
        System.out.println("In this picture " +  arr.size() + " silhouettes");
    }
}
