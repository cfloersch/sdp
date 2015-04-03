/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;
import xpertss.time.Chronology;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class TimeBuilderTest {

   private Chronology chrono;
   private TimeBuilder objectUnderTest;

   @Before
   public void setUp()
   {
      chrono = Chronology.create();
      objectUnderTest = TimeBuilder.create();
   }


   @Test(expected = IllegalArgumentException.class)
   public void testSetTimeMillisRoundedDown100()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,0,0,0,100));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetTimeMillisRoundedDown900()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,0,0,0,900));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetTimeStopBeforeStart()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,12,0,0,0), chrono.newDate(2013,5,30,0,0,0,0));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetTimeStopEqualStart()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,0,0,0,0));
   }

   @Test
   public void testGetTimeNullByDefault()
   {
      assertNull(objectUnderTest.getTime());
   }

   @Test
   public void testSetTime()
   {
      objectUnderTest.setTime(Utils.toDate(5000), Utils.toDate(6000)).setTime(Utils.toDate(7000), Utils.toDate(8000));
      assertEquals(7000, Utils.toNtpTime(objectUnderTest.getTime().getStart()));
      assertEquals(8000, Utils.toNtpTime(objectUnderTest.getTime().getStop()));
   }

   @Test
   public void testRepeatTime()
   {
      assertEquals(0, objectUnderTest.getRepeatTimes().length);
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 0)
            .addRepeatTime(DAYS.toSeconds(7), HOURS.toSeconds(1), 0, HOURS.toSeconds(2));
      assertEquals(2, objectUnderTest.getRepeatTimes().length);
      assertEquals(1, objectUnderTest.getRepeatTimes()[0].getOffsets().length);
      assertEquals(2, objectUnderTest.getRepeatTimes()[1].getOffsets().length);
      assertTrue(objectUnderTest.removeRepeatTime(objectUnderTest.getRepeatTimes()[0]));
      assertEquals(1, objectUnderTest.getRepeatTimes().length);
      assertEquals(0, objectUnderTest.clearRepeatRimes().getRepeatTimes().length);
   }



   @Test(expected = IllegalArgumentException.class)
   public void testAddRepeatTimeZeroInterval()
   {
      objectUnderTest.addRepeatTime(0, HOURS.toSeconds(1), 0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddRepeatTimeNegativeInterval()
   {
      objectUnderTest.addRepeatTime(-DAYS.toSeconds(1), HOURS.toSeconds(1), 0);
   }


   @Test(expected = IllegalArgumentException.class)
   public void testAddRepeatTimeZeroDuration()
   {
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), 0, 0);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddRepeatTimeNegativeDuration()
   {
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), -HOURS.toSeconds(1), 0);
   }

   @Test
   public void testDefaultBuild()
   {
      TimeDescription desc = objectUnderTest.build();
      assertNull(desc.getTime().getStart());
      assertNull(desc.getTime().getStop());
      assertEquals(0, desc.getRepeatTimes().length);
   }

   @Test
   public void testBuildWithTime()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,12,0,0,0));
      TimeDescription desc = objectUnderTest.build();
      assertNotNull(desc.getTime().getStart());
      assertNotNull(desc.getTime().getStop());
      assertEquals(0, desc.getRepeatTimes().length);
   }

   @Test
   public void testSuccessiveBuilds()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,12,0,0,0));
      TimeDescription one = objectUnderTest.build();
      TimeDescription two = objectUnderTest.build();
      assertTrue(one.equals(two));
      assertFalse(one == two);
      TimeDescription three = objectUnderTest.setTime(null, null).build();
      assertFalse(one.equals(three));
      assertFalse(one == three);
   }

   @Test(expected = IllegalStateException.class)
   public void testWithRepeatTimeButNoTime()
   {
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 0);
      objectUnderTest.build();
   }

   @Test(expected = IllegalStateException.class)
   public void testWithRepeatTimeAndOpenEndedTime()
   {
      objectUnderTest.setTime(null, null);
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 0);
      objectUnderTest.build();
   }

   @Test(expected = IllegalStateException.class)
   public void testWithRepeatTimeAndOpenEndedStartTime()
   {
      objectUnderTest.setTime(null, chrono.newDate(2013,5,30,12,0,0,0));
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 0);
      objectUnderTest.build();
   }

   @Test
   public void testWithSingleRepeatTime()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,12,0,0,0));
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 0);
      TimeDescription desc = objectUnderTest.build();
      assertEquals(1, desc.getRepeatTimes().length);
   }

   @Test
   public void testWithMultipleRepeatTimes()
   {
      objectUnderTest.setTime(chrono.newDate(2013,5,30,0,0,0,0), chrono.newDate(2013,5,30,12,0,0,0));
      objectUnderTest.addRepeatTime(DAYS.toSeconds(2), HOURS.toSeconds(1), 0);
      objectUnderTest.addRepeatTime(DAYS.toSeconds(1), HOURS.toSeconds(1), 3600);
      TimeDescription desc = objectUnderTest.build();
      assertEquals(2, desc.getRepeatTimes().length);
   }


}
