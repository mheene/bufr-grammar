# BUFR Grammar

This repository contains a grammar for FM 94 BUFR (Binary universal form for the representation of meteorological data).
The specification of the FM 94 BUFR is maintained by WMO in the [Manual on Codes](https://library.wmo.int/doc_num.php?explnum_id=5831).

# BUFR Grammar - Introduction
A BUFR is a binary code to encode meteorological data. The BUFR consists of sections. The following picture illustrates the different sections and their content.

![BUFR Sections](https://github.com/mheene/bufr-grammar/blob/master/pics/sections.png)

**Section 3** "Data description section" is the blue print for **Section 4** "Data section". 

**Section 3** consists of descriptors. A descriptor contains 3 parts: F (2 bits) X (6 bits) and Y (8 bits).

* F = 0 is the "element descriptor" which defines a single data item (temperature, pressure, humidity, ...) in [Table B](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFRCREX_TableB_en.pdf)

* F = 1 is the "replication descriptor". In this case X indicates the number of descriptors to be repeated, and Y the total number of replications. For Y = O we call the descriptor "delayed replication descriptor" and the number of replications is encoded in the data section with the so called "delayed descriptor replication factor" e.g. 0 31 000, 0 31 001, ...

* F = 2 is the "operator descriptor". The operations are described in detail in [Table C](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableC_en.pdf)

* F = 3 is the "sequence descriptor". A sequence descriptor defines a list of element  descriptors,  replication  descriptors,  operator  descriptors  and/or  sequence descriptors.  [Table D](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableD_en.pdf) contains all "sequence descriptors".

For the construction of **Section 3** with the 4 types of descriptors (element, replication, operator
and sequence descriptor) described above certain rules apply. The rules are described in detail
in the [Manual on Codes](https://library.wmo.int/doc_num.php?explnum_id=5831) starting at regulations 94.1 and following.

# BUFR Grammar – Implementation
Looking from a different perspective the rules as a whole can be interpreted as a language. This language is a set of valid sentences, and sentences are composed of phrases and clauses. Hereby a sentence structure follows a grammar. In our case the descriptors represent the phrases
and clauses. The following sequence of descriptors is a valid sentence: 3 10 014 2 22 000 2 36 000 1 01 103 0 31 031 0 01 031. The rules for
applying and combining the descriptors are our grammar. Additional rules for the replication descriptors are implemented as listener pattern.

The following picture shows a railroad diagram for the parser rule “operator descriptor expr” ![railroad diagram](https://github.com/mheene/bufr-grammar/blob/gradle/pics/operator_expr.png). 

You can find all railroad diagrams of the BUFR grammar in the [docs](https://github.com/mheene/bufr-grammar/tree/gradle/docu/bufr-grammar.html) folder.




Note: You can find the [grammar in BNF<sup>1</sup>-style convention for the Java programming language on Oracle website](https://docs.oracle.com/javase/specs/jls/se7/html/jls-18.html) or in [antlr-style on github](https://github.com/antlr/grammars-v4/tree/master/java) 
<sup>1</sup> Backus-Naur-Form.

In case of BUFR WMO maintains the specification in a human readable document while a machine readable grammar does not exists. Furthermore no reference implementation of the BUFR specification exists.  

# BUFR decoders and encoders

As mention before no reference BUFR decoder or encoder exists in addition no test suite exists. Nevertheless many BUFR decoders and encoders exists mostly developed by persons from the meteorological community. While writing a simple parser for CSV, JSON or XML isn't in most cases a big deal writing a BUFR decoder or encoder is different. Without having implemented a BUFR decoder or encoder yet I assume it is more a task of several 100s or even 1000s hours. In any case the developer of the BUFR decoder and encoder needs to read and interpret the specification. Therefore you will find BUFR decoders and encoders which implement different strategies. While one BUFR decoder tries to decode even wrong encoded BUFR messages other follow closer the rules and report an error. To demonstrate this we take a closer look to the specification of a data present bit-map. The specification defines a data present bit-map as follow:

> 94.5.5.3 A data present bit-map shall be defined as a set of N one bit values corresponding to N data entities described by N element descriptors (including element descriptors for delayed replication, if present); the data description of a data present bit-map is comprised of a replication operator followed by the element descriptor for the data present indicator. 

Note: At least for a non-native speaker this text is quite hard to understand :wink:

Example 1:

2 36 000 1 01 002 0 31 031

while the following is wrong:

2 36 000 0 31 031 0 31 031 (indeed it is the expanded descriptor list but according to the specification it **shall** contain a replication operator)

You can find a test BUFR and the ecCodes filter to create the BUFR in the example directory.

While the first example is decoded by all tested BUFR decoders (bufrtools(DWD), ecCodes (ECWMF), PyBufrKit, libecBUFR, Geo::BUFR and BUFRDC (ECWMF)) only bufrtools detect the wrong crafted data present bit-map.

Example 2a:

(Poster)[https://view.officeapps.live.com/op/view.aspx?src=https://github.com/mheene/bufr-grammar/blob/master/IN23D-0901_draft_00_2019112.pptx]
