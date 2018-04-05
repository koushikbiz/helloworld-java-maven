package com.integral.ds.emscope.trades.weights;

/**
 * Attribute weights.
 *
 * @author Rahul Bhattacharjee
 */
public class AttributeWeight {

    private String attributeName;
    private float weight;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
