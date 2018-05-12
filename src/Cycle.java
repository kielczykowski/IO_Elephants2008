import java.util.ArrayList;
import java.util.List;
/*
This is my implementation of task from Informatics Olimpiad (https://oi.edu.pl) from year 2008, first stage of tournament, which title is "Słonie"
Whole programme consists of 2 classes: Elephant and Cycle. Elephant is representing "elephants", Cycles are resposible for tracking a route which elephant needs to be swapped places with which elephant.
Cycles require only having weights of elephants which need to be swapped, because of the fact that it's not obligatory to sort them; only find the minimum energy input
*/

public class Cycle {
    private List<Long> weightList;

    public Cycle() {
        this.weightList = new ArrayList<>();
    }

    public List<Long> getWeightList() {
        return weightList;
    }

}
