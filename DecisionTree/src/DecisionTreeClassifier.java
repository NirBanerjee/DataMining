/**
 * @author nirmoho-Mac
 *
 */
import java.io.IOException;

import com.team13.datamining.DecisionTree.DecisionTree;
import com.team13.datamining.datamodels.DataSet;
import com.team13.datamining.fileIO.FileIO;

public class DecisionTreeClassifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		//Check if all command line arguments are Present
		if (args.length != 6)	{
			System.out.println("Some Arguments are missing. Please Check and Try Again !!!");
			System.exit(1);
		}
		
		//Get all command line arguments
		String trainFile = args[0];
		String testFile = args[1];
		String trainPredictionsFile = args[2];
		String testPredictionsFile = args[3];
		String metricsFile = args[4];
		String printTreeFile = args[5];
		
		//Process the input file and load all input data into data class
		FileIO fileIO = new FileIO();
		DataSet trainingSet = fileIO.readFile(trainFile);
		
		//Process Test File and Obtain Test Data
		DataSet testSet = fileIO.readFile(testFile);
		
		//Build Tree using Train Data.
		DecisionTree dTree = new DecisionTree();
		dTree.buildDecisionTree(trainingSet);
		
		//Use validation data to find optimal height of tree
		
		//Predict Train labels after building tree
		
		//Write train, validation and test to metrics File
		
		//Predict Test labels
		
		//Print tree

	}
}
