/*
MIT License

Copyright (c) 2019 Markus Heene

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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

		if (verboseErrorListener.getErrors() != null)
			System.err.println("Errors Listener: " + verboseErrorListener.getErrors());
		

	}
}
