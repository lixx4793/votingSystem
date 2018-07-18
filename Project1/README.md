# Team 4 Project 1 Code Implementation

## Members: Floyd Chen, Yuhao Li and Xueman Liang

This program requires the input voting data to be presented in a valid Comma Seperated Value (CSV) file, and this CSV file must be in the same working directory as the program excutable.
After running the program, the election results will be printed on console. Also, a text file named 'output.txt' that contains the election result and a text file named 'audit_report.txt' will be generated in the same working directory.

This prorgam assumes the input csv file is valid (i.e. the format of the file is correct, no empty lines is contained within the file). There is no validation step regarding the input csv file. Invalid csv file will cause un-handled exceptions and the termination of the this program. Also, please note, this program is not aim to process droop quota format csv file using plurality algorithm or vise versa; However, plurality algorithm can be run on a droop quota format csv file by only consider candidate with the highest ranking on each ballot. 

To compile source codes: javac VotingSystem.java

To run the program: java VotingSystem [input_file.csv]


