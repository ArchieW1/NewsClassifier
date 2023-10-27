package uob.oop;

import java.text.DecimalFormat;

public class NewsClassifier {
    public String[] myHTMLs;
    public String[] myStopWords = new String[127];
    public String[] newsTitles;
    public String[] newsContents;
    public String[] newsCleanedContent;
    public double[][] newsTFIDF;

    private final String TITLE_GROUP1 = "Osiris-Rex's sample from asteroid Bennu will reveal secrets of our solar system";
    private final String TITLE_GROUP2 = "Bitcoin slides to five-month low amid wider sell-off";

    public Toolkit myTK;

    public NewsClassifier() {
        myTK = new Toolkit();
        myHTMLs = myTK.loadHTML();
        myStopWords = myTK.loadStopWords();

        loadData();
    }

    public static void main(String[] args) {
        NewsClassifier myNewsClassifier = new NewsClassifier();

        myNewsClassifier.newsCleanedContent = myNewsClassifier.preProcessing();

        myNewsClassifier.newsTFIDF = myNewsClassifier.calculateTFIDF(myNewsClassifier.newsCleanedContent);

        //Change the _index value to calculate similar based on a different news article.
        double[][] doubSimilarity = myNewsClassifier.newsSimilarity(0);

        System.out.println(myNewsClassifier.resultString(doubSimilarity, 10));

        String strGroupingResults = myNewsClassifier.groupingResults(myNewsClassifier.TITLE_GROUP1, myNewsClassifier.TITLE_GROUP2);
        System.out.println(strGroupingResults);
    }

    public void loadData() {
        //TODO 4.1 - 2 marks
        newsTitles = new String[myHTMLs.length];
        newsContents = new String[myHTMLs.length];

        for (int i = 0; i < myHTMLs.length; i++) {
            newsTitles[i] = HtmlParser.getNewsTitle(myHTMLs[i]);
            newsContents[i] = HtmlParser.getNewsContent(myHTMLs[i]);
        }
    }

    public String[] preProcessing() {
        String[] myCleanedContent = new String[newsContents.length];
        //TODO 4.2 - 5 marks

        for (int i = 0; i < myCleanedContent.length; i++) {
            String cleaned = NLP.textCleaning(newsContents[i]);
            String cleanedLemmanized = NLP.textLemmatization(cleaned);
            myCleanedContent[i] = NLP.removeStopWords(cleanedLemmanized, myStopWords);
        }

        return myCleanedContent;
    }

    double TF(String word, String content) {
        double count = 0.0;
        String[] words = content.split(" ");
        for (String str : words) {
            if (word.equals(str)) {
                count++;
            }
        }
        return count / words.length;
    }

    double IDF(String word, String[] corpus) {
        double count = 0.0;
        for (String doc : corpus) {
            for (String str : doc.split(" ")) {
                if (word.equals(str)) {
                    count++;
                    break;
                }
            }
        }
        return Math.log(corpus.length/count) + 1;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        double[][] myTFIDF = new double[_cleanedContents.length][vocabularyList.length];

        //TODO 4.3 - 10 marks

        for (int i = 0; i < myTFIDF.length; i++) {
            for (int j = 0; j < myTFIDF[0].length; j++) {
                String word = vocabularyList[j];
                String content = _cleanedContents[i];
                myTFIDF[i][j] = TF(word, content) * IDF(word, _cleanedContents);
            }
        }

        return myTFIDF;
    }

    public String[] buildVocabulary(String[] _cleanedContents) {
        //TODO 4.4 - 10 marks

        String[] arrayVocabulary = new String[0];

        for (String sentence : _cleanedContents) {
            for (String word : sentence.split(" ")) {
                boolean isUnique = true;
                for (String str : arrayVocabulary) {
                    if (word.equals(str)) {
                        isUnique = false;
                        break;
                    }
                }
                if (isUnique) {
                    int newSize = arrayVocabulary.length + 1;
                    String[] scaledArray = new String[newSize];
                    System.arraycopy(arrayVocabulary, 0, scaledArray, 0, arrayVocabulary.length);
                    arrayVocabulary = scaledArray;
                    arrayVocabulary[arrayVocabulary.length - 1] = word;
                }
            }
        }

        return arrayVocabulary;
    }

    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = null;

        //TODO 4.5 - 15 marks


        return mySimilarity;
    }

    public String groupingResults(String _firstTitle, String _secondTitle) {
        int[] arrayGroup1 = null, arrayGroup2 = null;

        //TODO 4.6 - 15 marks


        return resultString(arrayGroup1, arrayGroup2);
    }

    public String resultString(double[][] _similarityArray, int _groupNumber) {
        StringBuilder mySB = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        for (int j = 0; j < _groupNumber; j++) {
            for (int k = 0; k < _similarityArray[j].length; k++) {
                if (k == 0) {
                    mySB.append((int) _similarityArray[j][k]).append(" ");
                } else {
                    String formattedCS = decimalFormat.format(_similarityArray[j][k]);
                    mySB.append(formattedCS).append(" ");
                }
            }
            mySB.append(newsTitles[(int) _similarityArray[j][0]]).append("\r\n");
        }
        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public String resultString(int[] _firstGroup, int[] _secondGroup) {
        StringBuilder mySB = new StringBuilder();
        mySB.append("There are ").append(_firstGroup.length).append(" news in Group 1, and ").append(_secondGroup.length).append(" in Group 2.\r\n").append("=====Group 1=====\r\n");

        for (int i : _firstGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }
        mySB.append("=====Group 2=====\r\n");
        for (int i : _secondGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }

        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

}
