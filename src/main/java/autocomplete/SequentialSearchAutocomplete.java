package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();

        if (prefix == null || prefix.length() == 0) {

            return result;
        }

        for (CharSequence individual : elements) {

            if (individual.length() >= prefix.length()) {

                boolean match = true;

                for (int i = 0; i < prefix.length(); i++) {

                    if (prefix.charAt(i) != individual.charAt(i)) {

                        match = false;
                    }
                }

                if (match) {
                    result.add(individual);
                }
            }
        }

        return result;
    }
}
