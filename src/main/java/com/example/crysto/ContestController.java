package com.example.crysto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import redis.clients.jedis.UnifiedJedis;

@RestController
public class ContestController {

	@Autowired
	ContestService contestService;

	@GetMapping("/contests")
	public List<Contest> getAllContests() {
		return contestService.getAllContests();
	}

	@GetMapping("/addContest")
	public List<Contest> getUpdatedContests() {
		contestService.removeContest();
		contestService.addContest();
		return contestService.getAllContests();
	}

	@Autowired
	private UnifiedJedis unifiedJedis;

	@GetMapping("/initializeStocks")
	public void initializeStocks() {
		unifiedJedis.hset("1", Map.of("stockname", "REL", "openingPrice", "100", "currentPoints", "50"));
		unifiedJedis.hset("2", Map.of("stockname", "HDFC", "openingPrice", "10", "currentPoints", "15"));
		unifiedJedis.hset("3", Map.of("stockname", "HDFCBNK", "openingPrice", "50", "currentPoints", "-25"));
		unifiedJedis.hset("4", Map.of("stockname", "AIRTEL", "openingPrice", "500", "currentPoints", "35"));
		unifiedJedis.hset("5", Map.of("stockname", "BAJAJ", "openingPrice", "100", "currentPoints", "-50"));
		unifiedJedis.hset("6", Map.of("stockname", "ICICI", "openingPrice", "150", "currentPoints", "70"));
		unifiedJedis.hset("7", Map.of("stockname", "ASIAN", "openingPrice", "330", "currentPoints", "30"));
		unifiedJedis.hset("8", Map.of("stockname", "TITAN", "openingPrice", "220", "currentPoints", "-20"));
		unifiedJedis.hset("9", Map.of("stockname", "TCS", "openingPrice", "100", "currentPoints", "0"));
		unifiedJedis.hset("10", Map.of("stockname", "INFY", "openingPrice", "650", "currentPoints", "10"));
	}
	
	@GetMapping("/addDummyContest")
	public void addDummyContest() {
		JSONObject json1 = new JSONObject();
		json1.put("totalPlayers", 2);
		json1.put("remainingSpots", 2);
		json1.put("winningPrice", 35);
		json1.put("tempId", UUID.randomUUID().toString());
		json1.put("joinedParticipants", "");
		
		//creating a list of template contests in the list
		unifiedJedis.lpush("17SEPT2022_CONTEST1_35", json1.toString());
		
		json1.put("tempId", UUID.randomUUID().toString());
		unifiedJedis.lpush("17SEPT2022_CONTEST1_35", json1.toString());
	}
	
	@GetMapping("/addUserInContest/{dummyContestId}/{userId}")
	public void addUserInContest(@PathVariable String dummyContestId, @PathVariable String userId) {
		JSONObject json1 = new JSONObject();
		json1.put("totalPlayers", 2);
		json1.put("remainingSpots", 2);
		json1.put("winningPrice", 35);
		json1.put("tempId", UUID.randomUUID().toString().replaceAll("-", ""));
		json1.put("joinedParticipants", "");
		
		//creating json of the user that is going to be added in the contest joined list.
		JSONObject userJson = new JSONObject();
		userJson.put("userId", userId);
		userJson.put("otherValues", "something");
		userJson.put("portfolio", "p" + userId);
		
		
		//add some code to check if the user is first to join or not
		
		//if the user is first to join do below stuff.
		JSONObject contestJson = new JSONObject(unifiedJedis.lpop(dummyContestId));
		if(contestJson != null) {
			int remainingSpots = contestJson.getInt("remainingSpots");
			contestJson.put("remainingSpots", --remainingSpots);
			unifiedJedis.set("contest:" + contestJson.getString("tempId"), contestJson.toString()); // this set is the primary redis set for storing the contest details and to keep track of the remaining participant spots.
			Map<String, String> contestMap = new HashMap<String, String>();
			contestMap.put(userId, userJson.toString());
			unifiedJedis.hset("hash:" + contestJson.getString("tempId"), contestMap);
			
			// storing portfolio of user in a hashset and storing the points in leaderboard.
			Map<String, String> stocksMap = new HashMap<String, String>();
			JSONObject stock1 = new JSONObject();
			stock1.put("stockId", "1");
			stock1.put("stockName", "REL");
			stock1.put("purchaseType", "BUY");
			
			JSONObject stock2 = new JSONObject();
			stock2.put("stockId", "3");
			stock2.put("stockName", "HDFCBNK");
			stock2.put("purchaseType", "SELL");
			
			stocksMap.put("1", stock1.toString());
			stocksMap.put("3", stock2.toString());
			
			unifiedJedis.hset(userJson.get("portfolio").toString(), stocksMap);
			Map<String, Double> scoreMap = new HashMap<String, Double>();
			
			//keep 0 points by default.
			scoreMap.put(userId, 0d);
			unifiedJedis.zadd("leaders:" + contestJson.getString("tempId"), scoreMap);
		}
	}
	
	@GetMapping("/addUserInExistingContest/{dummyContestId}/{userId}")
	public String addUserInExistingContest(@PathVariable String dummyContestId, @PathVariable String userId) {
		if(unifiedJedis.get("contest:" + dummyContestId) != null) {
			JSONObject contestJson = new JSONObject(unifiedJedis.get("contest:" + dummyContestId));
			if(contestJson != null && contestJson.getInt("remainingSpots") <= 0) {
				return "contest already full join another one.";
			}
			else if(contestJson != null && contestJson.getInt("remainingSpots") > 0) {
				int remainingSpots = contestJson.getInt("remainingSpots");
				contestJson.put("remainingSpots", --remainingSpots);
				unifiedJedis.set("contest:" + contestJson.getString("tempId"), contestJson.toString()); //updating the remaining spots asap to provide accurate info on available spots left.
				
				//adding user in the existing contest.
				JSONObject userJson = new JSONObject();
				userJson.put("userId", userId);
				userJson.put("otherValues", "something");
				userJson.put("portfolio", "p" + userId);
				Map<String, String> contestMap = unifiedJedis.hgetAll("hash:" + dummyContestId);
				contestMap.put(userId, userJson.toString());
				unifiedJedis.hset("hash:" + dummyContestId, contestMap);
				
				Map<String, String> stocksMap = new HashMap<String, String>();
				JSONObject stock1 = new JSONObject();
				stock1.put("stockId", "2");
				stock1.put("stockName", "HDFC");
				stock1.put("purchaseType", "SELL");
				
				JSONObject stock2 = new JSONObject();
				stock2.put("stockId", "6");
				stock2.put("stockName", "ICICI");
				stock2.put("purchaseType", "BUY");
				
				stocksMap.put("2", stock1.toString());
				stocksMap.put("6", stock2.toString());
				
				unifiedJedis.hset(userJson.get("portfolio").toString(), stocksMap);
				Map<String, Double> scoreMap = new HashMap<String, Double>();
				
				//keep 0 points by default.
				scoreMap.put(userId, 0d);
				unifiedJedis.zadd("leaders:" + contestJson.getString("tempId"), scoreMap);
				
				return "User added in the existing contest.";
			}
		}
		return "No contest found, please try again.";
	}

	@GetMapping("/updatePortfolioScore/{dummyContestId}/{userId}")
	public void updateValue(@PathVariable String dummyContestId, @PathVariable String userId) {
		unifiedJedis.zadd("leaders:" + dummyContestId, 500, userId);
	}
}
