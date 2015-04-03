/**
 * Created By: cfloersch
 * Date: 5/27/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BandWidthTest {

   private static final int VALUE = 0;

   private BandWidth objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, VALUE);
   }



   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNullType()
   {
      // We don't allow null types
      new BandWidth(null, 0);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionEmptyType()
   {
      // We don't allow empty types
      new BandWidth(" ", 0);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNegativeValue()
   {
      // We don't allow negative values
      new BandWidth(" ", -1);
   }

   @Test
   public void testGetters()
   {
      Assert.assertEquals(SdpConstants.BANDWIDTH_TYPE_AS, objectUnderTest.getType());
      assertEquals(VALUE, objectUnderTest.getValue());
   }

   @Test
   public void testTypeChar()
   {
      // BandWidth should always be 'b'
      assertEquals('b', objectUnderTest.getTypeChar());
   }

   @Test
   public void testHashCode()
   {
      BandWidth replica = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, VALUE);
      assertTrue(objectUnderTest.hashCode() == replica.hashCode());
      BandWidth diffVal = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, 99);
      assertFalse(objectUnderTest.hashCode() == diffVal.hashCode());
      BandWidth diffName = new BandWidth(SdpConstants.BANDWIDTH_TYPE_CT, VALUE);
      assertFalse(objectUnderTest.hashCode() == diffName.hashCode());
   }

   @Test
   public void testEquals()
   {
      BandWidth replica = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, VALUE);
      assertTrue(objectUnderTest.equals(replica));
      BandWidth diffVal = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, 99);
      assertFalse(objectUnderTest.equals(diffVal));
      BandWidth diffName = new BandWidth(SdpConstants.BANDWIDTH_TYPE_CT, VALUE);
      assertFalse(objectUnderTest.equals(diffName));
      assertFalse(objectUnderTest.equals(SdpConstants.BANDWIDTH_TYPE_AS));
   }

   @Test
   public void testCopy()
   {
      BandWidth copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      Assert.assertEquals(SdpConstants.BANDWIDTH_TYPE_AS, copy.getType());
      assertEquals(VALUE, copy.getValue());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("b=AS:0", objectUnderTest.toString());
      objectUnderTest = new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, 99);
      assertEquals("b=AS:99", objectUnderTest.toString());
      objectUnderTest = new BandWidth(SdpConstants.BANDWIDTH_TYPE_CT, 0);
      assertEquals("b=CT:0", objectUnderTest.toString());
   }

}
