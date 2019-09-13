import java.util.ArrayList;
/*
 * Luke Carpenter 22110274
 * Julian Kristandi
 * 
 * Considerations:
 * 1. Using arraylist wastes space, I think we only need a single int to track the number of
 * 		each card, its location is irrelevant for our search.
 * 2. First initialization of data structure should set values according to what you have,
 * 		might consider setting these statically and simply calling a reduce function.
 */
public class gameState<T> {

	    private int guard, priest, baron, handmaid, prince, king, countess, princess;
	    

	    /*
	     * Standard options are new gameState(5,2,2,2,2,1,1,1)
	     */
	    
	    public gameState(int guard, int priest, int baron, int handmaid, int prince,
	    					int king, int countess, int princess) {
	        this.guard = guard;
	        this.priest = priest;
	        this.baron = baron;
	        this.handmaid = handmaid;
	        this.prince = prince;
	        this.king = king;
	        this.countess = countess;
	        this.princess = princess;
	    }

	    /*
	     * Return the total number of each card remaining in the deck for each card
	     */
	    
        public int guardRemaining() {
        	return this.guard;
        }
        public int priestRemaining() {
        	return this.priest;
        }
        public int baronRemaining() {
        	return this.baron;
        }
        public int handmaidRemaining() {
        	return this.handmaid;
        }
        public int princeRemaining() {
        	return this.prince;
        }
        public int kingRemaining() {
        	return this.king;
        }
        public int countessRemaining() {
        	return this.countess;
        }
        public int princessRemaining() {
        	return this.princess;
        }
        
        
        /*
         * Used when a new card is seen, removes that card from our known deck
         * This set is treated as discarded
         */
        
        public void removeGuard() {
        	if(this.guard > 0) {
        		this.guard --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removePriest() {
        	if(this.priest > 0) {
        		this.priest --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removeBaron() {
        	if(this.baron > 0) {
        		this.baron --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removeHandmaid() {
        	if(this.handmaid > 0) {
        		this.handmaid --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removePrince() {
        	if(this.prince > 0) {
        		this.prince --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removeKing() {
        	if(this.king > 0) {
        		this.king --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removeCountess() {
        	if(this.countess > 0) {
        		this.countess --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
        public void removePrincess() {
        	if(this.princess > 0) {
        		this.princess --;
        	}else {
        		System.out.println("Error, there arent any of those left in the deck");
        	}
        }
	
}
