import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
This is my implementation of task from Informatics Olimpiad (https://oi.edu.pl) from year 2008, first stage of tournament, which title is "Słonie"
Main goal of this programme is to find most economic way of moving "elephants" from first to final position with the least energetic input required.
Each "elephant" has its own weight which means energetic input is sum of two elephants being swapped at their places
Whole programme consists of 2 classes: Elephant and Cycle. Elephant is representing "elephants", Cycles are resposible for tracking a route which elephant needs to be swapped places with which elephant.
Programme only needs to find the most energetic-efficient sum of sorting these objects (it doesn't sort it) and prints its value as a output
Whole content of exercise can be found there https://oi.edu.pl/old/php/show.php?ac=p180000&module=show&file=zadania/oi16 (in Polish only)
*/


public class Elephant {
    private int id;
    private long weight;

    private Elephant(int id, long weight) {
        this.id = id;
        this.weight = weight;
    }

    private long getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "" + this.weight;
    }
                                                                                                                        //funkcja sprawdza czy nie brakuje jakichś danych lub gdy dane są wprowadzane ręcznie przez użytkownika
    private static void checkData(int[]firstPosition,int[]finalPosition,long[] weightTab) {                             //checking if input is correct
        if(finalPosition==null || firstPosition==null || weightTab==null) System.exit(1);
        if(firstPosition.length!= finalPosition.length || finalPosition.length!=weightTab.length) System.exit(1);
    }
                                                                                                                        //funckja zwracająca tabele na podstawie stringa
    private static int[] parseToIntTable(String str) {                                                                  //function parses String into Int table
        String[] strTab = str.split(" ");
        int[] intTable = new int[strTab.length];
        for(int i=0;i<strTab.length;i++) {
            intTable[i]=Integer.parseInt(strTab[i]);
        }
        return intTable;
    }
                                                                                                                        //funckja zwracająca tabele na podstawie stringa
    private static long[] parseToLongTable(String str) {                                                                //function parses String into Long table
        String[] strTab = str.split(" ");
        long[] intTable = new long[strTab.length];
        for(int i=0;i<strTab.length;i++) {
            intTable[i]=Integer.parseInt(strTab[i]);
        }
        return intTable;
    }
                                                                                                                        //funkcja, która określa czy dany słoń musi zostać zamieniony z innym
    private static boolean[] setToChange(int[] firstPosition, int[] finalPosition) {                                    //function defines if elephants need to get swapped with another
        boolean[] tab = new boolean[firstPosition.length];
        for(int i=0;i<finalPosition.length;i++) {
            if(firstPosition[i]==finalPosition[i])
                tab[i]=false;                                                                                           // false - słoń jest na dobrym miejscu  true - słoń wymaga zamiany
            else tab[i]=true;                                                                                           // false - its not needed to swap it  true - it needs to be swapped
        }
        return tab;
    }
    private static List<Elephant> createElephants(int[] firstPostion, long[] weightTab) {
        List<Elephant> elephants = new ArrayList<>();
        for(int f:firstPostion) {                                                                                       //first - 1 bo słonie numerowane od 1 a w liscie od 0
            elephants.add(new Elephant(f,weightTab[f-1]));                                                              //first - 1 elephants are numbered from 1 whilst in table it starts from 0
        }
        return elephants;
    }

    private static List<Cycle> createCycles(int[] firstPosition, boolean[] toChange, List<Elephant> elephants, int[] compareTable) {
        List<Cycle> cycles = new ArrayList<>();
        int x;
        for(int i=0;i<firstPosition.length;i++) {
            if (toChange[i]) {
                x = i;
                cycles.add(new Cycle());
                while (toChange[x]) {
                    toChange[x] = false;                                                                                //do ostatnio dodanego cyklu bierze listę mas słoni w cyklu i oddaje masę słonia będącego pod indexem "x" z first position
                    cycles.get(cycles.size() - 1).getWeightList().add(elephants.get(x).getWeight());                    //in last cycle added, it gets the list of elephants weights in cycle and adds weight of an elephant being under "x" index in firstPosition table,
                    x = compareTable[x];
                }
            }
        }
        return cycles;
    }

    private static int findIndexInFirstPosition(int ident, int[] firstPostion) {                                        //znajduje słonia po identyfikatorze (id czyli numerze słonia)
        for(int i=0;i<firstPostion.length;i++) {                                                                        //finds elephant from its id
            if(ident==firstPostion[i])                                                                                  //zwraca na ktorej pozycji w liscie firstposition jest słoń o danym identyfikatorze
                return i;                                                                                               //returns on which position in firstPostion list is an elephant having specific identifier
        }
        return -1;

    }
    private static int[] compareTables(int[] firstPosition, int[] finalPosition) {
        int []compareTable=new int[firstPosition.length];
        for(int i=0;i<firstPosition.length;i++) {
            compareTable[i]=findIndexInFirstPosition(finalPosition[i],firstPosition);
        }
        return compareTable;
    }

    private static long findLightestElephant(List<Elephant> elephants) {
        long minWeight=elephants.get(0).getWeight();
        for(Elephant e: elephants) {
            minWeight=Math.min(e.getWeight(),minWeight);
        }
        return minWeight;
    }

    private static long calculateEnergy(List<Elephant> elephants, List<Cycle> cycles) {
        long lightestGlobally=findLightestElephant(elephants);
        long energy=0;
        for(Cycle c:cycles) {
            long energyMethod1=0;
            long energyMethod2=0;
            long lightestCycleElephant=c.getWeightList().get(0);
            long weightSum=0;
            for(int j=0;j<c.getWeightList().size();j++) {
                weightSum+=c.getWeightList().get(j);
                lightestCycleElephant=Math.min(c.getWeightList().get(j),lightestCycleElephant);
            }
            energyMethod1+=weightSum+(c.getWeightList().size()-2)*lightestCycleElephant;
            energyMethod2+=weightSum+lightestCycleElephant+(c.getWeightList().size()+1)*lightestGlobally;
            energy+=Math.min(energyMethod1,energyMethod2);
        }
        return energy;
    }



    public static void main(String[] args) {
        int numberOfElephants=0;
        long []weightTab=null;
        int  []firstPosition=null;
        int  []finalPosition=null;
        List<Elephant> elephants;

        int lnno=0;
        String IoLine;

        Scanner input = new Scanner(System.in);                                                                                                    //wczytywanie danych ze standardowego wejścia
        while (input.hasNextLine()) {                                                                                                              //reading data from standard input
            IoLine = input.nextLine();
            if (IoLine.isEmpty()) break;
            if (lnno == 0) numberOfElephants=Integer.parseInt(IoLine);
            if (lnno == 1) weightTab=parseToLongTable(IoLine);
            if (lnno == 2) firstPosition=parseToIntTable(IoLine);
            if (lnno == 3) finalPosition=parseToIntTable(IoLine);
            lnno++;
            if ( lnno == 4 ) break;
        }
                                                                                                                                                    //jeżeli nie podano wszystkich danych lub dane są błędne -> exit 1
        checkData(firstPosition,finalPosition,weightTab);                                                                                           //if data is incorrect ->exit 1

        elephants = createElephants(firstPosition,weightTab);                                                                                       //tworzy listę słoni które odpowiadają kolejnością słoniom zapisanym w tablicy firstPostion
                                                                                                                                                    //creates list of elephants which respond with its order to elephants in firstPosition table

        int[] compareTable = compareTables(firstPosition,finalPosition);                                                                            //tworzy listę, która reprezentuje p[bi]=ai

        boolean[] toChange = setToChange(firstPosition,finalPosition);                                                                              //ustawiam wartości, czy dana pozycja wymaga zmiany
                                                                                                                                                    //setting values if position needs to be changed/swapped

        List<Cycle> cycles = createCycles(firstPosition,toChange,elephants,compareTable);                                                           //tworzy listę cykli, cykle poprzez wcześniejsze uporządkowanie poprzednich tablic/list zawierają tylko masy słoni
                                                                                                                                                    //creates lists of cycles, cycles have only weights of elephants since other lists/tables are being sorted in expected maneer

        System.out.println(calculateEnergy(elephants,cycles));                                                                                      //funkcja licząca wydatek energetyczny + wypisanie jej jako wynik
    }                                                                                                                                               //function calculating energy input + printing the output

}
