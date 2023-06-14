/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Test;
import xpertss.net.NetUtils;

import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class OriginBuilderTest {

   @Test
   public void testUninitialized()
   {
      Origin origin = OriginBuilder.create().build();
      assertNotNull(NetUtils.getLocalAddress(), origin.getAddress());
      assertEquals(System.getProperty("user.name"), origin.getUsername());
      assertEquals(NETWORK_TYPE_INTERNET, origin.getNetworkType());
      assertEquals(ADDRESS_TYPE_IP4, origin.getAddressType());       // this test will fail on a machine configured with IP6 addresses
   }

   @Test
   public void testUsernameProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertNull(builder.getUsername());
      builder.setUsername("joe").setUsername("fred");
      assertEquals("fred", builder.getUsername());
      assertEquals("fred", builder.build().getUsername());
   }

   @Test
   public void testSessionIdProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertNull(builder.getSessionId());
      builder.setSessionId("220").setSessionId("320");
      assertEquals("320", builder.getSessionId());
      assertEquals("320", builder.build().getSessionId());
   }

   @Test
   public void tetSessionVersionProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertEquals(-1, builder.getSessionVersion());
      builder.setSessionVersion(220).setSessionVersion(320);
      assertEquals(320, builder.getSessionVersion());
      assertEquals(320, builder.build().getSessionVersion());
   }

   @Test
   public void testAddressProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertNull(builder.getAddress());
      builder.setAddress("10.0.0.1").setAddress("232.128.0.1/15");
      assertEquals("232.128.0.1/15", builder.getAddress());
      assertEquals("232.128.0.1/15", builder.build().getAddress());
   }

   @Test
   public void testAddressTypeProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertNull(builder.getAddressType());
      builder.setAddressType("IP6").setAddressType("IP4");
      assertEquals("IP4", builder.getAddressType());
      assertEquals("IP4", builder.build().getAddressType());
   }

   @Test
   public void testNetworkTypeProperty()
   {
      OriginBuilder builder = OriginBuilder.create();
      assertNull(builder.getNetworkType());
      builder.setNetworkType("TR").setNetworkType("IN");
      assertEquals("IN", builder.getNetworkType());
      assertEquals("IN", builder.build().getNetworkType());
   }

   @Test
   public void testPreexisting()
   {
      OriginBuilder original = OriginBuilder.create();
      Origin origin = original.setUsername("bill").setSessionId("10").setSessionVersion(20).build();
      OriginBuilder existing = OriginBuilder.create(origin);
      assertEquals(existing.getUsername(), origin.getUsername());
      assertEquals(existing.getSessionId(), origin.getSessionId());
      assertEquals(existing.getSessionVersion(), origin.getSessionVersion());
      assertEquals(existing.getAddress(), origin.getAddress());
      assertEquals(existing.getAddressType(), origin.getAddressType());
      assertEquals(existing.getNetworkType(), origin.getNetworkType());
      assertEquals(origin, existing.build());
      existing.setUsername("tom");
      assertFalse(existing.build().equals(original.build()));
   }
}
