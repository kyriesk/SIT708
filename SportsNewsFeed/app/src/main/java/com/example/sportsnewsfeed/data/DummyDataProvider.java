package com.example.sportsnewsfeed.data;

import com.example.sportsnewsfeed.models.FeaturedMatch;
import com.example.sportsnewsfeed.models.SportsNews;
import com.example.sportsnewsfeed.models.SportCategory;

import java.util.ArrayList;
import java.util.List;

public class DummyDataProvider {

    public static List<FeaturedMatch> getFeaturedMatches() {
        List<FeaturedMatch> matches = new ArrayList<>();
        matches.add(new FeaturedMatch(1, "Manchester United", "Liverpool", "3:00 PM Today",
                "ic_launcher_foreground", SportCategory.FOOTBALL));
        matches.add(new FeaturedMatch(2, "Lakers", "Celtics", "8:00 PM Tonight",
                "ic_launcher_foreground", SportCategory.BASKETBALL));
        matches.add(new FeaturedMatch(3, "India", "Australia", "2:30 PM Today",
                "ic_launcher_foreground", SportCategory.CRICKET));
        matches.add(new FeaturedMatch(1, "Manchester United", "Liverpool", "3:00 PM Today",
                "ic_launcher_foreground", SportCategory.FOOTBALL));
        matches.add(new FeaturedMatch(2, "Lakers", "Celtics", "8:00 PM Tonight",
                "ic_launcher_foreground", SportCategory.BASKETBALL));
        matches.add(new FeaturedMatch(3, "India", "Australia", "2:30 PM Today",
                "ic_launcher_foreground", SportCategory.CRICKET));
        return matches;

    }

    public static List<SportsNews> getSportsNews() {
        List<SportsNews> newsList = new ArrayList<>();
        
        newsList.add(new SportsNews(1, "Manchester United Wins Championship",
                "Manchester United defeats Liverpool 3-2 in an exciting match. The team showed exceptional performance with two goals from Rashford.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "John Smith", "Today at 4:30 PM"));
        
        newsList.add(new SportsNews(2, "Basketball: Lakers vs Celtics Tonight",
                "The Lakers prepare for a crucial playoff match against the Celtics. Both teams are in great form.",
                "ic_launcher_foreground", SportCategory.BASKETBALL, "Sarah Johnson", "Today at 3:15 PM"));
        
        newsList.add(new SportsNews(3, "India Cricket Team Dominates ODI Series",
                "India cricket team successfully completes the ODI series against Australia with a 3-0 victory. Virat Kohli scored 150 runs.",
                "ic_launcher_foreground", SportCategory.CRICKET, "Mike Davis", "Today at 2:00 PM"));
        
        newsList.add(new SportsNews(4, "Premier League: Teams Prepare for Weekend",
                "All Premier League teams are preparing for the weekend matches. Top teams are in excellent form.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "Emily Brown", "Today at 1:45 PM"));
        
        newsList.add(new SportsNews(5, "NBA: Playoff Predictions Released",
                "Experts release their playoff predictions for the NBA season. Lakers and Celtics are favorites.",
                "ic_launcher_foreground", SportCategory.BASKETBALL, "James Wilson", "Today at 12:30 PM"));
        
        newsList.add(new SportsNews(6, "Cricket: Test Series Begins Next Month",
                "The international test series is set to begin next month with exciting matches lined up.",
                "ic_launcher_foreground", SportCategory.CRICKET, "David Lee", "Today at 11:00 AM"));
        
        newsList.add(new SportsNews(7, "Football: World Cup Qualifiers Draw",
                "The World Cup qualifiers draw has been announced. Several countries face challenging groups.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "Lisa Anderson", "Yesterday at 5:00 PM"));
        
        newsList.add(new SportsNews(8, "Basketball: Young Star Breaks Scoring Record",
                "A rising basketball star breaks the scoring record in their debut season.",
                "ic_launcher_foreground", SportCategory.BASKETBALL, "Tom Harris", "Yesterday at 3:30 PM"));
        
        return newsList;
    }

    public static List<SportsNews> getRelatedStories(int newsId) {
        List<SportsNews> relatedStories = new ArrayList<>();
        
        relatedStories.add(new SportsNews(100, "Team Analysis: Tactical Breakdown",
                "Detailed analysis of the winning tactics used in the recent match.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "Coach Review", "2 hours ago"));
        
        relatedStories.add(new SportsNews(101, "Player Performance Ratings",
                "Rating all players from the championship match based on their performance.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "Sports Analyst", "1 hour ago"));
        
        relatedStories.add(new SportsNews(102, "Upcoming Fixture Schedule",
                "Next week's matches and schedule for all top teams.",
                "ic_launcher_foreground", SportCategory.FOOTBALL, "Schedule Desk", "30 minutes ago"));
        
        return relatedStories;
    }
}

