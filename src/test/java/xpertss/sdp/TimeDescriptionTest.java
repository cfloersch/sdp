/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class TimeDescriptionTest {

   private TimeDescription objectUnderTest;

   private static final Time TIME = new Time(5000, 6000);
   private static final RepeatTime REPEAT = new RepeatTime(DAYS.toSeconds(7), HOURS.toSeconds(1), 0,  HOURS.toSeconds(25));

   @Before
   public void setUp()
   {
      objectUnderTest = new TimeDescription(TIME, REPEAT);
   }

   @Test(expected = NullPointerException.class)
   public void testConstructionNullTime()
   {
      new TimeDescription(null, REPEAT);
   }

   @Test
   public void testConstructionNullRepeats()
   {
      TimeDescription desc = new TimeDescription(TIME, null);
      assertNotNull(desc.getRepeatTimes());
      assertEquals(0, desc.getRepeatTimes().length);
   }

   @Test
   public void testConstructionEmptyRepeats()
   {
      TimeDescription desc = new TimeDescription(new Time(5000, 6000), new RepeatTime[0]);
      assertNotNull(desc.getRepeatTimes());
      assertEquals(0, desc.getRepeatTimes().length);
   }

   @Test
   public void testGetters()
   {
      assertEquals(TIME, objectUnderTest.getTime());
      assertEquals(REPEAT, objectUnderTest.getRepeatTimes()[0]);
   }

   @Test
   public void testCopy()
   {
      TimeDescription copy = objectUnderTest.clone();
      assertFalse(objectUnderTest == copy);
      assertTrue(objectUnderTest.equals(copy));
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertEquals(objectUnderTest.toString(), copy.toString());
   }

   @Test
   public void testToString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append("t=5000 6000").append(System.lineSeparator());
      buf.append("r=7d 1h 0 25h").append(System.lineSeparator());
      assertEquals(buf.toString(), objectUnderTest.toString());
   }
}
