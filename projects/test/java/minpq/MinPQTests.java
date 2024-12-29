package minpq;

import org.eclipse.jetty.util.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MinPQTests {
    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract <E> MinPQ<E> createMinPQ();

    @Test
    public void wcagIndexAsPriority() throws FileNotFoundException {
        File inputFile = new File("data/wcag.tsv");
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        MinPQ<String> testing = createMinPQ();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            int index = Integer.parseInt(line[0].replace(".", ""));
            String title = line[1];
            reference.add(title, index);
            testing.add(title, index);
        }
        while (!reference.isEmpty()) {
            assertEquals(reference.removeMin(), testing.removeMin());
        }
        assertTrue(testing.isEmpty());
    }

    @Test
    public void randomPriorities() {
        int[] elements = new int[1000];
        for (int i = 0; i < elements.length; i = i + 1) {
            elements[i] = i;
        }
        Random random = new Random(373);
        int[] priorities = new int[elements.length];
        for (int i = 0; i < priorities.length; i = i + 1) {
            priorities[i] = random.nextInt(priorities.length);
        }

        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();
        for (int i = 0; i < elements.length; i = i + 1) {
            reference.add(elements[i], priorities[i]);
            testing.add(elements[i], priorities[i]);
        }

        for (int i = 0; i < elements.length; i = i+1) {
            int expected = reference.removeMin();
            int actual = testing.removeMin();

            if (expected != actual) {
                int expectedPriority = priorities[expected];
                int actualPriority = priorities[actual];
                assertEquals(expectedPriority, actualPriority);
            }
        }
    }

    @Test
    public void randomIntegersRandomPriorities() {
        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();

        int iterations = 10000;
        int maxElement = 1000;
        Random random = new Random();
        for (int i = 0; i < iterations; i += 1) {
            int element = random.nextInt(maxElement);
            double priority = random.nextDouble();
            reference.addOrChangePriority(element, priority);
            testing.addOrChangePriority(element, priority);
            assertEquals(reference.peekMin(), testing.peekMin());
            assertEquals(reference.size(), testing.size());
            for (int e = 0; e < maxElement; e += 1) {
                if (reference.contains(e)) {
                    assertTrue(testing.contains(e));
                    assertEquals(reference.getPriority(e), testing.getPriority(e));
                } else {
                    assertFalse(testing.contains(e));
                }
            }
        }
        for (int i = 0; i < iterations; i += 1) {
            boolean shouldRemoveMin = random.nextBoolean();
            if (shouldRemoveMin && !reference.isEmpty()) {
                assertEquals(reference.removeMin(), testing.removeMin());
            } else {
                int element = random.nextInt(maxElement);
                double priority = random.nextDouble();
                reference.addOrChangePriority(element, priority);
                testing.addOrChangePriority(element, priority);
            }
            if (!reference.isEmpty()) {
                assertEquals(reference.peekMin(), testing.peekMin());
                assertEquals(reference.size(), testing.size());
                for (int e = 0; e < maxElement; e += 1) {
                    if (reference.contains(e)) {
                        assertTrue(testing.contains(e));
                        assertEquals(reference.getPriority(e), testing.getPriority(e));
                    } else {
                        assertFalse(testing.contains(e));
                    }
                }
            } else {
                assertTrue(testing.isEmpty());
            }
        }
    }

    @Test
    public void randWCAGTags() throws IOException {
        MinPQ<String> pq = createMinPQ();

        List<String> wcagTags = readWCAGTagsFromFile();

        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            String tag = wcagTags.get(random.nextInt(wcagTags.size()));
            double priority = random.nextDouble();
            pq.addOrChangePriority(tag, priority);
        }

        if (!pq.isEmpty()) {
            System.out.println("First tag in MinPQ: " + pq.peekMin());
        }

        while (!pq.isEmpty()) {
            System.out.println("Removed: " + pq.removeMin());
        }
    }

    // Helper method to read WCAG tags from wcag.tsv file
    private List<String> readWCAGTagsFromFile() throws IOException {
        List<String> wcagTags = new ArrayList<>();
        File inputFile = new File("data/wcag.tsv");
        Scanner scanner = new Scanner(inputFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\t");
            if (parts.length > 1) {
                String tag = "wcag" + parts[0].replace(".", "");
                wcagTags.add(tag);
            }
        }
        scanner.close();
        return wcagTags;
    }

    @Test
    public void randWCAGTags2() throws IOException {
        MinPQ<String> pq = createMinPQ();

        List<String> wcagTags = readWCAGTagsFromFile();

        List<String> topTags = Arrays.asList("wcag412", "wcag258", "wcag244");

        List<String> weightedTags = new ArrayList<>(wcagTags);

        for (String topTag : topTags) {
            for (int i = 0; i < 5; i++) {
                weightedTags.add(topTag);
            }
        }

        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            String tag = weightedTags.get(random.nextInt(weightedTags.size()));
            double priority = random.nextDouble();
            pq.addOrChangePriority(tag, priority);
        }

        if (!pq.isEmpty()) {
            System.out.println("First tag in MinPQ: " + pq.peekMin());
        }

        while (!pq.isEmpty()) {
            System.out.println("Removed: " + pq.removeMin());
        }
    }
}
