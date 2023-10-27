package uob.oop;

public class NLP {
    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    public static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.1 - 3 marks

        for (char c : _content.toLowerCase().toCharArray()) {
            // use unicode decimal representations to check if character is in a correct range.
            boolean isDigit = '0' <= c && c <= '9';
            boolean isLetter = 'a' <= c && c <= 'z';
            boolean isWhiteSpace = c == ' ';
            if (isDigit || isLetter || isWhiteSpace)
                sbContent.append(c);
        }

        return sbContent.toString().trim();
    }

    /***
     * Text lemmatization. Delete 'ing', 'ed', 'es' and 's' from the end of the word.
     * @param _content Text that need to be lemmatized.
     * @return Lemmatized text.
     */
    public static String textLemmatization(String _content) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.2 - 3 marks

        final String[] ENDINGS = new String[] {"ing", "ed", "es", "s"};
        for (String word : _content.split(" ")) {
            for (String ending : ENDINGS) {
                boolean isEndingOfWord = word.length() > ending.length() &&
                        word.lastIndexOf(ending) + ending.length() == word.length();
                if (isEndingOfWord) {
                    word = word.substring(0, word.length() - ending.length());
                    break;
                }
            }
            sbContent.append(word).append(" ");
        }

        return sbContent.toString().trim();
    }

    /***
     * Remove stop-words from the text.
     * @param _content The original text.
     * @param _stopWords An array that contains stop-words.
     * @return Modified text.
     */
    public static String removeStopWords(String _content, String[] _stopWords) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.3 - 3 marks

        for (String word : _content.split(" ")) {
            boolean isStopWord = false;
            for (String stopWord : _stopWords) {
                if (word.equals(stopWord)) {
                    isStopWord = true;
                    break;
                }
            }
            if (!isStopWord)
                sbContent.append(word).append(" ");
        }

        return sbContent.toString().trim();
    }

}
