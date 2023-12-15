import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Score {
    public static  char PLAYER_A='A';
    public static char PLAYER_B='B';
    final String[] gamePoints={"0","15","30","40","Advantage",  "Game"};
    BiPredicate<Integer, Integer> predicateWinner=(a,b)->a>3 && b<3 || a==5 && b<4;
    Consumer<String> printWinner= System.out::println;
    Consumer<String> printPoint= System.out::println;
    Predicate<Integer> predicateAdvantage=(i)->i>4;
    BiPredicate<Integer, Integer> predicateDeuce=(i,j)->i==4 && j==4;
    Predicate<Integer> predicateAdvantageLoss=(i)->i>4;

    public void getScore(final String points){

        AtomicInteger scoreA = new AtomicInteger(0);
        AtomicInteger scoreB = new AtomicInteger(0);
        points.chars().mapToObj(c-> (char)c).forEach(player -> {
            //Update score according to player
            if(PLAYER_A==player){
                scoreA.getAndIncrement();
            }else if(PLAYER_B==player){
                scoreB.getAndIncrement();
            }
            adjustScoresAndPrintWinner(player,scoreA ,scoreB);
        });

    }
    protected void adjustScoresAndPrintWinner(char player, AtomicInteger scoreA, AtomicInteger  scoreB){
        boolean isGame=false;
        if(PLAYER_A==player){
            //check if th game is finished
            isGame= isGameFinished(player,scoreA,scoreB);
            //adjust scores: check for advantage and deuce
            adjustScore(scoreA,scoreB);

         }else if(PLAYER_B==player){

            isGame= isGameFinished(player,scoreB,scoreA);
            adjustScore(scoreB,scoreA);
         }
        if(!isGame){
            printPoint.accept(String.format("Player A : %s / Player B : %s", gamePoints[scoreA.get() ],gamePoints[scoreB.get()]));
        }
    }


    protected void adjustScore(AtomicInteger scoreA, AtomicInteger  scoreB){
        if (predicateAdvantage.test(scoreA.get())|| (predicateDeuce.test(scoreB.get(),scoreA.get()))){
            scoreA.getAndDecrement();
        }
        if(predicateAdvantageLoss.test(scoreB.get())){
            scoreB.getAndDecrement();
        }
    }
    protected boolean isGameFinished(char player,AtomicInteger scoreA, AtomicInteger  scoreB){
        if(predicateWinner.test(scoreA.get(),scoreB.get())){
            printWinner.accept(String.format("Player %s wins the game", player));
            return true;
        }
        return false;
    }

}
