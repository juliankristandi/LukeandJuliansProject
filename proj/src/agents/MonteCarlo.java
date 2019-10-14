package agents;
import loveletter.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
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
   * Reports the agents name
   * */
  public String toString(){return "MonteCarlo but not really";}

  class Node{
    int id;
    Action action;
    int[] ratio; // [win, played, available] 
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

  public Node newNode(int id, Action action, int[] ratio, ArrayList<Node> children, Node parent){
    Node x = new Node(nodeCounter, action, ratio, children, parent);
    nodeCounter++;
    if (x.parent != null){
      x.parent.children.add(x);
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
    root = new Node(nodeCounter, null, new int[]{-1, 1, 1}, rootList, null);
  }

  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
  }

  public ArrayList<Action> possibleActionsList (Card card, State state){
    ArrayList<Action> possibleActions = new ArrayList<Action>();
    Action act = null;
    try{
      switch(card.value()){

      	//ADD CASES
      	case 1:
      	  for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRIEST);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.BARON);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.HANDMAID);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCE);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.KING);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.COUNTESS);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCESS);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
          }
        case 2:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playPriest(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
          }  
        case 3:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playBaron(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            } 
          }
        case 4: 
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playHandmaid(myIndex);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            } 
          }
        case 5:
          for(int i = 0; i < 4; i++){
            act = Action.playPrince(myIndex, i);  
            if (current.legalAction(act, card) && state.getCard(myIndex).value() != 7){
              possibleActions.add(act);
            }
          }
        case 6:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playKing(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card) && state.getCard(myIndex).value() != 7){
              possibleActions.add(act);
            }
          }
        case 7:
          act = Action.playCountess(myIndex);  
          if (current.legalAction(act, card)){
            possibleActions.add(act);
          }
        default:
          act = null;
      }
    }
    catch(IllegalActionException e){}
    try{
      switch(state.getCard(myIndex).value()){
      	case 1:
      	  for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRIEST);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.BARON);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.HANDMAID);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCE);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.KING);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.COUNTESS);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
            act = Action.playGuard(myIndex, opponentsIndex[i], Card.PRINCESS);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
          }
        case 2:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playPriest(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            }
          }  
        case 3:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playBaron(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            } 
          }
        case 4: 
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playHandmaid(myIndex);  
            if (current.legalAction(act, state.getCard(myIndex))){
              possibleActions.add(act);
            } 
          }  
        case 5:
          for(int i = 0; i < 4; i++){
            act = Action.playPrince(myIndex, i);
            if (current.legalAction(act, state.getCard(myIndex)) && card.value() != 7){
              possibleActions.add(act);
            }
          }
        case 6:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playKing(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, state.getCard(myIndex)) && card.value() != 7){
              possibleActions.add(act);
            }
          }
        case 7:
          act = Action.playCountess(myIndex);  
          if (current.legalAction(act, state.getCard(myIndex))){
            possibleActions.add(act);
          }
        default:
          act = null;
      }
    }
    catch(IllegalActionException e){}  
    return possibleActions;
  }  

  public int[] simulateGameplay(int cardDrawn, int cardSelected, int[] ratio){
    int playerKilled = -1;
    ratio[0] = playerKilled;
    Random r = new Random();
    float chance = r.nextFloat();

    if(cardDrawn == 0){
      ratio[1]++;
      ratio[2]++;
      if(chance <= 0.15f){ // 15% success rate 
        playerKilled = r.nextInt(3); // random player
      }
    }
    else if(cardDrawn == 1){
      ratio[1]++;
      ratio[2]++;
    }
    else if(cardDrawn == 2){
      ratio[1]++;
      ratio[2]++;
      if(chance <= 0.30f){ // 30% success rate 
        playerKilled = r.nextInt(3); // random player
      }  
    }
    else if(cardDrawn == 3){
      ratio[1]++;
      ratio[2]++;
      //nothing
    }
    else if(cardDrawn == 4){
      ratio[1]++;
      ratio[2]++;
      //nothing
    }
    else if(cardDrawn == 5){
      ratio[1]++;
      ratio[2]++;
      //nothing
    }
    else if(cardDrawn == 6){
      ratio[1]++;
      ratio[2]++;
      //nothing
    }
    else if(cardDrawn == 7){
      ratio[1]++;
      ratio[2]++;
      //nothing
    }
    ratio[0] = playerKilled;
    return ratio;
  }

  public Card cardIndex(int index){
    if(index == 0){
      return Card.GUARD;
    }
    else if(index == 1){
      return Card.PRIEST;
    }
    else if(index == 2){
      return Card.BARON;
    }    
    else if(index == 3){
      return Card.HANDMAID;
    }    
    else if(index == 4){
      return Card.PRINCE;
    }    
    else if(index == 5){
      return Card.KING;
    }
    else if(index == 6){
      return Card.COUNTESS;
    } 
    else if(index == 7){
      return Card.PRINCESS;
    }
    return null;
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
    Node parent = root;
    ArrayList<Action> possibleActions = new ArrayList<Action>();
    possibleActions = possibleActionsList(c, current);
      
    int[] cardStorage = new int[] {5,2,2,2,2,1,1,1};

    int cardRemaining = 0;
    int playerRemaining = current.numPlayers();

    for(int i = 0; i < current.numPlayers();i++){

    	java.util.Iterator<Card> discardPile = current.getDiscards(i);
        
        while(discardPile.hasNext()){
          cardRemaining--;
          int val = discardPile.next().value();
          switch(val){
            case 1:
              cardStorage[0]--;
              break;
            case 2: // priest
              cardStorage[1]--;
              break;
              case 3: // baron
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

    for(int i = 0; i < possibleActions.size(); i++){
        int[] cardClone = cardStorage;
        int currentCard = c.value();
        int turn = current.nextPlayer();
        int win = 0;
        int played = 0;
        int available = 0;
        ArrayList<Node> arrayList = new ArrayList<Node>();
        int[] simulation = new int[]{-1, 1, 1};
        while(true){
        	turn = turn % 4;

          	ArrayList<Integer> remaining = new ArrayList<Integer>();
          	int exist = 0;
          	for (int x = 0; x < cardClone.length; x++){
            	if(cardClone[x] != 0){
            		remaining.add(x);
              		exist++;
              		cardRemaining = cardRemaining + cardClone[x];
            	}
          	}

          	if(exist == 0 || playerRemaining == 1) {
            	win = 1;
            	simulation[0] = win;
            	break;
          	}

          	int draw = Math.abs(rand.nextInt() % exist);
          	int result = remaining.get(draw);
          	cardClone[result]--;
          	cardRemaining--;

          	//SIMULATION FUNCTION
          	simulation = simulateGameplay(result, currentCard, simulation);
          		if(simulation[0] != -1){
            		playerRemaining--;
          		}
          		if (simulation[0] == myIndex){
            		win = 0;
            		simulation[0] = win;
            		break;
          		}
          	turn++;
        }

        Node child = new Node(nodeCounter, possibleActions.get(i), simulation, arrayList, parent);
        nodeList[i] = child;
    }

    Action best = null;
    double compare = 0.0;
    //for loop sort array
    for(int x = 0; x < possibleActions.size(); x++){
    	//NODE LIST
    	if((nodeList[x].ratio[0] / nodeList[x].ratio[2]) > compare){
    		compare = nodeList[x].ratio[0] / nodeList[x].ratio[2];
    		best = nodeList[x].action;
      	}  
    }
    if (best == null){
    	return possibleActions.get(0);
    }
    return best;

  }
    

}
