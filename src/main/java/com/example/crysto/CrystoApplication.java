package com.example.crysto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import io.swagger.v3.core.util.Json;
import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
public class CrystoApplication {
	/*
	@Autowired
	private UnifiedJedis unifiedJedis;

	@Bean
	CommandLineRunner loadData() {
		return args -> {

			// storng the price and point of each stock in redis
			unifiedJedis.hset("1", Map.of("stockname", "REL","openingPrice", "100", "currentPoints", "50"));
			unifiedJedis.hset("2", Map.of("stockname", "HDFC","openingPrice", "10", "currentPoints", "15"));
			unifiedJedis.hset("3", Map.of("stockname", "HDFCBNK","openingPrice", "50", "currentPoints", "-25"));
			unifiedJedis.hset("4", Map.of("stockname", "AIRTEL","openingPrice", "500", "currentPoints", "35"));
			unifiedJedis.hset("5", Map.of("stockname", "BAJAJ","openingPrice", "100", "currentPoints", "-50"));
			unifiedJedis.hset("6", Map.of("stockname", "ICICI","openingPrice", "150", "currentPoints", "70"));
			unifiedJedis.hset("7", Map.of("stockname", "ASIAN","openingPrice", "330", "currentPoints", "30"));
			unifiedJedis.hset("8", Map.of("stockname", "TITAN","openingPrice", "220", "currentPoints", "-20"));
			unifiedJedis.hset("9", Map.of("stockname", "TCS","openingPrice", "100", "currentPoints", "0"));
			unifiedJedis.hset("10", Map.of("stockname", "INFY","openingPrice", "650", "currentPoints", "10"));
			
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
			
			//user is now trying to join the contest.
			//scenario 1 : There are no users in the contest.
			
			if(!checkIfContestHasUsers("CONTEST1")) {
				setUserInNewContest("17SEPT2022_CONTEST1_35", json1, "101");
			}
			
			//scenario 2 : existing users in the contest.
			if(checkIfContestHasUsers("CONTEST1")) {
				JSONObject existingContest = new JSONObject(unifiedJedis.get("CONTEST1"));
				int spots = (int) existingContest.get("remainingSpots");
				if(spots == 0) {
					unifiedJedis.del("CONTEST1");
					setUserInNewContest("17SEPT2022_CONTEST1_35", json1, "102");
				}
				else {
					existingContest.put("remainingSpots", --spots);
					unifiedJedis.set("CONTEST1", existingContest.toString()); //updating this here in redis just to push the details about a new joinee and therefore preventing false entries.
					String contestParticipants = existingContest.get("joinedParticipants").toString();
					contestParticipants = contestParticipants + ",103";
					existingContest.put("joinedParticipants", contestParticipants.toString());
					unifiedJedis.set("CONTEST1", existingContest.toString());
				}
			}
			
		};
	}
	
	public boolean checkIfContestHasUsers(String contestId) {
		return unifiedJedis.get("CONTEST1") != null;
	}
	
	public void setUserInNewContest(String dummyContestid, JSONObject json1, String userId) {
		JSONObject contestJson = new JSONObject(unifiedJedis.lpop(dummyContestid));
		String contestParticipants = contestJson.get("joinedParticipants").toString();
		if(contestJson.get("joinedParticipants").toString().isEmpty()) {
			contestParticipants = userId;//dummy participant id/ user id
		}
		else {
			contestParticipants = contestParticipants + "," + userId;//dummy participant id/ user id
		}
		contestJson.put("joinedParticipants", contestParticipants.toString());
		contestJson.put("remainingSpots", 1);
		unifiedJedis.set("CONTEST1", contestJson.toString());
		
		//adding the dummy contest again to keep the count at 5 for in memory contests
		json1.put("tempId", UUID.randomUUID().toString());
		unifiedJedis.lpush(dummyContestid, json1.toString());
	}
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(CrystoApplication.class, args);
	}

}
