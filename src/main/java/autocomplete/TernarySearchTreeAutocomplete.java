package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {

        for (CharSequence individual : terms) {

            overallRoot = put(overallRoot, individual, 0);

        }
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {

        List<CharSequence> result = new ArrayList<>();
        Node root = get(overallRoot, prefix, 0);

        if (root == null) {

            return result;
        }

        if (root.isTerm) {

            result.add(prefix);
        }

        collect(root.mid, prefix.toString(), result);
        return result;

    }

    private Node get(Node root, CharSequence pref, int index) {

        if (root == null) {

            return null;

        }

        char c = pref.charAt(index);

        if (c < root.data) {

            return get(root.left, pref, index);

        } else if (c > root.data) {

            return get(root.right, pref, index);

        } else if (index < pref.length() - 1) {

            return get(root.mid, pref, index + 1);

        } else {

            return root;

        }
    }

    private Node put(Node root, CharSequence individual, int index) {

        char c = individual.charAt(index);

        if (root == null) {

            root = new Node(c);

        }

        if (c < root.data) {

            root.left = put(root.left, individual, index);

        } else if (c > root.data) {

            root.right = put(root.right, individual, index);

        } else if (index < individual.length() - 1) {

            root.mid = put(root.mid, individual, index + 1);

        } else {

            root.isTerm = true;

        }

        return root;

    }

    private void collect(Node root, String pref, List<CharSequence> result) {
        if (root != null) {

            collect(root.left, pref, result);

            String newP = pref + root.data;

            if (root.isTerm) {

                result.add(newP);

            }

            collect(root.mid, newP, result);

            collect(root.right, pref, result);
        }
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
