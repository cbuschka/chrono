/**
 * This software is written by arcus(x) GmbH and subject 
 * to a contract between arcus(x) and its customer.
 *
 * This software stays property of arcus(x) unless differing
 * arrangements between arcus(x) and its customer apply.
 *
 * arcus(x) GmbH
 * Bergiusstrasse 27
 * D-22765 Hamburg, Germany
 * 
 * Tel.: +49 (0)40.333 102 92  
 * http://www.arcusx.com
 * mailto:info@arcusx.com
 */

package com.arcusx.chrono.tests;

import java.util.*;
import junit.framework.*;
import com.arcusx.chrono.*;

/**
 * 
 * 
 * 
 * Created 20.05.2003, 15:15:45.
 * 
 * @author conni
 * @version $Id$
 */
public class MonthsTestCase extends TestCase
{

	public void testInnerYearMonths() throws Exception
	{
		Months period = new Months(new Month(2000, Calendar.JANUARY), new Month(2000, Calendar.DECEMBER));
		assertEquals(12, period.size());

		period = new Months(new Month(2000, Calendar.JANUARY), new Month(2000, Calendar.JUNE));
		assertEquals(6, period.size());

		period = new Months(new Month(2000, Calendar.JULY), new Month(2000, Calendar.DECEMBER));
		assertEquals(6, period.size());

		period = new Months(new Month(2000, Calendar.DECEMBER), new Month(2000, Calendar.DECEMBER));
		assertEquals(1, period.size());
	}

	public void testYearSpanningMonths() throws Exception
	{
		Months period = new Months(new Month(2000, Calendar.JANUARY), new Month(2001, Calendar.DECEMBER));
		assertEquals(24, period.size());

		period = new Months(new Month(2000, Calendar.JANUARY), new Month(2001, Calendar.DECEMBER));
		assertEquals(24, period.size());
	}

	public void testMonthsIteratorOne()
	{
		Months months = new Months(new Month(2000, Month.JANUARY), new Month(2000, Month.JANUARY));
		List monthList = new ArrayList();
		Iterator iter = months.iterator();
		while (iter.hasNext())
		{
			Month curr = (Month) iter.next();
			monthList.add(curr);
		}

		// check count
		assertEquals("Iterating on one months must return 1 month.", 1, monthList.size());

		assertEquals("Month at " + 0 + " must have month " + Month.JANUARY, ((Month) monthList.get(0)).getMonthValue(),
				Month.JANUARY);
	}

	public void testMonthsIteratorMany()
	{
		Months months = new Months(new Month(2000, Month.JANUARY), new Month(2000, Month.DECEMBER));
		List monthList = new ArrayList();
		Iterator iter = months.iterator();
		while (iter.hasNext())
		{
			Month curr = (Month) iter.next();
			monthList.add(curr);
		}

		// check count
		assertEquals("Iterating on year must return 12 months.", monthList.size(), 12);

		// check each month
		for (int i = 0; i < monthList.size(); ++i)
		{
			Month curr = (Month) monthList.get(i);
			assertEquals("Month at " + i + " must have month " + i, curr.getMonthValue(), i);
			assertEquals("Month at " + i + " must have year 2000", curr.getYearValue(), 2000);
		}
	}

	public void testContains()
	{
		Day entryDate = new Day(2003, 0, 1); // jan, 1st
		Day exitDate = new Day(2003, 5, 30); // jun, 30st
		Months period = new Months(entryDate.getMonth(), exitDate.getMonth());
		Day testStartDay = new Day(2002, 11, 31);
		Day testEndDay = new Day(2003, 6, 1);
		Day testCurrDay = testStartDay;
		while (testCurrDay.beforeOrEqual(testEndDay))
		{
			if (testCurrDay.equals(testStartDay) || testCurrDay.equals(testEndDay))
				assertFalse("Curr month " + testCurrDay.getMonth() + " must not be in period." + period, period
						.contains(testCurrDay.getMonth()));
			else
				assertTrue("Curr month " + testCurrDay.getMonth() + " must be in period." + period, period
						.contains(testCurrDay.getMonth()));

			testCurrDay = testCurrDay.add(1);
		}
	}

	/**
	 * This has been reported by Flo.
	 */
	public void testNewMonths1() throws Exception
	{
		Months months = new Months(new Month(2004, 0), 1);
		final int expectedStartYear = 2004;
		final int expectedStartMonth = 0;
		final int expectedEndYear = 2004;
		final int expectedEndMonth = 0;
		assertEquals(expectedStartYear, months.getFirstMonth().getYearValue());
		assertEquals(expectedStartMonth, months.getFirstMonth().getMonthValue());
		assertEquals(expectedEndYear, months.getLastMonth().getYearValue());
		assertEquals(expectedEndMonth, months.getLastMonth().getMonthValue());
	}

