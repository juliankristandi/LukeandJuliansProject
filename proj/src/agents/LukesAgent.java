package agents;
import loveletter.*;
import java.util.Random;


/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * Luke Carpenter 22110274
 * */
public class LukesAgent implements Agent{


  /*
  * Enables diagnostic about game states, error locations
  */
  private boolean GEN_DIAGNOSTICS = false;
  private boolean ILLEGAL_DIAGNOSTICS = true;
  private boolean INVALID_DIAGNOSTICS = true;


  private Random rand;
  private State current;
  private int myIndex;

  //0 place default constructor
  public LukesAgent(){
    rand  = new Random();
  }

  /**
   * Reports the agents name
   * */
  public String toString(){return "pizza_time";}


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
    int[] fear = priotitizePlayers(myIndex);


    /*
    * Full overview of agent decisions can be found in the report
    */
    int newCardValue = c.value();
    int oldCardValue = current.getCard(myIndex).value();
    int[] cardStorage = getDeckFromDiscard(myIndex, c);

    if(newCardValue == 4 || oldCardValue == 4){
      //System.out.println("Weve have a handmaid");
      try{
        act = Action.playHandmaid(myIndex);
      }catch(IllegalActionException e){
        if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal handmaid action!");
          }
      }
    }else if(newCardValue == 1 || oldCardValue == 1){ // HEURISTIC, PLAY FROM GROUND UP, KEEP HIGHER CARDS
      
      Card bestGuardTarget;
        bestTarget = getHighestPriorityTarget(myIndex, bestTarget, fear);
        bestGuardTarget = Card.values()[getMostLikelyCard(cardStorage, bestTarget)];
        attackPlayerPerfectlyIfWeCan(myIndex, bestTarget, bestGuardTarget, fear);
        try{
          
          if(bestTarget != -1){
           act = Action.playGuard(myIndex, bestTarget, bestGuardTarget);
          }else{
            if(INVALID_DIAGNOSTICS == true){
            System.out.println("Invalid guard action! Randomizing");
            fullStatePrintout(bestTarget, bestGuardTarget, cardStorage, act, c, fear);
          }
            act = playRandomCard(c);
          }
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal guard action!");
            fullStatePrintout(bestTarget, bestGuardTarget, cardStorage, act, c, fear);
          }
        } 
    
    }else if(newCardValue == 2 || oldCardValue == 2){
      bestTarget = smarterPriestTargetting(myIndex, bestTarget, fear);
      if(bestTarget != -1){
        try{
          act = Action.playPriest(myIndex, bestTarget);
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal priest action!");
          }
        }
      }else{
        if(INVALID_DIAGNOSTICS == true){
            System.out.println("Invalid priest action! Randomizing");
            fullStatePrintout(bestTarget, null, cardStorage, act, c, fear);
          }
        act = playRandomCard(c);
      }
    }else if(newCardValue == 3 || oldCardValue == 3){
      bestTarget = getHighestPriorityTarget(myIndex, bestTarget, fear);


      seeIfWeCanPickSafeBaronTarget(myIndex, bestTarget, c.value(), fear);
      if(bestTarget != -1){
        try{
          act = Action.playBaron(myIndex, bestTarget);
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal baron action!");
          }
        }
      }else{
        if(INVALID_DIAGNOSTICS == true){
            System.out.println("Invalid baron action! Randomizing");
          }
        act = playRandomCard(c);
      }
    }else if(newCardValue == 7 || oldCardValue == 7){
      try{
          act = Action.playCountess(myIndex);
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal countess action!");
          }
        }
      

    }else if(newCardValue == 5 || oldCardValue == 5){
        bestTarget = smarterKingTargetting(myIndex, bestTarget, fear);
        try{
          if(bestTarget == -1){
            act = playRandomCard(c);
          }else{
            act = Action.playPrince(myIndex, bestTarget);
          }
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal prince action!");
            fullStatePrintout(bestTarget, null, cardStorage, act, c, fear);
          }
        }
    }else if(newCardValue == 6 || oldCardValue == 6){
        bestTarget = smarterKingTargetting(myIndex, bestTarget, fear);
        
        try{
          if(bestTarget == -1){
            act = playRandomCard(c);
          }else{
          act = Action.playKing(myIndex, bestTarget);
          }
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal king action!");
            fullStatePrintout(bestTarget, null, cardStorage, act, c, fear);
          }
        }
      
      
    }

    /*
    * These decisions represent edge cases i consider to be more valuable than
    *   a handmaid play, and are prioritised as such.
    */
    if(c.value() == 6 || current.getCard(myIndex).value()== 6){

      if(c.value() != 1 && current.getCard(myIndex).value() != 1 && c.value() != 5 && current.getCard(myIndex).value() != 5
        && c.value() != 4 && current.getCard(myIndex).value() != 4 && c.value() != 7 && current.getCard(myIndex).value() != 7
        && c.value() != 8 && current.getCard(myIndex).value() != 8){
        for(int i = 2; i >= 0; i--){
          if(fear[i] != myIndex && current.score(fear[i]) > 6 && !current.eliminated(fear[i]) && !current.handmaid(fear[i])){
            bestTarget = fear[i];
          }
        }
            try{
          act = Action.playKing(myIndex, bestTarget);
        }catch(IllegalActionException e){
          if(ILLEGAL_DIAGNOSTICS == true){
            System.out.println("Illegal KINGS GAMBIT action!");
            fullStatePrintout(bestTarget, null, cardStorage, act, c, fear);
          }
        }
      }
    }

    for(int i = 2; i >= 0; i--){
      if(fear[i] != myIndex && current.getCard(fear[i]) != null){
        if(current.getCard(fear[i]).value() == 8 && !current.eliminated(fear[i]) && !current.handmaid(fear[i])){
          if(c.value() ==5 && current.getCard(myIndex).value() ==5){
            try{
                act = Action.playPrince(myIndex, fear[i]);
              }catch(IllegalActionException e){
                if(ILLEGAL_DIAGNOSTICS == true){
                  System.out.println("Illegal countess action!");
                }
              }



            }else if(c.value() == 1 && current.getCard(myIndex).value() == 1){
            try{
                act = Action.playGuard(myIndex, fear[i], Card.values()[7]);
              }catch(IllegalActionException e){
                if(ILLEGAL_DIAGNOSTICS == true){
                  System.out.println("Illegal countess action!");
                }
              }



            }
          }
      }
    }

    return act;
  }


    /*
    * Taken from the random agent, function is used as a backup whenever
    *   there is no other option available and SOME valid move is needed.
    */

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


  /**
   * Attempts to reconstruct the deck from the combined discards of the players
   * @param c the card drawn from the deck
   * @param myIndex index of the agent
   * @return an arbitrary int
   * */

  public int[] getDeckFromDiscard(int myIndex, Card c){
    int[] cardStorage = new int[] {5,2,2,2,2,1,1,1}; // 0 1 2 3 4 5 6 7
    cardStorage[current.getCard(myIndex).value()-1]--;
    cardStorage[c.value()-1]--;

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
        
      }
      /*
      for(int z = 0; z < 8;z++){
          System.out.println("There are " + cardStorage[z] + " " + Card.values()[z] + "'s");
        }
        */
      return cardStorage;
  }



