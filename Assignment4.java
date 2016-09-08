package finishedAssignment;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;



/*
 * 3/28/16
 * My code is based on the A* path-finding algorithm. As it traverses the imaginary tree, 
 * it places child nodes in a queue based on how close it is to the solved state. This is determined
 * from the function f(x) = h(x) + g(x), where h(x) is the similarity between the current
 * state and the solved state, and g(x) is the distance the current state is from the original state.
 * As we progress through the artificial tree, we expect to see a decrease in the values of f(x).
 * 
 * The more I weigh h(x) (the more I make h(x) more significant), the faster the algorithm is for solving
 * the problem, but the less optimal the solution.
 * 
 * 
 */
public class Assignment4{
	public static void main(String[] args){
		///*
		HiRiQ test = new HiRiQ((byte)4);
		HiRiQ solution = new HiRiQ((byte)0);
		String result = HeuristicsJava2.aStar(test,solution);
		if(test.equals(solution)){
			System.out.println("Input is already solution");
		}
		else if(!result.equals("no solution")){
			HeuristicsJava2.parseMoves(test,result);
		}
		System.out.println(result);
		//}
		//*/
		
		//HeuristicsJava2.stressTest();
	}
}

class HiRiQ{
	//int is used to reduce storage to a minimum...
	public int config;
	public byte weight;
	public int score;
	public int depth = 0;


	//initialize the configuration to one of 4 START setups n=0,1,2,3
	public HiRiQ(byte n)
	{
		if (n==0)
		{config=65536/2;weight=1;}
		else
			if (n==1)
			{config=1626;weight=6;}
			else
				if (n==2)
				{config=-1140868948; weight=10;}
				else
					if (n==3)
					{config=-411153748; weight=13;}
					else
					{config=-2147450879; weight=32;}
	}

	@Override 
	public boolean equals(Object o){
		if(o instanceof HiRiQ){
			HiRiQ other = (HiRiQ) o;
			return this.config  == other.config && this.weight == other.weight; 
		}
		return false; 
	}

