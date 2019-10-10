grammar Descriptor;

template: expr (SPACE expr)*;

expr: (element_descriptor | sequence_descriptor | operator_descriptor_expr)
	| replication_descriptor (SPACE replication_descriptor)* SPACE 
	(
		element_descriptor | sequence_descriptor | operator_descriptor_expr
	);

element_descriptor: F_EL SPACE X SPACE Y;

operator_descriptor_expr: 
	operator_descriptor | (operator_descriptor SPACE data_description_operator_qualifier)
	;

operator_descriptor: F_OP SPACE X SPACE ( Y | DELAYED_REPLICATION_FACTOR);

sequence_descriptor: F_SEQ SPACE X SPACE Y;

replication_descriptor:
	fixed_replication_descriptor | delayed_replication_expr;

delayed_replication_expr:
	delayed_replication_descriptor SPACE data_description_operator_qualifier
	;

delayed_replication_descriptor:
	F_REP SPACE X SPACE DELAYED_REPLICATION_FACTOR;

fixed_replication_descriptor:
	F_REP SPACE X SPACE Y {System.out.println("Replication factor: " + $Y.text);};

// (('000') | ('001') |('002') |('011') | ('012') | ('021')| ('031')) 
data_description_operator_qualifier:
	F_EL SPACE '31' SPACE (DELAYED_REPLICATION_FACTOR | Y);

F_EL: ('0');

F_REP: ('1');

F_OP: ('2');

F_SEQ: ('3');



DELAYED_REPLICATION_FACTOR: ('000');

Y: ('0')('0')('1' .. '9') | ('0')('1' .. '9') ('0' .. '9') |('1')('0' .. '9')('0' .. '9') |
	 ('2') ('0' .. '4') ('0' .. '9')
	| ('2') ('5') ('0' .. '5')
	;

SPACE: ' ';

X: ('0' .. '2') ('0' .. '9')| '30' | ('3')('2' .. '9') | ('4' .. '5')('0' .. '9') | ('6') ('0' .. '3');

/* 
Y: ('0' .. '1') ('0' .. '9') ('0' .. '9')
	| ('2') ('0' .. '4') ('0' .. '9')
	| ('2') ('5') ('0' .. '5')
	;
*/
WS: [\r\n\t]+ -> skip;
