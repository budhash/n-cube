package com.cedarsoftware.ncube

import com.cedarsoftware.ncube.proximity.Distance
import com.cedarsoftware.util.StringUtilities
import groovy.transform.CompileStatic

import static java.lang.Math.abs
/**
 * Class used to compute distance (proximity) for many different
 * data types.  For basic data types like ints, longs, Dates, etc.,
 * simple subtraction can be used.  For Strings, the LevenshteinDistance
 * is used.  For Lat/Lon, the Haversine calculation is used, and
 * for 2D, 3D, etc., the pythagorean distance is used.
 *
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License")
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
@CompileStatic
final class Proximity
{
    private Proximity() { }

	static double distance(Comparable source, Comparable target)
	{
        if (source == null || target == null)
        {
            throw new IllegalArgumentException("Neither source nor target can be null for NEAREST axis comparison." +
            "\nSource: " + source + ", Target: " + target)
        }

        if (source.getClass() != target.getClass())
        {
            throw new IllegalArgumentException("Source and Target data types must be the same for a NEAREST axis comparison." +
            "\nSource: " + source + ", Target: " + target)
        }

		if (source instanceof Long)
		{   // This covers byte, short, int, and long (data type promotion has already occurred).
			return abs((source as Long) - (target as Long))
		}
        else if (source instanceof Date)
        {
            long v = (source as Date).getTime()
            long thatValue = (target as Date).getTime()
            return abs(v - thatValue)
        }
        else if (source instanceof Double)
        {   // Covers float and double
            double v = source as Double
            double thatValue = target as Double
            return abs(v - thatValue)
        }
        else if (source instanceof BigDecimal)
        {
            BigDecimal v = source as BigDecimal
            BigDecimal thatValue = (BigDecimal) target
            return v.subtract(thatValue).abs().doubleValue()
        }
        else if (source instanceof String)
        {
            return StringUtilities.levenshteinDistance(source as String, (String) target)
        }
        else if (source instanceof Distance)
        {
            return (source as Distance).distance(target)
        }
        throw new IllegalArgumentException("Unsupported datatype for NEAREST axis." +
        "\nSource: " + source.getClass().getName() + ", Source Value: " + source +
        "\nTarget Value: " + target)
	}
}