	//checks to see if the config is solvable:
	public boolean parityCheck(){
		boolean[] vals = new boolean[33];
		this.load(vals);
		byte[] bCounter = new byte[3];
		byte[] yCounter = new byte[3];
		byte[] rCounter = new byte[3];
		//	  this.print();
		for(int j = 0; j < 3; j++){
			bCounter[j] = 0;
			yCounter[j] = 0;
			rCounter[j] = 0;
			for(int i = 0; i < vals.length; i++){
				if(vals[i]){
					if(i==0){
						if(j==0||j==2){bCounter[j]++;}
						else			{rCounter[j]++;}

					}
					else if(i==1){
						yCounter[j]++;
					}
					else if(i==2){
						if(j==0||j==2){rCounter[j]++;}
						else			{bCounter[j]++;}
					}  
					else if(i==3){
						if(j==0)		{yCounter[j]++;}
						else if(j==1)	{bCounter[j]++;}
						else			{rCounter[j]++;}
					}
					else if(i==4){
						if(j==0||j==1){rCounter[j]++;}
						else			{bCounter[j]++;}

					}
					else if(i==5){
						if(j==0)		{bCounter[j]++;}
						else 			{yCounter[j]++;}
					}
					else if(i>=6  && i <=26){
						int k = i%3;
						if(j==0){
							if	 (k==0) {bCounter[j]++;}
							else if(k==1)	{yCounter[j]++;}
							else if(k==2)	{rCounter[j]++;}
						}
						else if(j==1){
							if(i<=12){
								if	 (k==0) {bCounter[j]++;}
								else if(k==1)	{rCounter[j]++;}
								else if(k==2)	{yCounter[j]++;}
							}
							else if(i <=19){
								if	 (k==0) {rCounter[j]++;}
								else if(k==1)	{yCounter[j]++;}
								else if(k==2)	{bCounter[j]++;}
							}
							else if(i <=26){
								if	 (k==0) {yCounter[j]++;}
								else if(k==1)	{bCounter[j]++;}
								else if(k==2)	{rCounter[j]++;}
							}
						}
						else if(j==2){
							if	 (i <=12){
								if	 (k==0) {rCounter[j]++;}
								else if(k==1)	{bCounter[j]++;}
								else if(k==2)	{yCounter[j]++;}
							}
							else if(i <=19){
								if	 (k==0) {bCounter[j]++;}
								else if(k==1)	{yCounter[j]++;}
								else if(k==2)	{rCounter[j]++;}
							}
							else if(i <=26){
								if	 (k==0) {yCounter[j]++;}
								else if(k==1)	{rCounter[j]++;}
								else if(k==2)	{bCounter[j]++;}
							}
						}
					}
					else if(i==27){
						if(j==0)	{rCounter[j]++;}
						else		{yCounter[j]++;}
					}
					else if(i==28){
						if(j==0||j==1){bCounter[j]++;}
						else			{rCounter[j]++;}
					}
					else if(i==29){
						if(j==0)		{yCounter[j]++;}
						else if(j==1)	{rCounter[j]++;}
						else			{bCounter[j]++;}
					}
					else if(i==30){
						if(j==0||j==2){bCounter[j]++;}
						else			{rCounter[j]++;}
					}
					else if(i==31){
						yCounter[j]++;
					}
					else if(i==32){
						if(j==0||j==2){rCounter[j]++;}
						else			{bCounter[j]++;}
					}
				}
			}
		}	  
		byte[] bParity = new byte[3];
		byte[] yParity = new byte[3];
		byte[] rParity = new byte[3];

		for(int i = 0; i < 3; i++){
			bParity[i] = (byte) (bCounter[i]%2);
			yParity[i] = (byte) (yCounter[i]%2);
			rParity[i] = (byte) (rCounter[i]%2);
			//	  System.out.println(bParity[i] + " " + yParity[i] + " " + rParity[i]);
		}
		if((bParity[0] == rParity[0] && !(rParity[0] ==yParity[0]))
				&&(bParity[1] == rParity[1] && !(rParity[1] ==yParity[1]))
				&&(bParity[2] == rParity[2] && !(rParity[2] ==yParity[2]))){
			return true;
		}
		else{
			return false;
		}
	}

	@Override 
	public int hashCode(){
		int hash;
		hash = config;

		return hash;
	}
	
	public Long getHashCode(){
		Long hash = (long) 1;
		hash = hash * 31  + config;
		hash = hash * 17 + weight;
		return hash;
	}

	//initialize the configuration to one of 4 START setups n=0,10,20,30
	public HiRiQ(int config, byte weight){
		this.config = config;
		this.weight = weight;
	}

	public HiRiQ(int config, byte weight,HiRiQ parent){
		this.config = config;
		this.weight = weight;
		this.depth = parent.depth+1;
		setScore(parent); 
	}
	
	public HiRiQ(boolean[] a, HiRiQ parent, HiRiQ solution){
		this.store(a);
		this.depth = parent.depth+1;
		setScore(solution); 
	}

	public boolean IsSolved()
	{
		return( (config==65536/2) && (weight==1) );
	}

	//pretty much the core of the algorithm. Determines the value of the HiRiQ configuration.
	//a lower score = a better configuration.
	//Tests for how well the current configuration matches the target solution.
	public void setScore(HiRiQ solution){
		int n = 10;
		int m = 1;
		boolean[] boolSol = new boolean[33];
		boolean[] boolTest = new boolean[33];
		solution.load(boolSol);
		this.load(boolTest);
		int cost = 0;
		for(int i = 0; i < 33; i++){
			if(boolSol[i]!=boolTest[i]){
				cost++;
			}
		}
		this.score = n*cost + m*depth;
	}

	public int getScore(){
		return this.score;
	}

	public void setDepth(int depth){
		this.depth = depth;
	}

