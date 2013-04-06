package census;
/**
 * Main class that handles population Queries
 * 
 * @author Evan Casey
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PopulationQuery {
	// next four constants are relevant to parsing
	private static final int TOKENS_PER_LINE  = 7;
	private static final int POPULATION_INDEX = 4; // zero-based indices
	private static final int LATITUDE_INDEX   = 5;
	private static final int LONGITUDE_INDEX  = 6;
	
	// parse the input file into a large array held in a CensusData object
	public static CensusData parse(String filename) {
		CensusData result = new CensusData();
		
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(filename));
            
            // Skip the first line of the file
            // After that each line has 7 comma-separated numbers (see constants above)
            // We want to skip the first 4, the 5th is the population (an int)
            // and the 6th and 7th are latitude and longitude (floats)
            // If the population is 0, then the line has latitude and longitude of +.,-.
            // which cannot be parsed as floats, so that's a special case
            //   (we could fix this, but noisy data is a fact of life, more fun
            //    to process the real data as provided by the government)
            
            String oneLine = fileIn.readLine(); // skip the first line

            // read each subsequent line and add relevant data to a big array
            while ((oneLine = fileIn.readLine()) != null) {
                String[] tokens = oneLine.split(",");
                if(tokens.length != TOKENS_PER_LINE)
                	throw new NumberFormatException();
                int population = Integer.parseInt(tokens[POPULATION_INDEX]);
                if(population != 0)
                	result.add(population,
                			   Float.parseFloat(tokens[LATITUDE_INDEX]),
                		       Float.parseFloat(tokens[LONGITUDE_INDEX]));
            }
            
            // call findEdges to set minLat, minLon, maxLat, maxLon
           // result.findEdges();

            fileIn.close();
        } catch(IOException ioe) {
            System.err.println("Error opening/reading/writing input or output file.");
            System.exit(1);
        } catch(NumberFormatException nfe) {
            System.err.println(nfe.toString());
            System.err.println("Error in file format");
            System.exit(1);
        }
        return result;
	}
	

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
    // argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		// FOR YOU
	    System.out.println("File: " + args[0]);
        System.out.println("arg1: " + args[1]);
        System.out.println("arg2: " + args[2]);
        System.out.println("arg3: " + args[3]);

        CensusData data = new CensusData();
        data = parse(args[0]);
        
        int xBuckets = Integer.parseInt(args[1]);
        int yBuckets = Integer.parseInt(args[2]);
        
        System.out.println(data.getMinLat() + ", " + data.getMinLon() + ", " + data.getMaxLat() + ", " + data.getMaxLon());
        
        // parses the input txt file and calculates the min and max longitudes
        CensusData thedata = parse(args[0]);
        
        // request user input
        System.out.println("Please enter your box coordinates (separate by spaces - left right top bottom):");
        
        // stores user input in string box
        Scanner input = new Scanner(System.in);
        String dims = input.nextLine();
        String[] dimsArray = dims.split(" ");
        
        if(Integer.parseInt(dimsArray[0]) >= 1 && Integer.parseInt(dimsArray[1]) <= Integer.parseInt(args[1]) 
        		&& Integer.parseInt(dimsArray[2]) >= 1 && Integer.parseInt(dimsArray[3]) <= Integer.parseInt(args[2])) {

            Rectangle popRec = new Rectangle(Integer.parseInt(dimsArray[0]), Integer.parseInt(dimsArray[1])+1, 
					Integer.parseInt(dimsArray[2]), Integer.parseInt(dimsArray[3])+1);

			// storing in values before hand to increase prevent multiple lookup
			int size = thedata.getData_size();
			CensusGroup[] censusGroups = thedata.getData();
			int sumPop = 0;
			int sumTotal = 0;
			int evalTrue = 0;
			
			for (int i = 0; i < size; i++) {
				
				Rectangle currentGroupRect = Rectangle.makeOneRec(thedata, censusGroups[i], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
			
				// adds the population of census group if it is contained in the census rectangle
				if(currentGroupRect.isContained(popRec)){
					sumPop = sumPop + censusGroups[i].getPopulation();
					evalTrue++;
				}
				
				// keeps track of all the people
				sumTotal = sumTotal + censusGroups[i].getPopulation();
			}
			
			System.out.println("Population in census rectangle: " + sumPop);
			System.out.println("Percentage of total population in rectangle: " + (sumPop/sumTotal)*100 + "%");
			System.out.println("Number of truths " + evalTrue);
        } else {
        	System.out.println("Error: Please reenter a valid set of coordinates");
        }
        
        //Create a bunch of rectangles based on max lon and lat and number of buckets
        //How do we associate each census group with a particular rectangle --> no preprocessing?
        
        //Query: Map entire rectangle of query base on lat and lon (create new rectangle)
        //Look at each CensusGroup and determine the small rectangle that it lies in 
        //And then see if that small rectangle is in the big rectangle (use encompass method)
        
        //Version3: Use matrix?
	}
}
