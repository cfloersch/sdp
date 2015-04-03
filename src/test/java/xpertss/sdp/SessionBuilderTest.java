/**
 * Created By: cfloersch
 * Date: 6/2/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.URI;
import java.util.Date;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.BANDWIDTH_TYPE_AS;
import static xpertss.sdp.SdpConstants.BANDWIDTH_TYPE_CT;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class SessionBuilderTest {

   private SessionBuilder objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = SessionBuilder.create();
   }


   @Test
   public void testVersion()
   {
      assertEquals(0, objectUnderTest.getVersion());
      objectUnderTest.setVersion(1).setVersion(2);
      assertEquals(2, objectUnderTest.getVersion());
      assertEquals(2, objectUnderTest.build().getVersion());
      objectUnderTest.setVersion(0);
      assertEquals(0, objectUnderTest.getVersion());
      assertEquals(0, objectUnderTest.build().getVersion());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetNegativeVersion()
   {
      SessionBuilder.create().setVersion(-1);
   }

   @Test
   public void testOrigin()
   {
      assertNull(objectUnderTest.getOrigin());
      assertNotNull(objectUnderTest.build().getOrigin());
      OriginBuilder origin = OriginBuilder.create();
      objectUnderTest.setOrigin(origin.setUsername("joe").build()).setOrigin(origin.setSessionId(200).build());
      assertEquals("joe", objectUnderTest.getOrigin().getUsername());
      assertEquals(200L, objectUnderTest.getOrigin().getSessionId());
      assertEquals("joe", objectUnderTest.build().getOrigin().getUsername());
      assertEquals(200L, objectUnderTest.build().getOrigin().getSessionId());
      objectUnderTest.setOrigin(null);
      assertNull(objectUnderTest.getOrigin());
      assertNotNull(objectUnderTest.build().getOrigin());
   }

   @Test
   public void testSessionName()
   {
      assertNull(objectUnderTest.getSessionName());
      objectUnderTest.setSessionName("Test1").setSessionName("Test2");
      assertEquals("Test2", objectUnderTest.getSessionName());
      assertEquals("Test2", objectUnderTest.build().getSessionName());
      objectUnderTest.setSessionName(null);
      assertNull(objectUnderTest.getSessionName());
      assertEquals("SessionName", objectUnderTest.build().getSessionName());
      objectUnderTest.setSessionName("    ");
      assertNull(objectUnderTest.getSessionName());
      assertEquals("SessionName", objectUnderTest.build().getSessionName());
   }

   @Test
   public void testInfo()
   {
      assertNull(objectUnderTest.getInfo());
      assertNull(objectUnderTest.build().getInfo());
      objectUnderTest.setInfo("Info1").setInfo("Info2");
      assertEquals("Info2", objectUnderTest.getInfo());
      assertEquals("Info2", objectUnderTest.build().getInfo());
      objectUnderTest.setInfo(null);
      assertNull(objectUnderTest.getInfo());
      assertNull(objectUnderTest.build().getInfo());
      objectUnderTest.setInfo(" ");
      assertNull(objectUnderTest.getInfo());
      assertNull(objectUnderTest.build().getInfo());
   }

   @Test
   public void testUri()
   {
      assertNull(objectUnderTest.getUri());
      assertNull(objectUnderTest.build().getUri());
      objectUnderTest.setUri("Uri1").setUri("Uri2");
      assertEquals("Uri2", objectUnderTest.getUri());
      assertEquals("Uri2", objectUnderTest.build().getUri());
      objectUnderTest.setUri((String) null);
      assertNull(objectUnderTest.getUri());
      assertNull(objectUnderTest.build().getUri());
      objectUnderTest.setUri(" ");
      assertNull(objectUnderTest.getUri());
      assertNull(objectUnderTest.build().getUri());
      URI uri = URI.create("http://simulcast.manheim.com");
      objectUnderTest.setUri(uri);
      assertEquals("http://simulcast.manheim.com", objectUnderTest.getUri());
      assertEquals("http://simulcast.manheim.com", objectUnderTest.build().getUri());
      objectUnderTest.setUri((URI) null);
      assertNull(objectUnderTest.getUri());
      assertNull(objectUnderTest.build().getUri());
   }


   @Test
   public void testEmails()
   {
      assertEquals(0, objectUnderTest.getEmails().length);
      assertEquals(0, objectUnderTest.build().getEmails().length);
      objectUnderTest.addEmail("joe").addEmail("fred");
      assertEquals(2, objectUnderTest.getEmails().length);
      assertEquals(2, objectUnderTest.build().getEmails().length);
      assertTrue(objectUnderTest.removeEmail("joe"));
      assertEquals(1, objectUnderTest.getEmails().length);
      assertEquals(1, objectUnderTest.build().getEmails().length);
      assertFalse(objectUnderTest.removeEmail("joe"));
      assertEquals(1, objectUnderTest.getEmails().length);
      assertEquals(1, objectUnderTest.build().getEmails().length);
   }

   @Test
   public void testEmailMutability()
   {
      objectUnderTest.addEmail("joe").addEmail("fred");
      String[] emails = objectUnderTest.getEmails();
      emails[0] = "george";
      emails[1] = "john";
      String[] replica = objectUnderTest.getEmails();
      assertEquals("joe", replica[0]);
      assertEquals("fred", replica[1]);
   }

   @Test
   public void testPhones()
   {
      assertEquals(0, objectUnderTest.getPhones().length);
      assertEquals(0, objectUnderTest.build().getPhones().length);
      objectUnderTest.addPhone("joe").addPhone("fred");
      assertEquals(2, objectUnderTest.getPhones().length);
      assertEquals(2, objectUnderTest.build().getPhones().length);
      assertTrue(objectUnderTest.removePhone("joe"));
      assertEquals(1, objectUnderTest.getPhones().length);
      assertEquals(1, objectUnderTest.build().getPhones().length);
      assertFalse(objectUnderTest.removePhone("joe"));
      assertEquals(1, objectUnderTest.getPhones().length);
      assertEquals(1, objectUnderTest.build().getPhones().length);
   }

   @Test
   public void testPhoneMutability()
   {
      objectUnderTest.addPhone("joe").addPhone("fred");
      String[] phones = objectUnderTest.getPhones();
      phones[0] = "george";
      phones[1] = "john";
      String[] replica = objectUnderTest.getPhones();
      assertEquals("joe", replica[0]);
      assertEquals("fred", replica[1]);
   }

   @Test
   public void testTimeDescription()
   {
      TimeBuilder builder = TimeBuilder.create();
      assertEquals(0, objectUnderTest.getTimeDescriptions().length);
      assertEquals(1, objectUnderTest.build().getTimeDescriptions().length);
      TimeDescription one = builder.build();
      TimeDescription two = builder.setTime(new Date(), null).build();
      objectUnderTest.addTimeDescription(one).addTimeDescription(two);
      assertEquals(2, objectUnderTest.getTimeDescriptions().length);
      assertEquals(2, objectUnderTest.build().getTimeDescriptions().length);
      assertEquals(two, objectUnderTest.getTimeDescriptions()[1]);
      assertEquals(two, objectUnderTest.build().getTimeDescriptions()[1]);
      assertTrue(objectUnderTest.removeTimeDescription(one));
      assertEquals(1, objectUnderTest.getTimeDescriptions().length);
      assertEquals(1, objectUnderTest.build().getTimeDescriptions().length);
      assertEquals(two, objectUnderTest.getTimeDescriptions()[0]);
      assertEquals(two, objectUnderTest.build().getTimeDescriptions()[0]);
      objectUnderTest.clearTimeDescriptions().addTimeDescription(one);
      assertEquals(1, objectUnderTest.getTimeDescriptions().length);
      assertEquals(1, objectUnderTest.build().getTimeDescriptions().length);
      assertEquals(one, objectUnderTest.getTimeDescriptions()[0]);
      assertEquals(one, objectUnderTest.build().getTimeDescriptions()[0]);
      objectUnderTest.clearTimeDescriptions();
      assertEquals(0, objectUnderTest.getTimeDescriptions().length);
      assertEquals(1, objectUnderTest.build().getTimeDescriptions().length);
   }


   @Test
   public void testTimeAdjustment()
   {
      assertNull(objectUnderTest.build().getTimeZones());
      objectUnderTest.addTimeAdjustment(new Date(500), 3600).addTimeAdjustment(new Date(5000), -3600);
      assertEquals(2, objectUnderTest.getTimeAdjustments().length);
      assertNotNull(objectUnderTest.build().getTimeZones());
      assertEquals(2, objectUnderTest.build().getTimeZones().getAdjustments().length);
      objectUnderTest.removeTimeAdjustment(new TimeAdjustment(Utils.toNtpTime(new Date(500)), 3600));
      assertEquals(1, objectUnderTest.getTimeAdjustments().length);
      assertNotNull(objectUnderTest.build().getTimeZones());
      assertEquals(1, objectUnderTest.build().getTimeZones().getAdjustments().length);
      SessionDescription desc = objectUnderTest.clearTimeAdjustments().build();
      assertNull(desc.getTimeZones());
   }


   @Test
   public void testConnection() throws Exception
   {
      assertNull(objectUnderTest.getConnection());
      assertNotNull(objectUnderTest.setConnection("10.0.0.1", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET).getConnection());
      InetAddress address = InetAddress.getByAddress(new byte[] { 10, 5, 5, 1 });
      Connection conn = objectUnderTest.setConnection(address).getConnection();
      assertEquals("10.5.5.1", conn.getAddress());
      Assert.assertEquals(SdpConstants.ADDRESS_TYPE_IP4, conn.getAddressType());
      Assert.assertEquals(SdpConstants.NETWORK_TYPE_INTERNET, conn.getNetworkType());
      assertNull(objectUnderTest.clearConnection().getConnection());
   }

   @Test
   public void testAttributes()
   {
      assertEquals(0, objectUnderTest.getAttributes().length);
      objectUnderTest.addAttribute("range","npt=0-").addAttribute("control", "track5").addAttribute("control","*");
      assertEquals(3, objectUnderTest.getAttributes().length);
      assertNotNull(objectUnderTest.getAttribute("control"));
      assertEquals("track5", objectUnderTest.getAttribute("control").getValue());
      assertEquals(2, objectUnderTest.getAttributes("control").length);
      assertTrue(objectUnderTest.removeAttributes("range"));
      assertTrue(objectUnderTest.removeAttributes("control"));
      assertFalse(objectUnderTest.removeAttributes("fmt"));
      assertNull(objectUnderTest.getAttribute("control"));
   }

   @Test
   public void testBandwidths()
   {
      assertEquals(0, objectUnderTest.getBandwidths().length);
      objectUnderTest.addBandwidth(SdpConstants.BANDWIDTH_TYPE_AS, 365).addBandwidth(SdpConstants.BANDWIDTH_TYPE_AS, 1024);
      assertEquals(1024, objectUnderTest.getBandwidth(SdpConstants.BANDWIDTH_TYPE_AS).getValue());
      assertTrue(objectUnderTest.removeBandwidth(SdpConstants.BANDWIDTH_TYPE_AS));
      assertFalse(objectUnderTest.removeBandwidth(SdpConstants.BANDWIDTH_TYPE_CT));
      assertEquals(0, objectUnderTest.getBandwidths().length);
   }

   @Test
   public void testKey()
   {
      assertNull(objectUnderTest.getKey());
      assertNull(objectUnderTest.build().getKey());
      objectUnderTest.setKey("clear","0xff00f01a").setKey("prompt", null);
      assertNotNull(objectUnderTest.getKey());
      assertNotNull(objectUnderTest.build().getKey());
      assertEquals("prompt", objectUnderTest.getKey().getMethod());
      assertEquals("prompt", objectUnderTest.build().getKey().getMethod());
      assertNull(objectUnderTest.clearKey().getKey());
      assertNull(objectUnderTest.build().getKey());
   }

   @Test
   public void testMediaDescriptions()
   {
      MediaDescription audio = MediaBuilder.create().setMedia("audio", 0, 1, "RTP/AVP", 31).build();
      MediaDescription video = MediaBuilder.create().setMedia("video", 0, 1, "RTP/AVP", 98).build();
      assertEquals(0, objectUnderTest.getMediaDescriptions().length);
      objectUnderTest.addMediaDescription(audio).addMediaDescription(video);
      assertEquals(2, objectUnderTest.getMediaDescriptions().length);
      assertEquals("audio", objectUnderTest.getMediaDescriptions()[0].getMedia().getType());
      assertEquals("video", objectUnderTest.getMediaDescriptions()[1].getMedia().getType());
      objectUnderTest.removeMediaDescription(audio);
      assertEquals(1, objectUnderTest.getMediaDescriptions().length);
      assertEquals("video", objectUnderTest.getMediaDescriptions()[0].getMedia().getType());
      assertEquals(0, objectUnderTest.clearMediaDescriptions().getMediaDescriptions().length);
   }


   @Test
   public void testCreateFromExisting()
   {
      SessionDescription desc = objectUnderTest.setVersion(1).setInfo("Info").setKey("prompt", null).build();
      objectUnderTest = SessionBuilder.create(desc);
      assertEquals(desc, objectUnderTest.build());
      assertNotNull(objectUnderTest.getOrigin());
      assertNotNull(objectUnderTest.getSessionName());
   }

}