	//transforms the array of 33 booleans to an (int) cinfig and a (byte) weight.
	public void store(boolean[] B)
	{
		int a=1;
		config=0;
		weight=(byte) 0;
		if (B[0]) {weight++;}
		for (int i=1; i<32; i++)
		{
			if (B[i]) {config=config+a;weight++;}
			a=2*a;
		}
		if (B[32]) {config=-config;weight++;}
	}

	//transform the int representation to an array of booleans.
	//the weight (byte) is necessary because only 32 bits are memorized
	//and so the 33rd is decided based on the fact that the config has the
	//correct weight or not.
	public boolean[] load(boolean[] B)
	{
		byte count=0;
		int fig=config;
		B[32]=fig<0;
		if (B[32]) {fig=-fig;count++;}
		int a=2;
		for (int i=1; i<32; i++)
		{
			B[i]= fig%a>0;
			if (B[i]) {fig=fig-a/2;count++;}
			a=2*a;
		}
		B[0]= count<weight;
		return(B);
	}

	//prints the int representation to an array of booleans.
	//the weight (byte) is necessary because only 32 bits are memorized
	//and so the 33rd is decided based on the fact that the config has the
	//correct weight or not.
	public void printB(boolean Z)
	{if (Z) {System.out.print("[ ]");} else {System.out.print("[@]");}}

	public void print()
	{
		byte count=0;
		int fig=config;
		boolean next,last;
		last=fig<0;
		if (last) {fig=-fig;count++;}
		int a=2;
		for (int i=1; i<32; i++)
		{
			next= fig%a>0;
			if (next) {fig=fig-a/2;count++;}
			a=2*a;
		}
		next= count<weight;

		count=0;
		fig=config;
		if (last) {fig=-fig;count++;}
		a=2;

		System.out.print("      ") ; printB(next);
		for (int i=1; i<32; i++)
		{
			next= fig%a>0;
			if (next) {fig=fig-a/2;count++;}
			a=2*a;
			printB(next);
			if (i==2 || i==5 || i==12 || i==19 || i==26 || i==29) {System.out.println() ;}
			if (i==2 || i==26 || i==29) {System.out.print("      ") ;};
		}
		printB(last); System.out.println() ;

	}
}

//THIS CLASS IS, IN SOME WAYS, AN EXTENSION OF THE HIRIQ CLASS; IT JUST STORES THE STRING OF COMBINATIONS
//FROM THE START CONFIG TO THE CURRENT CONFIG AS WELL.
class StateToSolved2 {
	private HiRiQ state;
	private String combos;

	public StateToSolved2(HiRiQ state, String combos){
		this.state= state;
		this.combos = combos;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof StateToSolved2){
			StateToSolved2 other = (StateToSolved2) o;
			return this.state.config == other.state.config && this.state.config == other.state.config;
		}
		return false; 
	}

	@Override 
	public int hashCode(){
		return state.hashCode();
	}

	public int compare(StateToSolved2 o){
		return this.state.score-o.state.score;
	}

	public int getScore(){
		return this.getState().getScore();
	}

	public void setCombos(String combos){
		this.combos = combos;
	}

	public String getCombos(){
		return this.combos;
	}

	public HiRiQ getState(){
		return this.state;
	}

	public void print(){
		state.print();
		System.out.println();
		System.out.println(combos);
	}
}

class HeuristicsJava2 {
		
	
	//this method was made just to see how efficient my algorithm is:
	public static void stressTest(){
		boolean[][] values = new boolean[10000][33];
		Random random = new Random();
		for(int j = 0; j < values.length; j++){
			for(int i = 0; i < 33; i++){
				values[j][i] = random.nextBoolean();
			}
			HiRiQ a = new HiRiQ((byte)0);
			a.store(values[j]);
			if(!a.parityCheck()){
				j--;
			}
		}
		long t = System.currentTimeMillis();
		long end = t+10000;
		int counter = 0;
		for(int j = 0; j < values.length; j++){
			if(System.currentTimeMillis()<end){
				System.out.println(wrapper(values[j]));
				counter++;
			}
			else{
				break;
			}
		}
		System.out.println(counter + " configurations solved in 10 seconds");
	}
	
