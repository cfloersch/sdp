/**
 * Created By: cfloersch
 * Date: 5/27/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

   private static final String RTPMAP = "rtpmap";
   private static final String VALUE = "98";

   private Attribute objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Attribute(RTPMAP, VALUE);
   }


   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNullName()
   {
      // We don't allow null names
      new Attribute(null, null);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionEmptyName()
   {
      // We don't allow empty names
      new Attribute(" ", null);
   }

   @Test
   public void testConstructionNullValue()
   {
      new Attribute("rtpmap", null);
      // No exception means it passes
   }

   @Test
   public void testConstructionEmptyValue()
   {
      new Attribute("rtpmap", "");
      // No exception means it passes
   }

   @Test
   public void testGetters()
   {
      assertEquals(RTPMAP, objectUnderTest.getName());
      assertEquals(VALUE, objectUnderTest.getValue());
      // Null, or empty values are translated to null
      objectUnderTest = new Attribute(RTPMAP, null);
      assertNull(objectUnderTest.getValue());
      objectUnderTest = new Attribute(RTPMAP, "");
      assertNull(objectUnderTest.getValue());
      objectUnderTest = new Attribute(RTPMAP, " ");
      assertNull(objectUnderTest.getValue());
   }

   @Test
   public void testTypeChar()
   {
      // Attribute should always be 'a'
      assertEquals('a', objectUnderTest.getTypeChar());
   }

   @Test
   public void testHashCode()
   {
      Attribute replica = new Attribute(RTPMAP, VALUE);
      assertTrue(objectUnderTest.hashCode() == replica.hashCode());
      Attribute nullVal = new Attribute(RTPMAP, null);
      assertFalse(objectUnderTest.hashCode() == nullVal.hashCode());
      Attribute emptyVal = new Attribute(RTPMAP, "");
      assertFalse(objectUnderTest.hashCode() == emptyVal.hashCode());
      Attribute diffVal = new Attribute(RTPMAP, "99");
      assertFalse(objectUnderTest.hashCode() == diffVal.hashCode());
      Attribute diffName = new Attribute("RTPMAP", VALUE);
      assertFalse(objectUnderTest.hashCode() == diffName.hashCode());
   }

   @Test
   public void testEquals()
   {
      Attribute replica = new Attribute(RTPMAP, VALUE);
      assertTrue(objectUnderTest.equals(replica));
      Attribute nullVal = new Attribute(RTPMAP, null);
      assertFalse(objectUnderTest.equals(nullVal));
      Attribute emptyVal = new Attribute(RTPMAP, "");
      assertFalse(objectUnderTest.equals(emptyVal));
      Attribute diffVal = new Attribute(RTPMAP, "99");
      assertFalse(objectUnderTest.equals(diffVal));
      Attribute diffName = new Attribute("RTPMAP", VALUE);
      assertFalse(objectUnderTest.equals(diffName));
      assertFalse(objectUnderTest.equals(RTPMAP));
   }

   @Test
   public void testCopy()
   {
      Attribute copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertEquals(RTPMAP, copy.getName());
      assertEquals(VALUE, copy.getValue());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("a=rtpmap:98", objectUnderTest.toString());
      objectUnderTest = new Attribute(RTPMAP, null);
      assertEquals("a=rtpmap", objectUnderTest.toString());
      objectUnderTest = new Attribute(RTPMAP, " ");
      assertEquals("a=rtpmap", objectUnderTest.toString());
   }
}
