# SimpleNem12Parser

## Task Objectives:

- Parse a NEM12 file and then store it in a Collection of Object type MeterRead
- Is written using JDK 8
- Mavenized the project
- Create a Markdown file with a list of assumptions and questions

## Installation and Usage

- Project was built using maven and can be either built using the maven plugin or can built using the command line.
- To run with maven, clone the source code and run the following command: mvn compile exec:java -Dexec.args="path/to/csv/file"ie mvn compile exec:java -Dexec.args="C:/temp/SimpleNem12.csv"
- To run from command line copy all the source .java files into a folder simplenem12 and then compile all the sources files using javac \*.java and then run using the following command outside of the Simplenem12 folder: java simplenem12.TestHarness /path/to/csv/file ie java simplenem12.TestHarness C:\temp\SimpleNem12.csv

## Assumptions and Exclusions:

- The first record in the file is always a record type 100 and the last record of the file is record type 900
- If there is no matching header or trailer record in the file, the Collection will return null
- The Record Type 100 and 900 only have record type and no other data
- Record type 200 is split into three data elements which are separated by commas. The Data elements are record type, NM ID and Energy Unit
- The Record Type 300 is split into four elements which are separated by commas. The Data elements are Record Type, Date, Volume and Quality
- The start of Record 200 indicates records pertaining to the NM ID and all subsequent Record types of 300 belong to that NM ID (MeterRead Model) until the next Record type 200 is encountered or the last Record type of 900 is encountered.
- It is assumed all the data elements in each of the record types is of the correct data type and the data is already cleansed. ie The value of the Meter Volume is of type numeric or the meter reading date is in the following format yyyyMMdd
- The existing interface SimpleNem12Parser signature was not changed. ie It was not changed to cater for throwing new exceptions such as when a Record type of 100 or 900 was missing in the NEM12 file
- No External Libraries were used and functionality within JDK 8 was used
