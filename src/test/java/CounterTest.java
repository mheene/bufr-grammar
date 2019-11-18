import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.Iterator;
import java.io.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "deprecation" })
public class CounterTest {
	public static void testMain(String[] args) throws Exception {
		// create a CharStream that reads from standard input
	    final InputStream original = System.in;
	    final FileInputStream fips = new FileInputStream(new File("wrong.txt"));
	    System.setIn(fips);

		ANTLRInputStream input = new ANTLRInputStream(System.in);
		BUFRLexer lexer = new BUFRLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		BUFRParser parser = new BUFRParser(tokens);
		parser.removeErrorListeners();
		VerboseListener verboseErrorListener = new VerboseListener();
		parser.addErrorListener(verboseErrorListener);
		ParseTree tree = parser.template();

		ParseTreeWalker walker = new ParseTreeWalker();
		CountReplicationDescriptor crd = new CountReplicationDescriptor();
		walker.walk(crd, tree);
		List<String> errors = crd.getErrors();

		Iterator<String> it = errors.listIterator();
		while (it.hasNext()) {
			System.err.println(it.next());
		}

		System.err.println(verboseErrorListener.getErrors());

		System.setIn(original);


	}
}
