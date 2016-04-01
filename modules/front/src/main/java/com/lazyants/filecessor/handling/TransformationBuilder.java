package com.lazyants.filecessor.handling;

import java.awt.image.BufferedImage;
import java.util.List;

public class TransformationBuilder {

    private final BufferedImage image;
    private List<Operation> transformationsChain;

    public TransformationBuilder(BufferedImage image) {
        this.image = image;
    }

    public TransformationBuilder addOperation(Operation operation) {
        transformationsChain.add(operation);

        return this;
    }

    public TransformationBuilder addOperations(List<Operation> operations) {
        transformationsChain.addAll(operations);

        return this;
    }

    public BufferedImage applyTransformations() {
        BufferedImage result = image;
        for (Operation operation: transformationsChain) {
            result = operation.apply(result);
        }

        return result;
    }
}
