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
		//Print out our run configurations
	    System.out.println("File: " + args[0]);
        System.out.println("arg1: " + args[1]);
        System.out.println("arg2: " + args[2]);
        System.out.println("arg3: " + args[3]);

        //Initialize a new CensusData and call parse to read in the data
        CensusData data = new CensusData();
        data = parse(args[0]); 
        
        //Store our grid dimensions as ints
        int xBuckets = Integer.parseInt(args[1]);
	    int yBuckets = Integer.parseInt(args[2]);
	    
	    //Request user input
	    System.out.println("Please enter your box coordinates (separate by spaces - left right top bottom):");
	    
	    //Stores user input in dimsArray
	    Scanner input = new Scanner(System.in);
	    String dims = input.nextLine();
	    String[] dimsArray = dims.split(" ");
	    
		//Initializing values - used in all versions
		int size = data.getData_size(); //number of data entries
		CensusGroup[] censusGroups = data.getData(); //array of censusGroups
		int sumPop = 0; //total population within the queryRec
		int sumTotal = 0; //total population of all the data
		int evalTrue = 0; //test variable
	    
	    //Check to make sure input query is valid
	    if(Integer.parseInt(dimsArray[0]) >= 1 && Integer.parseInt(dimsArray[1]) <= Integer.parseInt(args[1]) 
	    		&& Integer.parseInt(dimsArray[2]) >= 1 && Integer.parseInt(dimsArray[3]) <= Integer.parseInt(args[2])) {
	    	
	    	//Create a rectangle to represent the query rectangle
	        Rectangle queryRec = new Rectangle(Integer.parseInt(dimsArray[0]), Integer.parseInt(dimsArray[1])+1, 
					Integer.parseInt(dimsArray[2]), Integer.parseInt(dimsArray[3])+1);
	
	        //If v1, we execute our program sequentially - least efficient
	        if(args[3].equals("-v1")) {
			    
			    //Call findEdgesSeq to set minLat, minLon, maxLat, maxLon sequentially
			    data.findEdgesSeq();
			    
			    //Print out the minLat, minLon, maxLat, maxLon
			    System.out.println(data.getMinLat() + ", " + data.getMinLon() + ", " + data.getMaxLat() + ", " + data.getMaxLon());
			    
			    	//We make a new rectangle for each censusGroup and check to see if it is contained by queryRec
					for (int i = 0; i < size; i++) {
						
						Rectangle currentGroupRect = Rectangle.makeOneRec(data, censusGroups[i], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
					
						//Adds the population of census group if it is contained in the census rectangle
						if(currentGroupRect.isContained(queryRec)){
							sumPop = sumPop + censusGroups[i].getPopulation();
							evalTrue++; //update our test variable
						}
						
						//Keeps track of all the people
						sumTotal = sumTotal + censusGroups[i].getPopulation();
					}
	        }
	        
	        if(args[3].equals("-v2")) {
	        	
	        	//Call findEdgesSeq to set minLat, minLon, maxLat, maxLon sequentially
			    data.findEdgesPar();
			    
			  //Print out the minLat, minLon, maxLat, maxLon
			    System.out.println(data.getMinLat() + ", " + data.getMinLon() + ", " + data.getMaxLat() + ", " + data.getMaxLon());
			    
			    	//We make a new rectangle for each censusGroup and check to see if it is contained by queryRec
					for (int i = 0; i < size; i++) {
						
						Rectangle currentGroupRect = Rectangle.makeOneRec(data, censusGroups[i], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
					
						//Adds the population of census group if it is contained in the census rectangle
						if(currentGroupRect.isContained(queryRec)){
							sumPop = sumPop + censusGroups[i].getPopulation();
							evalTrue++; //update our test variable
						}
						
						//Keeps track of all the people
						sumTotal = sumTotal + censusGroups[i].getPopulation();
					}
	        }
					
	    } else {
			    	
	    	System.out.println("Error: Please reenter a valid set of coordinates");
			    	
	    }
	    
		System.out.println("Population in census rectangle: " + sumPop);
		System.out.println("Percentage of total population in rectangle: " + (sumPop/sumTotal)*100 + "%");
		System.out.println("Number of truths " + evalTrue);
	        
        //Create a bunch of rectangles based on max lon and lat and number of buckets
        //How do we associate each census group with a particular rectangle --> no preprocessing?
        
        //Query: Map entire rectangle of query base on lat and lon (create new rectangle)
        //Look at each CensusGroup and determine the small rectangle that it lies in 
        //And then see if that small rectangle is in the big rectangle (use encompass method)
        
        //Version3: Use matrix?
	}
}
