/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;
import static xpertss.sdp.SdpConstants.BANDWIDTH_TYPE_AS;
import static xpertss.sdp.SdpConstants.BANDWIDTH_TYPE_CT;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MediaDescriptionTest {

   private static final Media media = new Media("audio", 0, 1, "RTP/AVP", 97);
   private static final BandWidth[] bw = { new BandWidth(SdpConstants.BANDWIDTH_TYPE_AS, 320) };
   private static final Attribute[] atts = {
         new Attribute("rtpmap", "97 mpeg4-generic/8000/2"),
         new Attribute("control", "trackID=5"),
         new Attribute("fmtp", "97 streamtype=5; profile-level-id=15; mode=AAC-hbr; config=1410;SizeLength=13; IndexLength=3; IndexDeltaLength=3; CTSDeltaLength=0; DTSDeltaLength=0;"),
   };
   private static final Key key = new Key("prompt", null);
   private static final Connection conn = new Connection("232.128.1.2/15", SdpConstants.ADDRESS_TYPE_IP4, SdpConstants.NETWORK_TYPE_INTERNET);
   private static final String info = "This is an example info";

   private MediaDescription objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new MediaDescription(media, info, conn, bw, key, atts);
   }

   @Test(expected = NullPointerException.class)
   public void testConstructionNullMedia()
   {
      new MediaDescription(null, info, conn, bw, key, atts);
   }

   @Test
   public void testConstructionEmptyInfo()
   {
      MediaDescription desc = new MediaDescription(media, " ", conn, bw, key, atts);
      assertNull(desc.getInfo());
   }

   @Test
   public void testConstructionNullBandwidth()
   {
      MediaDescription desc = new MediaDescription(media, info, conn, null, key, atts);
      assertNotNull(desc.getBandwidths());
   }

   @Test
   public void testConstructionNullAttributes()
   {
      MediaDescription desc = new MediaDescription(media, info, conn, bw, key, null);
      assertNotNull(desc.getAttributes());
   }

   @Test
   public void testGetters()
   {
      assertEquals(media, objectUnderTest.getMedia());
      assertEquals(conn, objectUnderTest.getConnection());
      assertEquals(key, objectUnderTest.getKey());
      assertEquals(info, objectUnderTest.getInfo());
   }

   @Test
   public void testAttributes()
   {
      assertEquals(3, objectUnderTest.getAttributes().length);
      assertNotNull(objectUnderTest.getAttribute("control"));
      assertNotNull(objectUnderTest.getAttribute("rtpmap"));
      assertNotNull(objectUnderTest.getAttribute("fmtp"));
      assertNull(objectUnderTest.getAttribute("non-existing"));
      assertFalse(objectUnderTest.getAttributes() == atts);
   }

   @Test
   public void testBandwidths()
   {
      assertEquals(1, objectUnderTest.getBandwidths().length);
      assertNotNull(objectUnderTest.getBandwidth(SdpConstants.BANDWIDTH_TYPE_AS));
      assertNull(objectUnderTest.getBandwidth(SdpConstants.BANDWIDTH_TYPE_CT));
      assertFalse(objectUnderTest.getBandwidths() == bw);
   }

   @Test
   public void testCopy()
   {
      MediaDescription copy = objectUnderTest.clone();
      assertEquals(objectUnderTest.getKey(), copy.getKey());
      assertEquals(objectUnderTest.getMedia(), copy.getMedia());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
      assertTrue(objectUnderTest.toString().equals(copy.toString()));
   }

   @Test
   public void testToString()
   {
      String[] lines = objectUnderTest.toString().split(System.lineSeparator());
      assertEquals(8, lines.length);
      assertEquals(media.toString(), lines[0]);
      assertTrue(lines[1].startsWith("i=") && lines[1].contains(info));
      assertEquals(conn.toString(), lines[2]);
      assertEquals(bw[0].toString(), lines[3]);
      assertEquals(key.toString(), lines[4]);
      assertEquals(atts[0].toString(), lines[5]);
      assertEquals(atts[1].toString(), lines[6]);
      assertEquals(atts[2].toString(), lines[7]);
   }
}
