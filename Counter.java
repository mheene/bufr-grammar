import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "deprecation"})
public class Counter {
    public static void main(String[] args) throws Exception {
	// create a CharStream that reads from standard input
	ANTLRInputStream input = new ANTLRInputStream(System.in);
	BUFRLexer lexer = new BUFRLexer(input);
	CommonTokenStream tokens = new CommonTokenStream(lexer);
	BUFRParser parser = new BUFRParser(tokens);
	ParseTree tree = parser.template();

	ParseTreeWalker walker = new ParseTreeWalker();
	walker.walk(new CountReplicationDescriptor(), tree);
	System.out.println();
    }
}
