/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;

import static java.util.concurrent.TimeUnit.HOURS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TimeZonesTest {

   private static final TimeAdjustment[] ADJUSTMENTS = { new TimeAdjustment(1555555, HOURS.toSeconds(1)),
                                                         new TimeAdjustment(2555555, -HOURS.toSeconds(1))};
   private TimeZones objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new TimeZones(ADJUSTMENTS);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullAdjustments()
   {
      new TimeZones(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyAdjustments()
   {
      new TimeZones(new TimeAdjustment[0]);
   }

   @Test
   public void testGetters()
   {
      assertEquals(2, objectUnderTest.getAdjustments().length);
      assertTrue(Arrays.equals(ADJUSTMENTS, objectUnderTest.getAdjustments()));
      assertFalse(ADJUSTMENTS == objectUnderTest.getAdjustments());
      assertEquals('z', objectUnderTest.getTypeChar());
   }

   @Test
   public void testCopy()
   {
      TimeZones copy = objectUnderTest.clone();
      assertFalse(objectUnderTest == copy);
      assertTrue(objectUnderTest.equals(copy));
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(Arrays.equals(objectUnderTest.getAdjustments(), copy.getAdjustments()));
      assertFalse(objectUnderTest.getAdjustments() == copy.getAdjustments());
   }

   @Test
   public void testToString()
   {
      assertEquals("z=1555555 1h 2555555 -1h", objectUnderTest.toString());
   }

}
