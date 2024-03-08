package org.jfree.data;

import static org.junit.Assert.*;

import java.security.InvalidParameterException;
import java.util.Random;

import org.jfree.data.DataUtilities;
import org.jfree.data.Range;
import org.jfree.data.Values2D;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jfree.data.KeyedValues;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.DefaultKeyedValues2D;
import org.junit.*;


public class DataUtilitiesTest extends DataUtilities {
    private Mockery mockingContext;
    private Values2D values;
    private KeyedValues data;
    private double[] posArray;
    private double[] emptyArray;
    private double[] negArray;
    private double[] mixedArray;
    private double[] boundaryArray;
    private double[][] sourceArray;
    private DefaultKeyedValues2D valuesRow3;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        mockingContext = new Mockery();
        values = mockingContext.mock(Values2D.class);
        data = mockingContext.mock(KeyedValues.class);
        posArray = new double[] { 1.8, 3, 2 };
        emptyArray = new double[] {};
        negArray = new double[] { -6.0, -4.0, -8.0, -21.0, -41.0 };
        mixedArray = new double[] { -1.0, 2.0, -3.0, 4.0, -5.0 };
        boundaryArray = new double[] { Double.MIN_VALUE, Double.MAX_VALUE };
        sourceArray = new double[][] {{1.0, 2.0}, {3.0, 4.0}};
        

    }
    
    
  
    
//  -------- Method tested: equal() -----------
    @Test
    public void testEqual_NullArrays() {
        assertTrue(DataUtilities.equal(null, null));
    }

    @Test
    public void testEqual_OneArrayNull() {
        double[][] a = {{1.0}};
        assertFalse(DataUtilities.equal(a, null));
        assertFalse(DataUtilities.equal(null, a));
    }

    @Test
    public void testEqual_DifferentLengthArrays() {
        double[][] a = {{1.0}};
        double[][] b = {{1.0}, {2.0}};
        assertFalse(DataUtilities.equal(a, b));
    }

    @Test
    public void testEqual_EqualArrays() {
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] b = {{1.0, 2.0}, {3.0, 4.0}};
        assertTrue(DataUtilities.equal(a, b));
    }

    @Test
    public void testEqual_DifferentArrays() {
        double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] b = {{1.0, 2.0}, {3.0, 5.0}};
        assertFalse(DataUtilities.equal(a, b));
    }

    @Test
    public void testEqual_ArraysWithNaN() {
        double[][] a = {{1.0, Double.NaN}, {Double.NaN, 4.0}};
        double[][] b = {{1.0, Double.NaN}, {Double.NaN, 4.0}};
        assertTrue(DataUtilities.equal(a, b));
    }

    @Test
    public void testEqual_ArraysWithInfinity() {
        double[][] a = {{1.0, Double.POSITIVE_INFINITY}, {Double.NEGATIVE_INFINITY, 4.0}};
        double[][] b = {{1.0, Double.POSITIVE_INFINITY}, {Double.NEGATIVE_INFINITY, 4.0}};
        assertTrue(DataUtilities.equal(a, b));
    }
    
    
    