	//wrapper method to accept the right input. CALL THIS METHOD!!!!!
	public static String wrapper(boolean[] a){
		HiRiQ start = new HiRiQ((byte)0);
		start.store(a);
		HiRiQ solution = new HiRiQ((byte)0);
		String result = aStar(start,solution);
		if(start.equals(solution)){
			System.out.println("Input is already solution");
		}
		else if(!result.equals("no solution")){
			//parseMoves(start,result);
		}
		return result;
	}
	
	//checks which triplets have a potential substitution:
	public static byte[][] possibilities(boolean[] values, byte[] horiSubs, byte[] vertiSubs){
		int count = 0;
		//0 = nothing, 1 = white, 2 = black, 
		for(int i = 0; i < 33; i++){
			if((i < 6 && i % 3 == 0)||(i>=6 && i < 11)||(i>=13 && i < 18)||(i>=20 && i < 25)||(i > 26 && i % 3 == 0)){
				byte subType = 0;
				int t1 = values[i] ? 1:0;
				int t2 = values[i+1] ? 1:0;
				int t3 = values[i+2] ? 1:0;
				if(t1 != t3){
					if(t2 == 1){
						subType = 1;
					}
					else if(t2 == 0){
						subType = 2;
					}
				}
				horiSubs[count] = subType;
				count++;
			}
		}
		count = 0;
		for(int i = 0; i < 33; i++){
			byte subType = 0;
			int t1 = values[i] ? 1:0;
			int t2 = 0;
			int t3 = 0;
			if(i < 3){
				t2 = values[i+3] ? 1:0; t3 = values[i+8] ? 1:0;

			}
			else if(i < 6){
				t2 = values[i+5] ? 1:0; t3 = values[i+12] ? 1:0;
			}
			else if(i < 13){
				t2 = values[i+7] ? 1:0; t3 = values[i+14] ? 1:0;
			}
			else if(i > 14 && i < 18){
				t2 = values[i+7] ? 1:0; t3 = values[i+12] ? 1:0;
			}
			else if(i >21 && i < 25){
				t2 = values[i+7] ? 1:0; t3 = values[i+8] ? 1:0;
			}
			if((i<13)||(i>14 && i < 18)||(i>21 && i < 25)){
				if(t1 != t3){
					if(t2 == 1){
						subType = 1;
					}
					else if(t2 == 0){
						subType = 2;
					}
				}
				vertiSubs[count] = subType;
				count++;

			}
			


		}
		byte[][] subs = {horiSubs, vertiSubs};
		return subs;
	}
	
	//the core of the program: it substitutes based on which triplet it is:
	public static HiRiQ substitute(HiRiQ p,HiRiQ solution, byte[] triplet){
  		boolean[] b = new boolean[33];
		b = p.load(b);
		b[triplet[0]] = ! b[triplet[0]];
		b[triplet[1]] = ! b[triplet[1]];
		b[triplet[2]] = ! b[triplet[2]];
		HiRiQ neighbor = new HiRiQ(b,p,solution);
		return neighbor;
	}

	//these two methods return the value of the triplet based on the index (i):
	public static byte[] horizontalTriplet(byte[] subs, int i){
		byte triplet[] = new byte[3];
		if(subs[i] == -1){
			return null;
		}
		
		if(i == 0){
			triplet[0] = 0; triplet[1] = 1; triplet[2] = 2;
		}
		else if(i == 1){
			triplet[0] = 3; triplet[1] = 4; triplet[2] = 5;
		}
		else if(i >= 2 && i < 7){
			triplet[0] = (byte)((2*3)+(i-2)); triplet[1] = (byte)((2*3)+(i-1)); triplet[2] = (byte)((2*3)+(i));
		}
		else if(i>=7 && i < 12){
			triplet[0] = (byte)((2*3)+(i)); triplet[1] = (byte)(((2*3)+(i+1))); triplet[2] = (byte)((2*3)+(i+2));
		}
		else if(i>=12 && i < 17){
			triplet[0] = (byte)((2*3)+(i+2)); triplet[1] = (byte)(((2*3)+(i+3))); triplet[2] = (byte)((2*3)+(i+4));

		}
		else if(i == 18){
			triplet[0] = 27; triplet[1] = 28; triplet[2] = 29;
		}
		else{
			triplet[0] = 30; triplet[1] = 31; triplet[2] = 32;

		}
		return triplet;			

	}
	
