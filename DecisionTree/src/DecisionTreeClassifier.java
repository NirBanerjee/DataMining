/**
 * @author nirmoho-Mac
 *
 */
import java.io.IOException;

import com.team13.datamining.DecisionTree.DecisionTree;

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

		DecisionTree dTree = new DecisionTree();
		dTree.treeOperations(args);
	}
}
