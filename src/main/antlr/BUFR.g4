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
	operator_descriptor (SPACE associated_field_significance)?
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

Y_PARTS: '003' | '004' | '005' | '006' | '007' | '008' | 
'009' | '010' | '013' | '014' | '015' | '016' | 
'017' | '018' | '019' | '020' | '022' | '023' | '024' | 
'025' | '026' | '027' | '028' | '029' | '030' | '032' | 
'033' | '034' | '035' | '036' | '037' | '038' | '039' | '040' | 
'041' | '042' | '043' | '044' | '045' | '046' | '047' | '048' | 
'049' | '050' | '051' | '052' | '053' | '054' | '055' | '056' | 
'057' | '058' | '059' | '060' | '061' | '062' | '063' | '064' | 
'065' | '066' | '067' | '068' | '069' | '070' | '071' | '072' | 
'073' | '074' | '075' | '076' | '077' | '078' | '079' | '080' | 
'081' | '082' | '083' | '084' | '085' | '086' | '087' | '088' | 
'089' | '090' | '091' | '092' | '093' | '094' | '095' | '096' | 
'097' | '098' | '099' | '100' | '101' | '102' | '103' | '104' | 
'105' | '106' | '107' | '108' | '109' | '110' | '111' | '112' | 
'113' | '114' | '115' | '116' | '117' | '118' | '119' | '120' | 
'121' | '122' | '123' | '124' | '125' | '126' | '127' | '128' | 
'129' | '130' | '131' | '132' | '133' | '134' | '135' | '136' | 
'137' | '138' | '139' | '140' | '141' | '142' | '143' | '144' | 
'145' | '146' | '147' | '148' | '149' | '150' | '151' | '152' | 
'153' | '154' | '155' | '156' | '157' | '158' | '159' | '160' | 
'161' | '162' | '163' | '164' | '165' | '166' | '167' | '168' | 
'169' | '170' | '171' | '172' | '173' | '174' | '175' | '176' | 
'177' | '178' | '179' | '180' | '181' | '182' | '183' | '184' | 
'185' | '186' | '187' | '188' | '189' | '190' | '191' | '192' | 
'193' | '194' | '195' | '196' | '197' | '198' | '199' | '200' | 
'201' | '202' | '203' | '204' | '205' | '206' | '207' | '208' | 
'209' | '210' | '211' | '212' | '213' | '214' | '215' | '216' | 
'217' | '218' | '219' | '220' | '221' | '222' | '223' | '224' | 
'225' | '226' | '227' | '228' | '229' | '230' | '231' | '232' | 
'233' | '234' | '235' | '236' | '237' | '238' | '239' | '240' | 
'241' | '242' | '243' | '244' | '245' | '246' | '247' | '248' | 
'249' | '250' | '251' | '252' | '253' | '254' | '255';

SPACE: ' ';

/* 
X_ALL: X_001 | X_031 | X_036 | X_PARTS
;
*/

X_001: ('01');

X_031: ('31');

X_036: ('36');

X_PARTS: 
	'00'  | '02' | '03' | '04' | '05' | '06' | '07' | '08' | 
            '09' | '10' | '11' | '12' | '13' | '14' | '15' | '16' | 
            '17' | '18' | '19' | '20' | '21' | '22' | '23' | '24' | 
            '25' | '26' | '27' | '28' | '29' | '30' | '32' | 
            '33' | '34' | '35' | '37' | '38' | '39' | '40' | 
            '41' | '42' | '43' | '44' | '45' | '46' | '47' | '48' | 
            '49' | '50' | '51' | '52' | '53' | '54' | '55' | '56' | 
            '57' | '58' | '59' | '60' | '61' | '62' | '63' ;



WS: [\r\n\t]+ -> skip;