	/**
	 * This has been reported by Flo.
	 */
	public void testNewMonths12() throws Exception
	{
		Months months = new Months(new Month(2004, 0), 12);
		final int expectedStartYear = 2004;
		final int expectedStartMonth = 0;
		final int expectedEndYear = 2004;
		final int expectedEndMonth = 11;
		assertEquals(expectedStartYear, months.getFirstMonth().getYearValue());
		assertEquals(expectedStartMonth, months.getFirstMonth().getMonthValue());
		assertEquals(expectedEndYear, months.getLastMonth().getYearValue());
		assertEquals(expectedEndMonth, months.getLastMonth().getMonthValue());
	}

	/**
	 * This has been reported by Flo.
	 */
	/*
	 public void testNewMonthsWithZeroYear() throws Exception
	 {
	 Months months = new Months(new Month(0,0),5);
	 final int expectedStartYear = 0;
	 final int expectedStartMonth = 0;
	 final int expectedEndYear = 0;
	 final int expectedEndMonth = 5;
	 assertEquals(expectedStartYear, months.getFirstMonth().getYearValue());
	 assertEquals(expectedStartMonth, months.getFirstMonth().getMonthValue());
	 assertEquals(expectedEndYear, months.getLastMonth().getYearValue());
	 assertEquals(expectedEndMonth, months.getLastMonth().getMonthValue());
	 }
	 */

	/**
	 * This has been reported by Flo.
	 */
	/*
	 public void testInvalidNegativeCalc1() throws Exception
	 {
	 Months months = new Months(new Month(-10, 1), 10);
	 final int expectedStartYear = -10;
	 final int expectedStartMonth = 1;
	 final int expectedEndYear = -10;
	 final int expectedEndMonth = 11;
	 assertEquals(expectedStartYear, months.getFirstMonth().getYearValue());
	 assertEquals(expectedStartMonth, months.getFirstMonth().getMonthValue());
	 assertEquals(expectedEndYear, months.getLastMonth().getYearValue());
	 assertEquals(expectedEndMonth, months.getLastMonth().getMonthValue());
	 }
	 */

	public void testOpenMonths() throws Exception
	{
		Months openMonths = new OpenMonths(new Month(2003, 0));

		assertNotNull(openMonths.getFirstMonth());
	}

	public void testOpenMonthsGetLastMonthFails() throws Exception
	{
		Months openMonths = new OpenMonths(new Month(2003, 0));
		assertNull(openMonths.getLastMonth());
	}

	public void testOpenMonthsContains() throws Exception
	{
		Months openMonths = new OpenMonths(new Month(2003, 0));
		assertTrue(openMonths.contains(new Day(2003, 0, 1)));
		assertTrue(openMonths.contains(new Month(2003, 0)));
		assertFalse(openMonths.contains(new Day(2002, 0, 1)));
		assertFalse(openMonths.contains(new Month(2002, 0)));
	}

	public void testOpenMonthsEquals() throws Exception
	{
		Months oneOpenMonths = new OpenMonths(new Month(2003, 0));
		Months otherOpenMonths = new OpenMonths(new Month(2003, 0));
		assertEquals(oneOpenMonths, otherOpenMonths);
	}

	public void testOpenMonthsLimit() throws Exception
	{
		Months openMonths = new OpenMonths(new Month(2003, 0));

		// limited min, open max
		Month min = new Month(2003, 1);
		Months limitedMonths = openMonths.limit(min, null);
		Months expectedMonths = new OpenMonths(new Month(2003, 1));
		assertEquals(expectedMonths, limitedMonths);

		// limited min and max
		min = new Month(2003, 1);
		Month max = new Month(2003, 6);
		limitedMonths = openMonths.limit(min, max);
		expectedMonths = new Months(new Month(2003, 1), new Month(2003, 6));
		assertEquals(expectedMonths, limitedMonths);

		// limited none
		limitedMonths = openMonths.limit(null, null);
		expectedMonths = new OpenMonths(new Month(2003, 0));
		assertEquals(expectedMonths, limitedMonths);

		// open min, limited max
		max = new Month(2003, 6);
		limitedMonths = openMonths.limit(null, max);
		expectedMonths = new Months(new Month(2003, 0), max);
		assertEquals(expectedMonths, limitedMonths);
	}

	public void testHeadDownMonthLimit() throws Exception
	{
		try
		{
			Months months = new Months(new Month(2003, Month.JANUARY), 12);
			months.limit(new Month(2003, Month.MAY), new Month(2003, Month.JANUARY));

			fail("Limit by wrong period should raise an exception.");
		}
		catch (Exception ex)
		{
		}
	}

	public void testOneMonthLimit() throws Exception
	{
		Months months = new Months(new Month(2003, Month.JANUARY), 12);
		Months limitedMonths = months.limit(new Month(2003, Month.MAY), new Month(2003, Month.MAY));
		Months expectedMonths = new Months(new Month(2003, Month.MAY), 1);
		assertEquals(expectedMonths, limitedMonths);
	}

}