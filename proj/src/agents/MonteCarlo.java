package agents;
import loveletter.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * Ignatius Julian Kristandi 22167432
 * */
public class MonteCarlo implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  private Node root;
  private int nodeCounter;
  private int[] opponentsIndex;

  //0 place default constructor
  public MonteCarlo(){
    rand  = new Random();
  }

  /**
   * Reports the agents name.
   **/
  public String toString(){return "Imperfect MonteCarlo";}

  /**
   * Custom Node class for Monte Carlo Tree Search.
   **/
  class Node{
    int id;
    Action action;
    int[] ratio; // 4 elements which contains = [win, played, available, playerKilled] 
    ArrayList<Node> children;
    Node parent;

    Node(int id, Action action, int[] ratio, ArrayList<Node> children, Node parent){
      this.id = id;
      this.action = action;
      this.ratio = ratio;
      this.children = children;
      this.parent = parent;
    }

  }
  /**
   * Method called to create new nodes, ensures parents are linked with children, and provides a unique counter for the id.
   * @param id the node count
   * @param action action linked to the node 
   * @param ratio ratio array that consists of win, played, available; used to calculate the best option
   * @param children an arraylist that contains the children of this node
   * @param parent the parent node for this specific node
   * @return the new node created
   **/  
  public Node newNode(int id, Action action, int[] ratio, ArrayList<Node> children, Node parent){
    Node x = new Node(nodeCounter, action, ratio, children, parent);
    nodeCounter++;
    if (x.parent != null){
      	if(!x.parent.children.contains(x)){ // ensures no duplicates
      		x.parent.children.add(x);
      	}
    }
    return x;
  }

  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();

    // if functions to have an array of index for our opponents
    if(myIndex == 0){
      opponentsIndex = new int[]{1, 2, 3};  
    }
    else if(myIndex == 1){
      opponentsIndex = new int[]{0, 2, 3};  
    }
    else if(myIndex == 2){
      opponentsIndex = new int[]{0, 1, 3};  
    }
    else{
      opponentsIndex = new int[]{0, 1, 2};  
    }
    nodeCounter = 0;
    ArrayList<Node> rootList = new ArrayList<Node>();
    root = newNode(nodeCounter, null, new int[]{0, 0, 0, -1}, rootList, null);
  }

  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
  }

  /**
   * Method to build an arraylist of all the possible actions based on cards in hand
   * @param card the card drawn from the deck
   * @param state the current state to obtain the card already in hand
   * @return an array list of all the possible actions
   * @throws IllegalActionException when action is illegal
   * **/
  public ArrayList<Action> possibleActionsList (Card card, State state){
    ArrayList<Action> possibleActions = new ArrayList<Action>();
    Action act = null;
    try{
      switch(card.value()){ // card drawn from deck
      	case 1:
      	  for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRIEST);  
            if(current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.BARON);  
            if (current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	} 	 
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.HANDMAID);  
            if (current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCE);  
            if (current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.KING);  
            if (current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.COUNTESS);  
            if (current.legalAction(act, card)){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCESS);  
            if (current.legalAction(act, card)){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 2:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playPriest(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }  
        case 3:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playBaron(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            } 
          }
        case 4: 
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playHandmaid(myIndex);  
            if (current.legalAction(act, card)){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            } 
          }
        case 5:
          for(int i = 0; i < 4; i++){
            act = Action.playPrince(myIndex, i);  
            if (current.legalAction(act, card) && state.getCard(myIndex).value() != 7){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 6:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playKing(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card) && state.getCard(myIndex).value() != 7){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 7:
          act = Action.playCountess(myIndex);  
          if (current.legalAction(act, card)){
              if(!possibleActions.contains(act)){
            		possibleActions.add(act);
              }
          }
        default:
          act = null;
      }
    }
    catch(IllegalActionException e){}
    try{
      switch(state.getCard(myIndex).value()){ // card already in hand
      	case 1:
      	  for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRIEST);  
            if (current.legalAction(act, state.getCard(myIndex))){
            	if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.BARON);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.HANDMAID);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCE);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.KING);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.COUNTESS);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCESS);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 2:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playPriest(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }  
        case 3:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playBaron(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            } 
          }
        case 4: 
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playHandmaid(myIndex);  
            if (current.legalAction(act, state.getCard(myIndex))){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            } 
          }  
        case 5:
          for(int i = 0; i < 4; i++){
            act = Action.playPrince(myIndex, i);
            if (current.legalAction(act, state.getCard(myIndex)) && card.value() != 7){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 6:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playKing(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex)) && card.value() != 7){
                if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
            }
          }
        case 7:
          act = Action.playCountess(myIndex);  
          if (current.legalAction(act, state.getCard(myIndex))){
              if(!possibleActions.contains(act)){
            		possibleActions.add(act);
            	}
          }
        default:
          act = null;
      }
    }
    catch(IllegalActionException e){}  
    return possibleActions;
  }  

  /**
   * Method to simulate gameplay (Simulation aspect of Monte Carlo)
   * Currently the simulation method is very basic, where we calculate a random chance
   * for a player to get eliminated.
   * @param cardDrawn the card drawn
   * @param cardSelected the card of the node being simulated
   * @param ratio main monte carlo ratio
   * @param turn index of player of the current turn
   * @return the monte carlo ratio
   * **/
  public int[] simulateGameplay(int cardDrawn, int cardSelected, int[] ratio, int turn){
    int playerKilled = -1;
    ratio[3] = playerKilled;
    Random r = new Random();
    float chance = r.nextFloat();
    if(cardDrawn == cardSelected) {
      ratio[2]++;
    }
    if(cardDrawn == 0){
      ratio[1]++;
      ratio[2]++;
      if(chance <= 0.15f){ // 15% success rate
        playerKilled = r.nextInt(4); // random player
        while (playerKilled == turn){
        	playerKilled = r.nextInt(4);
        }
      }
    }
    else if(cardDrawn == 1){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 2){
      ratio[1]++;
      ratio[2]++;
      if(chance <= 0.25f){ // 25% success rate 
        playerKilled = r.nextInt(4); // random player
        while (playerKilled == turn){
        	playerKilled = r.nextInt(4);
        }
      }  
    }
    else if(cardDrawn == 3){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 4){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 5){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 6){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 7){
      ratio[1]++;
      ratio[2]++;
    }
    ratio[3] = playerKilled;
    return ratio;
  }

  /**
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * @throws IllegalActionException when the Action produced is not legal.
   * */
  public Action playCard(Card c){
  	Action act = null;
    Card play;
    ArrayList<Action> possibleActions = new ArrayList<Action>();
    possibleActions = possibleActionsList(c, current); // all the possible moves.
    int[] cardStorage = new int[] {5,2,2,2,2,1,1,1}; //no. of guards, priests, etc.
    int playerRemaining = current.numPlayers();
    for(int i = 0; i < current.numPlayers();i++){ // for loop to update cardStorage to have an idea of what cards are left.
    	java.util.Iterator<Card> discardPile = current.getDiscards(i);
        while(discardPile.hasNext()){
          int val = discardPile.next().value();
          switch(val){
            case 1:
              cardStorage[0]--;
              break;
            case 2: 
              cardStorage[1]--;
              break;
              case 3: 
              cardStorage[2]--;
              break;
              case 4:
              cardStorage[3]--;
              break;
              case 5:
              cardStorage[4]--;
              break;
              case 6:
              cardStorage[5]--;
              break;
              case 7:
              cardStorage[6]--;
              break;
              case 8:
              cardStorage[7]--;
              break;
          }
        }
    }
    Node[] nodeList = new Node[possibleActions.size()];
    for(int a = 0; a < nodeList.length; a++){ // populates nodelist with all the possible moves
    	nodeList[a] = newNode(nodeCounter, possibleActions.get(a), new int[]{0, 0, 0, -1}, new ArrayList<Node>(), root);
    }
	nodeList = monteLoop(nodeList, cardStorage, c.value(), 0, 0); 
    Action best = null;
    double compare = 0.0;
    for(int x = 0; x < nodeList.length; x++){
    	if(nodeList[x].parent.ratio[1] == 0){ //increases winrate & can't divide by zero
    		nodeList[x].parent.ratio[1] = 1;
    	}
    	if(nodeList[x].parent.ratio[2] == 0){ //increases winrate & can't divide by zero
    		nodeList[x].parent.ratio[2] = 1;
    	}
    	if((nodeList[x].parent.ratio[0] / nodeList[x].parent.ratio[1] / nodeList[x].parent.ratio[2]) > compare){ // main ratio
    		compare = (nodeList[x].parent.ratio[0] / nodeList[x].parent.ratio[1] / nodeList[x].parent.ratio[2]);	
    		best = nodeList[x].parent.action;
      	}      	
    }    
    if (best == null){
    	return possibleActions.get(possibleActions.size() - 1); //if can't determine best action, use the last possible action
    }
    return best;
  }
  
  /**
   * Monte Carlo function, where the selection, expansion and back propagation phase is done.
   * The simulation phase is done in the simulateGameplay() function.
   * @param nodeList the node list to work with
   * @param cardStorage the deck of cards that is left in play
   * @param currentCard the current card being drawn from deck
   * @param depth to specify depth for the search
   * @param max the highest number of win
   * @return an array of nodes with ratios already calculated
   * */  
  public Node[] monteLoop(Node[] nodeList, int[] cardStorage, int currentCard, int depth, int max){ 
  	int maxwin = 1;
  	 	for(int i = 0; i < nodeList.length; i++){ //inspects each node in the list
  	  		int playerRemaining = current.numPlayers();
  	  		int cardRemaining = 0;
  	  		int[] simulation;
  	  		if (depth == 0){
  	  			simulation = new int[]{0, 0, 0, -1};
  	  		}
  	  		else{
  	  			simulation = nodeList[i].ratio;
  	  		}
  	  		int turn = current.nextPlayer();
  	  		int[] cardClone = cardStorage;
  	  		ArrayList<Node> arrayList = new ArrayList<Node>();  	  		
  	  		ArrayList<Integer> remaining = new ArrayList<Integer>();
  	  		int exist = 0;
  	  		for (int x = 0; x < cardClone.length; x++){
  	  			if(cardClone[x] != 0){
  		  	  		remaining.add(x);
  	  	    		exist++;
  		    		cardRemaining = cardRemaining + cardClone[x];
  	  		  	}
  	  		}
  	  		while(true){
  	  			turn = turn % 4; 	
  	  			if(playerRemaining == 1){ // win situation
  	  				simulation[0]++;
  	  				if(simulation[0] > maxwin){
  	  					maxwin = simulation[0];
  	  				}
  	  				break;
  	  			}
  	  			if(cardRemaining == 0){ // no cards left, 25% win chance
  	  				Random rng = new Random();
  	  				float success = rng.nextFloat();
  	  				if(success <= 0.25f){  	  					
  	  					simulation[0]++;
  	  					if(simulation[0] > maxwin){
  	  						maxwin = simulation[0];
  	  					}
  	  				}
  	  				else{
  	  					break;
  	  				}
  	  				break;
  	  			}
  	  			int draw = Math.abs(rand.nextInt() % exist); //draws a random existing card
  	  			int result = remaining.get(draw);
  	  			cardClone[result]--;
  	  			cardRemaining--;
  	  			simulation = simulateGameplay(result, currentCard, simulation, turn); // simulation phase
  	  			if(simulation[3] != -1){
  	  		  		playerRemaining--;
  	  			}
  	  			if (simulation[3] == myIndex){
   		  			break;
  	  			}
  	  			turn++;
  	  		}
  	  		nodeList[i].ratio = simulation;
  	  		Node temp = nodeList[i];
  	  		while(temp.parent != null){ // back propagation phase
  	  			temp.parent.ratio[0] += simulation[0];
  	  			temp.parent.ratio[1] += simulation[1];
  	  			temp.parent.ratio[2] += simulation[2];
  	  			temp = temp.parent;
  	  		}
  	  	}
  	  	ArrayList<Node> solulist = new ArrayList<Node>();
  	  	for(int x = 0; x < nodeList.length; x++){
  	  		if (nodeList[x].ratio[0] == maxwin){ // selection phase
  	  			solulist.add(newNode(nodeCounter, nodeList[x].action, new int[]{0, 0, 0, -1}, new ArrayList<Node>(), nodeList[x]));
  	  		}
  	  	}
  	  	if(solulist.size() == 0){ // nothing to be expanded
  	  		return nodeList; 
  	  	}
  	  	Node[] solList = solulist.toArray(new Node[solulist.size()]);
  	  	if(solulist.size() == 1){ // only one node selected
  	  		return solList; 
  	  	}  	  	
  	  	solList = monteLoop(solList, cardStorage, currentCard, depth++, maxwin); // expansion phase
  		return solList;
  }

}
