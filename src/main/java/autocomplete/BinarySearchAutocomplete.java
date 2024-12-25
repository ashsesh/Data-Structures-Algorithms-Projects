package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
        Collections.sort(elements, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();

        if (prefix == null || prefix.length() == 0) {

            return result;
        }

        int index = Collections.binarySearch(elements, prefix, CharSequence::compare);

        if (index < 0) {

            index = -(index + 1);
        }

        while (index < elements.size()) {

            CharSequence individual = elements.get(index);

            if (individual.length() >= prefix.length() && individual.subSequence(0, prefix.length()).equals(prefix)) {

                result.add(individual);
                index++;

            } else {

                return result;
            }
        }

        return result;
    }
}
