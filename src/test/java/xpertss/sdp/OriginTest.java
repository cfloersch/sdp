/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OriginTest {

   private static final String USERNAME = "joe";
   private static final String ADDRESS = "10.0.0.1";

   private Origin objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Origin(USERNAME, 200, 1, ADDRESS, SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullUsername()
   {
      new Origin(null, 200, 1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyUsername()
   {
      new Origin("", 200, 1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullAddress()
   {
      new Origin("joe", 200, 1, null, SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyAddress()
   {
      new Origin("joe", 200, 1, "", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullAddressType()
   {
      new Origin("joe", 200, 1, "10.0.0.1", null, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyAddressType()
   {
      new Origin("joe", 200, 1, "10.0.0.1", "", SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullNetworkType()
   {
      new Origin("joe", 200, 1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullEmptyType()
   {
      new Origin("joe", 200, 1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, "");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeSessionId()
   {
      new Origin("joe", -200, 1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeSessionVersion()
   {
      new Origin("joe", 200, -1, "10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   }

   @Test
   public void testGetters()
   {
      assertEquals(USERNAME, objectUnderTest.getUsername());
      assertEquals(ADDRESS, objectUnderTest.getAddress());
      assertEquals(200, objectUnderTest.getSessionId());
      assertEquals(1, objectUnderTest.getSessionVersion());
      Assert.assertEquals(SdpConstants.ADDRESS_TYPE_IP4, objectUnderTest.getAddressType());
      Assert.assertEquals(SdpConstants.NETWORK_TYPE_INTERNET, objectUnderTest.getNetworkType());
      assertEquals('o', objectUnderTest.getTypeChar());
   }

   @Test
   public void testCopy()
   {
      Origin copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("o=joe 200 1 IN IP4 10.0.0.1", objectUnderTest.toString());
   }
}
