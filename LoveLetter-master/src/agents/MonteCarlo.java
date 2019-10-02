package loveletter;
import loveletter.*;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class MonteCarlo implements Agent{

  private State current; 
  private int myIndex; 

  class Node{
    Node left;
    Node right;
    int score;

    Node(int score){
      left = null;
      right = null;
      this.score = score; 
    }
  }

  public class BinaryTree{
    Node root;
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
    BinaryTree tree = new BinaryTree();
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
    //tree.add();
  }
}