/**
   * Uses the discard pile and current deck the decided what the player is most
   *  likely to have.
   * @param cardStorage the deck as an int array, indexes represent card value - 
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @return the integer corresponding to the card value we are selecting
   * */

public int getMostLikelyCard(int[] cardStorage, int bestTarget){ //from an int array representing the current deck, get the best card to play


        java.util.Iterator<Card> discardPile = current.getDiscards(bestTarget);
        
        if(discardPile.hasNext()){
          int val = discardPile.next().value();
          for(int i = 0; i < val + 1;i++){
            
            cardStorage[i] = 0;
          
          }
          
        }

        int bestGuardTarget = -1;
        for(int j = 1; j <8; j++){ 
          if(cardStorage[j] == 1){
            bestGuardTarget = j ;
          }
        }
        for(int j = 1; j <8; j++){ 
          if(cardStorage[j] == 2){
            bestGuardTarget = j;
          }
        }// bestGuardTarget is -1 from here in edge case
        if(bestGuardTarget == -1){
          bestGuardTarget = 7;
        }


        return bestGuardTarget;
        
}

/**
   * Uses the prioritized array to determine who is currently the biggest target.
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @return the index of the player we want to attack
   * */

  public int getHighestPriorityTarget(int myIndex, int bestTarget, int[] fear){ // scans current scores to find the biggest threat
    
    int bestScore = -1;
    int highestDiscard = -1;
    for(int i = 2; i >=0; i--){
          if(fear[i] != myIndex && current.score(fear[i]) > bestScore && !current.eliminated(fear[i]) && !current.handmaid(fear[i])){
            //System.out.println("Player " + i + " has score: " + current.score(i));
            bestTarget = fear[i];
            bestScore = current.score(fear[i]);
          }
        }


    if(bestTarget == -1){
      for(int i = 0; i < current.numPlayers(); i++){
        if(!current.eliminated(i)){
          bestTarget = i;
        }
      }
    }
    return bestTarget;
  }

