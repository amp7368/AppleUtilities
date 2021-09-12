package apple.utilities.util;

import java.util.function.Consumer;

public class FuzzyStringMatcher {
    private final char[] aChars;
    private char[] bChars;
    private boolean caseInsensitive = false;
    private boolean aContainsB = false;
    private FuzzyCharacter[] best;

    public FuzzyStringMatcher(String aChars, Flag... flags) {
        this.aChars = aChars.toCharArray();
        for (Flag flag : flags) {
            flag.modify(this);
        }
    }

    public int operationsToMatch(String other, int limit) {
        initMatcher(other);
        if (aChars.length == 0 || bChars.length == 0) return -1;
        int operations = 0;
        int fails = fails(best);
        while (fails != 0) {
            if (operations == limit) return -1;
            FuzzyCharacter[] left = copyBest();
            removeLeft(left);
            FuzzyCharacter[] right = copyBest();
            removeRight(right);
            FuzzyCharacter[] iLeft = copyBest();
            insertLeft(iLeft);
            FuzzyCharacter[] iRight = copyBest();
            insertRight(iRight);
            int originalFails = fails;
            FuzzyCharacter[] replace = copyBest();
            replaceChar(replace);
            NumberUtils.Scored<FuzzyCharacter[]> highest = NumberUtils.getHighest(null, fails,
                    new FuzzyScoreable(left),
                    new FuzzyScoreable(right),
                    new FuzzyScoreable(iLeft),
                    new FuzzyScoreable(iRight),
                    new FuzzyScoreable(replace)
            );
            this.best = highest.val();
            fails = highest.score();

            if (originalFails == fails) {
                return -1;
            }
            operations++;
        }
        return operations;
    }

    private void initMatcher(String other) {
        bChars = other.toCharArray();
        best = new FuzzyCharacter[aChars.length];
        if (aChars.length == 0 || bChars.length == 0) return;
        int bOffset;
        for (bOffset = 0; aChars[bOffset] != bChars[0]; ) {
            if (aChars.length - bChars.length <= ++bOffset) break;
        }
        for (int i = 0; i < aChars.length; i++) {
            best[i] = new FuzzyCharacter(i, i - bOffset);
        }
    }

    private void removeRight(FuzzyCharacter[] cs) {
        int i;
        for (i = 0; i < cs.length; i++) {
            if (!cs[i].isMatch) {
                break;
            }
        }
        for (i++; i < cs.length; i++) {
            cs[i].shift(-1);
        }
    }

    private void removeLeft(FuzzyCharacter[] cs) {
        int i;
        for (i = 0; i < cs.length; i++) {
            if (!cs[i].isMatch) {
                break;
            }
        }
        for (i++; i < cs.length; i++) {
            cs[i].shift(1);
        }
    }

    private void insertRight(FuzzyCharacter[] cs) {
        int i;
        for (i = 0; i < cs.length; i++) {
            if (!cs[i].isMatch) {
                break;
            }
        }
        for (int j = cs.length - 1; j > i; j--) {
            cs[j].shift(-1);
        }
        cs[i].override(aChars[i]);
    }

    private void insertLeft(FuzzyCharacter[] cs) {
        int i;
        for (i = cs.length - 1; i > 0; i--) {
            if (!cs[i].isMatch) {
                break;
            }
        }
        for (int j = 0; j < i; j++) {
            cs[j].shift(1);
        }
        cs[i].override(aChars[i]);
    }

    private void replaceChar(FuzzyCharacter[] cs) {
        for (int i = cs.length - 1; i > 0; i--) {
            if (!cs[i].isMatch) {
                cs[i].override(aChars[i]);
                return;
            }
        }
    }

    private FuzzyCharacter[] copyBest() {
        FuzzyCharacter[] copy = new FuzzyCharacter[best.length];
        for (int i = 0; i < best.length; i++) {
            copy[i] = new FuzzyCharacter(best[i]);
        }
        return copy;
    }

    private int fails(FuzzyCharacter[] cs) {
        int fails = 0;
        for (FuzzyCharacter c : cs) {
            if (!c.isMatch) fails++;
        }
        return fails;
    }

    private boolean matchCharacter(char a, char b) {
        if (caseInsensitive)
            return Character.toLowerCase(a) == Character.toLowerCase(b);
        return a == b;
    }

    public enum Flag {
        CASE_INSENSITIVE(o -> o.caseInsensitive = true),
        CONTAINS(o -> o.aContainsB = true);

        private final Consumer<FuzzyStringMatcher> modify;

        Flag(Consumer<FuzzyStringMatcher> modify) {
            this.modify = modify;
        }

        public void modify(FuzzyStringMatcher matcher) {
            modify.accept(matcher);
        }
    }

    private class FuzzyScoreable implements NumberUtils.Scoreable<FuzzyCharacter[]> {
        private final FuzzyCharacter[] chars;

        public FuzzyScoreable(FuzzyCharacter[] chars) {
            this.chars = chars;
        }

        @Override
        public int getAsInt() {
            return fails(chars);
        }

        @Override
        public FuzzyCharacter[] get() {
            return chars;
        }
    }

    private class FuzzyCharacter {
        private int charAIndex;
        private int charBIndex;
        private boolean isMatch;
        private boolean overrideExists;
        private char override;

        public FuzzyCharacter(int charAIndex, int charBIndex) {
            this.charAIndex = charAIndex;
            this.charBIndex = charBIndex;
            this.overrideExists = false;
            doIsMatch();
        }

        public FuzzyCharacter(FuzzyCharacter other) {
            charAIndex = other.charAIndex;
            charBIndex = other.charBIndex;
            isMatch = other.isMatch;
            this.overrideExists = other.overrideExists;
            this.override = other.override;
        }

        public void shift(int shift) {
            charBIndex += shift;
            doIsMatch();
        }

        private void doIsMatch() {
            if (NumberUtils.between(0, charAIndex, aChars.length)) {
                if (overrideExists) {
                    isMatch = matchCharacter(aChars[this.charAIndex], override);
                } else if (NumberUtils.between(0, charBIndex, bChars.length)) {
                    isMatch = matchCharacter(aChars[this.charAIndex], bChars[this.charBIndex]);
                } else {
                    isMatch = aContainsB;
                }
            } else {
                isMatch = false;
            }
        }

        public void override(char override) {
            this.override = override;
            this.overrideExists = true;
            doIsMatch();
        }

        @Override
        public String toString() {
            return String.format("[%c-%c=%b]",
                    NumberUtils.between(0, charAIndex, aChars.length) ? aChars[charAIndex] : '?',
                    overrideExists ? override :
                            NumberUtils.between(0, charBIndex, bChars.length) ? bChars[charBIndex] : '?',
                    isMatch);
        }
    }
}
