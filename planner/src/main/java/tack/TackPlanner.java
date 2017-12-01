package tack;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.google.common.base.Preconditions;

import formulae.mitli.MITLIFormula;
import formulae.mitli.parser.MITLILexer;
import formulae.mitli.parser.MITLIParser;
import ta.SystemDecl;
import ta.parser.TALexer;
import ta.parser.TAParser;
import tack.checker.Planner;

public class TackPlanner {

	public static void main(String[] args) throws Exception {
		PrintStream out = System.out;
		/*PrintStream out = 
				new PrintStream(ByteStreams.nullOutputStream());
*/
		out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
		out.println("TACK  - Planner");
		out.println("v. 0.0.1 - 18/06/2017\n");
		out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		Preconditions.checkArgument(args.length >= 3,
				"you must specify the file that contains the MITLI formula and the bound to be used");

		


		String modelFile = args[0];
		String propertyFile = args[1];		
		String bound=args[2];
		
		Preconditions.checkArgument(Files.exists(Paths.get(modelFile)), "The file: " + modelFile + " containing the model does not exist");
		Preconditions.checkArgument(Files.exists(Paths.get(propertyFile)), "The file: " + propertyFile + " containing the property does not exist");
	
		out.println("Loading the property: "+propertyFile);	
		// loading the property
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(propertyFile));
		MITLILexer lexer = new MITLILexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MITLIParser parser = new MITLIParser(tokens);
		parser.setBuildParseTree(true);
		MITLIFormula formula = parser.mitli().formula;

		out.println("Property loaded");
		
		
		// loading the model
		out.println("Loading the model: "+modelFile);

		ANTLRInputStream tainput = new ANTLRFileStream(modelFile);
		TALexer talexer = new TALexer(tainput);
		CommonTokenStream tatokens = new CommonTokenStream(talexer);
		TAParser taparser = new TAParser(tatokens);
		taparser.setBuildParseTree(true);
		SystemDecl system = taparser.ta().systemret;

		out.println("Model loaded");
		Planner checker = new Planner(system, formula, Integer.parseInt(bound), System.out);
		boolean result = checker.plan(null);

		out.println();
		out.println();
		out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		out.println("A plan is : "+((result) ? "available" : "not available"));
		out.println(((result) ? "" : "check the file output.hist.smt.txt to see the plan"));
		out.println(((result) ? "" : "the mapping between the elements of the model and the used id can be found in the file elementsIDmap.txt"));
		out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
	}

}
