/* 
 * Copyright (C) 2002-2008 by Brockmann Consult
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation. This program is distributed in the hope it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.esa.beam.util.math;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Tests for class {@link MatrixLookupTable}.
 *
 * @author Ralf Quast
 * @version $Revision$ $Date$
 */
public class MatrixLookupTableTest extends TestCase {

    public void testCreateMatrixFromArray() {
        final double[] values = {1, 2, 3, 4, 5, 6};

        final double[][] matrix = MatrixLookupTable.createMatrix(2, 3, values);
        assertEquals(2, matrix.length);
        assertEquals(3, matrix[0].length);
        assertEquals(3, matrix[1].length);

        assertTrue(Arrays.equals(new double[]{1, 2, 3}, matrix[0]));
        assertTrue(Arrays.equals(new double[]{4, 5, 6}, matrix[1]));
    }
}
