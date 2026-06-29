package encryptor;

/*
@author anonymous6291
Github:   https://github.com/anonymous6291
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("ClassWithoutLogger")
public class RemoveChilds {

    public static final String FILE_SEPARATOR_PATTERN = Pattern.quote(File.separator);

    public static void remove(List<File> files) {
        if (files == null) {
            return;
        }
        Trie root = new Trie();
        for (File f : files) {
            addToTrie(root, f.toString().split(FILE_SEPARATOR_PATTERN));
        }
        List<String> res = new ArrayList<>(1);
        retrieveFromTrie(root, "", File.separator, res);
        files.clear();
        for (String path : res) {
            files.add(new File(path));
        }
    }

    @SuppressWarnings("NestedAssignment")
    private static void addToTrie(Trie root, String vals[]) {
        if (vals == null) {
            return;
        }
        Trie curr = root;
        for (String s : vals) {
            Trie nxt = curr.childs.get(s);
            if (nxt == null) {
                curr.childs.put(s, nxt = new Trie());
            }
            curr = nxt;
        }
        curr.setPathEnd();
    }

    private static void retrieveFromTrie(Trie root, String prefix, String separator, List<String> list) {
        if (root == null) {
            return;
        }
        if (root.pathend) {
            list.add(prefix);
            return;
        }
        if (root.childs == null) {
            return;
        }
        for (String texts : root.childs.keySet()) {
            retrieveFromTrie(root.childs.get(texts), prefix.concat(separator).concat(texts), separator, list);
        }
    }

    private RemoveChilds() {
    }

    private static class Trie {

        Map<String, Trie> childs;
        boolean pathend;

        Trie() {
            childs = new HashMap<>(1);
            pathend = false;
        }

        void setPathEnd() {
            pathend = true;
        }
    }
}
