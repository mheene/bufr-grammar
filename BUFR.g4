/* 94.5.3 Data description syntax for BUFR

94.5.3.1 Data description shall consist of one or more descriptors. 
Each descriptor shall occupy 2 octets and contain 3 parts: 
F (2 bits), X (6 bits) and Y (8 bits).

*/
grammar BUFR;

template: expr (SPACE expr)*;

expr: (
		element_descriptor
		| sequence_descriptor
		| operator_descriptor_expr
	)
	| replication_descriptor (SPACE replication_descriptor)* SPACE (
		element_descriptor
		| sequence_descriptor
		| operator_descriptor_expr
	);

/* 94.5.3.2 If F = 0, the descriptor shall be called an “element descriptor”.
 An element descriptor shall define a single data item by reference to Table B. */
element_descriptor:
	F_EL SPACE (X_PARTS | X_036 | X_001) SPACE (
			Y_PARTS
			| Y_001
			| Y_002
			| Y_011
			| Y_012
			| Y_021
			| Y_031
			| Y_000) 
		| data_description_operator_qualifier;


/* 94.5.5.1 If F = 2, the descriptor shall be called an “operator descriptor”. An operator descriptor shall
define an operation by reference to Table C.
Notes:
(1) X denotes the value corresponding to an operator defined within Table C.
(2) Y contains a value to be used as an operand in completing the defined operation.  */
operator_descriptor_expr:
	operator_descriptor
	| (operator_descriptor SPACE associated_field_significance)
	| operator_236000_expr
	;

operator_descriptor:
	F_OP SPACE (X_PARTS | X_031 | X_001) SPACE (
			Y_PARTS
			| Y_001
			| Y_002
			| Y_011
			| Y_012
			| Y_021
			| Y_031
			| Y_000) ; 


/* 94.5.5.3 A data present bit-map shall be defined as a set of N one bit values corresponding to N data
entities described by N element descriptors (including element descriptors for delayed
replication, if present); the data description of a data present bit-map is comprised of a
replication operator followed by the element descriptor for the data present indicator.  */
// 236000 101005 031031
// 236000 101000 031001 031031
operator_236000_expr:
	(operator_236000 SPACE ( fixed_replication_descriptor_one_element | delayed_replication_expr_one_element) SPACE data_present_indicator)
	;

operator_236000:
	F_OP SPACE X_036 SPACE Y_000
;

/* 94.5.6 Indirect reference to descriptors
94.5.6.1 If F = 3, the descriptor shall be called a “sequence descriptor”. A sequence descriptor shall
define a list of element descriptors, replication descriptors, operator descriptors and/or
sequence descriptors by reference to Table D.
Note: X denotes the Table D category, Y denotes the entry within the category. Table D entries contain
lists of commonly associated descriptors for convenience.  */
sequence_descriptor:
	F_SEQ SPACE (X_001 | X_031 | X_036 | X_PARTS) SPACE  (
			Y_PARTS
			| Y_001
			| Y_002
			| Y_011
			| Y_012
			| Y_021
			| Y_031
			| Y_000) ;

/*  94.5.4 The replication operation

94.5.4.1 If F = 1, the descriptor shall be called a “replication descriptor”. For this case, X shall indicate
the number of descriptors to be repeated, and Y the total number of occurrences (replications)
of the repeated subsequence.
Note: Where a replication operation includes delayed replication(s) within the scope of its replication, the
replication (or repetition) factor descriptor(s) from Class 31 shall be counted for X, except the one (if any)
located immediately after the replication description for which X is being calculated, as in the following
example:

106000 031001 008002 103000 031001 005002 006002 010002.

94.5.4.2 A value of Y = 0 associated with the replication descriptor shall indicate delayed replication. In
this case, the replication data description operator shall be completed by the next element
descriptor, which shall define a data item indicating the number of replications. This descriptor
may also indicate (by its value of Y) that the following datum is to be replicated together with
the following descriptor.
  */
replication_descriptor:
	fixed_replication_descriptor
	| delayed_replication_expr;


fixed_replication_descriptor:
	fixed_replication_descriptor_part | fixed_replication_descriptor_one_element
;


delayed_replication_expr:
 delayed_replication_expr_part | delayed_replication_expr_one_element
;

fixed_replication_descriptor_part:
F_REP SPACE (X_031 | X_036 | X_PARTS) SPACE (Y_PARTS
		| Y_001
		| Y_002
		| Y_011
		| Y_012
		| Y_021
		| Y_031)
// {System.out.println("Fixed Replicaton: Number of element to replicate: " + $x_all.text);}
;

fixed_replication_descriptor_one_element:
(F_REP SPACE X_001 SPACE (Y_PARTS
		| Y_001
		| Y_002
		| Y_011
		| Y_012
		| Y_021
		| Y_031))
;

delayed_replication_expr_part:
	delayed_replication_descriptor_part SPACE delayed_descriptor_replication_factor;

delayed_replication_expr_one_element:
	delayed_replication_descriptor_one_element SPACE delayed_descriptor_replication_factor;

delayed_replication_descriptor:
	delayed_replication_descriptor_part | delayed_replication_descriptor_one_element 
;

delayed_replication_descriptor_part:
	F_REP SPACE (X_031 | X_036 | X_PARTS) SPACE Y_000

	;

delayed_replication_descriptor_one_element:
	(F_REP SPACE X_001 SPACE Y_000)
	;

 data_description_operator_qualifier: delayed_descriptor_replication_factor |
 associated_field_significance | data_present_indicator;


delayed_descriptor_replication_factor:
	F_EL SPACE X_031 SPACE (
		Y_000
		| Y_001
		| Y_002
		| Y_011
		| Y_012
	);

associated_field_significance: F_EL SPACE X_031 SPACE Y_021;

data_present_indicator: F_EL SPACE X_031 SPACE Y_031;

/* x_all als parser rule 
x_all : X_031 | X_PARTS | X_036 | X_001;
*/

/*
y_all : y_without_0 | Y_000;

y_without_0: 	Y_PARTS
		| Y_001
		| Y_002
		| Y_011
		| Y_012
		| Y_021
		| Y_031
		;
 */

F_EL: ('0');
F_REP: ('1');
F_OP: ('2');
F_SEQ: ('3');

Y_000: ('000');
Y_001: ('001');
Y_002: ('002');
Y_011: ('011');
Y_012: ('012');
Y_021: ('021');
Y_031: ('031');

Y_PARTS: ('0') ('0') ('3' .. '9')
	| ('010')
	| ('01') ('2' .. '9')
	| ('020')
	| ('02') ('2' ..'9')
	| ('030')
	| ('03') ('2' ..'9')
	| ('0') ('4' .. '9') ('0' .. '9')
	| ('1') ('0' .. '9') ('0' .. '9')
	| ('2') ('0' .. '4') ('0' .. '9')
	| ('2') ('5') ('0' .. '5');

SPACE: ' ';

/* 
X_ALL: X_001 | X_031 | X_036 | X_PARTS
;
*/

X_001: ('01');

X_031: ('31');

X_036: ('36');

X_PARTS: 
	('00') | ('0')('2' .. '9')
	| ('1' .. '2') ('0' .. '9')
	| '30'
	| ('3') ('1' .. '5')
	| ('3') ('7' .. '9')
	| ('4' .. '5') ('0' .. '9')
	| ('6') ('0' .. '3');



WS: [\r\n\t]+ -> skip;
