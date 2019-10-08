package agents;
import loveletter.*;
import java.util.Random;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class heuristicAgent implements Agent{

  private Random rand;
  private State current;
  private int myIndex;

  //0 place default constructor
  public heuristicAgent(){
    rand  = new Random();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "Ragnar, God of RNG";}


  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();
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
    int bestTarget = -1;

    /*
    *if you have a handmaid, play it, else if you have a guard, play it, else random your move
    */
    int newCardValue = c.value();
    int oldCardValue = current.getCard(myIndex).value();

    //System.out.println(newCardValue);
    //System.out.println(oldCardValue);

          if(newCardValue == 1 || oldCardValue == 1){ // HEURISTIC, PLAY FROM GROUND UP, KEEP HIGHER CARDS

      Card bestGuardTarget;
      int[] cardStorage = new int[] {5,2,2,2,2,1,1,1}; // 0 1 2 3 4 5 6 7

      cardStorage = getDeckFromDiscard();
      bestGuardTarget = Card.values()[getMostLikelyCard(cardStorage)];

        //System.out.println("Im picking the " + bestGuardTarget);

        bestTarget = getHighestPriorityTarget(myIndex, bestTarget);
        //System.out.println("So im picking " + bestTarget);
        try{
          /*
          System.out.println("id like to attack" + bestTarget + " and im picking" + Card.values()[bestGuardTarget]);
          System.out.println(current.eliminated(0));
          System.out.println(current.eliminated(1));
          System.out.println(current.eliminated(2));
          System.out.println(current.eliminated(3));
          */
          if(bestTarget != -1){
           act = Action.playGuard(myIndex, bestTarget, bestGuardTarget);
          }else{
            act = playRandomCard(c);
          }
          /*
          System.out.println("The stateof my move is" + current.legalAction(act, c));

          System.out.println("MyIndex is " + myIndex);
          System.out.println("The best target is " + bestTarget);
          System.out.println("The card im going to guess is " + Card.values()[bestGuardTarget]);
          */
        }catch(IllegalActionException e){} 
    
    }else if(newCardValue == 2 || oldCardValue == 2){
      //System.out.println("Weve have a priest");
      act = playRandomCard(c);
    }else if(newCardValue == 4 || oldCardValue == 4){
      //System.out.println("Weve have a handmaid");
      try{
        act = Action.playHandmaid(myIndex);
      }catch(IllegalActionException e){
      }
    }else if(newCardValue == 3 || oldCardValue == 3){
      //System.out.println("Weve have a baron");
      bestTarget = getHighestPriorityTarget(myIndex, bestTarget);
      if(bestTarget != -1){
        try{
          act = Action.playBaron(myIndex, bestTarget);
        }catch(IllegalActionException e){
        }
      }else{
        act = playRandomCard(c);
      }
    }else if(newCardValue == 5 || oldCardValue == 5){
      //System.out.println("Weve have a prince");
      act = playRandomCard(c);
    }else if(newCardValue == 6 || oldCardValue == 6){
      //System.out.println("Weve have a king");
      act = playRandomCard(c);
    }else if(newCardValue == 7 || oldCardValue == 7){
      //System.out.println("Weve have a countess");
      act = playRandomCard(c);
    }else if(newCardValue == 8 || oldCardValue == 8){
      //System.out.println("Weve have a princess");
      act = playRandomCard(c);
    }
    return act;
  }

  public Action playRandomCard(Card c){ // backup incase something goes wrong
    Action act = null;
    Card play;
    while(!current.legalAction(act, c)){
      if(rand.nextDouble()<0.5) play= c;
      else play = current.getCard(myIndex);
      int target = rand.nextInt(current.numPlayers());
      try{
        switch(play){
          case GUARD:
          Card pick = Card.values()[rand.nextInt(7)+1];
            act = Action.playGuard(myIndex, target, pick);
            break;
          case PRIEST:
            act = Action.playPriest(myIndex, target);
            break;
          case BARON:  
            act = Action.playBaron(myIndex, target);
            break;
          case HANDMAID:
            act = Action.playHandmaid(myIndex);
            break;
          case PRINCE:  
            act = Action.playPrince(myIndex, target);
            break;
          case KING:
            act = Action.playKing(myIndex, target);
            break;
          case COUNTESS:
            act = Action.playCountess(myIndex);
            break;
          default:
            act = null;//never play princess
        }
      }catch(IllegalActionException e){/*do nothing, just try again*/}  
    }
    return act;
  }

  public int[] getDeckFromDiscard(){
    int[] cardStorage = new int[] {5,2,2,2,2,1,1,1}; // 0 1 2 3 4 5 6 7

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

          //System.out.println(val);
        }
        
      }/*
      for(int z = 0; z < 8;z++){
          System.out.println("There are " + cardStorage[z] + " " + Card.values()[z] + "'s");
        }
        */
      return cardStorage;
  }

public int getMostLikelyCard(int[] cardStorage){ //from an int array representing the current deck, get the best card to play

        int bestGuardTarget = -1;
        for(int j = 1; j <8; j++){ // problem here
          if(cardStorage[j] == 1){
            bestGuardTarget = j ;
          }
        }
        for(int j = 1; j <8; j++){ // problem here
          if(cardStorage[j] == 2){
            bestGuardTarget = j;
          }
        }// bestGuardTarget is -1 from here in edge case
        return bestGuardTarget;
}

  public int getHighestPriorityTarget(int myIndex, int bestTarget){ // scans current scores to find the biggest threat
    
    int bestScore = -1;

    for(int i = 0; i < current.numPlayers(); i++){
          if( i != myIndex && current.score(i) >= bestScore && !current.eliminated(i) && !current.handmaid(i)){
            //System.out.println("Player " + i + " has score: " + current.score(i));
            bestTarget = i;
            bestScore = current.score(i);
          }
        }
    return bestTarget;
  }
}