/**
   * Attempts to ignore opponents whos cards we already know for priest targetting
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @return the index of the player we wish to attack
   * */
    public int smarterPriestTargetting(int myIndex, int bestTarget, int[] fear){ // scans current scores to find the biggest threat
    
        int bestScore = -1;
    for(int i = 2; i >= 0; i--){

       if(current.score(fear[i]) > bestScore && !current.eliminated(fear[i]) && !current.handmaid(fear[i])){
              if(current.getCard(fear[i]) == null){
                //System.out.println("we know their card already");
                bestTarget = fear[i];
                bestScore = current.score(fear[i]);
              }
            
            
          }
      }
      if(bestTarget == -1){
        for(int i = 2; i >=0; i--){
          if(!current.eliminated(fear[i]) && !current.handmaid(fear[i])){
                bestTarget = fear[i];
              }
        }
      }
      if(bestTarget == -1){
        for(int i = 2; i >=0; i--){
          if(!current.eliminated(fear[i])){
                bestTarget = fear[i];
              }
        }
      }
    return bestTarget;
  }

/*
  public int attackPlayerWhoThreatensUs(int myIndex, int bestTarget, Card knownCard){

    for(int i = 0; i < current.numPlayers(); i++){

      for(int j = 0; j < current.numPlayers(); j++){
        if(j != myIndex && current.getCard(i) != null){
          System.out.println("BEWARE, player" + j + " knows our card");

      }
      }
    }
    return 0;
  }
  */

  /**
   * Attempts to ensure we dont suicide with baron
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param newVal the number representing the card value we have (not baron)
   * @param rankedPlayers the ordered list of players with highest scores 
   * @return an arbitrary int
   * */
  public int seeIfWeCanPickSafeBaronTarget(int myIndex, int bestTarget, int newVal, int[] rankedPlayers){

    int ourValue = -1;
    if(newVal >= current.getCard(myIndex).value()){
      ourValue = newVal;
    }else{
      ourValue = current.getCard(myIndex).value();
    }

    for(int j = rankedPlayers.length - 1; j >= 0; j--){
        if(rankedPlayers[j] != myIndex && current.getCard(rankedPlayers[j]) != null){
          if(current.getCard(rankedPlayers[j]).value() < ourValue){
            bestTarget = rankedPlayers[j];
          }
        }
      }
    return 0;
  }


/**
   * Checks if we can made a perfect guard play with what we know.
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @param bestGuardTarget the number corresponding to the card value we have selected
   * @return an arbitrary int
   * */
  public int attackPlayerPerfectlyIfWeCan(int myIndex, int bestTarget, Card bestGuardTarget, int[] fear){

    for(int i = 2; i >= 0;i--){
      if(fear[i] != myIndex && current.getCard(fear[i]) != null){
        bestTarget = fear[i];
        bestGuardTarget = current.getCard(fear[i]);
        //System.out.println("We know player" + bestTarget + "'s card, its" + current.getCard(bestTarget));
      }
      

    }
    //System.out.println("So we are attacking " + bestTarget + " because he has score: " + current.score(bestTarget));


    return 0;
  }


/**
   * For baron and prince plays, attempt to attack the person with the highest known card
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @return an arbitrary int
   * */
  public int pickTheGuyWithTheBestCard(int myIndex, int bestTarget, int[] fear){

    int bestCardSoFar = -1;

    for(int i = 2; i >=0; i--){
      if( fear[i] != myIndex && current.getCard(fear[i]) != null && current.getCard(fear[i]).value() > bestCardSoFar){
        bestCardSoFar = current.getCard(fear[i]).value();
        bestTarget = fear[i];
      }
    }
    return 0;
  }


