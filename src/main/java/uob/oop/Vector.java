package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 3.1 - 0.5 marks
        this.doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 3.2 - 2 marks

        if (_index < 0 || _index >= this.doubElements.length)
            return -1;

        return this.doubElements[_index];
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 3.3 - 2 marks

        if (_index < 0 || _index >= this.doubElements.length) {
            this.doubElements[this.doubElements.length - 1] = _value;
        } else {
            this.doubElements[_index] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 3.4 - 0.5 marks

        return this.doubElements;
    }

    public int getVectorSize() {
        //TODO Task 3.5 - 0.5 marks
        return this.doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 3.6 - 6 marks

        if (_size == this.doubElements.length || _size <= 0)
            return new Vector(this.doubElements.clone());

        double[] newVector = new double[_size];
        for (int i = 0; i < newVector.length; i++) {
            if (i >= this.doubElements.length) {
                newVector[i] = -1;
            } else {
                newVector[i] = this.doubElements[i];
            }
        }
        return new Vector(newVector);
    }

    Vector resizeVectorsToSame(Vector _v) {
        if (_v.getVectorSize() > this.getVectorSize()) {
            this.doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        } else if (this.getVectorSize() > _v.getVectorSize()) {
            _v = _v.reSize(this.getVectorSize());
        }
        return _v;
    }

    public Vector add(Vector _v) {
        //TODO Task 3.7 - 2 marks

        _v = this.resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double[] newVector = new double[this.getVectorSize()];
        for (int i = 0; i < this.doubElements.length; i++) {
            newVector[i] = this.doubElements[i] + vElements[i];
        }

        return new Vector(newVector);
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 3.8 - 2 marks

        _v = this.resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double[] newVector = new double[this.getVectorSize()];
        for (int i = 0; i < this.doubElements.length; i++) {
            newVector[i] = this.doubElements[i] - vElements[i];
        }

        return new Vector(newVector);
    }

    public double dotProduct(Vector _v) {
        //TODO Task 3.9 - 2 marks

        _v = this.resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double sum = 0.0;
        for (int i = 0; i < this.doubElements.length; i++) {
            sum += this.doubElements[i] * vElements[i];
        }

        return sum;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 3.10 - 6.5 marks

        _v = this.resizeVectorsToSame(_v);

        // could be broken up for readability but kept in 1 loop for optimisation since used thousands of times.
        double[] vElements = _v.getAllElements();
        double dotProductSum = 0.0, magnitudeSumDoub = 0.0, magnitudeSumV = 0.0;
        for (int i = 0; i < this.doubElements.length; i++) {
            dotProductSum += this.doubElements[i] * vElements[i];
            magnitudeSumDoub += Math.pow(this.doubElements[i], 2);
            magnitudeSumV += Math.pow(vElements[i], 2);
        }

        return dotProductSum / Math.sqrt(magnitudeSumDoub * magnitudeSumV);
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if (this.getVectorSize() != v.getVectorSize())
            return false;

        for (int i = 0; i < this.getVectorSize(); i++) {
            if (this.getElementatIndex(i) != v.getElementatIndex(i)) {
                boolEquals = false;
                break;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
