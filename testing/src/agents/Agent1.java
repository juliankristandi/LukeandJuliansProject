package agents;
import loveletter.*;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class Agent1 implements Agent{

  private Random rand;
  private State current;
  private int myIndex;
  private HashMap<State, Node> stateNode;
  private Node root;
  private int[] opponentsIndex;

  //0 place default constructor
  public Agent1(){
    rand  = new Random();
    stateNode = new HashMap<State, Node>();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "The First Agent";}

  class Node{
    State state;
    int win; // 0 for lose, 1 for win, -1 for unchecked
    List<Node> children;
    Node parent;

    Node(State state, int win, List<Node> children, Node parent){
      this.state = state;
      this.win = win;
      this.children = children;
      this.parent = parent;
    }
  }

  public void addTree(State state, Node node){
    stateNode.put(state, node);
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
    stateNode.clear();
    root = new Node(start, -1, Collections.<Node>emptyList(), null);
    addTree(root.state, root);
  }

  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
  }  

  public ArrayList<Action> possibleActionsList (Card card){
    ArrayList<Action> possibleActions = new ArrayList<Action>();
    Action act = null;
    try{
      switch(card.value()){
        case 2:
          for(int i = 0; i < opponentsIndex.length; i++){
            act = Action.playPriest(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
          }  
        case 3:
          for(int i = 0; i < opponentsIndex.length; i ++){
            act = Action.playBaron(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
          }
        case 5:
          for(int i = 0; i < opponentsIndex.length; i ++){
            act = Action.playPrince(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
              possibleActions.add(act);
            }
          }
        case 6:
          for(int i = 0; i < opponentsIndex.length; i ++){
            act = Action.playKing(myIndex, opponentsIndex[i]);  
            if (current.legalAction(act, card)){
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
    return possibleActions;
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

    /*
    *if you have a handmaid, play it, else if you have a guard, play it, else random your move
    */
    if(c.value() == 4  || current.getCard(myIndex).value() == 4){
      //System.out.println("theres a HANDMAID in our hand");
      while(!current.legalAction(act, c)){
        try{
          act = Action.playHandmaid(myIndex);
        }catch(IllegalActionException e){
      }

      }
    }
    else if(c.value() == 1  || current.getCard(myIndex).value() == 1){

      
      int bestTarget = -1;
      int bestScore = -1;
      int bestGuardTarget = -1;
      int[] cardStorage = new int[] {5,2,2,2,2,1,1,1};

      for(int i = 0; i < current.numPlayers();i++){
        java.util.Iterator<Card> discardPile = current.getDiscards(i);
        
        while(discardPile.hasNext()){
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

          System.out.println(val);
        }
      }
        for(int z = 0; z < 8;z++){
          System.out.println("There are " + cardStorage[z] + " " + Card.values()[z] + "'s");
        }
          for(int j = 7; j > 0; j--){ // prob;e, here
            if(cardStorage[j] ==1){
              bestGuardTarget = j + 1;
            }
          }
          for(int j = 7; j >0;j--){
            if(cardStorage[j] == 2){
              bestGuardTarget = j + 1;
            }
          }

        System.out.println(" im picking" + Card.values()[bestGuardTarget]);


      
        for(int i = 0; i < current.numPlayers(); i++){
          if( i != myIndex && current.score(i) >= bestScore && !current.eliminated(i)){
            System.out.println("Player " + i + " has score: " + current.score(i));
            bestTarget = i;
            bestScore = current.score(i);
          }
        }
        System.out.println("So im picking " + bestTarget);
        try{

          System.out.println("id like to attack" + bestTarget + " and im picking" + Card.values()[bestGuardTarget]);
          System.out.println(current.eliminated(0));
          System.out.println(current.eliminated(1));
          System.out.println(current.eliminated(2));
          System.out.println(current.eliminated(3));
          
          act = Action.playGuard(myIndex, bestTarget, Card.values()[bestGuardTarget]);
          System.out.println("MyIndex is " + myIndex);
          System.out.println("The best target is " + bestTarget);
          System.out.println("The card im going to guess is " + Card.values()[bestGuardTarget]);
        }catch(IllegalActionException e){} 
    System.out.println("Weve made it out of the catch statement");
      
      //System.out.println("theres a GUARD in our hand");
    }
    else{ 
      //  while(!current.legalAction(act, c)){
      //     if(rand.nextDouble()<0.5) play= c;
      //     else play = current.getCard(myIndex);
      //     int target = rand.nextInt(current.numPlayers());
      //     try{
      //       switch(play){
      //         case PRIEST:
      //           act = Action.playPriest(myIndex, target);
      //           break;
      //         case BARON:  
      //           act = Action.playBaron(myIndex, target);
      //           break;
      //         case PRINCE:  
      //           act = Action.playPrince(myIndex, target);
      //           break;
      //         case KING:
      //           act = Action.playKing(myIndex, target);
      //           break;
      //         case COUNTESS:
      //           act = Action.playCountess(myIndex);
      //           break;
      //         default:
      //           act = null;//never play princess
      //       }
      //     }catch(IllegalActionException e){/*do nothing, just try again*/}  
      //   }
      // return act;

      Node parent = stateNode.get(current);
   	  ArrayList<Action> possibleActions = new ArrayList<Action>();
      possibleActions = possibleActionsList(c);

      for(int i = 0; i < possibleActions.size(); i++){
      	State clone = current;
        try{
          clone.update(possibleActions.get(i), clone.drawCard());
        }
        catch(IllegalActionException e){}

        Node child = new Node(clone, -1, Collections.<Node>emptyList(), parent);
        // CREATE NODE FUNCTION? LINK TO PARENTS, UPDATE PARENTS CHILDREN LIST

      }

      }
    return act;
    }

    // C
    // O
    // P
    // Y
    //
    // P 
    // A
    // S
    // T 
    // E

    

} 



