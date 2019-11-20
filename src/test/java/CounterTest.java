import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "deprecation" })
public class CounterTest {

	class ParserErrorMessages {
		private String iterationErrorText;
		private String otherErrorText;

		public ParserErrorMessages(String iterationErrorText, String otherErrorText) {
			this.iterationErrorText = iterationErrorText;
			this.otherErrorText = otherErrorText;
		}

		public String getIterationErrorText() {
			return this.iterationErrorText;
		}

		public String getOtherErrorText() {
			return this.getOtherErrorText();
		}

	}

	private ParserErrorMessages parseFile(File p_file) throws Exception {
		final InputStream original = System.in;
		final FileInputStream fis = new FileInputStream(p_file);
		System.setIn(fis);
		String iterationErrorString = null;

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
			String errorText = it.next();
			iterationErrorString = iterationErrorString + errorText;
		}

		ParserErrorMessages pem = new ParserErrorMessages(iterationErrorString, verboseErrorListener.getErrors());

		fis.close();

		System.setIn(original);

		return pem;

	}

	@Test
	public void testMain() throws Exception {
		// create a CharStream that reads from standard input
		final InputStream original = System.in;
		final FileInputStream fips = new FileInputStream(
				new File("build/classes/java/test/invalid/replications/wrong.txt"));
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
			String errorText = it.next();
			assertNotNull("Iterations should not be null", errorText);
			System.err.println(errorText);
		}

		System.err.println(verboseErrorListener.getErrors());

		System.setIn(original);

	}

	@Test
	public void testValidSequences() throws Exception {
		final InputStream original = System.in;

		final File validTestFileFolder = new File("build/classes/java/test/valid");
		for (final File fileEntry : validTestFileFolder.listFiles()) {
			if (fileEntry.isFile()) {
				final FileInputStream fis = new FileInputStream(fileEntry);

				System.setIn(fis);
				String iterationErrorString = null;

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
					String errorText = it.next();
					iterationErrorString = iterationErrorString + errorText;
				}

				
				assertNull("Iteration Errors should be null for file: " + fileEntry.getName(), iterationErrorString);
				assertNull("Other Errors should be null for file: " + fileEntry.getName(), verboseErrorListener.getErrors());
				fis.close();
				System.setIn(original);

			}
		}

	}

	@Test
	public void testInvalid() throws Exception {
		final InputStream original = System.in;

		final File validTestFileFolder = new File("build/classes/java/test/invalid");
		for (final File fileEntry : validTestFileFolder.listFiles()) {
			if (fileEntry.isFile()) {
				final FileInputStream fis = new FileInputStream(fileEntry);

				System.setIn(fis);
				String iterationErrorString = null;

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
					String errorText = it.next();
					iterationErrorString = iterationErrorString + errorText;
				}

				
				assertNull("Iteration Errors should be null for file: " + fileEntry.getName(), iterationErrorString);
				assertNotNull("Other Errors should not be null for file: " + fileEntry.getName(), verboseErrorListener.getErrors());
				fis.close();
				System.setIn(original);

			}
		}

	}

	@Test
	public void testInvalidReplications() throws Exception {
		final InputStream original = System.in;

		final File validTestFileFolder = new File("build/classes/java/test/invalid/replications");
		for (final File fileEntry : validTestFileFolder.listFiles()) {
			if (fileEntry.isFile()) {
				final FileInputStream fis = new FileInputStream(fileEntry);

				System.setIn(fis);
				String iterationErrorString = null;

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
					String errorText = it.next();
					iterationErrorString = iterationErrorString + errorText;
				}

				
				assertNotNull("Iteration Errors should not be null for file: " + fileEntry.getName(), iterationErrorString);
				assertNull("Other Errors should be null for file: " + fileEntry.getName(), verboseErrorListener.getErrors());
				fis.close();
				System.setIn(original);

			}
		}

	}

	@Test
	public void testInvalidReplications2() throws Exception {
		final InputStream original = System.in;

		final File validTestFileFolder = new File("build/classes/java/test/invalid/replications/both");
		for (final File fileEntry : validTestFileFolder.listFiles()) {
			if (fileEntry.isFile()) {
				final FileInputStream fis = new FileInputStream(fileEntry);

				System.setIn(fis);
				String iterationErrorString = null;

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
					String errorText = it.next();
					iterationErrorString = iterationErrorString + errorText;
				}

				assertNotNull("Iteration Errors should not be null for file: " + fileEntry.getName(), iterationErrorString);
				assertNotNull("Other Errors should not be null for file: " + fileEntry.getName(), verboseErrorListener.getErrors());
		
				
				fis.close();
				System.setIn(original);

			}
		}
	
	}
}
