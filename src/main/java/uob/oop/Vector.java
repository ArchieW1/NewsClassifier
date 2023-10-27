package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 3.1 - 0.5 marks
        this.doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        //TODO Task 3.2 - 2 marks

        if (_index < 0 || _index >= doubElements.length)
            return -1;

        return doubElements[_index];
    }

    public void setElementatIndex(double _value, int _index) {
        //TODO Task 3.3 - 2 marks

        if (_index < 0 || _index >= doubElements.length) {
            doubElements[doubElements.length - 1] = _value;
        } else {
            doubElements[_index] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 3.4 - 0.5 marks

        return doubElements;
    }

    public int getVectorSize() {
        //TODO Task 3.5 - 0.5 marks
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        //TODO Task 3.6 - 6 marks

        if (_size == doubElements.length || _size <= 0)
            return this;

        double[] newVector = new double[_size];
        for (int i = 0; i < newVector.length; i++) {
            if (i >= doubElements.length) {
                newVector[i] = -1;
            } else {
                newVector[i] = doubElements[i];
            }
        }
        return new Vector(newVector);
    }

    Vector resizeVectorsToSame(Vector _v) {
        if (_v.getVectorSize() > this.getVectorSize()) {
            doubElements = this.reSize(_v.getVectorSize()).getAllElements();
        } else if (this.getVectorSize() > _v.getVectorSize()) {
            _v = _v.reSize(this.getVectorSize());
        }
        return _v;
    }

    public Vector add(Vector _v) {
        //TODO Task 3.7 - 2 marks

        _v = resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double[] newVector = new double[this.getVectorSize()];
        for (int i = 0; i < doubElements.length; i++) {
            newVector[i] = doubElements[i] + vElements[i];
        }

        return new Vector(newVector);
    }

    public Vector subtraction(Vector _v) {
        //TODO Task 3.8 - 2 marks

        _v = resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double[] newVector = new double[this.getVectorSize()];
        for (int i = 0; i < doubElements.length; i++) {
            newVector[i] = doubElements[i] - vElements[i];
        }

        return new Vector(newVector);
    }

    public double dotProduct(Vector _v) {
        //TODO Task 3.9 - 2 marks

        _v = resizeVectorsToSame(_v);

        double[] vElements = _v.getAllElements();
        double sum = 0.0;
        for (int i = 0; i < doubElements.length; i++) {
            sum += doubElements[i] * vElements[i];
        }

        return sum;
    }

    public double magnitude() {
        double sum = 0.0;
        for (double element : doubElements) {
            sum += Math.pow(element, 2);
        }
        return Math.sqrt(sum);
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 3.10 - 6.5 marks

        _v = resizeVectorsToSame(_v);
        return this.dotProduct(_v) / (this.magnitude() * _v.magnitude());
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