//  -------- Method tested: equal() -----------


    @Test(expected = IllegalArgumentException.class)
    public void testClone_NullSource() {
        DataUtilities.clone(null); // Passing null source array
    }

    @Test
    public void testClone_EmptySource() {
        double[][] clone = DataUtilities.clone(new double[][] {});
        assertNotNull(clone);
        assertEquals(0, clone.length);
    }

    @Test
    public void testClone_SingleRow() {
        double[][] source = {{1.0, 2.0, 3.0}};
        double[][] clone = DataUtilities.clone(source);
        assertNotSame(source, clone);
        assertArrayEquals(source, clone);
    }

    @Test
    public void testClone_MultipleRows() {
        double[][] clone = DataUtilities.clone(sourceArray);
        assertNotSame(sourceArray, clone);
        assertArrayEquals(sourceArray, clone);
    }

    @Test
    public void testClone_NullInSource() {
        double[][] source = {{1.0, 2.0}, null, {5.0, 6.0, 7.0}};
        double[][] clone = DataUtilities.clone(source);
        assertNotSame(source, clone);
        assertArrayEquals(source, clone);
    }

    @Test
    public void testClone_WithNaN() {
        double[][] source = {{1.0, Double.NaN}, {Double.NaN, 4.0}};
        double[][] clone = DataUtilities.clone(source);
        assertNotSame(source, clone);
        assertArrayEquals(source, clone);
    }

    @Test
    public void testClone_WithInfinity() {
        double[][] source = {{1.0, Double.POSITIVE_INFINITY}, {Double.NEGATIVE_INFINITY, 4.0}};
        double[][] clone = DataUtilities.clone(source);
        assertNotSame(source, clone);
        assertArrayEquals(source, clone);
    }
 

    // -------- Method tested: createNumberArray2D() -----------

    // Objective: Tests the function with an array containing only zero values,
    // checking if it can handle the boundary condition of minimal non-negative
    // values.
    // Type: BVT
    @Test
    public void createFromArrayWithZeros() {

        double[][] data = {
                { 0.0, 0.0 },
                { 0.0, 0.0 }
        };

        Number[][] expected = {
                { 0.0, 0.0 },
                { 0.0, 0.0 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Verifies the function's ability to process arrays containing
    // negative values.
    // Type: ECP
    @Test
    public void createFromArrayWithNegativeValues() {

        double[][] data = {
                { -1.0, -10.0 },
                { -100.0, -1000.0 }
        };

        Number[][] expected = {
                { -1.0, -10.0 },
                { -100.0, -1000.0 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Tests the function with an array that includes a mix of positive,
    // negative, zero, and special floating-point values (NaN, Infinity).
    // Type: ECP
    @Test
    public void createFromArrayWithMixedValues() {

        double[][] data = {
                { 1.0, -1.0, 0.0 },
                { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }
        };

        Number[][] expected = {
                { 1.0, -1.0, 0.0 },
                { Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Checks the function's handling of a minimal array size (single
    // element), testing the boundary condition of the smallest non-empty array.
    // Type: BVT
    @Test
    public void createFromSingleElementArray() {

        double[][] data = {
                { 42.0 }
        };

        Number[][] expected = {
                { 42.0 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Validates the function with a simple 2x2 array of positive
    // numbers, representing a basic case of a square array with uniform positive
    // values.
    // Type: ECP
    @Test
    public void create2x2Array() {

        double[][] data = {
                { 1.0, 10.0 },
                { 1.0, 10.0 }
        };

        java.lang.Number[][] expected = {
                { 1.0, 10.0 },
                { 1.0, 10.0 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Assesses the function's performance with a 3x3 array, testing its
    // capability to handle a slightly larger square array of positive values.
    // Type: ECP
    @Test
    public void create3x3Array() {

        double[][] data = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0, 30.0 },
                { 1.0, 10.0, 40.0 }
        };

        java.lang.Number[][] expected = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0, 30.0 },
                { 1.0, 10.0, 40.0 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Examines the function's ability to process a rectangular
    // (non-square) array, ensuring it can handle arrays with differing row lengths.
    // Type: ECP
    @Test
    public void createNonSquareArray() {

        double[][] data = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0, 30.0 },
        };

        java.lang.Number[][] expected = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0, 30.0 },
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Tests the function with arrays having rows of unequal lengths,
    // checking its robustness in handling boundary conditions related to array
    // shapes.
    // Type: BVT
    @Test
    public void createFromNonUniformDataShapes() {

        double[][] data = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0 },
        };

        java.lang.Number[][] expected = {
                { 1.0, 10.0, 20.0 },
                { 1.0, 10.0 },
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Validates the function's capability to handle a large 100x100
    // array, testing performance and scalability.
    // Type: BVT
    @Test
    public void createLargeArray() {
        int SIZE = 100;
        Random rng = new Random();

        double[][] data = new double[SIZE][SIZE];
        Number[][] expected = new Number[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                double value = rng.nextDouble();
                data[i][j] = value;
                expected[i][j] = value;
            }
        }

        Number[][] actual = DataUtilities.createNumberArray2D(data);
        assertArrayEquals("The generated Number[][] array should match the expected array with random values.",
                expected, actual);
    }

    // Objective: Ensures the function correctly handles an empty array, testing the
    // boundary condition of no data.
    // Type: BVT
    @Test
    public void emptyArguments() {

        double[][] data = {};

        java.lang.Number[][] expected = {};

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Checks the function's error handling when given invalid data (NaN
    // values), expecting an exception.
    // Type: BVT
    @Test
    public void createFromInvalidData() {
        double[][] data = { { Double.NaN }, { Double.NaN } };

        try {
            DataUtilities.createNumberArray2D(data);
            fail("Expected an InvalidParameterException to be thrown");
        } catch (InvalidParameterException e) {
        }
    }

    // Objective: Verifies the function's behavior with null input, expecting an
    // exception to ensure robust error handling.
    // Type: BVT
    @Test
    public void createFromNullData() {
        double[][] data = null;

        try {
            DataUtilities.createNumberArray2D(data);
            fail("Expected an InvalidParameterException to be thrown");
        } catch (InvalidParameterException e) {
        } catch (Exception e) {
            fail("Expected an InvalidParameterException, but caught a different exception");
        }
    }

    // Objective: Tests the function with the maximum double value, assessing its
    // handling of extreme boundary values.
    // Type: BVT
    @Test
    public void createFromMaxValues() {
        double[][] data = { { Double.MAX_VALUE, Double.MAX_VALUE } };
        java.lang.Number[][] expected = { { Double.MAX_VALUE, Double.MAX_VALUE } };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Examines the function's response to positive infinity values,
    // representing a special value class.
    // Type: ECP
    @Test
    public void createFromPosInfs() {
        double[][] data = { { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } };
        java.lang.Number[][] expected = { { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Evaluates the function with the smallest positive double value,
    // checking its processing at the lower boundary.
    // Type: BVT
    @Test
    public void createFromMinValues() {
        double[][] data = { { Double.MIN_VALUE, Double.MIN_VALUE } };
        java.lang.Number[][] expected = { { Double.MIN_VALUE, Double.MIN_VALUE } };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Tests the function with negative infinity values to see how it
    // handles this special class of inputs.
    // Type: ECP
    @Test
    public void createFromNegInfs() {
        double[][] data = { { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY } };
        java.lang.Number[][] expected = { { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY } };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Challenges the function with values just below the maximum double
    // value, probing near-boundary conditions.
    // Type: BVT
    @Test
    public void createFromNearlyMaxValues() {

        double[][] data = {
                { Double.MAX_VALUE - 1, Double.MAX_VALUE - 1 }
        };

        Number[][] expected = {
                { Double.MAX_VALUE - 1, Double.MAX_VALUE - 1 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Assesses the function's precision and range with extremely large
    // and small exponent values, testing boundary handling.
    // Type: BVT
    @Test
    public void createFromVeryLargeAndSmallExponents() {

        double[][] data = {
                { 1e307, 1e-307 },
                { -1e307, -1e-307 }
        };

        Number[][] expected = {
                { 1e307, 1e-307 },
                { -1e307, -1e-307 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // Objective: Verifies the function's precision with values that exceed the
    // typical double precision, exploring its numerical accuracy.
    // Type: ECP
    @Test
    public void createFromHighPrecisionValues() {

        double[][] data = {
                { 1.1234567890123456, 1.1234567890123457 }, // Beyond double's precision
                { -1.1234567890123456, -1.1234567890123457 }
        };

        Number[][] expected = {
                { 1.1234567890123456, 1.1234567890123457 },
                { -1.1234567890123456, -1.1234567890123457 }
        };

        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data));
    }

    // -------- Method tested: createNumberArray() -----------

    // Test the creation of number array with valid input. Checks the arrays length
    // type: ECP
    @Test
    public void testCreateNumberArrayLength() {
        java.lang.Number[] result = DataUtilities.createNumberArray(posArray);
        assertEquals("Array length mismatch", posArray.length, result.length);
    }

    // Test the creation of number array with valid input. Checks the values of the
    // elements
    // type: ECP
    @Test
    public void testCreateNumberArrayElementsValue() {
        java.lang.Number[] result = DataUtilities.createNumberArray(posArray);
        for (int i = 0; i < posArray.length - 1; i++) {
            assertEquals("Element at index " + i + " has incorrect value", posArray[i], result[i].doubleValue(),
                    0.000001d);
        }
    }

    // Test the creation of number array with an empty input array
    // type: BVT
    @Test
    public void testCreateNumberArrayWithEmptyArray() {
        java.lang.Number[] result = DataUtilities.createNumberArray(emptyArray);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // Test the creation of number array with negative values. Checks the arrays
    // length
    // type: ECP
    @Test
    public void testCreateNumberArrayWithNegativeValuesArrayLength() {
        java.lang.Number[] result = DataUtilities.createNumberArray(negArray);
        assertNotNull(result);
        assertEquals(negArray.length, result.length);
    }

    // Test the creation of number array with negative values. Checks the values of
    // the elements
    // type: ECP
    @Test
    public void testCreateNumberArrayWithNegativeValuesArrayElements() {
        java.lang.Number[] result = DataUtilities.createNumberArray(negArray);
        for (int i = 0; i < negArray.length - 1; i++) {
            assertEquals(negArray[i], result[i].doubleValue(), 0.000001);
        }
    }

    // Test the creation of number array with mixed values. Checks the arrays length
    // type: ECP
    @Test
    public void testCreateNumberArrayWithMixedValuesArrayLength() {
        java.lang.Number[] result = DataUtilities.createNumberArray(mixedArray);
        assertNotNull(result);
        assertEquals(mixedArray.length, result.length);
    }

    // Test the creation of number array with mixed values. Checks the values of the
    // elements
    // type: ECP
    @Test
    public void testCreateNumberArrayWithMixedValuesArrayElements() {
        java.lang.Number[] result = DataUtilities.createNumberArray(mixedArray);
        for (int i = 0; i < mixedArray.length - 1; i++) {
            assertEquals(mixedArray[i], result[i].doubleValue(), 0.000001);
        }
    }

    // Test the creation of number array with null input data, expecting an
    // IllegalArgumentException
    // type: BVT
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNumberArrayNull() {
        DataUtilities.createNumberArray(null);
    }

    // Test the creation of number array with invalid data, expecting an
    // InvalidParameterException
    // type: ECP
    @Test(expected = InvalidParameterException.class)
    public void testCreateNumberArrayInvalidData() {
        double[] invalidData = { Double.POSITIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY };
        DataUtilities.createNumberArray(invalidData);
    }

    // Test the creation of number array with boundary values (min and max values of
    // double type)
    // type: BVT
    @Test
    public void testCreateNumberArrayBoundary() {
        java.lang.Number[] expected = { Double.MIN_VALUE, Double.MAX_VALUE };
        assertArrayEquals(expected, DataUtilities.createNumberArray(boundaryArray));
    }

    // -------- Method tested: getCumulativePercentages() -----------

    // Test case 1: Testing with a simple scenario from the example on the javadoc
    // page:
    // Math:
    // Key Value
    // 0 5
    // 1 9
    // 2 2
    //
    // Returns:
    //
    // Key Value
    // 0 0.3125 (5 / 16)
    // 1 0.875 ((5 + 9) / 16)
    // 2 1.0 ((5 + 9 + 2) / 16)
    @Test
    public void testGetCumulativePercentagesForTwoValues() {
        mockingContext.checking(new Expectations() {
            {
                allowing(data).getItemCount();
                will(returnValue(3));

                allowing(data).getKey(0);
                will(returnValue(0));
                allowing(data).getKey(1);
                will(returnValue(1));
                allowing(data).getKey(2);
                will(returnValue(2));

                allowing(data).getValue(0);
                will(returnValue(5));
                allowing(data).getValue(1);
                will(returnValue(9));
                allowing(data).getValue(2);
                will(returnValue(2));
            }
        });

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Verify that the cumulative percentage for the first value is 0.3125
        assertEquals(0.3125, result.getValue(0));
        // Verify that the cumulative percentage for the second value is 0.875
        assertEquals(0.875, result.getValue(1));
        // Verify that the cumulative percentage for the third value is 1.0
        assertEquals(1.0, result.getValue(2));
    }

    // Test strategy: This set of tests verifies the behavior of
    // getCumulativePercentages
    // with different input values, including negative numbers.
    // Test case 2: Testing with a simple scenario I made up:
    // (Key Value) Input values
    // 0 7
    // 1 -2
    // 2 5
    //
    // Returns:
    //
    // Key Value
    // 0 0.7 (7 / 10)
    // 1 0.5 ((7 + (-2)) / 10)
    // 2 1.0 ((7 + (-2) + 5) / 10)
    
    @Test
    public void testGetCumulativePercentagesForTwoValuesWithNegativeNumber() {
        mockingContext.checking(new Expectations() {
            {
                allowing(data).getItemCount();
                will(returnValue(3));

                allowing(data).getKey(0);
                will(returnValue(0));
                allowing(data).getKey(1);
                will(returnValue(1));
                allowing(data).getKey(2);
                will(returnValue(2));

                allowing(data).getValue(0);
                will(returnValue(7));
                allowing(data).getValue(1);
                will(returnValue(-2)); // Negative number
                allowing(data).getValue(2);
                will(returnValue(5));
            }
        });

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Verify that the cumulative percentage for the first value is 0.7
        assertEquals(0.7, result.getValue(0));
        // Verify that the cumulative percentage for the second value is 0.5
        assertEquals(0.5, result.getValue(1));
        // Verify that the cumulative percentage for the third value is 1.0
        assertEquals(1.0, result.getValue(2));
    }
    
    
    @Test
    public void testGetCumulativePercentagesWithNullValues() {
        // Mocking behavior for data
        mockingContext.checking(new Expectations() {{
            allowing(data).getItemCount();
            will(returnValue(3));

            allowing(data).getKey(0);
            will(returnValue(0));
            allowing(data).getKey(1);
            will(returnValue(1));
            allowing(data).getKey(2);
            will(returnValue(2));

            allowing(data).getValue(0);
            will(returnValue(null));
            allowing(data).getValue(1);
            will(returnValue(null));
            allowing(data).getValue(2);
            will(returnValue(null));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Verify that all values are NaN
        for (int i = 0; i < result.getItemCount(); i++) {
            assertTrue(Double.isNaN(result.getValue(i).doubleValue()));
        }
    }
    @Test
    public void testGetCumulativePercentagesWithNonNullValues() {
        // Mocking behavior for data
        mockingContext.checking(new Expectations() {{
            allowing(data).getItemCount();
            will(returnValue(3));

            allowing(data).getKey(0);
            will(returnValue(0));
            allowing(data).getKey(1);
            will(returnValue(1));
            allowing(data).getKey(2);
            will(returnValue(2));

            allowing(data).getValue(0);
            will(returnValue(7));
            allowing(data).getValue(1);
            will(returnValue(2));
            allowing(data).getValue(2);
            will(returnValue(5));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

     // Verify that the cumulative percentage for the first value is approximately 0.3333
        assertEquals(1.0 / 3.0, result.getValue(0).doubleValue(), 0.0001);

        // Verify that the cumulative percentage for the second value is approximately 0.6667
        assertEquals(2.0 / 3.0, result.getValue(1).doubleValue(), 0.0001);

        // Verify that the cumulative percentage for the third value is approximately 1.0
        assertEquals(1.0, result.getValue(2).doubleValue(), 0.0001);

    }

    // ---------- Tests for calculateColumnTotal ----------

    /**
     * Test strategy:
     * This set of tests verifies the behavior of calculateColumnTotal
     * with different input values, including negative numbers, null values, and
     * edge cases.
     * 
     * returns:
     * The sum of a row in a particular column
     **/

    // This test verifies the calculation of the column total for a dataset with two
    // values.
    // type: ECP
    @Test
    public void calculateColumnTotalForTwoValues() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(2));
                one(values).getValue(0, 0);
                will(returnValue(7.5));
                one(values).getValue(1, 0);
                will(returnValue(2.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        // verify
        assertEquals(result, 10.0, .000000001d);
    }

    // Test To check columnTotal for 5 negative values in a column
    // type: ECP
    @Test
    public void calculateColumnTotalFor5NegativeValues() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(5));
                one(values).getValue(0, 0);
                will(returnValue(-7.5));
                one(values).getValue(1, 0);
                will(returnValue(-2.5));
                one(values).getValue(2, 0);
                will(returnValue(-3.5));
                one(values).getValue(3, 0);
                will(returnValue(-4.5));
                one(values).getValue(4, 0);
                will(returnValue(-5.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum of 5 negative values was unexpected.", -23.5, result, .000000001d);
    }

    // Test To check sum of mixed negative and positive values in a column
    // type: ECP
    @Test
    public void calculateColumnTotalForMixedPositiveAndNegativeValues() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(4));
                one(values).getValue(0, 0);
                will(returnValue(-7.5));
                one(values).getValue(1, 0);
                will(returnValue(2.5));
                one(values).getValue(2, 0);
                will(returnValue(3.5));
                one(values).getValue(3, 0);
                will(returnValue(-4.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum of mixed positive and negative values was unexpected.", -6.0, result, .000000001d);
    }

    // Test to check if an empty matrix of rows and columns can be handled
    // type: BVT
    @Test
    public void calculateColumnTotalForEmptyData() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(0));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum was unexpected when handling an empty matrix.", 0, result, .000000001d);
    }

    // Test to check if a large number of rows can be handled
    // type: BVT
    @Test
    public void calculateColumnTotalForLargeNumberOfRows() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(100));
                for (int i = 0; i < 100; i++) {
                    one(values).getValue(i, 0);
                    will(returnValue(2));
                }
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum of Large Number of Rows was unexpected.", 200, result, .000000001d);
    }

    // test to check if the sum is 0 when a column contains a null value
    // type: ECP
    @Test
    public void calculateColumnTotalThatContainsANullValue() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(4));
                one(values).getValue(0, 0);
                will(returnValue(null));
                one(values).getValue(1, 0);
                will(returnValue(5));
                one(values).getValue(2, 0);
                will(returnValue(7.5));
                one(values).getValue(3, 0);
                will(returnValue(8.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum was unexpected when handling null value in column.", 0, result, .000000001d);
    }

    // test to check if value when a column contains is an invalid index
    // type: BVT
    @Test
    public void calculateColumnTotalThatContainsInvalidColumnIndex() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
            	one(values).getColumnCount();
                will(returnValue(2));
                allowing(values).getRowCount();
                will(returnValue(2)); // Ensure that the getColumnCount() method returns an appropriate value
                allowing(values).getValue(with(any(int.class)), with(any(int.class)));
                will(returnValue(0.0));
            }
        });
        try {
            double result = DataUtilities.calculateColumnTotal(values, -4);
            assertEquals("Sum was unexpected when handling invalud index in column.", 0, result, .000000001d);
        } catch (Exception e) {
            // Handle the exception here
            assertTrue("Raised an exception" + e + ". Should have return 0 as per documentation.", false);
        }
    }

    // Test To check sum of a large indexed column
    // type: BVT
    @Test
    public void calculateColumnTotalForLargeColumnIndex() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(4));
                one(values).getValue(0, 100);
                will(returnValue(7.5));
                one(values).getValue(1, 100);
                will(returnValue(2.5));
                one(values).getValue(2, 100);
                will(returnValue(3.5));
                one(values).getValue(3, 100);
                will(returnValue(4.5));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 100);
        assertEquals("Sum of Large Column Index was unexpected.", 18.0, result, .000000001d);
    }
    
    @Test
    public void calculateColumnTotalThatContainsNonNullValue() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        final double nonNullValue = 5.0; // Arbitrary non-null value
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(4));
                one(values).getValue(0, 0);
                will(returnValue(null)); // First row has null value
                one(values).getValue(1, 0);
                will(returnValue(nonNullValue)); // Second row has a non-null value
                allowing(values).getValue(with(any(int.class)), with(any(int.class)));
                will(returnValue(0.0)); // Other rows return null
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("Sum was unexpected when column contains a non-null value.", nonNullValue, result, .000000001d);
    }
    
    // --------- tests for calculateRowTotal() method with different args ------------
    
    @Test
    public void testCalculateColumnTotalWithValidValues() {
        // Set up mock to return valid values
        mockingContext.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(2.0));
            allowing(values).getValue(2, 0); will(returnValue(3.0));
            allowing(values).getValue(3, 0); will(returnValue(null));
        }});
        
        int[] validRows = {0, 1, 2};
        double expectedTotal = 6.0; // Sum of all values in the column

        double actualTotal = DataUtilities.calculateColumnTotal(values, 0, validRows);

        assertEquals(expectedTotal, actualTotal, 0.0001);
    }

    @Test
    public void testCalculateColumnTotalWithNullValues() {
        // Set up mock to return null values
        mockingContext.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(1, 0); will(returnValue(null));
            allowing(values).getValue(2, 0); will(returnValue(null));
            allowing(values).getValue(3, 0); will(returnValue(null));
        }});

        int[] validRows = {0, 1, 2};
        double expectedTotal = 0.0; // Since all values are null, total should be 0

        double actualTotal = DataUtilities.calculateColumnTotal(values, 0, validRows);

        assertEquals(expectedTotal, actualTotal, 0.0001);
    }

    @Test
    public void testCalculateColumnTotalWithInvalidRowIndex() {
        // Set up mock to return valid values
        mockingContext.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(2.0));
            allowing(values).getValue(2, 0); will(returnValue(3.0));
            allowing(values).getValue(3, 0); will(returnValue(null));
        }});

        int[] validRows = {0, 1, 2, 3}; // Including an invalid row index
        double expectedTotal = 6.0; // Sum of all valid values

        double actualTotal = DataUtilities.calculateColumnTotal(values, 0, validRows);

        assertEquals(expectedTotal, actualTotal, 0.0001); // Only valid rows should be considered
    }

    
    
//    THE CODE IS NOT CORRECT IN THE DataUtilities SO THIS TEST WILL NOT PASS
    @Test
    public void testCalculateColumnTotalWithTotalGreaterThanZero() {
        // Set up mock to return valid values
        mockingContext.checking(new Expectations() {{
            allowing(values).getRowCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(1, 0); will(returnValue(2.0));
            allowing(values).getValue(2, 0); will(returnValue(3.0));
            allowing(values).getValue(3, 0); will(returnValue(null));
        }});

        int[] validRows = {0, 1, 2};
        double expectedTotal = 106.0; // Sum of all values plus the initial total (100)

        double actualTotal = DataUtilities.calculateColumnTotal(values, 0, validRows);

        assertEquals(expectedTotal, actualTotal, 0.0001); // The initial total is added
    }


    @Test
    public void testCalculateColumnTotalWithNoValidRows() {
        // Set up mock to return valid values for the first half of the rows
        mockingContext.checking(new Expectations() {
            {
                allowing(values).getRowCount(); will(returnValue(4)); // Increase row count to cover the second loop
                allowing(values).getColumnCount(); will(returnValue(0)); // Force the condition colCount < 0
                allowing(values).getValue(0, 0); will(returnValue(null));
                allowing(values).getValue(1, 0); will(returnValue(null));
                allowing(values).getValue(2, 0); will(returnValue(null));
                allowing(values).getValue(3, 0); will(returnValue(null));
                allowing(values).getValue(4, 0); will(returnValue(4.0)); // Add a non-null value for the last row
            }
        });

        int[] validRows = {}; // Empty valid rows array
        double expectedTotal = 0.0; // The total should be 0.0 due to the condition colCount < 0

        double actualTotal = DataUtilities.calculateColumnTotal(values, 0);

        assertEquals(expectedTotal, actualTotal, 0.0001);
    }

    // --------- tests for calculateRowTotal() method ------------

    /**
     * Test strategy:
     * This set of tests verifies the behavior of calculateRowTotal
     * with different input values, including negative numbers, null values, and
     * edge cases.
     * 
     * returns:
     * The sum of a Column in a particular row
     **/

    // Test to calculate the total for a row with two values
    // type: ECP
    @Test
    public void calculateRowTotalForTwoValues() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(2));
                one(values).getValue(0, 0);
                will(returnValue(7.5));
                one(values).getValue(0, 1);
                will(returnValue(2.5));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals(result, 10.0, .000000001d);
    }

    // Test To check rowTotal for 5 negative values in a row
    // type: ECP
    @Test
    public void calculateRowTotalFor5NegativeValues() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(5));
                one(values).getValue(0, 0);
                will(returnValue(-7.5));
                one(values).getValue(0, 1);
                will(returnValue(-2.5));
                one(values).getValue(0, 2);
                will(returnValue(-3.5));
                one(values).getValue(0, 3);
                will(returnValue(-4.5));
                one(values).getValue(0, 4);
                will(returnValue(-5.5));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("Sum of 5 negative values was unexpected.", -23.5, result, .000000001d);
    }

    // Test To check sum of mixed negative and positive values in a row
    // type: ECP
    @Test
    public void calculateRowTotalForMixedPositiveAndNegativeValues() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(4));
                one(values).getValue(0, 0);
                will(returnValue(-7.5));
                one(values).getValue(0, 1);
                will(returnValue(2.5));
                one(values).getValue(0, 2);
                will(returnValue(3.5));
                one(values).getValue(0, 3);
                will(returnValue(-4.5));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("Sum of mixed positive and negative values was unexpected.", -6.0, result, .000000001d);
    }

    // Test to check if an empty matrix of rows and columns can be handled
    // type: BVT
    @Test
    public void calculateRowTotalForEmptyData() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(0));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("Sum was unexpected when handling an empty matrix.", 0, result, .000000001d);
    }

    // Test to check if a large number of rows can be handled
    // type: BVT
    @Test
    public void calculateRowTotalForLargeNumberOfColumns() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(100));
                for (int i = 0; i < 100; i++) {
                    one(values).getValue(0, i);
                    will(returnValue(2));
                }
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("Sum of Large Number of Rows was unexpected.", 200, result, .000000001d);
    }

    // test to check if the sum is 0 when a column contains a null value
    // type: ECP
    @Test
    public void calculateRowTotalThatContainsANullValue() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(4));
                one(values).getValue(0, 0);
                will(returnValue(null));
                one(values).getValue(0, 1);
                will(returnValue(5));
                one(values).getValue(0, 2);
                will(returnValue(7.5));
                one(values).getValue(0, 3);
                will(returnValue(8.5));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("Sum was unexpected when handling null value in Row.", 0, result, .000000001d);
    }

    // test to check if value when a column contains is an invalid index
    // type: BVT
    @Test
    public void calculateRowTotalThatContainsInvalidRowIndex() {
        // setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
            	one(values).getRowCount();
                will(returnValue(2));
                allowing(values).getColumnCount();
                will(returnValue(2)); // Ensure that the getColumnCount() method returns an appropriate value
                allowing(values).getValue(with(any(int.class)), with(any(int.class)));
                will(returnValue(0.0));
            }
        });
        try {
            double result = DataUtilities.calculateRowTotal(values, -4);
            assertEquals("Sum was unexpected when handling invalid index in row.", 0, result, .000000001d);
        } catch (InvalidParameterException e) {
            // Handle the exception here
            fail("Raised an exception" + e + ". Should have return 0 as per documentation.");
        }
    }

    // Test To check sum of a large indexed column
    // type: BVT
    @Test
    public void calculateRowTotalForLargeRowIndex() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(4));
                one(values).getValue(100, 0);
                will(returnValue(7.5));
                one(values).getValue(100, 1);
                will(returnValue(2.5));
                one(values).getValue(100, 2);
                will(returnValue(3.5));
                one(values).getValue(100, 3);
                will(returnValue(4.5));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 100);
        assertEquals("Sum of Large Row Index was unexpected.", 18.0, result, .000000001d);
    }
    
    // --------- tests for calculateRowTotal() method 2 different Inputs!------------


    @Test
    public void testCalculateRowTotalWithValidValues() {
        // Set up mock to return valid values
        mockingContext.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(0, 2); will(returnValue(3.0));
            allowing(values).getValue(0, 3); will(returnValue(null));
        }});
        
        int[] validCols = {0, 1, 2};
        double expectedTotal = 6.0; // Sum of all values in the row

        double actualTotal = DataUtilities.calculateRowTotal(values, 0, validCols);

        assertEquals(expectedTotal, actualTotal, 0.0001);
    }

    @Test
    public void testCalculateRowTotalWithNullValues() {
        // Set up mock to return null values
        mockingContext.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(null));
            allowing(values).getValue(0, 1); will(returnValue(null));
            allowing(values).getValue(0, 2); will(returnValue(null));
            allowing(values).getValue(0, 3); will(returnValue(null));
        }});

        int[] validCols = {0, 1, 2};
        double expectedTotal = 0.0; // Since all values are null, total should be 0

        double actualTotal = DataUtilities.calculateRowTotal(values, 0, validCols);

        assertEquals(expectedTotal, actualTotal, 0.0001);
    }

    @Test
    public void testCalculateRowTotalWithInvalidColumnIndex() {
        // Set up mock to return valid values
        mockingContext.checking(new Expectations() {{
            allowing(values).getColumnCount(); will(returnValue(3));
            allowing(values).getValue(0, 0); will(returnValue(1.0));
            allowing(values).getValue(0, 1); will(returnValue(2.0));
            allowing(values).getValue(0, 2); will(returnValue(3.0));
            allowing(values).getValue(0, 3); will(returnValue(null));
        }});

        int[] validCols = {0, 1, 2, 3}; // Including an invalid column index
        double expectedTotal = 6.0; // Sum of all valid values

        double actualTotal = DataUtilities.calculateRowTotal(values, 0, validCols);

        assertEquals(expectedTotal, actualTotal, 0.0001); // Only valid columns should be considered
    }
    @Test
    public void calculateRowTotalForNegativeColumnCount() {
        // Setup
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(-1)); // Negative column count
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0, new int[] {});
        assertEquals("Sum was unexpected when handling negative column count.", 0, result, .000000001d);
    }
}