# Team 4 Project 2 Code Implementation

#### Members: Floyd Chen, Yuhao Li and Xueman Liang

## The purpose of the Program
This program is an automatic voting system for user whose input is a CSV file that contains useful information about the vote. This program supports both Droop Quota Algorithm and Plurality Algorithm for voting.

## Input Requirement
This program requires the input voting data to be presented in a valid Comma Seperated Value (CSV) file, and this CSV file must be in the same working directory as the program excutable. This CSV file also contains the user inputs of seats to fill and algorithm to run. The CSV file must strictly follow the following format to run the program.

![](https://raw.githubusercontent.com/floydchenchen/pictures/master/870aace0-442b-11e8-9c94-86fb1170b0bd.png)

This first row of the CSV file contains the 'Number of Winners' information with the second(2) column contains an interger that indicates the seats to be filled of the election. The second row of the CSV file contains the 'Type of Election' information with the second(2) column contains an integer indicating the chosen algorithm (0 for plurality and 1 for droop quota). The third row contains the information of candidate, and starting from the fourth row are the ballot date.


## Detailed Information about the Program
This program can be run under the developer mode, at which the shuffle option will be automatically disabled. To enter the developer mode, add letter "d" as the first argument while executing the program; otherwise, the program will be run under user mode.

After running the program, the election results will be printed on console with votes received by each candidate. Please note, if droop quota is chosen, the printed number of votes received by each loser is BEFORE the vote re-distribution.  Also, a text file named 'output.txt' which is the 'short report' requested by product owner and a text file named 'audit_report.txt' will be generated in the same working directory.

This prorgam assumes the input csv file is valid (i.e. the format of the file is correct, no empty lines is contained within the file). There is no validation step regarding the input csv file. Invalid csv file will cause un-handled exceptions and the termination of the this program. Also, please note, this program is not aim to process droop quota format csv file using plurality algorithm or vise versa; However, plurality algorithm can be run on a droop quota format csv file by only consider candidate with the highest ranking on each ballot.

## How to Run and Compile the Program

#### To compile source codes: 

```
javac VotingSystem.java
```

#### To run the program:
* developer mode

```
java VotingSystem d  or java VotingSystem d [input.csv]
```
* user mode

```
java VotingSystem or java VotingSystem [input.csv]
```

