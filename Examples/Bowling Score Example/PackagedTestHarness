#!/bin/bash


#Arguments
#1 - location of file in package to start compilation process
#2 - the name of the test file

#Store the root directory.
rootdir=$(pwd)
#Check that all necessary directories are in the same directory as the test script.
if [ ! -d "./original" ]; then
	echo $'Original source directory was not located.\nScript will now terminate.'
	exit 0
fi

if [ ! -d "./mutants" ]; then
	echo $'Mutated source code directory was not located.\nScript will now terminate.'
	exit 0
fi

if [ ! -d "./testing_location" ]; then
	echo $'Testing location was not located.\nScript will now terminate.'
	exit 0
fi

#Create a variable that will hold a list of all the paths to the original source files.
sourceholder=$(find ./original/ -name '*.java')
mutantholder=$(find $rootdir/mutants/ -name '*.java*')


#Recursively copy the contents of the original directory to the testing_location.
cp -r "./original/." "./testing_location"

#For loop strucutre to iterate over each entry in the mutantholder and run the JUnit tests with
#each version of the mutant program.
for mutantpath in $mutantholder
do
	#Read each line from the mutant file to see if it is a package definition by using
	#a regular expression.
	packaged=false;
	while read codeline
	do
		#Read each line of the code and check if it is the package declaration.
		if [[ $codeline =~ package(.*?)\; ]]; then
			packaged=true;
			packagepath="${BASH_REMATCH[1]}"
			packagepath="${packagepath//.//}"
			packagepath="${packagepath// /}"
			break
		fi	
	done <$mutantpath
	
	#Using / as a delimiter, divide the path into an array of strings.  The last index in the array holds the name of the file
	#and the index before that holds the name of the mutant (TYPE_#).
	IFS='/' read -a patharray <<< "$mutantpath"
	filename=${patharray[${#patharray[@]}-1]}
	mutanttype=${patharray[${#patharray[@]}-2]}
	
	if [ $packaged = true ]; then
		#Determine the path to the unmutated version of the file.
		oldfile="$rootdir/testing_location/$packagepath/$filename"
	fi

	if [ $packaged = false ]; then
		#Determine the path to the unmutated version of the file.
		oldfile="$rootdir/testing_location/$filename"
	fi

	if [ $mutanttype != "original" ]; then

		mv $oldfile $oldfile.bak
		#Move the mutated version of the file into the testing_location.
		cp $mutantpath $oldfile
		#Change the directory to the testing_location and compile the source.
		cd "$rootdir/testing_location"
		javac $1.java
		#Place a header detailing the what mutant is being tested.
		echo "=$filename=$mutanttype=$mutantpath=" >> testingres.txt
	
		#Run the tests.
		java -cp ".:junit-4.10.jar" TestResultsSingle $2 >> testingres.txt
		
		#Delete the mutated source file.
		rm $oldfile
		
		#Rename the original file.
		mv $oldfile.bak $oldfile
	fi
	
done







