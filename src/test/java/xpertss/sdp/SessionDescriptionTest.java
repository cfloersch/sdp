/**
 * Created By: cfloersch
 * Date: 6/1/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static xpertss.sdp.SdpConstants.BANDWIDTH_TYPE_AS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class SessionDescriptionTest {

   private static final int VERSION = 0;
   private static final String NAME = "RTSP Stream";
   private static final String INFO = "Sample Info String";
   private static final String URI  = "http://simulcast.manheim.com/mediahelp";
   private static final String[] EMAILS = {"joe@nowhere.com", "simulcast@manheim.com"};
   private static final String[] PHONES = {"800-MANHEIM"};
   private static final TimeDescription[] TIMES = {new TimeDescription(new Time(50000,60000), null)};
   private static final TimeZones ZONES = new TimeZones(new TimeAdjustment(55000,3600));
   private static final Connection CONNECTION = new Connection("0.0.0.0", ADDRESS_TYPE_IP4, NETWORK_TYPE_INTERNET);
   private static final BandWidth[] BANDWIDTHS = {new BandWidth(BANDWIDTH_TYPE_AS, 0)};
   private static final Key KEY = new Key("prompt",null);
   private static final Attribute[] ATTRIBUTES = {new Attribute("control", "*"), new Attribute("source-filter", "incl IN IP4 239.128.1.1 172.16.10.1")};




   private SessionDescription objectUnderTest;

   @Before
   public void setUp()
   {
      OriginBuilder origin = OriginBuilder.create();
      origin.setUsername("RTSP").setSessionId("2285599416").setSessionVersion(34);
      origin.setAddress("172.16.10.1").setAddressType(ADDRESS_TYPE_IP4).setNetworkType(NETWORK_TYPE_INTERNET);

      MediaBuilder audio = MediaBuilder.create();
      audio.setMedia("audio", 0, 1, "RTP/AVP", 97);
      audio.addAttribute("control", "trackID=5").addAttribute("rtpmap","97 mpeg4-generic/8000/2");
      audio.addAttribute("fmtp","97 profile-level-id=15; mode=AAC-hbr;");

      MediaBuilder video = MediaBuilder.create();
      video.setMedia("video", 0, 1, "RTP/AVP", 98);
      video.addAttribute("control", "trackID=2").addAttribute("rtpmap","98 H264/30000");
      video.addAttribute("fmtp", "98 packetization-mode=1; profile-level-id=42801f;");

      objectUnderTest = new SessionDescription(VERSION, origin.build(), NAME, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
                              CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[] { audio.build(), video.build() });
   }


   @Test(expected = NullPointerException.class)
   public void testConstructionNullOrigin()
   {
      new SessionDescription(VERSION, null, NAME, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeVersion()
   {
      new SessionDescription(-1, OriginBuilder.create().build(), NAME, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullSessionName()
   {
      new SessionDescription(VERSION, OriginBuilder.create().build(), null, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptySessionName()
   {
      new SessionDescription(VERSION, OriginBuilder.create().build(), " ", INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
   }


   @Test
   public void testSimpleGetters()
   {
      assertEquals(VERSION, objectUnderTest.getVersion());
      assertEquals(NAME, objectUnderTest.getSessionName());
      assertEquals(INFO, objectUnderTest.getInfo());
      assertEquals(URI, objectUnderTest.getUri());
      assertEquals(CONNECTION, objectUnderTest.getConnection());
      assertEquals(ZONES, objectUnderTest.getTimeZones());
      assertEquals(KEY, objectUnderTest.getKey());
   }

   @Test
   public void testStringArrayGetters()
   {
      assertTrue(Arrays.equals(EMAILS, objectUnderTest.getEmails()));
      assertTrue(Arrays.equals(PHONES, objectUnderTest.getPhones()));
      objectUnderTest = new SessionDescription(VERSION, OriginBuilder.create().build(), NAME, INFO, URI, null, null, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
      assertEquals(0, objectUnderTest.getEmails().length);
      assertEquals(0, objectUnderTest.getPhones().length);
   }

   @Test
   public void testTimeGetters()
   {
      TimeDescription[] desc = objectUnderTest.getTimeDescriptions();
      assertTrue(desc.length == 1);
      assertTrue(desc[0].getRepeatTimes().length == 0);
      assertTrue(desc[0].getTime().getStart() != null);
      assertTrue(desc[0].getTime().getStop() != null);
      objectUnderTest = new SessionDescription(VERSION, OriginBuilder.create().build(), NAME, INFO, URI, EMAILS, PHONES, null, ZONES,
            CONNECTION, BANDWIDTHS, KEY, ATTRIBUTES, new MediaDescription[0]);
      assertEquals(0, objectUnderTest.getTimeDescriptions().length);
   }

   @Test
   public void testOriginGetter()
   {
      Origin origin = objectUnderTest.getOrigin();
      assertEquals("RTSP", origin.getUsername());
      assertEquals("2285599416", origin.getSessionId());
      assertEquals("172.16.10.1", origin.getAddress());
   }

   @Test
   public void testBandwidthGetter()
   {
      assertEquals(1, objectUnderTest.getBandwidths().length);
      assertNotNull(objectUnderTest.getBandwidth(BANDWIDTH_TYPE_AS));
      objectUnderTest = new SessionDescription(VERSION, OriginBuilder.create().build(), NAME, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, null, KEY, ATTRIBUTES, new MediaDescription[0]);
      assertEquals(0, objectUnderTest.getBandwidths().length);
   }

   @Test
   public void testAttributeGetter()
   {
      assertEquals(2, objectUnderTest.getAttributes().length);
      assertNotNull(objectUnderTest.getAttribute("control"));
      assertNotNull(objectUnderTest.getAttribute("source-filter"));
      objectUnderTest = new SessionDescription(VERSION, OriginBuilder.create().build(), NAME, INFO, URI, EMAILS, PHONES, TIMES, ZONES,
            CONNECTION, BANDWIDTHS, KEY, null, new MediaDescription[0]);
      assertEquals(0, objectUnderTest.getAttributes().length);
   }

   @Test
   public void testCopy()
   {
      SessionDescription copy = objectUnderTest.clone();
      assertFalse(objectUnderTest == copy);
      assertTrue(objectUnderTest.equals(copy));
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
   }

   @Test
   public void testToString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append("v=0").append(System.lineSeparator());
      buf.append("o=RTSP 2285599416 34 IN IP4 172.16.10.1").append(System.lineSeparator());
      buf.append("s=RTSP Stream").append(System.lineSeparator());
      buf.append("i=Sample Info String").append(System.lineSeparator());
      buf.append("u=http://simulcast.manheim.com/mediahelp").append(System.lineSeparator());
      buf.append("e=joe@nowhere.com").append(System.lineSeparator());
      buf.append("e=simulcast@manheim.com").append(System.lineSeparator());
      buf.append("p=800-MANHEIM").append(System.lineSeparator());
      buf.append("c=IN IP4 0.0.0.0").append(System.lineSeparator());
      buf.append("b=AS:0").append(System.lineSeparator());
      buf.append("t=50000 60000").append(System.lineSeparator());
      buf.append("z=55000 1h").append(System.lineSeparator());
      buf.append("k=prompt").append(System.lineSeparator());
      buf.append("a=control:*").append(System.lineSeparator());
      buf.append("a=source-filter:incl IN IP4 239.128.1.1 172.16.10.1").append(System.lineSeparator());
      buf.append("m=audio 0 RTP/AVP 97").append(System.lineSeparator());
      buf.append("a=control:trackID=5").append(System.lineSeparator());
      buf.append("a=rtpmap:97 mpeg4-generic/8000/2").append(System.lineSeparator());
      buf.append("a=fmtp:97 profile-level-id=15; mode=AAC-hbr;").append(System.lineSeparator());
      buf.append("m=video 0 RTP/AVP 98").append(System.lineSeparator());
      buf.append("a=control:trackID=2").append(System.lineSeparator());
      buf.append("a=rtpmap:98 H264/30000").append(System.lineSeparator());
      buf.append("a=fmtp:98 packetization-mode=1; profile-level-id=42801f;").append(System.lineSeparator());
      assertEquals(buf.toString(), objectUnderTest.toString());
   }

}
