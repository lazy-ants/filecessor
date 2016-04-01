package com.lazyants.filecessor.utils;

import com.lazyants.filecessor.exception.ApplicationClientException;
import com.lazyants.filecessor.handling.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OperationCreatorTest {
    @Test
    public void test_createSimpleOperation() {

        assertEquals(OperationCreator.createSimpleOperation("crop_coordinates_10x20_200x300"), new CropCoordinates(10, 20, 200, 300));

        assertEquals(OperationCreator.createSimpleOperation("rotate_270"), new Rotate(270));

        assertEquals(OperationCreator.createSimpleOperation("crop_100x200"), new Crop(100, 200));

        assertEquals(OperationCreator.createSimpleOperation("resize_100x200"), new Resize(100, 200));
    }

    @Test(expected = ApplicationClientException.class)
    public void testFailureRotate_createSimpleOperation() {
        OperationCreator.createSimpleOperation("rotate_280");
    }

    @Test(expected = ApplicationClientException.class)
    public void testFailure_createSimpleOperation() {
        OperationCreator.createSimpleOperation("crop_");
    }

    @Test
    public void test_createOperations() {
        List<Operation> operations = OperationCreator.createOperations("transformation_rotate_180+crop_coordinates_100x200_250x350+resize_100x150");

        assertEquals(operations.size(), 3);

        assertEquals(operations.indexOf(new Resize(100, 150)), 2);
        assertEquals(operations.indexOf(new CropCoordinates(100, 200, 250, 350)), 1);
        assertEquals(operations.indexOf(new Rotate(180)), 0);
    }
}
