import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Display the most commonly-reported WCAG recommendations.
 */
public class ReportAnalyzer {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("data/wcag.tsv");
        Map<String, String> wcagDefinitions = new LinkedHashMap<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            String index = "wcag" + line[0].replace(".", "");
            String title = line[1];
            wcagDefinitions.put(index, title);
        }

        Pattern re = Pattern.compile("wcag\\d{3,4}");
        List<String> wcagTags = Files.walk(Paths.get("data/reports"))
                .map(path -> {
                    try {
                        return Files.readString(path);
                    } catch (IOException e) {
                        return "";
                    }
                })
                .flatMap(contents -> re.matcher(contents).results())
                .map(MatchResult::group)
                .toList();

        Map<String, Integer> tagCountMap = new HashMap<>();
        for (String tag : wcagTags) {
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
        }

        List<Map.Entry<String, Integer>> topTags = new ArrayList<>(tagCountMap.entrySet());
        topTags.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        for (int i = 0; i < 3 && i < topTags.size(); i++) {
            Map.Entry<String, Integer> entry = topTags.get(i);
            String tag = entry.getKey();
            String description = wcagDefinitions.getOrDefault(tag, "N/A");
            System.out.println(tag + ": " + description);
        }
    }
}