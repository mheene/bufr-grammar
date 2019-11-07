# BUFR Grammar

This repository contains a grammar for FM 94 BUFR (Binary universal form for the representation of meteorological data).
The specification of the FM 94 BUFR is maintained by WMO in the [Manual on Codes](https://library.wmo.int/doc_num.php?explnum_id=5831).

# Motiviation
A BUFR is a binary code to encode meteorological data. The BUFR consists of sections. The following picture illustrates the different sections and their content.

![BUFR Sections](https://github.com/mheene/bufr-grammar/blob/master/pics/sections.png)

Section 3 "Data description section" is the blue print for Section 4 "Data section". 

Section 3 consists of descriptors. A descriptor contains 3 parts: F (2 bits) X (6 bits) and Y (8 bits).

* F = 0 is the "element descriptor" which defines a single data item (temperature, pressure, ...) in [Table B](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFRCREX_TableB_en.pdf)

* F = 1 is the "replication descriptor". In this case X indicates the number of descriptors to be repeated, and Y the total number of replications. For Y = O we call the the descriptor "delayed replication descriptor" and the number of replications is encoded in the data section with the so called "delayed descriptor replication factor" e.g. 0 31 000, 0 31 001, ...

* F = 2 is the "operator descriptor". The operations the descriptors of an "operator descriptor" is described in [Table C](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableC_en.pdf)

* F = 3 is the "sequence descriptor". A sequence descriptor defines a list of element  descriptors,  replication  descriptors,  operator  descriptors  and/or  sequence descriptors by reference to [Table D](http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableD_en.pdf)

For the construction of section 3 with the 4 descriptors (element, replication, operator and sequence descriptor) described above certain rules apply. The rules are layout in detail in the [Manual on Codes](https://library.wmo.int/doc_num.php?explnum_id=5831) starting at regulations 94.1 and following. In addition the rules in the tables apply in particular those for the operator descriptors.

Looking from a different perspective the rules as a whole can be interpreted as a language. This language is a set of valid sentences, and sentences are composed of phrases ... (can than be described my a grammar like Java, python, ... . 


 Obviously a replication descriptor can't be at the end. The 