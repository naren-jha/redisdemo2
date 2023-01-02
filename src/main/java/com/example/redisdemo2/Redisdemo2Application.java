package com.example.redisdemo2;

import com.example.redisdemo2.applications.leaderboard.LeaderboardService;
import com.example.redisdemo2.service.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class Redisdemo2Application {

	@Autowired
	private ValueOperationService valueOperationService;

	@Autowired
	private HashOperationService hashOperationService;

	@Autowired
	private ListOperationService listOperationService;

	@Autowired
	private SetOperationService setOperationService;

	@Autowired
	private ZSetOperationService zSetOperationService;

	@Autowired
	private LeaderboardService leaderboardService;

	public static void main(String[] args) {
		SpringApplication.run(Redisdemo2Application.class, args);
	}

	@PostConstruct
	public void start() {
		// === Value operations ===
		valueOperationService.addOrUpdate("bob", "1");
		valueOperationService.addOrUpdate("alice", "2");

		Object val = valueOperationService.get("bob");
		log.info(val.toString()); // 1
		val = valueOperationService.get("alice");
		log.info(val.toString()); // 2

		valueOperationService.addWithTtl("rohan", "3", 10); // this entry will expire after 10 secs



		// === Hash Operations ===
		String hashKey = "userLogin";
		hashOperationService.setHashField(hashKey, "rohan", "19-dec-2022");
		hashOperationService.setHashField(hashKey, "joe", "21-dec-2022");
		hashOperationService.setHashField(hashKey, "sunil", "12-dec-2022");
		hashOperationService.setHashField(hashKey, "chhetri", "16-dec-2022");
		hashOperationService.setHashField(hashKey, "katie", "23-dec-2022");
		hashOperationService.setHashField(hashKey, "jacob", "26-dec-2022");

		Map<Object, Object> userLoginActivityCache = hashOperationService.getAllHashFields(hashKey);
		log.info(userLoginActivityCache.toString()); // {rohan=19-dec-2022, joe=21-dec-2022, sunil=12-dec-2022, chhetri=16-dec-2022, katie=23-dec-2022, jacob=26-dec-2022}

		Object joeLastLogin = hashOperationService.getHashField(hashKey, "joe");
		log.info(joeLastLogin.toString()); // 21-dec-2022

		hashOperationService.deleteHashField(hashKey, "katie", "jacob"); // deletes entries for "katie" and "jacob" from "userLogin"
		userLoginActivityCache = hashOperationService.getAllHashFields(hashKey);
		log.info(userLoginActivityCache.toString()); // {rohan=19-dec-2022, joe=21-dec-2022, sunil=12-dec-2022, chhetri=16-dec-2022}



		// === List Operations ===
		String listKey = "productIds";
		listOperationService.addToList(listKey, "0");
		listOperationService.addToList(listKey, "1");
		listOperationService.addToList(listKey, "1");
		listOperationService.addToList(listKey, "2");
		listOperationService.addToList(listKey, "2");
		listOperationService.addToList(listKey, "3");
		listOperationService.addToList(listKey, "4");

		List<Object> allProdIdsFromList = listOperationService.getAllFromList(listKey);
		log.info(allProdIdsFromList.toString()); // [4, 3, 2, 2, 1, 1, 0]

		List<Object> listRange = listOperationService.getListRange(listKey, 0, 2);
		log.info(listRange.toString()); // [4, 3, 2]
		listRange = listOperationService.getListRange(listKey, 0, 4);
		log.info(listRange.toString()); // [4, 3, 2, 2, 1]
		listRange = listOperationService.getListRange(listKey, 2, 5);
		log.info(listRange.toString()); // [2, 2, 1, 1]

		listOperationService.deleteFromList(listKey, 2, "1"); // remove 2 occurrences of 1 from the list
		allProdIdsFromList = listOperationService.getAllFromList(listKey);
		log.info(allProdIdsFromList.toString()); // [4, 3, 2, 2, 0]

		Object lastElementOfList = listOperationService.getFromList(listKey);
		log.info(lastElementOfList.toString()); // 0
		lastElementOfList = listOperationService.getFromList(listKey);
		log.info(lastElementOfList.toString()); // 2
		allProdIdsFromList = listOperationService.getAllFromList(listKey);
		log.info(allProdIdsFromList.toString()); // [4, 3, 2]



		// === Set Operations ===
		String setKey = "card";
		setOperationService.addToSet(setKey, "iphone", "lamp", "guitar");
		Set<Object> cardItems = setOperationService.getSetMembers(setKey);
		log.info(cardItems.toString()); // [iphone, guitar, lamp]

		// add some more items including some duplicate items
		setOperationService.addToSet(setKey, "iphone", "diary", "speaker");
		cardItems = setOperationService.getSetMembers(setKey); // [iphone, diary, guitar, lamp, speaker]
		log.info(cardItems.toString());

		setOperationService.removeFromSet(setKey, "diary", "speaker");
		cardItems = setOperationService.getSetMembers(setKey);
		log.info(cardItems.toString()); // [lamp, guitar, iphone]



		// === Sorted Set Operations ===
		String zSetKey = "pageView";
		zSetOperationService.addToZSet(zSetKey, "home", 2);
		zSetOperationService.addToZSet(zSetKey, "search", 100);
		zSetOperationService.addToZSet(zSetKey, "contact", 5);
		zSetOperationService.addToZSet(zSetKey, "career", 10);

		Set<Object> pagesSortedByViewCount = zSetOperationService.getAllKeys(zSetKey);
		log.info(pagesSortedByViewCount.toString());

		Set<ZSetOperations.TypedTuple<Object>> pageToViewCount = zSetOperationService.getAllKeyValues(zSetKey);
		log.info(pageToViewCount.toString()); // [DefaultTypedTuple [score=2.0, value=home], DefaultTypedTuple [score=5.0, value=contact], DefaultTypedTuple [score=10.0, value=career], DefaultTypedTuple [score=100.0, value=search]]
		for (ZSetOperations.TypedTuple<Object> tuple : pageToViewCount) {
			System.out.println(tuple.getValue() + ": " + tuple.getScore());
		}
//		home: 2.0
//		contact: 5.0
//		career: 10.0
//		search: 100.0

		zSetOperationService.removeFromZSet(zSetKey, "home", "contact");
		pageToViewCount = zSetOperationService.getAllKeyValues(zSetKey);
		for (ZSetOperations.TypedTuple<Object> tuple : pageToViewCount) {
			System.out.println(tuple.getValue() + ": " + tuple.getScore());
		}
//		career: 10.0
//		search: 100.0



		// === Leaderboard example using sorted set===
		String leaderboardName = "leagues:2022";
		leaderboardService.addScore(leaderboardName, "chirkut", 5000);
		leaderboardService.addScore(leaderboardName, "carry", 1000);
		leaderboardService.addScore(leaderboardName, "jack", 100);
		leaderboardService.addScore(leaderboardName, "dorthy", 300);
		leaderboardService.addScore(leaderboardName, "minty", 200);
		leaderboardService.addScore(leaderboardName, "monu", 500);
		leaderboardService.addScore(leaderboardName, "kaju", 4000);
		leaderboardService.addScore(leaderboardName, "emli", 2000);
		leaderboardService.addScore(leaderboardName, "katie", 1500);
		leaderboardService.addScore(leaderboardName, "grandi", 2200);
		Set<ZSetOperations.TypedTuple<Object>> playerAndScores = leaderboardService.getAllPlayerAndScores(leaderboardName);
		for (ZSetOperations.TypedTuple<Object> tuple : playerAndScores) {
			System.out.println(tuple.getValue() + ": " + tuple.getScore());
		}
		/*
		jack: 100.0
		minty: 200.0
		dorthy: 300.0
		monu: 500.0
		carry: 1000.0
		katie: 1500.0
		emli: 2000.0
		grandi: 2200.0
		kaju: 4000.0
		chirkut: 5000.0
		 */

		Long rank = leaderboardService.getRank(leaderboardName, "jack");
		log.info(rank.toString()); // 9

		Double score = leaderboardService.getPlayerScore(leaderboardName, "grandi");
		log.info(score.toString()); // 2200.0
	}
}
