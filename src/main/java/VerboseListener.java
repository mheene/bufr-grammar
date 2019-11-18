import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
public class VerboseListener extends BaseErrorListener {
    public String errorString ;
@Override
public void syntaxError(Recognizer<?, ?> recognizer,Object offendingSymbol,
			int line, int charPositionInLine,
			String msg,
			RecognitionException e)
    {
	List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
	Collections.reverse(stack);
	errorString = errorString + "rule stack: " + stack;
	errorString = "line " + line + ":" + charPositionInLine + " at " +
	    offendingSymbol + ": " + msg ;
	System.err.println("rule stack: "+stack);
	System.err.println("line "+line+":"+charPositionInLine+" at "+
			   offendingSymbol+": "+msg);
    }
    public String getErrors() {
	return this.errorString;
    }
}
