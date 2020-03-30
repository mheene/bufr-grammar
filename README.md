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

You can find all railroad diagrams of the BUFR grammar in the [BUFR grammar](https://mheene.github.io/bufr-grammar.html).

A basic version of a BUFR grammar is available on [here](https://github.com/mheene/bufr-grammar/blob/gradle/src/main/antlr/BUFR.g4). The grammar is formulated in [ANTLR](https://www.antlr.org/). With the help of the grammar a BUFR template developer can now check her/his template for syntax errors. The BUFR specification is maintained by WMO as a human readable document while a machine readable grammar does not exists. Furthermore no reference implementation of the BUFR specification exists. Therefore a validation of a BUFR decoder/encoder or an BUFR against the specification isn’t easy. This step could be simplified with a grammar.


Note: You can find the [grammar in BNF<sup>1</sup>-style convention for the Java programming language on Oracle website](https://docs.oracle.com/javase/specs/jls/se7/html/jls-18.html) or in [antlr-style on github](https://github.com/antlr/grammars-v4/tree/master/java) 
<sup>1</sup> Backus-Naur-Form.

# Usage - java version
The project uses [gradle](https://gradle.org/) as a build tool. The project comes with a gradle wrapper. In case you consider to create an own gradle wrapper please you a recent gradle version (5.x or higher) installation and keep in mind that JDK 13 requires a gradle version 6.x.

Requirements:
You need a recent java installation (JDK 8 or higher)
```
javac -version
```

Clone this repository:

```
git clone https://github.com/mheene/bufr-grammar.git
```

or download it as a zip-archive

```
wget https://github.com/mheene/bufr-grammar/archive/master.zip
```

Build the project as a standalone jar:

```
cd bufr-grammar
./gradlew jar
```

The jar file contains all dependencies. You will find the jar in the directory /build/libs

Check the file 301003.txt with the BUFR grammar and the additional replication descriptor checks:

```
java -jar build/libs/counter-0.0.1.jar material/valid/301003.txt
```

Use ANTLRs test rig (grun) to check a file against the grammar and visualize the parse tree:

```
./gradlew grun -PinputFile=material/valid/301003.txt
```

Verify all WMO sequences (Table D) against the grammar (including additional replication descriptor checks) and some additional test cases.

```
./gradlew test
``` 

# Usage - python version
The project uses [gradle](https://gradle.org/) as a build tool. The project comes with a gradle wrapper. In case you consider to create an own gradle wrapper please use a recent gradle version (5.x or higher) installation and keep in mind that JDK 13 requires a gradle version 6.x.

Clone repository (see above)

Build the project as a standalone distribution:
```
cd bufr-grammar
./gradlew pythonDist
```
This creates the archive build/dist/bufr-grammar.tgz 
The archive contains the grammar, a lexer and a parser in python.

## Install the python distribution
Create a directory and unpack the archive 

```
tar xvfz bufr-grammar.tgz) 
```

Install python dependencies
```
pip3 install --user -r requirements.txt
```

Check a BUFR template against the grammar
```
python3 BUFRStart.py <template.txt>
```





# Presentation and poster
An motivation and introduction including examples to the BUFR grammar is available as a [powerpoint talk](https://view.officeapps.live.com/op/view.aspx?src=https://github.com/mheene/bufr-grammar/raw/master/talk/BUFR-Grammar-Talk.ppt). The poster is available as pdf and a [powerpoint](https://view.officeapps.live.com/op/view.aspx?src=https://github.com/mheene/bufr-grammar/raw/master/poster/IN23D-0901_agu_2019.pptx).
