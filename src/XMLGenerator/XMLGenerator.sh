#!/bin/bash
#XML Generator
#David Petras
#July 8, 2013
#This script will take the results file in conjunction with the directory structures from
#the test harness and produce an XML file for the VisMAN visualization tool.

#Arguments
#1 - location of the results file from the test harness
#2 - location of the original source directory
#3 - location of the JUnit source file
#4 - location of the mutant source directory

resultsfile=$1
mutantfile=""
mutanttype=""
mutantpath=""

#Add the XML header tag.
echo "<?xml version=\"1.0\"?>" >> xml_output.txt

#Add the opening data tag.
echo "<data>" >> xml_output.txt

#Add the opening tag marking the original program.
echo "<original_program>" >> xml_output.txt

#For each .java file in the source folder extract the code, the name of the file, and 
#determine if it contains the main method.
sourceholder=$(find $2 -name '*.java')

#Aggregate the results from all the mutant logs.
logholder=$(find $4 -name 'mutation_log')

fulllog=""

for logpath in $logholder
do
	while read logline
	do
		fulllog="$fulllog $logline"
	done < $logpath 
done


for sourcepath in $sourceholder
do
	#TODO check if class contains the main method.

	#Get the name of the source file.
	IFS='/' read -a patharray <<< "$sourcepath"
	sourcename=${patharray[${#patharray[@]}-1]}

	#Add the opening tag for the source code.
	echo "<source_code name=\"$sourcename\" main=\"false\">" >> xml_output.txt

	#Read each line from the source code, replace reserved symbols, and add the line
	#to the output file.
	while read sourceline
	do
		sourceline="${sourceline//\&/&amp;}"
		sourceline="${sourceline//\</&lt;}"
		sourceline="${sourceline//\>/&gt;}"
		sourceline="${sourceline//\"/&quot;}"
		sourceline="${sourceline//\'/&apos;}"
		echo $sourceline >> xml_output.txt
	done < $sourcepath

	#Add the closing tag for the source code.
	echo "</source_code>" >> xml_output.txt
	
done

#Add the closing tag for the original program section.
echo "</original_program>" >> xml_output.txt

#Get the names of each test file and add them to a variable.
testnames=""
nextistest=false
while read testline
do
	if [ $nextistest = true ]; then
		testnames="$testnames $testline"
		nextistest=false
	else
		if [ "$testline" == "@Test" ]; then
			nextistest=true
		fi
	fi
	
done < $3

testmethods=""

#Extract only the name of the test case.
for test in $testnames
do	
	if [[ $test =~ (.*)\(\) ]]; then
		name="${BASH_REMATCH[1]}"
		testmethods="$testmethods $name"
	fi
done

while read line
do
	#Check if the line is a header for a new mutant.
	if [[ $line =~ =(.*?)=(.*?)=(.*?)= ]]; then
		mutantfile="${BASH_REMATCH[1]}"
		mutanttype="${BASH_REMATCH[2]}"
		mutantpath="${BASH_REMATCH[3]}"

		successtests=""
		
		IFS='_' read -a mutantarray <<< "$mutanttype"
		cleanmutant="${mutantarray[0]}"
		
		
		#Add the opening tag for the mutant.
		echo "<mutant_program name=\"$mutanttype\" type=\"$cleanmutant\">" >> xml_output.txt
		
		#Get the line numbers from the mutation log.
		for logline in $fulllog
		do
			IFS=':' read -a logarray <<< "$logline"
			loggedmutant="${logarray[0]}"
			loggedlinenumber="${logarray[1]}"

			if [ "$loggedmutant" == "$mutanttype" ]; then
				finalline=$loggedlinenumber
				break
			fi


		done
		#Add the modified source.
		echo "<modified_source name=\"$mutantfile\" start_line=\"$finalline\" end_line=\"$finalline\">" >> xml_output.txt
		
		while read modifiedline
		do
			modifiedline="${modifiedline//\&/&amp;}"
			modifiedline="${modifiedline//\</&lt;}"
			modifiedline="${modifiedline//\>/&gt;}"
			modifiedline="${modifiedline//\"/&quot;}"
			modifiedline="${modifiedline//\'/&apos;}"
			echo $modifiedline >> xml_output.txt
		done < $mutantpath

		#Add the closing tag for the modified source.
		echo "</modified_source>" >> xml_output.txt
		
		
	elif [[ $line =~ true|false ]]; then

		#Compare the list of all tests to those that are known successful and output the required tags.
		for testcase in $testmethods
		do
			match=false
			for checkcase in $successtests
			do
				if [ "$testcase" == "$checkcase" ]; then
					match=true
				fi
			done
			
			if [ $match = true ]; then
				echo "<test name=\"$testcase\">" >> xml_output.txt
				echo "<result>yes</result>" >> xml_output.txt
				echo "</test>" >> xml_output.txt
			else
				echo "<test name=\"$testcase\">" >> xml_output.txt
				echo "<result>no</result>" >> xml_output.txt
				echo "</test>" >> xml_output.txt
			fi
		done
		#The last line for the mutant has been reached.  Close the mutant program tag.
		echo "</mutant_program>" >> xml_output.txt
	else
		#Add the name of the test case to the variable containing all of the successful test cases (ie. those that detected the mutant).
		if [[ $line =~ (.*)\(.*\) ]]; then
			successtests="$successtests ${BASH_REMATCH[1]}"
		fi 
		
	fi

done < $resultsfile

#Add the closing data tag.
echo "</data>" >> xml_output.txt