	public static byte[] verticalTriplet(byte[] subs, int i){
		byte[] triplet = new byte[3];
		if(subs[i] == -1){
			return null;
		}
		if(i >=0 && i < 3){
			triplet[0] = (byte) i; triplet[1] = (byte) (i+3); triplet[2] = (byte) (i+8);
		}
		else if(i >=3 && i < 6){
			triplet[0] = (byte)i; triplet[1] = (byte) (i+5); triplet[2] = (byte) (i+12);
		}
		else if(i >=6 && i < 13){
			triplet[0] = (byte)i; triplet[1] = (byte) (i+7); triplet[2] = (byte) (i+14);
		}
		else if(i>=13 && i < 16){
			triplet[0] = (byte)(i+2); triplet[1] = (byte) (i+9); triplet[2] = (byte) (i+14);
		}
		else if(i >=16 && i < 19){
			triplet[0] = (byte)(i+6); triplet[1] = (byte) (i+11); triplet[2] = (byte) (i+14);
		}
		return triplet;
	}

	
	//this is the beginning of the important code, takes as input the start config and the goal:
	public static String aStar(HiRiQ start, HiRiQ end){
		if(!start.parityCheck()){
			return "no solution";
		}
		//this is the set of HiRiQ objects that I have already evaluated, stored so that I don't loop back:
		HashSet<HiRiQ> closedList = new HashSet<HiRiQ>();
		
		//this is what I use for my Priority Queue; I order the queue such that the lowest score (closest to the 
		//end configuration & least deep) are placed first.
		Comparator<StateToSolved2> comparator = new Comparator<StateToSolved2>(){
			@Override
			public int compare(StateToSolved2 s1, StateToSolved2 s2){
				int s1score = s1.getState().getScore();
				int s2score = s2.getState().getScore();
				if(s1score > s2score){
					return 1;
				}
				if(s1score < s2score){
					return -1;
				}
				return 0;
			}
		};	
		
		//the queue that stores nodes that have been found, but not yet evaluated:
		PriorityQueue<StateToSolved2> openList = new PriorityQueue<StateToSolved2>(comparator);		
		
		//The StateToSolved2 object stores the HiRiQ object and the string of moves it took to get to it:
		StateToSolved2 startState = new StateToSolved2(start, "");
		startState.getState().setScore(end);
		openList.add(startState);
		while(!openList.isEmpty()){
			StateToSolved2 p = openList.poll();
			closedList.add(p.getState());
			if(p.getState().IsSolved()){
				System.out.println(closedList.size() +" nodes checked.");
				return p.getCombos();
			}
			else{				
				createNeighbors(openList,closedList,p,end);
			}
		}
		return "no solution";
	}	
	
