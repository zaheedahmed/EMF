package cmu.pasta.mu2.fuzz;

import cmu.pasta.mu2.instrument.MutationInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MutantFilter that filters a list of MutationInstances
 * by choosing the k least-executed mutants.
 */
public class KLeastExecutedFilter implements MutantFilter {
    /**
     * The number of MutationInstances the filtered list should contain,
     * as set in the constructor.
     */
    private int k;
    /**
     * A map of MutationInstances to the number of times they have been executed.
     */
    private HashMap<MutationInstance, Integer> executionCounts;
    
    /**
    * Constructor for KLeastExecutedFilter
    * @param k the number of MutationInstances the filtered list should contain
    */
    public KLeastExecutedFilter(int k) {
        this.k = k;
        this.executionCounts = new HashMap<MutationInstance, Integer>();
    }
    
    /**
    * Filter method that takes in a list of MutationInstances to be filtered
    * and returns a filter list of the k least-executed MutationInstances.
    * @param toFilter the list of MutationInstances to be filtered
    * @return         the filtered list of k least-executed MutationInstances
    */
    @Override
    public List<MutationInstance> filterMutants(List<MutationInstance> toFilter) {
 
        // initialize filtered list to be returned
        ArrayList<MutationInstance> filteredList = new ArrayList<MutationInstance>();

        // add (up to k) mutants in toFilter that have not been executed before to filteredList
        int numMutants = 0;
        for (MutationInstance mutant : toFilter){
            if (numMutants < k && !executionCounts.containsKey(mutant)){
                filteredList.add(mutant);
                numMutants++;
            }
        }

        // if numMutants < k mutants have never been executed, add the next k - numMutants least executed to filtered list
        if (numMutants < k){

            // get list of MutationInstances sorted by execution count
            List<Map.Entry<MutationInstance, Integer>> sortedMutants = new ArrayList<Map.Entry<MutationInstance, Integer>>(executionCounts.entrySet());
            Collections.sort(sortedMutants, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));

            // add least executed to sortedMutants until |filteredList| = k
            int size = sortedMutants.size();
            for(int i = 0; i < size && numMutants < k; i++){
                MutationInstance mutant = sortedMutants.get(i).getKey();
                filteredList.add(mutant);
                numMutants++;
            }
        }

        // increment execution count for each mutant in filteredList
        for (MutationInstance mutant: filteredList) {
            if (!executionCounts.containsKey(mutant)){
                executionCounts.put(mutant, 1); 
            } else {
                executionCounts.put(mutant, executionCounts.get(mutant)+1);
            }
        }

        return filteredList;
    }
    
}
