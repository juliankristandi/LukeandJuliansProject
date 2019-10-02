package agents;
import loveletter.*;
import java.util.Random;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class MonteCarlo implements Agent{

  private Random rand;
  private State current; 
  private int myIndex; 

  public MonteCarlo(){

  }
    
  /**
   * Class to initialize Binary Tree
   * */
  class Node{
    Node left;
    Node right;
    int wins;
    int selected;
    int available;

    Node(int wins, int selected, int available){
      left = null;
      right = null;
      this.wins = wins; 
      this.selected = selected;
      this.available = available;
    }
  }

  class BinaryTree{
    Node root;

    BinaryTree(int wins, int selected, int available){
      root = new Node(wins, selected, available);
    }

    BinaryTree(){
      root = null;
    }
  }

  /**
   * Reports the agents name
   * */
  public String toString(){
    return("Monte Carlo");
  }


  /**
   * Method called at the start of a round
   * @param start the initial state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();
    BinaryTree bt = new BinaryTree(0, 0, 0); 
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
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * @throws IllegalActionException when the Action produced is not legal.
   * */
  public Action playCard(Card c){
    Action act = null; 
    Card play;
    return act;
  }

  public void addTree(Node currentNode, int winsNumber, int selectedNumber, int availableNumber) {

  } 
}


