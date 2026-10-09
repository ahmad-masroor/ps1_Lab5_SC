/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;
import static org.junit.Assert.*;
import org.junit.Test;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class FilterTest {
    /*
     * - Testing strategy for writtenBy(tweets, username):
     * - number of tweets: 0, 1, > 1
     * - number of tweets by username: 0, 1, > 1
     * - case of username: same as author, different from author
     *
     * - Testing strategy for inTimespan(tweets, timespan):
     * - number of tweets: 0, > 1
     * - tweets inside timespan: none, some, all
     * - tweet exactly on start or end of timespan: yes, no
     *
     * - Testing strategy for containing(tweets, words):
     * - number of words: 1, > 1
     * - matching tweets: none, some, all
     * - case of word: same as in tweet, different
     * - word appears only as part of a bigger word: yes, no
     *
     * - Every result is also checked for keeping the input order.
     */
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "Talking about the pset tonight", d3);
    // Test 1
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    // Tests for writtenBy    
    // Test 2: covers > 1 tweets by username, author written in a different case
    @Test
    public void testWrittenByDifferentCaseMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "ALYSSA");
        assertEquals("expected two tweets in order", Arrays.asList(tweet1, tweet3), writtenBy);
    }    
    // Test 3: covers 0 tweets in list
    @Test
    public void testWrittenByEmptyList() {
        List<Tweet> writtenBy = Filter.writtenBy(Collections.emptyList(), "alyssa");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    // Test 4: covers 0 matching tweets by username
    @Test
    public void testWrittenByNoMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "unknown");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    // Test 5: covers 1 tweet by username, case same as author
    @Test
    public void testWrittenBySameCaseSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "bbitdiddle");
        assertEquals("expected one tweet", Arrays.asList(tweet2), writtenBy);
    }
    // Tests for inTimespan
    // Test 6: covers tweets exactly on start and end are included, one tweet outside
    @Test
    public void testInTimespanEndpointsIncluded() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(d2, d3));
        assertEquals("expected tweets on the endpoints", Arrays.asList(tweet2, tweet3), inTimespan);
    }    
    // Test 7: covers 0 tweets in list
    @Test
    public void testInTimespanEmptyList() {
        List<Tweet> inTimespan = Filter.inTimespan(Collections.emptyList(), new Timespan(d1, d2));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }    
    // Test 8: covers none inside timespan
    @Test
    public void testInTimespanNoMatch() {
        Instant d4 = Instant.parse("2016-02-17T13:00:00Z");
        Instant d5 = Instant.parse("2016-02-17T14:00:00Z");
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(d4, d5));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    // Test 9: covers all inside timespan, no endpoints exactly matching
    @Test
    public void testInTimespanAllInside() {
        Instant before = Instant.parse("2016-02-17T09:00:00Z");
        Instant after = Instant.parse("2016-02-17T13:00:00Z");
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(before, after));
        assertEquals("expected all tweets", Arrays.asList(tweet1, tweet2), inTimespan);
    }
    // Tests for containing
    // Test 10: covers different case, word only inside a bigger word ("Talking" is not "talk")
    @Test
    public void testContainingCaseAndWholeWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("TALK"));
        assertEquals("expected tweets 1 and 2 only", Arrays.asList(tweet1, tweet2), containing);
    }
    // Test 11: covers > 1 words, matching some tweets
    @Test
    public void testContainingMultipleWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("reasonable", "pset"));
        assertEquals("expected tweets 1 and 3", Arrays.asList(tweet1, tweet3), containing);
    }    
    // Test 12: covers 1 word, matching none
    @Test
    public void testContainingNoMatch() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("hello"));
        assertTrue("expected empty list", containing.isEmpty());
    }
}