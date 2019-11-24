/*
MIT License

Copyright (c) 2019 Markus Heene <Markus.Heene@dwd.de>

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
//import org.antlr.v4.runtime.tree.*;
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
