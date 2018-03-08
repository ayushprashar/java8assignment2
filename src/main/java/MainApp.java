import Operations.Functionalities;

import java.util.concurrent.TimeUnit;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
/*
        Functionalities.getTweets("#modi")
                .thenAccept(list ->list.forEach(status ->System.out.println(status.getText())));
*/
/*
        Functionalities.getTweetCount("#modi")
                .thenAccept(count -> System.out.print("Total tweets are " + count));
*/
/*
        Functionalities.getAverageTweets("#modi")
                .thenAccept(avg -> System.out.println("Average tweets are " + avg));
*/
/*
        Functionalities.getAverageLikes("#modi").thenAccept(likes -> System.out.println("Average likes for one month are " + likes));

*/
        Functionalities.getAverageRetweets("#modi").thenAccept(rt -> System.out.print("Average retweets for one month are" + rt));
        TimeUnit.SECONDS.sleep(10);

    }






    /*Twitter twitter = Functionalities.getInstance()*/
}
