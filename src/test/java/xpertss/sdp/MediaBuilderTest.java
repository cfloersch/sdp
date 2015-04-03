/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Test;

import java.net.InetAddress;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MediaBuilderTest {

   @Test (expected = IllegalStateException.class)
   public void testUninitialized()
   {
      MediaBuilder.create().build();
   }

   @Test
   public void testSimpleCase()
   {
      MediaBuilder builder = MediaBuilder.create();
      builder.setMedia("audio", 0, 1, "RTP/AVP", 97);
      assertEquals("m=audio 0 RTP/AVP 97\r\n", builder.build().toString());
   }

   @Test
   public void testMediaProperty()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertNull(builder.getMedia());
      assertNotNull(builder.setMedia("audio", 0, 1, "RTP/AVP", 97).getMedia());
   }

   @Test
   public void testKeyProperty()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertNull(builder.getKey());
      assertNotNull(builder.setKey("prompt", null).getKey());
      assertNull(builder.clearKey().getKey());
   }

   @Test
   public void testInfoProperty()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertNull(builder.getInfo());
      assertEquals("Info", builder.setInfo("Info").getInfo());
   }

   @Test
   public void testConnectionProperty()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertNull(builder.getConnection());
      assertNotNull(builder.setConnection("10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET).getConnection());
      assertNull(builder.clearConnection().getConnection());
   }


   @Test
   public void testMediaAndAttribute()
   {
      MediaBuilder builder = MediaBuilder.create();
      builder.setMedia("audio", 0, 1, "RTP/AVP", 97).setInfo("Simple Info");
      builder.addAttribute("rtpmap","97 mpeg4-generic/8000/2").addAttribute("control", "trackID=5");
      MediaDescription desc = builder.build();
      assertNotNull(desc.getAttribute("control"));
      assertNotNull(desc.getAttribute("rtpmap"));
      assertEquals("audio", desc.getMedia().getType());
      assertEquals("RTP/AVP", desc.getMedia().getProtocol());
   }


   @Test
   public void testInetAddressSetConnection() throws Exception
   {
      InetAddress address = InetAddress.getByAddress(new byte[] { 10, 5, 5, 1 });
      MediaBuilder builder = MediaBuilder.create();
      builder.setConnection(address);
      Connection conn = builder.getConnection();
      assertEquals("10.5.5.1", conn.getAddress());
      org.junit.Assert.assertEquals(SdpConstants.ADDRESS_TYPE_IP4, conn.getAddressType());
      org.junit.Assert.assertEquals(SdpConstants.NETWORK_TYPE_INTERNET, conn.getNetworkType());
   }


   @Test
   public void testInvocationChaining() throws Exception
   {
      MediaBuilder builder = MediaBuilder.create();
      builder.setMedia("audio", 0, 1, "RTP/AVP", 97).setInfo("Simple Info").setKey("prompt",null);
      builder.addAttribute("rtpmap","97 mpeg4-generic/8000/2").addAttribute("control", "trackID=5");
      builder.addBandwidth(SdpConstants.BANDWIDTH_TYPE_AS, 356).setConnection(InetAddress.getLocalHost()).setInfo("Change Me");
      builder.setKey("clear","hello").setConnection("10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET).build();
   }

   @Test
   public void testExistingInitializer() {
      MediaBuilder original = MediaBuilder.create();
      original.setMedia("audio", 0, 1, "RTP/AVP", 97).setInfo("Simple Info").setKey("prompt",null);
      original.addAttribute("rtpmap", "97 mpeg4-generic/8000/2").addAttribute("control", "trackID=5");
      MediaDescription descOne = original.build();

      MediaBuilder existing = MediaBuilder.create(descOne);
      assertTrue(descOne.equals(existing.build()));

      assertFalse(descOne.equals(existing.setInfo("Modified Info").build()));
   }

   @Test
   public void testAttributes()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertEquals(0, builder.getAttributes().length);
      builder.addAttribute("rtpmap","97 mpeg4-generic/8000/2").addAttribute("control", "trackID=5").addAttribute("control","trackID=2");
      assertEquals(3, builder.getAttributes().length);
      assertNotNull(builder.getAttribute("control"));
      assertEquals("trackID=5", builder.getAttribute("control").getValue());
      assertEquals(2, builder.getAttributes("control").length);
      assertTrue(builder.removeAttributes("rtpmap"));
      assertTrue(builder.removeAttributes("control"));
      assertFalse(builder.removeAttributes("fmt"));
      assertNull(builder.getAttribute("control"));
   }

   @Test
   public void testBandwidths()
   {
      MediaBuilder builder = MediaBuilder.create();
      assertEquals(0, builder.getBandwidths().length);
      builder.addBandwidth(SdpConstants.BANDWIDTH_TYPE_AS, 365).addBandwidth(SdpConstants.BANDWIDTH_TYPE_AS, 1024);
      assertEquals(1024, builder.getBandwidth(SdpConstants.BANDWIDTH_TYPE_AS).getValue());
      assertTrue(builder.removeBandwidth(SdpConstants.BANDWIDTH_TYPE_AS));
      assertFalse(builder.removeBandwidth(SdpConstants.BANDWIDTH_TYPE_CT));
      assertEquals(0, builder.getBandwidths().length);
   }


}
