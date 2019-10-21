package loveletter;
import java.util.Random;
import java.io.PrintStream;
import agents.RandomAgent;
import agents.MonteCarlo;
import agents.AgentSmith;

/**
 * A class for running a single game of LoveLetter.
 * An array of 4 agents is provided, a deal is initialised and players takes turns until the game ends and the score is reported.
 * @author Tim French
 * */
public class LoveLetter{

  private Agent rando;
  private Agent monte;
  private Agent smith;
  private Random random;
  private PrintStream ps;
  static int[] winners = new int[4];

  // ************************ SET THESE TO ENABLE TEST MODE
  static boolean TESTMODE = true;
  static int gameNumber = 1000;
  // ************************ SET THESE TO ENABLE TEST MODE
  /**
   * Constructs a LoveLetter game.
   * @param seed a seed for the random number generator.
   * @param ps a PrintStream object to record the events of the game
   * **/
  public LoveLetter(long seed, PrintStream ps){
    this.random = new Random(seed);
    this.ps = ps;
    rando = new RandomAgent();
    monte = new MonteCarlo();
    smith = new AgentSmith();
  }

  /**
   * Constructs a LoveLetter game.
   * Defauklt construct with system random seed, and System.out as the PrintStream
   * **/
  public LoveLetter(){
    this(0,System.out);
    this.ps = System.out;
  }


  /**
   * Plays a game of LoveLetter
   * @param agents the players in the game
   * @return scores of each agent as an array of integers
   * **/
  public int[] playGame(Agent[] agents){
    boolean gameOver = false;
    int winner=0;
    int numPlayers = agents.length;
    State gameState = new State(random, agents);//the game state
    State[] playerStates = new State[numPlayers];
    try{
      while(!gameState.gameOver()){
        for(int i = 0; i<numPlayers; i++){
          playerStates[i] = gameState.playerState(i);
          agents[i].newRound(playerStates[i]);
        }
        while(!gameState.roundOver()){
          if(!TESTMODE){System.out.println("Cards are:\nplayer 0:"+gameState.getCard(0)+"\nplayer 1:"+gameState.getCard(1)+"\nplayer 2:"+gameState.getCard(2)+"\nplayer 3:"+gameState.getCard(3));}      
          Card topCard = gameState.drawCard(); 
          if(!TESTMODE){System.out.println("Player "+gameState.nextPlayer()+" draws the "+topCard);}
          Action act = agents[gameState.nextPlayer()].playCard(topCard);

          try{
            if(!TESTMODE){ps.println(gameState.update(act,topCard));

            }else if(TESTMODE == true){gameState.update(act,topCard);}
            
          }
          catch(IllegalActionException e){
            ps.println("ILLEGAL ACTION PERFORMED BY PLAYER "+agents[gameState.nextPlayer()]+
              "("+gameState.nextPlayer()+")\nRandom Move Substituted");
            act = rando.playCard(topCard);
          }
          for(int p = 0; p<numPlayers; p++)
            agents[p].see(act,playerStates[p]);
        }
        if(!TESTMODE){System.out.println("New Round, scores are:\nplayer 0:"+gameState.score(0)+"\nplayer 1:"+gameState.score(1)+"\nplayer 2:"+gameState.score(2)+"\nplayer 3:"+gameState.score(3));}        
        gameState.newRound();
      }
      winners[gameState.gameWinner()]++;
      if(!TESTMODE){ps.println("Player "+gameState.gameWinner()+" wins the Princess's heart!");}
      int[] scoreboard = new int[numPlayers];
      for(int p = 0; p<numPlayers; p++)scoreboard[p] = gameState.score(p);
      return scoreboard;
    }catch(IllegalActionException e){
      ps.println("Something has gone wrong.");
      e.printStackTrace();
      return null;
    } 
  }

  /**
   * This main method is provided to run a simple test game with provided agents.
   * The agent implementations should be in the default package.
   * Enable testmode and games to run a larger test
   * */
  public static void main(String[] args){

    if(TESTMODE == true){
      
      
      for(int i = 0;i<gameNumber;i++){
        if(i == gameNumber/5){
          System.out.println("~~~~~~~20% Complete\n");
        }else if(i == gameNumber/2.5){
          System.out.println("~~~~~~~50% Complete\n");
        }else if(i == gameNumber/1.25){
          System.out.println("~~~~~~~75% Complete\n");
        }        
        Agent[] agents = {new agents.MonteCarlo(),new agents.RandomAgent(), new agents.RandomAgent(), new agents.RandomAgent()};
        LoveLetter env = new LoveLetter();
        int[] results = env.playGame(agents);
        
      }
      System.out.println("The result of the test are:\n");
      for(int i= 0; i<4; i++){
        int total = (winners[0] + winners[1] + winners[2] + winners[3]);
        System.out.println("Agent " + i + ": " + winners[i] + " out of " + total);
      }
    }else{
      Agent[] agents = {new agents.MonteCarlo(),new agents.RandomAgent(), new agents.RandomAgent(), new agents.RandomAgent()};
      LoveLetter env = new LoveLetter();
      StringBuffer log = new StringBuffer("A simple game for four random agents:\n");
      int[] results = env.playGame(agents);
      if(!TESTMODE){
      env.ps.print("The final scores are:\n");
      for(int i= 0; i<agents.length; i++)
        env.ps.print("\t Agent "+i+", \""+agents[i]+"\":\t "+results[i]+"\n");
    }
  }
  }
}


