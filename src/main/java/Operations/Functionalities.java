package Operations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Functionalities {
    static private Config load = ConfigFactory.load();
    static private String consumerKey = load.getString("keyholder.key.consumerKey");
    static private String consumerSecret = load.getString("keyholder.key.consumerSecret");
    static private String accessToken = load.getString("keyholder.key.accessToken");
    static private String accessTokenSecret = load.getString("keyholder.key.accessTokenSecret");
    private static Twitter twitter = Functionalities.getInstance(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    ConfigurationBuilder cb = new ConfigurationBuilder();

    private static Twitter getInstance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory factory = new TwitterFactory(builder.build());
        return factory.getInstance();
    }

    public static CompletableFuture<List<Status>> getTweets(String hashtag){
         return CompletableFuture.supplyAsync(() -> {
            Query query = new Query(hashtag);
            query.setCount(100);
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return result != null ? result.getTweets() : new ArrayList<Status>() {
            };
        });
    }

    public static CompletableFuture<Integer> getTweetCount(String hashtag){
        return CompletableFuture.supplyAsync(()->{
            Query query = new Query(hashtag);
            query.setCount(100);
            QueryResult result = null;
            try{
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return result != null ? result.getCount() : 0;
        });
    }

    public  static CompletableFuture<Float> getAverageTweets(String hashtag){
        return CompletableFuture.supplyAsync(()-> {
           Query query = new Query(hashtag);
           query.setCount(100);
           query.setSince("2018-01-01");
           query.setUntil("2018-02-01");
           QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return ((float) result.getCount())/30;
        });
    }

    public static CompletableFuture<Float> getAverageLikes(String hashtag){

        Query query = new Query(hashtag);
        query.setCount(100);
        int totalTweetCount = query.getCount();

        CompletableFuture<Integer> totalLikes = CompletableFuture.supplyAsync(()->{
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return (result.getTweets().stream().mapToInt(Status::getFavoriteCount).sum());
        });
        return totalLikes.thenApply(totalTweetLikes -> (float)totalTweetLikes/totalTweetCount);
    }

    public static CompletableFuture<Float> getAverageRetweets(String hashtag){
        Query query = new Query(hashtag);
        query.setCount(100);
        int totalTweetCount = query.getCount();

        CompletableFuture<Integer> retweetCount = CompletableFuture.supplyAsync(() -> {
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return (result.getTweets().stream().mapToInt(Status::getRetweetCount).sum());
        });
        return retweetCount.thenApply(totalRetweets -> (float)totalRetweets/totalTweetCount);
    }
}
