package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        //TODO Task 1.1 - 5 marks

        // named parts of the HTML for readability.
        final String TITLE_TAG = "<title>";
        final String TITLE_END_TAG = "</title>";
        final String SECTION_SEPARATOR = " | ";

        if (_htmlCode.contains(TITLE_TAG) && _htmlCode.contains(TITLE_END_TAG)) {
            int titleStartIndex = _htmlCode.indexOf(TITLE_TAG) + TITLE_TAG.length();
            int titleEndIndex = _htmlCode.indexOf(SECTION_SEPARATOR, titleStartIndex);
            return _htmlCode.substring(titleStartIndex, titleEndIndex);
        }

        return "Title not found!";
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        //TODO Task 1.2 - 5 marks

        final String BODY_KEY = "\"articleBody\":";

        if (_htmlCode.contains(BODY_KEY)) {
            int keyIndex = _htmlCode.indexOf(BODY_KEY) + BODY_KEY.length();
            int bodyStartIndex = _htmlCode.indexOf('"', keyIndex) + 1;
            String htmlOnlyEssentialQuotes = _htmlCode.replace("\\\"", "  ");
            int bodyEndIndex = htmlOnlyEssentialQuotes.indexOf('"', bodyStartIndex) - 1;

            return _htmlCode.substring(bodyStartIndex, bodyEndIndex).toLowerCase();
        }

        return "Content not found!";
    }


}
