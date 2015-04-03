/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TimeAdjustmentTest {

   private TimeAdjustment objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new TimeAdjustment(1555555, HOURS.toSeconds(1));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionZeroTime()
   {
      new TimeAdjustment(0, HOURS.toSeconds(1));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeTime()
   {
      new TimeAdjustment(-100, HOURS.toSeconds(1));
   }

   @Test
   public void testGetters()
   {

      assertEquals(Utils.toDate(1555555), objectUnderTest.getTime());
      assertEquals(3600, objectUnderTest.getOffset());
   }

   @Test
   public void testCopy()
   {
      TimeAdjustment copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertTrue(copy.equals(objectUnderTest));
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertEquals(objectUnderTest.getTime(), copy.getTime());
      assertEquals(objectUnderTest.getOffset(), copy.getOffset());
   }

   @Test
   public void testToString()
   {
      assertEquals("1555555 1h", objectUnderTest.toString());
      objectUnderTest = new TimeAdjustment(1555555, HOURS.toSeconds(1) + 1);
      assertEquals("1555555 3601", objectUnderTest.toString());
      objectUnderTest = new TimeAdjustment(1555555, MINUTES.toSeconds(1));
      assertEquals("1555555 1m", objectUnderTest.toString());
      objectUnderTest = new TimeAdjustment(1555555, MINUTES.toSeconds(1) + 1);
      assertEquals("1555555 61", objectUnderTest.toString());
      objectUnderTest = new TimeAdjustment(1555555, 0);
      assertEquals("1555555 0", objectUnderTest.toString());
      objectUnderTest = new TimeAdjustment(1555555, -MINUTES.toSeconds(1));
      assertEquals("1555555 -1m", objectUnderTest.toString());
   }
}
