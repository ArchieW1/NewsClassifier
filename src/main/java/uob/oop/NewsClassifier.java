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
        this.newsTitles = new String[this.myHTMLs.length];
        this.newsContents = new String[this.myHTMLs.length];

        for (int i = 0; i < myHTMLs.length; i++) {
            this.newsTitles[i] = HtmlParser.getNewsTitle(this.myHTMLs[i]);
            this.newsContents[i] = HtmlParser.getNewsContent(this.myHTMLs[i]);
        }
    }

    public String[] preProcessing() {
        String[] myCleanedContent = null;
        //TODO 4.2 - 5 marks
        myCleanedContent = new String[this.newsContents.length];

        for (int i = 0; i < myCleanedContent.length; i++) {
            String cleaned = NLP.textCleaning(this.newsContents[i]);
            String cleanedLemmanized = NLP.textLemmatization(cleaned);
            myCleanedContent[i] = NLP.removeStopWords(cleanedLemmanized, this.myStopWords);
        }

        return myCleanedContent;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        double[][] myTFIDF = null;

        //TODO 4.3 - 10 marks

        int[][] totalWordCountMatrix = new int[_cleanedContents.length][vocabularyList.length];
        for (int i = 0; i < _cleanedContents.length; i++){
            String[] words = _cleanedContents[i].split(" ");
            int count = 0;
            for (int j = 0; j < vocabularyList.length; j++) {
                for (String word : words) {
                    if (vocabularyList[j].equals(word)) {
                        count++;
                    }
                }
                totalWordCountMatrix[i][j] = count;
                count = 0;
            }
        }

        int[] wordAppearsCounts = new int[vocabularyList.length];
        for (int i = 0; i < vocabularyList.length; i++) {
            for (int j = 0; j < _cleanedContents.length; j++) {
                if (totalWordCountMatrix[j][i] != 0) {
                    wordAppearsCounts[i]++;
                }
            }
        }

        myTFIDF = new double[_cleanedContents.length][vocabularyList.length];
        for (int i = 0; i < _cleanedContents.length; i++) {
            String[] words = _cleanedContents[i].split(" ");
            for (int j = 0; j < vocabularyList.length; j++) {
                myTFIDF[i][j] = ((double) totalWordCountMatrix[i][j] / words.length) *
                        (Math.log((double) _cleanedContents.length / wordAppearsCounts[j]) + 1);
            }
        }

        return myTFIDF;
    }

    public String[] buildVocabulary(String[] _cleanedContents) {
        String[] arrayVocabulary = null;

        //TODO 4.4 - 10 marks

        arrayVocabulary = new String[1000];
        int size = 0;
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
                    if (size >= arrayVocabulary.length) {
                        int newSize = arrayVocabulary.length * 2;
                        String[] scaledArray = new String[newSize];
                        System.arraycopy(arrayVocabulary, 0, scaledArray, 0, arrayVocabulary.length);
                        arrayVocabulary = scaledArray;
                    }
                    arrayVocabulary[size] = word;
                    size++;
                }
            }
        }

        if (size != arrayVocabulary.length){
            String[] trimmedArray = new String[size];
            System.arraycopy(arrayVocabulary, 0, trimmedArray, 0, size);
            arrayVocabulary = trimmedArray;
        }

        return arrayVocabulary;
    }

    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = null;

        //TODO 4.5 - 15 marks

        mySimilarity = new double[this.newsCleanedContent.length][2];

        Vector vec1 = new Vector(this.newsTFIDF[_newsIndex]);
        for (int i = 0; i < mySimilarity.length; i++) {
            mySimilarity[i][0] = i;
            Vector vec2 = new Vector(this.newsTFIDF[i]);
            mySimilarity[i][1] = vec1.cosineSimilarity(vec2);
        }

        mergeSort(mySimilarity, mySimilarity.length);

        return mySimilarity;
    }

    static void mergeSort(double[][] matrix, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        double[][] l = new double[mid][2];
        double[][] r = new double[n - mid][2];

        System.arraycopy(matrix, 0, l, 0, mid);
        if (n - mid >= 0) {
            System.arraycopy(matrix, mid, r, 0, n - mid);
        }

        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(matrix, l, r, mid, n - mid);
    }

    static void merge(double[][] matrix, double[][] l, double[][] r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l[i][1] >= r[j][1]) {
                matrix[k++] = l[i++];
            } else {
                matrix[k++] = r[j++];
            }
        }
        while (i < left) {
            matrix[k++] = l[i++];
        }
        while (j < right) {
            matrix[k++] = r[j++];
        }
    }

    static int[] trimArray(int _newSize, int[] _arr) {
        int[] trimmedArray = new int[_newSize];
        System.arraycopy(_arr, 0, trimmedArray, 0, _newSize);
        return trimmedArray;
    }

    public String groupingResults(String _firstTitle, String _secondTitle) {
        int[] arrayGroup1 = null, arrayGroup2 = null;

        //TODO 4.6 - 15 marks

        arrayGroup1 = new int[this.newsTitles.length];
        arrayGroup2 = new int[this.newsTitles.length];

        int firstIndex = -1, secondIndex = -1;
        for (int i = 0; i < this.newsTitles.length; i++) {
            if (this.newsTitles[i].equals(_firstTitle)) {
                firstIndex = i;
            } else if (this.newsTitles[i].equals(_secondTitle)) {
                secondIndex = i;
            }
        }

        double[][] firstSimilarMatrix = this.newsSimilarity(firstIndex);
        double[][] secondSimilarMatrix = this.newsSimilarity(secondIndex);

        int firstCount = 0, secondCount = 0;
        for (int i = 0; i < this.newsTitles.length; i++) {
            double[] currentFRow = new double[0];
            double[] currentSRow = new double[0];
            boolean isFirstFound = false, isSecondFound = false;
            for (int j = 0; j < firstSimilarMatrix.length; j++) {
                if (firstSimilarMatrix[j][0] == i) {
                    currentFRow = firstSimilarMatrix[j];
                    isFirstFound = true;
                } else if (secondSimilarMatrix[j][0] == i) {
                    currentSRow = secondSimilarMatrix[j];
                    isSecondFound = true;
                }
                if (isFirstFound && isSecondFound) {
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
