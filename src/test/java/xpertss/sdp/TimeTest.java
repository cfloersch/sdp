/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class TimeTest {

   private static final long START = 1555666;
   private static final long STOP  = 2555666;

   private Time objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Time(START, STOP);
   }


   @Test(expected = IllegalArgumentException.class)
   public void testConstructionStopBeforeStart()
   {
      new Time(STOP, START);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionStopEqualToStart()
   {
      new Time(START, START);
   }


   @Test
   public void testGetters()
   {
      assertEquals(Utils.toDate(START), objectUnderTest.getStart());
      assertEquals(Utils.toDate(STOP), objectUnderTest.getStop());

      objectUnderTest = new Time(0, 0);
      assertNull(objectUnderTest.getStart());
      assertNull(objectUnderTest.getStop());

      objectUnderTest = new Time(START, 0);
      assertEquals(Utils.toDate(START), objectUnderTest.getStart());
      assertNull(objectUnderTest.getStop());

      objectUnderTest = new Time(0, STOP);
      assertNull(objectUnderTest.getStart());
      assertEquals(Utils.toDate(STOP), objectUnderTest.getStop());

      assertEquals('t', objectUnderTest.getTypeChar());
   }


   @Test
   public void testGetterImmutability()
   {
      Date start = objectUnderTest.getStart();
      start.setTime(System.currentTimeMillis());
      assertFalse(start.equals(objectUnderTest.getStart()));

      Date stop = objectUnderTest.getStop();
      stop.setTime(System.currentTimeMillis());
      assertFalse(stop.equals(objectUnderTest.getStop()));
   }


   @Test
   public void testCopy()
   {
      Time copy = objectUnderTest.clone();

      assertFalse(objectUnderTest == copy);
      assertTrue(objectUnderTest.hashCode() == copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));

      assertEquals(objectUnderTest.getStart(), copy.getStart());
      assertFalse(objectUnderTest.getStart() == copy.getStart());

      assertEquals(objectUnderTest.getStop(), copy.getStop());
      assertFalse(objectUnderTest.getStop() == copy.getStop());
   }

   @Test
   public void testToString()
   {
      Time objectUnderTest = new Time(START, STOP);
      assertEquals("t=1555666 2555666", objectUnderTest.toString());
   }

}
