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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.arcusx.chrono.Day;
import com.arcusx.chrono.Month;
import com.arcusx.chrono.MonthSequence;
import com.arcusx.chrono.OpenMonthSequence;

/**
 * 
 * 
 * 
 * Created 20.05.2003, 15:15:45.
 * 
 * @author conni
 * @version $Id$
 */
public class MonthSequenceTestCase2 extends TestCase
{

	public void testInnerYearMonths() throws Exception
	{
		MonthSequence period = new MonthSequence(new Month(2000, Calendar.JANUARY), new Month(2000, Calendar.DECEMBER));
		assertEquals(12, period.size());

		period = new MonthSequence(new Month(2000, Calendar.JANUARY), new Month(2000, Calendar.JUNE));
		assertEquals(6, period.size());

		period = new MonthSequence(new Month(2000, Calendar.JULY), new Month(2000, Calendar.DECEMBER));
		assertEquals(6, period.size());

		period = new MonthSequence(new Month(2000, Calendar.DECEMBER), new Month(2000, Calendar.DECEMBER));
		assertEquals(1, period.size());
	}

	public void testYearSpanningMonths() throws Exception
	{
		MonthSequence period = new MonthSequence(new Month(2000, Calendar.JANUARY), new Month(2001, Calendar.DECEMBER));
		assertEquals(24, period.size());

		period = new MonthSequence(new Month(2000, Calendar.JANUARY), new Month(2001, Calendar.DECEMBER));
		assertEquals(24, period.size());
	}

	public void testLimitMonthsMinNoMax() throws Exception
	{
		MonthSequence period = new MonthSequence(new Month(2001, Month.JANUARY), new Month(2001, Month.MARCH));
		assertEquals(3, period.size());

		// limit
		Month minMonth = new Month(2001, Month.MARCH);
		period = period.limitBy(minMonth, null);

		assertEquals("MonthsPeriod must keep year.", 2001, period.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in March.", Calendar.MARCH, period.getFirstMonth().getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, period.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in March", Calendar.MARCH, period.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be cut.", 1, period.size());
	}

	public void testMonthsIteratorOne()
	{
		MonthSequence months = new MonthSequence(new Month(2000, Month.JANUARY), new Month(2000, Month.JANUARY));
		List monthList = new ArrayList();
		Iterator iter = months.iterator();
		while (iter.hasNext())
		{
			Month curr = (Month) iter.next();
			monthList.add(curr);
		}

		// check count
		assertEquals("Iterating on one months must return 1 month.", monthList.size(), 1);

		assertEquals("Month at " + 0 + " must have month " + Month.JANUARY, ((Month) monthList.get(0)).getMonthValue(),
				Month.JANUARY);
	}

	public void testMonthsIteratorMany()
	{
		MonthSequence months = new MonthSequence(new Month(2000, Month.JANUARY), new Month(2000, Month.DECEMBER));
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
		MonthSequence period = new MonthSequence(entryDate.getMonth(), exitDate.getMonth());
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

	public void testLimitMonthsMinAndMax() throws Exception
	{
		MonthSequence origPeriod = new MonthSequence(new Month(2001, Calendar.JANUARY), 3);

		// check period
		assertEquals("MonthsPeriod must keep year.", 2001, origPeriod.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in January.", Calendar.JANUARY, origPeriod.getFirstMonth()
				.getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, origPeriod.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in Marchl", Calendar.MARCH, origPeriod.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be aligned.", 3, origPeriod.size());

		// limit to lower border
		MonthSequence period = origPeriod.limitBy(new Month(2001, Calendar.JANUARY), new Month(2001, Calendar.FEBRUARY));

		assertEquals("MonthsPeriod must keep year.", 2001, period.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in Jan.", Calendar.JANUARY, period.getFirstMonth().getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, period.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in Feb", Calendar.FEBRUARY, period.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be cut.", 2, period.size());

		// limit to upper border
		period = origPeriod.limitBy(new Month(2001, Calendar.FEBRUARY), new Month(2001, Calendar.MARCH));

		assertEquals("MonthsPeriod must keep year.", 2001, period.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in Feb.", Calendar.FEBRUARY, period.getFirstMonth().getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, period.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in March", Calendar.MARCH, period.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be cut.", 2, period.size());

		// outer limit
		period = origPeriod.limitBy(new Month(2000, Calendar.DECEMBER), new Month(2001, Calendar.APRIL));

		assertEquals("MonthsPeriod must keep year.", 2001, origPeriod.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in January.", Calendar.JANUARY, origPeriod.getFirstMonth()
				.getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, origPeriod.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in Marchl", Calendar.MARCH, origPeriod.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be aligned.", 3, origPeriod.size());

		// limit to one
		period = origPeriod.limitBy(new Month(2001, Calendar.FEBRUARY), new Month(2001, Calendar.FEBRUARY));

		assertEquals("MonthsPeriod must keep year.", 2001, period.getFirstMonth().getYearValue());
		assertEquals("MonthsPeriod must start in Feb.", Calendar.FEBRUARY, period.getFirstMonth().getMonthValue());
		assertEquals("MonthsPeriod must keep year,", 2001, period.getLastMonth().getYearValue());
		assertEquals("MonthsPeriod must end in Feb", Calendar.FEBRUARY, period.getLastMonth().getMonthValue());
		assertEquals("MonthsPeriod must be cut.", 1, period.size());
	}
	
	
	public void testLimitClosedPeriodWithOpen() throws Exception
	{
		MonthSequence period = new MonthSequence(new Month(2001, Calendar.JANUARY), 10);
		MonthSequence limitPeriod = new OpenMonthSequence(new Month(2001,Month.FEBRUARY));
		MonthSequence expectedPeriod = new MonthSequence(new Month(2001, Month.FEBRUARY), 9);
		MonthSequence calculatedPeriod = period.limitBy(limitPeriod);
		assertEquals( expectedPeriod, calculatedPeriod );
	}

}