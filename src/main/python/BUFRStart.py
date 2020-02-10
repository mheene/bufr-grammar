import sys
from antlr4 import *
from BUFRLexer import BUFRLexer
from BUFRParser import BUFRParser
 
def main(argv):
    input_stream = FileStream(argv[1])
    lexer = BUFRLexer(input_stream)
    stream = CommonTokenStream(lexer)
    parser = BUFRParser(stream)
    tree = parser.template()
    print(tree.toStringTree(recog=parser))
 
if __name__ == '__main__':
    main(sys.argv)
