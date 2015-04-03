/**
 * Created By: cfloersch
 * Date: 5/27/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectionTest {

   private static final String ADDRESS = "10.21.0.1";

   private Connection objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Connection(ADDRESS, ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
   }


   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNullAddress()
   {
      new Connection(null, ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionEmptyAddress()
   {
      new Connection(" ", ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNullAddressType()
   {
      new Connection(ADDRESS, null, NETWORK_TYPE_INTERNET);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionEmptyAddressType()
   {
      new Connection(ADDRESS, " ", NETWORK_TYPE_INTERNET);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionNullNetworkType()
   {
      new Connection(ADDRESS, ADDRESS_TYPE_IP4, null);
   }

   @Test (expected = IllegalArgumentException.class)
   public void testConstructionEmptyNetworkType()
   {
      new Connection(ADDRESS, ADDRESS_TYPE_IP4, "");
   }


   @Test
   public void testGetters()
   {
      assertEquals(ADDRESS, objectUnderTest.getAddress());
      assertEquals(ADDRESS_TYPE_IP4, objectUnderTest.getAddressType());
      assertEquals(NETWORK_TYPE_INTERNET, objectUnderTest.getNetworkType());
   }

   @Test
   public void testTypeChar()
   {
      // BandWidth should always be 'c'
      assertEquals('c', objectUnderTest.getTypeChar());
   }

   @Test
   public void testHashCode()
   {
      Connection replica = new Connection(ADDRESS, ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
      assertTrue(objectUnderTest.hashCode() == replica.hashCode());

      Connection diffAddress = new Connection("diff", ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
      assertFalse(objectUnderTest.hashCode() == diffAddress.hashCode());

      Connection diffAddressType = new Connection(ADDRESS, "diff", NETWORK_TYPE_INTERNET);
      assertFalse(objectUnderTest.hashCode() == diffAddressType.hashCode());

      Connection diffNetworkType = new Connection(ADDRESS, ADDRESS_TYPE_IP4, "diff");
      assertFalse(objectUnderTest.hashCode() == diffNetworkType.hashCode());
   }

   @Test
   public void testEquals()
   {
      Connection replica = new Connection(ADDRESS, ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
      assertTrue(objectUnderTest.equals(replica));

      Connection diffAddress = new Connection("diff", ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
      assertFalse(objectUnderTest.equals(diffAddress));

      Connection diffAddressType = new Connection(ADDRESS, "diff", NETWORK_TYPE_INTERNET);
      assertFalse(objectUnderTest.equals(diffAddressType));

      Connection diffNetworkType = new Connection(ADDRESS, ADDRESS_TYPE_IP4, "diff");
      assertFalse(objectUnderTest.equals(diffNetworkType));
   }

   @Test
   public void testCopy()
   {
      Connection copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertEquals(ADDRESS, copy.getAddress());
      assertEquals(ADDRESS_TYPE_IP4, copy.getAddressType());
      assertEquals(NETWORK_TYPE_INTERNET, copy.getNetworkType());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("c=IN IP4 10.21.0.1", objectUnderTest.toString());
   }


}
