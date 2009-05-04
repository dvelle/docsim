package edu.indiana.cs.docsim;

public enum ShingleAlgorithm {
    Original, IgnoreOrder, Weighted, None, LSA;

    public static String original = "original";
    public static String none = "original";
    public static String weighted = "weighted";
    public static String ignoreOrder = "ignore-order";

    public static ShingleAlgorithm getValue(String value) {
        if (value == null) return null;
        if (value.equalsIgnoreCase(original)) {
            return Original;
        } else if (value.equalsIgnoreCase(weighted)) {
            return Weighted;
        } else if (value.equalsIgnoreCase(ignoreOrder)) {
            return IgnoreOrder;
        } else if (value.equalsIgnoreCase(none)) {
            return None;
        } else {
            return null;
        }
    }

    public static ShingleAlgorithm defaultAlg() {
        return Original;
    }
}
