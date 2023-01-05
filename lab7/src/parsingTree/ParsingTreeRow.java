package parsingTree;

public class ParsingTreeRow {

    private Integer index;
    private String symbol;
    private ParsingTreeRow parent;
    private ParsingTreeRow rightSibling;

    private ParsingTreeRow leftChild;

    private Integer level;

    public ParsingTreeRow(String info) {
        this.symbol = info;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ParsingTreeRow getParent() {
        return parent;
    }

    public void setParent(ParsingTreeRow parent) {
        this.parent = parent;
    }

    public ParsingTreeRow getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(ParsingTreeRow rightSibling) {
        this.rightSibling = rightSibling;
    }

    public ParsingTreeRow getLeftChild() {
        return this.leftChild;
    }

    public void setLeftChild(ParsingTreeRow leftChild) {
        this.leftChild = leftChild;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("ParsingTree.ParsingTreeRow: ");
        result.append("index = ").append(index);
        result.append(", symbol = ").append(symbol);
        result.append(", leftChild = ").append(leftChild != null ? leftChild.getIndex() : -1);
        result.append(", rightChild = ").append(rightSibling != null ? rightSibling.getIndex() : -1);
        result.append(", parent = ").append(parent != null ? parent.getIndex() : -1);
        result.append(", level = ").append(level);

        return result.toString();
    }
}