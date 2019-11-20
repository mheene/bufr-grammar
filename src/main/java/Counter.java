import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "deprecation" })
public class Counter {
	public static void main(String[] args) throws Exception {
		// create a CharStream that reads from standard input
		if (args.length == 0) {
			System.err.println("Please provide a filename with descriptors");
			System.exit(1);
		} else if (args.length > 1) {
			System.err.println("Please provide only one parameter");
			System.exit(1);
		}

		File templateFile = new File(args[0]);
		FileInputStream fis = new FileInputStream(templateFile);
		ANTLRInputStream input = new ANTLRInputStream(fis);
		//ANTLRInputStream input = new ANTLRInputStream(System.in);
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
			System.err.println("Errors iterator: " + it.next());
		}

		System.err.println("Errors Listener: " + verboseErrorListener.getErrors());
		

	}
}
