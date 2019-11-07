# BUFR Grammar

This repository contains a grammar for FM 94 BUFR (Binary univeral form for the representation of meteorological data).
The specification of the FM 94 BUFR is maintained by WMO in the (Manual on Codes)[https://library.wmo.int/doc_num.php?explnum_id=5831].

# Motiviation
A BUFR is build-up of sections. The following picture illustrates the different sections and their content.

[![BUFR Sections](https://raw.githubusercontent.com/mheene/bufr-grammar/master/pics/sections.png)]

Section 3 "Data description section" is the blue print for Section 4 "Data section".

Section 3 consists of descriptors. A descriptor contains 3 parts: F (2 bits) X (6 bits) and Y (8 bits).
* F = 0 is the "element descriptor" which defines a single data item in Table B[http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFRCREX_TableB_en.pdf]
* F = 1 is the "replication descriptor". In this case X indicates the number of descriptors to be repeated, and Y the total number of replications.
* F = 2 is the "operator descriptor". The operations the descriptors of an "operator descriptor" is described in Table C (http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableC_en.pdf)
* F = 3 is the "sequence descriptor". A sequence descriptor defins a list of lement  descriptors,  replication  descriptors,  operator  descriptors  and/or  sequence descriptors by reference to Table D (http://www.wmo.int/pages/prog/www/WMOCodes/WMO306_vI2/LatestVERSION/WMO306_vI2_BUFR_TableD_en.pdf)

