/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {


   @Test
   public void testEmptyIfNull()
   {
      assertEquals(0, Utils.emptyIfNull(null, Object[].class).length);
   }

   @Test
   public void testToCompactTimeMultipleOfDays() {
      assertEquals("1d", Utils.toCompactTime(86400));
      assertEquals("-1d", Utils.toCompactTime(-86400));
      assertEquals("2d", Utils.toCompactTime(2*86400));
      assertEquals("-2d", Utils.toCompactTime(-2*86400));
      assertEquals("5d", Utils.toCompactTime(5*86400));
      assertEquals("-5d", Utils.toCompactTime(-5*86400));
   }

   @Test
   public void testToCompactTimeMultipleOfHours() {
      assertEquals("1h", Utils.toCompactTime(3600));
      assertEquals("-1h", Utils.toCompactTime(-3600));
      assertEquals("2h", Utils.toCompactTime(2*3600));
      assertEquals("-2h", Utils.toCompactTime(-2*3600));
      assertEquals("5h", Utils.toCompactTime(5*3600));
      assertEquals("-5h", Utils.toCompactTime(-5*3600));
      assertEquals("25h", Utils.toCompactTime(86400+3600));
      assertEquals("-25h", Utils.toCompactTime(-(86400+3600)));
   }

   @Test
   public void testToCompactTimeMultipleOfMinutes() {
      assertEquals("1m", Utils.toCompactTime(60));
      assertEquals("-1m", Utils.toCompactTime(-60));
      assertEquals("2m", Utils.toCompactTime(2*60));
      assertEquals("-2m", Utils.toCompactTime(-2*60));
      assertEquals("5m", Utils.toCompactTime(5*60));
      assertEquals("-5m", Utils.toCompactTime(-5*60));
      assertEquals("61m", Utils.toCompactTime(3660));
      assertEquals("-61m", Utils.toCompactTime(-3660));
   }

   @Test
   public void testToCompactTimeNonMultiple() {
      assertEquals("0", Utils.toCompactTime(0));
      assertEquals("61", Utils.toCompactTime(60 + 1));
      assertEquals("86399", Utils.toCompactTime(86400 - 1));
      assertEquals("86401", Utils.toCompactTime(86400 + 1));
      assertEquals("3659", Utils.toCompactTime(3600 + 59));
      assertEquals("3661", Utils.toCompactTime(3600 + 61));
   }

}
