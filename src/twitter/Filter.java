/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 */
public class Filter {
    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> filteredTweets = new ArrayList<>();
        for (Tweet tweet : tweets) {
            // Twitter usernames are case-insensitive
            if (tweet.getAuthor().equalsIgnoreCase(username)) {
                filteredTweets.add(tweet);
            }
        }
        return filteredTweets;
    }
    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> filteredTweets = new ArrayList<>();
        for (Tweet tweet : tweets) {
            // Timespan endpoints are inclusive
            boolean notBeforeStart = !tweet.getTimestamp().isBefore(timespan.getStart());
            boolean notAfterEnd = !tweet.getTimestamp().isAfter(timespan.getEnd());
            
            if (notBeforeStart && notAfterEnd) {
                filteredTweets.add(tweet);
            }
        }
        return filteredTweets;
    }
    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> filteredTweets = new ArrayList<>();
        // Convert search words to lowercase for case-insensitive matching
        List<String> lowerCaseSearchWords = new ArrayList<>();
        for (String word : words) {
            lowerCaseSearchWords.add(word.toLowerCase());
        }
        for (Tweet tweet : tweets) {
            // Split tweet text by whitespace to isolate words bounded by spaces
            String[] tweetWords = tweet.getText().split("\\s+");
            
            boolean found = false;
            for (String tweetWord : tweetWords) {
                if (lowerCaseSearchWords.contains(tweetWord.toLowerCase())) {
                    found = true;
                    break; 
                }
            }            
            if (found) {
                filteredTweets.add(tweet);
            }
        }        
        return filteredTweets;
    }
}