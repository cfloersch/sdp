/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;




import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RepeatTimeTest {

   private RepeatTime objectUnderTest;
   private static final long[] offsets = { 0, HOURS.toSeconds(25) };

   @Before
   public void setUp()
   {
      // 604800 3600 0 90000
      objectUnderTest = new RepeatTime(DAYS.toSeconds(7),HOURS.toSeconds(1), offsets);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeInterval()
   {
      new RepeatTime(-1, HOURS.toSeconds(1), offsets);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionZeroInterval()
   {
      new RepeatTime(0, HOURS.toSeconds(1), offsets);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeDuration()
   {
      new RepeatTime(DAYS.toSeconds(7), -1, offsets);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionZeroDuration()
   {
      new RepeatTime(DAYS.toSeconds(7), 0, offsets);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullOffsets()
   {
      new RepeatTime(DAYS.toSeconds(7),HOURS.toSeconds(1), null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyOffsets()
   {
      new RepeatTime(DAYS.toSeconds(7), HOURS.toSeconds(1), new long[0]);
   }



   @Test
   public void testGetters()
   {
      assertEquals(DAYS.toSeconds(7), objectUnderTest.getInterval());
      assertEquals(HOURS.toSeconds(1), objectUnderTest.getDuration());
      long[] offsets = objectUnderTest.getOffsets();
      assertEquals(2, offsets.length);
      assertEquals(0, offsets[0]);
      assertEquals(HOURS.toSeconds(25), offsets[1]);
      assertEquals('r', objectUnderTest.getTypeChar());
   }

   @Test
   public void testOffsets()
   {
      assertFalse(offsets == objectUnderTest.getOffsets());
      assertTrue(Arrays.equals(offsets, objectUnderTest.getOffsets()));
      assertFalse(objectUnderTest.getOffsets() == objectUnderTest.getOffsets());
   }


   @Test
   public void testCopy()
   {
      RepeatTime copy = objectUnderTest.clone();
      assertFalse(objectUnderTest == copy);
      assertTrue(objectUnderTest.hashCode() == copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("r=7d 1h 0 25h", objectUnderTest.toString());
   }
}