	//generate the neighbors of the current node and store them in the openList:
	public static void createNeighbors(PriorityQueue<StateToSolved2> openList,HashSet<HiRiQ> closedList, StateToSolved2 p, HiRiQ solution){
		//store the values of the current State:
		boolean[] values = new boolean[33];
		HiRiQ table = p.getState();
		values = table.load(values);
		
		//the arrays I use to store the possible substitutions:
		byte[] horizontal = new byte[19];
		byte[] vertical = new byte[19];
		byte[][] possibilities = possibilities(values, horizontal, vertical);
		
		
		//cycling through the possible moves and checking if I've seen them before:
		for(int i = 0; i < possibilities.length; i++){
			int j = 0;
			while(j < possibilities[i].length){
				if(possibilities[i][j] == 0){
					j++;
				}
				else{
					byte[] triplet = new byte[3];
					String newMove = "";
					
					//how I determine which triplet the substitution is:
					if(i == 0){
						triplet = horizontalTriplet(possibilities[i], j);
					}
					else{
						triplet = verticalTriplet(possibilities[i], j);
					}
					
					//I make the relevant HiRiQ and StateToSolved2 classes for the neighbor here:
					HiRiQ n = substitute(table, solution, triplet);
					StateToSolved2 neighbor = new StateToSolved2(n,newMove);
					if(!openList.contains(neighbor) && !closedList.contains(n)){
						if(possibilities[i][j] == 1){
							newMove =  p.getCombos() + (triplet[0] + "W" + triplet[2]) + ",";
						}
						else if(possibilities[i][j] == 2){
							newMove = p.getCombos() + (triplet[0] + "B" + triplet[2]) + ",";
						}
						
						//add them to their respective lists:
						neighbor.setCombos(newMove);
						openList.add(neighbor);
					}
					j++;
				}
			}
		}
	}

	//THESE LAST THREE METHODS PARSE THE STRING AND APPLY THE SUBSTITUTIONS THAT MY ALGORITHM 
	//OUTPUTS; THIS BASICALLY FACT CHECKS!
	public static void parseMoves(HiRiQ initTable, String moveSet){
		String[] indivMoves = moveSet.split(",");
		HiRiQ sub = initTable;

		for(int i = 0; i < indivMoves.length; i++){
			sub.print();
			System.out.println();
			sub = makeMove(sub,indivMoves[i]);
			System.out.println(indivMoves[i]);
		}
		sub.print();
		
	}
	
	public static HiRiQ makeMove(HiRiQ table, String move){
		HiRiQ solution = new HiRiQ((byte)0);
		String[] parse2;
		byte[] triplet = new byte[3];
		
		//[@] = 0, [ ] = 1
		if(move.contains("W")){
			parse2 = move.split("W");
			if(Integer.parseInt(parse2[0]) > Integer.parseInt(parse2[1])){
				triplet[0] = (byte) Integer.parseInt(parse2[1]);
				triplet[2] = (byte) Integer.parseInt(parse2[0]);
			}
			else{
				triplet[0] = (byte) Integer.parseInt(parse2[0]);
				triplet[2] = (byte) Integer.parseInt(parse2[1]);
			}
		}
		else{
			parse2 = move.split("B");
			if(Integer.parseInt(parse2[0]) > Integer.parseInt(parse2[1])){
				triplet[0] = (byte) Integer.parseInt(parse2[1]);
				triplet[2] = (byte) Integer.parseInt(parse2[0]);
			}
			else{
				triplet[0] = (byte) Integer.parseInt(parse2[0]);
				triplet[2] = (byte) Integer.parseInt(parse2[1]);
			}
		}
		fSVInTriplet(triplet);
		HiRiQ sub = substitute(table,solution,triplet);
		return sub;
		
	}

	public static void fSVInTriplet(byte[]triplet){
		int diff = Math.abs(triplet[0]-triplet[2]);
		int max = (triplet[0] > triplet[2]) ? triplet[0] : triplet[2];

		if(diff == 2){
			triplet[1] = (byte) (max - 1);
		}
		else if(diff == 8){
			if(max < 11){
				triplet[1] = (byte) (max - 5);
			}
			else{
				triplet[1] = (byte) (max - 3);
			}
		}
		else if(diff == 12){
			if(max < 18){
				triplet[1] = (byte) (max - 7);
			}
			else{
				triplet[1] = (byte) (max - 5);
			}
		}
		else if(diff == 14){
			triplet[1] = (byte) (max - 7);
		}		
	}
	
	
}
