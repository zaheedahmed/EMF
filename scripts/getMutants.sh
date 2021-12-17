#!/bin/bash

#Leaves a venn diagram of Zest's mutant finding vs. Mu2's mutant finding in the current directory.
#example usage:
#  ./getMutants.sh edu.berkeley.cs.jqf.examples.commons.PatriciaTrieTest testPrefixMap org.apache.commons.collections4.trie 3 1000 ../../jqf/examples

fuzz() {
    echo mvn jqf:fuzz -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dout=tmpZest/exp_$4 -Dengine=zest -DrandomSeed=$4 -Dtrials=$5
    mvn jqf:fuzz -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dout=tmpZest/exp_$4 -Dengine=zest -DrandomSeed=$4 -Dtrials=$5
    echo mvn jqf:fuzz -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dout=tmpMu2/exp_$4 -Dengine=mutation -DrandomSeed=$4 -Dtrials=$5
    mvn jqf:fuzz -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dout=tmpMu2/exp_$4 -Dengine=mutation -DrandomSeed=$4 -Dtrials=$5
}

getResults() {

    # Debug purposes - dump args to look at actual files
    mvn jqf:repro -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dinput=target/tmpZest/exp_$4/corpus -DdumpArgsDir=target/tmpZest/exp_$4/args_corpus/
    mvn jqf:repro -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dinput=target/tmpMu2/exp_$4/corpus -DdumpArgsDir=target/tmpMu2/exp_$4/args_corpus/

    mvn mu2:mutate -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dinput=target/tmpZest/exp_$4/corpus > results/zest-results-$4.txt
    mvn mu2:mutate -Dclass=$1 -Dmethod=$2 -Dincludes=$3 -Dinput=target/tmpMu2/exp_$4/corpus > results/mutate-results-$4.txt

    cat results/zest-results-$4.txt | grep "Running Mutant\|FAILURE" > filters/zest-filter-$4.txt
    cat results/mutate-results-$4.txt | grep "Running Mutant\|FAILURE" > filters/mutate-filter-$4.txt

}

CURDIR=$(pwd)
cd $6
mkdir filters
mkdir results

for i in $(seq 1 1 $4)
do
    fuzz $1 $2 $3 $i $5
done

for i in $(seq 1 1 $4)
do
    getResults $1 $2 $3 $i
done

cd $CURDIR
python3 venn.py --filters_dir $6/filters --num_experiments $4 --output_img venn.png

#comment the below lines to not remove the created files
# rm -r filters
# rm -r results
# rm -r target/tmpZest
# rm -r target/tmpMu2