/**
   * Rank order the players in the game based on their scores
   * @param myIndex the index of the player
   * @return the fear array of ordered players.
   * */

  public int[] priotitizePlayers(int myIndex){ // rank ordering the 3 players in the game
    int first,second,third;
    int topScore = -1;
    int[] ranking = new int[3];

      for(int j = 0; j < current.numPlayers(); j++){
        if(j != myIndex && current.score(j)>=topScore){
          ranking[0] = j;
          topScore = current.score(j);
        }
      }
      first = ranking[0];
        topScore = -1;
        for(int j = 0; j < current.numPlayers(); j++){
          if(j != myIndex && current.score(j)>=topScore && j != first){
            ranking[1] = j;
            topScore = current.score(j);
          }
        }
        
      
      second = ranking[1];

      
        topScore = -1;
        for(int j = 0; j < current.numPlayers(); j++){
          if(j != myIndex && current.score(j)>=topScore && j != first && j != second){
            ranking[2] = j;
            topScore = current.score(j);
          }
        }
        third = ranking[2];
    

    if(GEN_DIAGNOSTICS == true){
      System.out.println("The current players ranked:");
      for(int i = 0; i < 3;i++){
        System.out.println("Player " + ranking[i] + " in " + i + " place");
      }
    }
    return ranking;    
  }

  /**
   * Gives a full diagnostic printout of the current gamestate, complete with handmaid
   *  information, scores and the agents full intentions
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @param cardStorage the array containing the deck
   * @param action the move we intend to make
   * @param c the card we drew
   * @param bestGuardTarget the number corresponding to the card value we have selected
   * @return an arbitrary int
   * */

  public int fullStatePrintout(int bestTarget, Card bestGuardTarget, int[] cardStorage, Action act, Card c, int[] fear){
    
    System.out.println("My Index is " + myIndex);
    System.out.println("id like to attack" + bestTarget + " and im picking" + bestGuardTarget);

    for(int z = 0; z < current.numPlayers();z++){
      System.out.println("Player Status: " + current.eliminated(z));
    }

    for(int z = 0; z < current.numPlayers();z++){
      System.out.println("Handmaid status: "+ current.handmaid(z));
    }

    System.out.println("The stateof my move is" + current.legalAction(act, c));
    System.out.println("The card i had was: : " + current.getCard(myIndex));
    System.out.println("The card i picked up was: : " + c);
    for(int z = 0; z < current.numPlayers();z++){
      System.out.println("Player " + (fear[z]) + " has" + current.getCard(fear[z]));
    }
    for(int z = 0; z < current.numPlayers();z++){
      System.out.println("Player in " + (z+1) + " place is" + fear[z]);
    }
    for(int z = 0; z < 8;z++){
        System.out.println("There are " + cardStorage[z] + " " + Card.values()[z] + "'s");
       }
        
    
  return 0;
}


 /**
   * If we have determined the optimal target is tied, pick a random enemy
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param fear the ordered list of players with highest scores
   * @return an arbitrary int
   * */

  public int pickRandomFromFear(int bestTarget, int[] fear){
    for(int i = 2; i >= 0; i--){
      if(!current.eliminated(fear[i]) && !current.handmaid(fear[i])){
        if(rand.nextDouble()<0.5){
          bestTarget = fear[i];
        }
      }
    }
    return 0;
  }


 /**
   * Upon inspection, king plays by intelligent agents often indicate a princess,
   *  attack this person.
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param bestGuardTarget the number corresponding to the card value we have selected
   * @return an arbitrary int
   * */

  public int attackPlayerWhoPlayedKing(int myIndex, int bestTarget, Card bestGuardTarget){
    for(int i = 0; i < current.numPlayers();i++){
        java.util.Iterator<Card> discardPile = current.getDiscards(i);
        
        if(i != myIndex && discardPile.hasNext()){
          int val = discardPile.next().value();
          if(val == 6){
            bestTarget = i;
            bestGuardTarget = Card.values()[7];
          }
        }
      }

    return 0;
  }

 /**
   * Uses the discard pile as an extra parameter for king, priest and prince selection
   * @param bestTarget the current best attempt at an enemy target, default -1
   * @param myIndex the index of the player
   * @param fear the ordered list of players with highest scores 
   * @return the index of the best target for prince, king or priest
   * */

  public int smarterKingTargetting(int myIndex, int bestTarget, int[] fear){


 int highestDiscard = -1;

      if(bestTarget == -1){
      int bestScore = -1;
    for(int i = 2; i >=0; i--){
          if( fear[i] != myIndex && current.score(fear[i]) > bestScore && !current.eliminated(fear[i]) && !current.handmaid(fear[i])){
            //System.out.println("Player " + i + " has score: " + current.score(i));
            bestTarget = fear[i];
            bestScore = current.score(fear[i]);
          }
        }
      }
      if(bestTarget == -1){
      for(int i = 2; i >=0; i--){
        if(fear[i] != myIndex &&!current.eliminated(fear[i]) && !current.handmaid(fear[i])){
          java.util.Iterator<Card> discardPile = current.getDiscards(fear[i]);
        
          if(discardPile.hasNext()){
            int val = discardPile.next().value();
            if(val > highestDiscard){
              highestDiscard = val;
              bestTarget = fear[i];
            }
          }
        }

      }
    }


    if(bestTarget == -1){
        for(int i = 2; i >=0; i--){
          if(fear[i] != myIndex && !current.eliminated(fear[i])&& !current.handmaid(fear[i])){
                bestTarget = fear[i];
              }
        }
      }
      //System.out.println("THE LAST TARGET WE HAD WAS" + bestTarget);


    return bestTarget;
  }
}

