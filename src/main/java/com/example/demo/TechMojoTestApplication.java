package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class TechMojoTestApplication {

	public static void main(String[] args) {
		acceptInputAndDisplayTop10HashTags();
		SpringApplication.run(TechMojoTestApplication.class, args);
	}

	public static void acceptInputAndDisplayTop10HashTags() {
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
				if (word.startsWith("#"))
					hashTags.addAll(Arrays.asList(word.split("#")));
			});
		});

		Map<String, Long> tagsVsCount = hashTags.stream().filter(tag -> !StringUtils.isEmpty(tag))
				.collect(Collectors.groupingBy(tag -> tag, Collectors.counting())).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		if (!CollectionUtils.isEmpty(tagsVsCount)) {
			System.out.println("Top 10 Hashtags and count are displayed below:");
			tagsVsCount.keySet().forEach(tag -> System.out.println("Tag: " + tag + " Count: " + tagsVsCount.get(tag)));
		} else {
			System.out.println("Top 10 Hashtags and count are not available.");
		}

	}

}
