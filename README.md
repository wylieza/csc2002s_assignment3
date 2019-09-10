# Assignmnet 3 for the csc2002s course at UCT. Focus is on parallel computing to speed up a weather simulation.

To run application use make for compilation and execution.

Make command:               | Action
make                        | Compile all source files
make run args="<args here>" | Run CloudDataApp, replace <args here> with desired args - No args will show help
make clean                  | Clean the bin folder
make docs                   | Generare java docs
make cleandocs              | Clean the doc folder

CloudDataApp Arguments:
==================================================================
                >>> Cloud Data App <<< 
Arguments [0]: <>                                               | Show Help
Arguments [1]: <benchmarking script file name>                  | Run benchmarking script from input file supplied
Arguments [2]: <input data file name> <output data file name>   | Take input file, compute, produce output file (performs both seq. and para.)
================================================================== 

Benchmarking Script File Formatting (Example Supplied: benchmarking.txt):
<seq set bit> <para. set bit> <no. data sets> <no. cut offs> <index of test data> <index of test cut off> <architecture name>
<data file 1> <data file 2> .... <data file n>
<seq cut off 1> <seq cut off 2> .... <seq cut off m>


Description:
<seq set bit> -> Perform sequential vs parallel runtime testing: 1 = Yes | 0 = No
<para. set bit> -> Perform sequential cut off testing: 1 = Yes | 0 = No
<no. data sets> -> File names of the data files (end with .txt)
<no. cut offs> -> Numbers (integer) that will be used as the sequential cut off
<index of test data> -> Which data file to use during the sequential cut off testing phase (index starting from zero)
<index of test cut off> -> Which sequential cut off to use during the sequential vs paralle run time testing (index starting from zero)
<architecture name> -> The two output text files will have this keyword in them to identify the benchmark from others.

Final Notes:
Place all input files in the same directory as the makefile


Created by:
Justin Wylie
WYLJUS002