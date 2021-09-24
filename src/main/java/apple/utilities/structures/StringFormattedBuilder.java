package apple.utilities.structures;

import apple.utilities.lamdas.ObjectSupplier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class StringFormattedBuilder {
    private final List<Pair<Integer, String>> stringMatched;
    private final String string;
    private final ObjectSupplier[] argsSupplier;

    public StringFormattedBuilder(String string, ObjectSupplier... argsSupplier) {
        this.string = string.replace("%", "%%");
        this.argsSupplier = argsSupplier;
        MatchResult matchResult = Pattern.compile("[{][0-9]*[}]")
                .matcher(this.string).toMatchResult();
        List<Pair<Integer, String>> results = new ArrayList<>();
        String result;
        for (int i = 0; i < matchResult.groupCount(); i++) {
            results.add(new Pair<>(matchResult.start(i), matchResult.group(i)));
        }
        results.sort(Comparator.comparingInt(Pair::getKey));
        this.stringMatched = results;
    }

    public String getString() {
        StringBuilder s = new StringBuilder();
        int lastMatch = 0;
        Object[] argsSupplied = new Object[argsSupplier.length];
        for (int i = 0; i < argsSupplier.length; i++)
            argsSupplied[i] = argsSupplier[i].get();
        Object[] args = new Object[stringMatched.size()];
        int i = 0;
        for (Pair<Integer, String> match : stringMatched) {
            s.append(string, lastMatch, lastMatch = match.getKey());

            int index = match.getKey();
            while (string.length() > index && string.charAt(index++) != '}') ;
            int objectIndex = Integer.parseInt(string.substring(lastMatch + 1, index - 1));
            s.append("%");
            lastMatch = index;
            args[i++] = argsSupplied[objectIndex];
        }
        s.append(string, lastMatch, string.length());
        return String.format(s.toString(), args);
    }

    @Override
    public String toString() {
        return this.getString();
    }
}
