package tack;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Ignore;
import org.junit.Test;

import formulae.mitli.MITLIFormula;
import formulae.mitli.parser.MITLILexer;
import formulae.mitli.parser.MITLIParser;
import solvers.CLTLocsolver;
import solvers.MITLIsolver;
import ta.SystemDecl;
import ta.TA;
import ta.parser.TALexer;
import ta.parser.TAParser;
import ta.visitors.TANetwork2CLTLocRC;
import tack.checker.SystemChecker;
import zotrunner.ZotException;

public class TACheckerExample2Test {

	/**
	 * The property is not related to the model and thus it is not satisfied
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ZotException
	 */
	@Test
	public void testFormula2() throws FileNotFoundException, IOException, ZotException {
		String path = ClassLoader.getSystemResource("tack/ta/examples/formula2.mitli").getPath();

		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(path));
		MITLILexer lexer = new MITLILexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MITLIParser parser = new MITLIParser(tokens);
		parser.setBuildParseTree(true);
		MITLIFormula formula2 = parser.mitli().formula;
		
	
		ANTLRInputStream tainput = new ANTLRFileStream(
				ClassLoader.getSystemResource("tack/ta/examples/example2.ta").getPath());
		TALexer talexer = new TALexer(tainput);
		CommonTokenStream tatokens = new CommonTokenStream(talexer);
		TAParser taparser = new TAParser(tatokens);
		parser.setBuildParseTree(true);
		SystemDecl system = taparser.ta().systemret;

		TA ta = system.getTimedAutomata().iterator().next();

		
		System.out.println(ta);
		System.out.println(ta.getLocalVariables());
		SystemChecker checker = new SystemChecker(system, formula2, 20, new TANetwork2CLTLocRC(), System.out);
		boolean result = checker.check(null);

		assertTrue(result);

	}
	
	/**
	 * The property is not related to the model and thus it is not satisfied
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ZotException
	 */
	@Test
	public void testFormula3() throws FileNotFoundException, IOException, ZotException {
		String path = ClassLoader.getSystemResource("tack/ta/examples/formula3.mitli").getPath();

		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(path));
		MITLILexer lexer = new MITLILexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MITLIParser parser = new MITLIParser(tokens);
		parser.setBuildParseTree(true);
		MITLIFormula formula3 = parser.mitli().formula;


		ANTLRInputStream tainput = new ANTLRFileStream(
				ClassLoader.getSystemResource("tack/ta/examples/example2.ta").getPath());
		TALexer talexer = new TALexer(tainput);
		CommonTokenStream tatokens = new CommonTokenStream(talexer);
		TAParser taparser = new TAParser(tatokens);
		parser.setBuildParseTree(true);
		SystemDecl system = taparser.ta().systemret;

		TA ta = system.getTimedAutomata().iterator().next();


		SystemChecker checker = new SystemChecker(system, formula3, 20, new TANetwork2CLTLocRC(),System.out);
		boolean result = checker.check(null);

		assertFalse(result);
	}

}
