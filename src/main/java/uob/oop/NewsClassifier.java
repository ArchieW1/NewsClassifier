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

    double TF(String _word, String _content) {
        double count = 0.0;
        String[] words = _content.split(" ");
        for (String str : words) {
            if (_word.equals(str)) {
                count++;
            }
        }
        return count / words.length;
    }

    double IDF(String _word, String[] _corpus) {
        double count = 0.0;
        for (String doc : _corpus) {
            for (String str : doc.split(" ")) {
                if (_word.equals(str)) {
                    count++;
                    break;
                }
            }
        }
        return Math.log(_corpus.length/count) + 1;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        double[][] myTFIDF = new double[_cleanedContents.length][vocabularyList.length];

        //TODO 4.3 - 10 marks

        for (int i = 0; i < myTFIDF.length; i++) {
            for (int j = 0; j < myTFIDF[0].length; j++) {
                String word = vocabularyList[j];
                String content = _cleanedContents[i];
                myTFIDF[i][j] = this.TF(word, content) * this.IDF(word, _cleanedContents);
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
        double[][] mySimilarity = new double[newsCleanedContent.length][2];

        //TODO 4.5 - 15 marks

        for (int i = 0; i < mySimilarity.length; i++) {
            mySimilarity[i][0] = i;
            Vector vec1 = new Vector(newsTFIDF[_newsIndex]);
            Vector vec2 = new Vector(newsTFIDF[i]);
            mySimilarity[i][1] = vec1.cosineSimilarity(vec2);
        }

        // basic slightly altered bubble sort algorithm.
        for (int i = 0; i < mySimilarity.length; i++) {
            for (int j = 0; j < mySimilarity.length - 1; j++) {
                if (mySimilarity[j][1] < mySimilarity[j + 1][1]) {
                    double[] temp = mySimilarity[j];
                    mySimilarity[j] = mySimilarity[j + 1];
                    mySimilarity[j + 1] = temp;
                }
            }
        }

        return mySimilarity;
    }

    static int[] trimArray(int _newSize, int[] _arr) {
        int[] trimmedArray = new int[_newSize];
        System.arraycopy(_arr, 0, trimmedArray, 0, _newSize);
        return trimmedArray;
    }

    public String groupingResults(String _firstTitle, String _secondTitle) {
        int[] arrayGroup1 = new int[newsTitles.length], arrayGroup2 = new int[newsTitles.length];

        //TODO 4.6 - 15 marks

        int firstIndex = -1, secondIndex = -1;
        for (int i = 0; i < newsTitles.length; i++) {
            if (newsTitles[i].equals(_firstTitle)) {
                firstIndex = i;
            } else if (newsTitles[i].equals(_secondTitle)) {
                secondIndex = i;
            }
        }

        double[][] firstSimilarMatrix = this.newsSimilarity(firstIndex);
        double[][] secondSimilarMatrix = this.newsSimilarity(secondIndex);

        int firstCount = 0, secondCount = 0;
        for (int i = 0; i < newsTitles.length; i++) {
            double[] currentFRow = new double[0];
            double[] currentSRow = new double[0];
            for (double[] fRow : firstSimilarMatrix) {
                if (fRow[0] == i) {
                    currentFRow = fRow;
                    break;
                }
            }
            for (double[] sRow : secondSimilarMatrix) {
                if (sRow[0] == i) {
                    currentSRow = sRow;
                    break;
                }
            }

            if (currentFRow[1] >= currentSRow[1]) {
                arrayGroup1[firstCount] = (int) currentFRow[0];
                firstCount++;
            } else {
                arrayGroup2[secondCount] = (int) currentSRow[0];
                secondCount++;
            }
        }

        arrayGroup1 = trimArray(firstCount, arrayGroup1);
        arrayGroup2 = trimArray(secondCount, arrayGroup2);

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
