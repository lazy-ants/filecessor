package com.lazyants.filecessor.utils;

import com.lazyants.filecessor.exception.ApplicationClientException;
import com.lazyants.filecessor.handling.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class OperationCreator {

    public static List<Operation> createOperations(String transformation) {
        Pattern r = Pattern.compile("transformation_(.+)");
        Matcher matcher = r.matcher(transformation);
        if (matcher.find()) {
            return Arrays.stream(matcher.group(1).split("\\+")).map(OperationCreator::createSimpleOperation).collect(Collectors.toList());
        }

        return singletonList(createSimpleOperation(transformation));
    }

    public static Operation createSimpleOperation(String transformation) {
        TransformationChain chain = new TransformationChain();

        chain.addTransformer(new OperationTransformer("crop_coordinates_(\\d+)x(\\d+)_(\\d+)x(\\d+)", (matcher ->
                new CropCoordinates(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)),
                        Integer.valueOf(matcher.group(3)), Integer.valueOf(matcher.group(4))))));

        chain.addTransformer(new OperationTransformer("rotate_(90|180|270)", (matcher ->
                new Rotate(Integer.valueOf(matcher.group(1))))));

        chain.addTransformer(new OperationTransformer("resize_(\\d+)x(\\d+)", (matcher ->
                new Resize(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2))))));

        chain.addTransformer(new OperationTransformer("crop_(\\d+)x(\\d+)", matcher ->
                new Crop(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)))));

        return chain.transform(transformation);
    }

    private static class OperationTransformer {
        private final Pattern pattern;
        private final Function<Matcher, Operation> transforming;

        public OperationTransformer(String regexp, Function<Matcher, Operation> transforming) {
            this.pattern = Pattern.compile(regexp);
            this.transforming = transforming;
        }

        public Operation transform(String entry) {
            Matcher matcher = pattern.matcher(entry);
            if (matcher.find()) {
                return transforming.apply(matcher);
            }
            return null;
        }
    }

    private static class TransformationChain {
        private List<OperationTransformer> transformers = new ArrayList<>();

        public void addTransformer(OperationTransformer transformer) {
            transformers.add(transformer);
        }

        public Operation transform(String string) {
            for (OperationTransformer transformer: transformers) {
                Operation result = transformer.transform(string);
                if (result != null) {
                    return result;
                }
            }

            throw new ApplicationClientException("Invalid transformation url");
        }
    }
}
