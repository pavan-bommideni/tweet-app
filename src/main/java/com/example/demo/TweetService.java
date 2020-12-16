package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class TweetService {

	private static final String TWEET_DELIMITER = "#";

	@Value("${top-tweets-count}")
	private int topTweetsCount;

	public void acceptInputAndDisplayTopHashTags() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of tweets to be processed: ");
		int n = scanner.nextInt();
		System.out.println("Enter " + n + " tweets\n");

		List<String> inputLines = new ArrayList<>();
		for (int i = 0; i <= n; i++) {
			String str = scanner.nextLine();
			inputLines.add(str);
		}

		scanner.close();

		List<String> hashTags = new ArrayList<>();
		inputLines.parallelStream().forEach(line -> {
			List<String> lineWords = Arrays.asList(line.split(" "));
			lineWords.parallelStream().forEach(word -> {
				if (word.startsWith(TWEET_DELIMITER))
					hashTags.addAll(Arrays.asList(word.split(TWEET_DELIMITER)));
			});
		});

		Map<String, Long> tagsVsCount = hashTags.stream().filter(tag -> !StringUtils.isEmpty(tag))
				.collect(Collectors.groupingBy(tag -> tag, Collectors.counting())).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(topTweetsCount)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

		if (!CollectionUtils.isEmpty(tagsVsCount)) {
			System.out.println("Top " + topTweetsCount + " Hashtags and count are displayed below:");
			tagsVsCount.keySet().forEach(tag -> System.out.println("Tag: " + tag + " Count: " + tagsVsCount.get(tag)));
		} else {
			System.out.println("Top " + topTweetsCount + " Hashtags and count are not available.");
		}

	}
}
