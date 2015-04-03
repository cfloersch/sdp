/**
 * Created By: cfloersch
 * Date: 6/3/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;
import xpertss.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class SessionParserTest {

   private SessionParser objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new SessionParser();
   }



   @Test(expected = NullPointerException.class)
   public void testParseNullContent()
   {
      objectUnderTest.parse((String)null);
   }

   @Test(expected = NullPointerException.class)
   public void testParseNullReader()
   {
      objectUnderTest.parse((Reader)null);
   }

   @Test(expected = NullPointerException.class)
   public void testParseNullPath() throws IOException
   {
      objectUnderTest.parse((Path)null);
   }

   @Test(expected = NullPointerException.class)
   public void testParseNullStream()
   {
      objectUnderTest.parse((InputStream)null);
   }




   @Test(expected = SdpParseException.class)
   public void testEmptyContent()
   {
      objectUnderTest.parse("");
   }

   @Test(expected = SdpParseException.class)
   public void testGibberish()
   {
      objectUnderTest.parse("gibberish");
   }


   @Test(expected = SdpParseException.class)
   public void testMissingVersion()
   {
      objectUnderTest.parse("o=joe 1234 2345 IN IP4 10.0.0.1");
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidVersion()
   {
      objectUnderTest.parse("v=hello");
   }

   @Test(expected = SdpParseException.class)
   public void testMissingOrigin()
   {
      objectUnderTest.parse("v=0");
   }

   @Test(expected = SdpParseException.class)
   public void testIncompleteOrigin()
   {
      objectUnderTest.parse("v=0\r\no=joe 1234 2345 IN IP4");
   }

   @Test(expected = SdpParseException.class)
   public void testOriginInvalidSessionId()
   {
      objectUnderTest.parse("v=0\r\no=joe 1f34 2345 IN IP4");
   }

   @Test(expected = SdpParseException.class)
   public void testOriginInvalidVersion()
   {
      objectUnderTest.parse("v=0\r\no=joe 1234 2h45 IN IP4");
   }

   @Test(expected = SdpParseException.class)
   public void testMissingSessionName()
   {
      objectUnderTest.parse("v=0\r\no=joe 1234 2345 IN IP4 10.0.0.1\r\n");
   }

   @Test(expected = SdpParseException.class)
   public void testBlankLine() throws Exception
   {
      objectUnderTest.parse(load("/blank-line.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInfoOutOfOrder() throws Exception
   {
      objectUnderTest.parse(load("/info-out-of-order.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidConnection() throws Exception
   {
      objectUnderTest.parse(load("/invalid-connection.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidBandwidth() throws Exception
   {
      objectUnderTest.parse(load("/invalid-bandwidth.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testIncompleteBandwidth() throws Exception
   {
      objectUnderTest.parse(load("/incomplete-bandwidth.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testMissingTime() throws Exception
   {
      objectUnderTest.parse(load("/missing-time.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidTime() throws Exception
   {
      objectUnderTest.parse(load("/invalid-time.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testIncompleteTime() throws Exception
   {
      objectUnderTest.parse(load("/incomplete-time.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidRepeat() throws Exception
   {
      objectUnderTest.parse(load("/invalid-repeat.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testIncompleteRepeat() throws Exception
   {
      objectUnderTest.parse(load("/incomplete-repeat.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidTimezone() throws Exception
   {
      objectUnderTest.parse(load("/invalid-timezone.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testIncompleteTimezone() throws Exception
   {
      objectUnderTest.parse(load("/incomplete-timezone.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidField() throws Exception
   {
      objectUnderTest.parse(load("/invalid-field.sdp"));
   }

   @Test(expected = SdpParseException.class)
   public void testInvalidLine() throws Exception
   {
      objectUnderTest.parse(load("/invalid-line.sdp"));
   }

   // Single email
   @Test
   public void testSingleEmail() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/single-email.sdp"));
      assertEquals(1, desc.getEmails().length);
   }

   // Dual Email
   @Test
   public void testDualEmail() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/dual-email.sdp"));
      assertEquals(2, desc.getEmails().length);
   }

   // Dual Email/Dual Phone
   @Test
   public void testDualEmailDualPhone() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/dual-email-phone.sdp"));
      assertEquals(2, desc.getEmails().length);
      assertEquals(2, desc.getPhones().length);
   }

   // Dual Bandwidth
   @Test
   public void testDualBandwidth() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/dual-bandwidth.sdp"));
      assertEquals(0, desc.getBandwidth("RR").getValue());
      assertEquals(0, desc.getBandwidth("RS").getValue());
   }

   // Time with Single Repeat
   @Test
   public void testSingleRepeat() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/single-repeat.sdp"));
      assertEquals(1, desc.getTimeDescriptions()[0].getRepeatTimes().length);
   }

   // Time with Dual Repeat
   @Test
   public void testDualRepeat() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/dual-repeat.sdp"));
      assertEquals(2, desc.getTimeDescriptions()[0].getRepeatTimes().length);
   }

   // Time with Multi-Whitespace Repeat
   @Test
   public void testTimeMultiWhitespace() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/time-multi-white.sdp"));
      assertEquals(Utils.toDate(2873404696L), desc.getTimeDescriptions()[0].getTime().getStop());
   }

   // Time with Multi-Whitespace Repeat
   @Test
   public void testRepeatMultiWhitespace() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/repeat-multi-white.sdp"));
      assertEquals(3600L, desc.getTimeDescriptions()[0].getRepeatTimes()[0].getDuration());
   }

   // Origin with multiple whitespace
   @Test
   public void testOriginMultiWhitespace() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/origin-multi-white.sdp"));
      assertEquals(2890842807L, desc.getOrigin().getSessionVersion());
   }

   // Conn with multiple whitespace
   @Test
   public void testConnMultiWhitespace() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/conn-multi-white.sdp"));
      assertEquals("IP4", desc.getConnection().getAddressType());
   }

   // Key no value
   @Test
   public void testKeyNoValue() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/key-no-value.sdp"));
      assertEquals("prompt", desc.getKey().getMethod());
      assertNull(desc.getKey().getKey());
   }


   // Key with value
   @Test
   public void testKeyWithValue() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/key-base64.sdp"));
      assertEquals("base64", desc.getKey().getMethod());
      assertNotNull(desc.getKey().getKey());
   }

   // Key no value, trailing :
   @Test
   public void testKeyTrailingColon() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/key-trailing-colon.sdp"));
      assertEquals("prompt", desc.getKey().getMethod());
      assertNull(desc.getKey().getKey());
   }

   // Timezone single offset
   @Test
   public void testTimeZoneSingleOffset() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/timezone-single-offset.sdp"));
      assertEquals(-3600, desc.getTimeZones().getAdjustments()[0].getOffset());
   }

   // Timezone dual offsets
   @Test
   public void testTimeZoneDualOffset() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/timezone-dual-offset.sdp"));
      assertEquals(0, desc.getTimeZones().getAdjustments()[1].getOffset());
   }

   // Attribute no value
   @Test
   public void testAttributeNoValue() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/attr-no-value.sdp"));
      assertNull(desc.getAttribute("recvonly").getValue());
   }

   // Attribute with value
   @Test
   public void testAttributeWithValue() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/attr-with-value.sdp"));
      assertEquals("*", desc.getAttribute("control").getValue());
   }

   // Attribute with multiple colons
   @Test
   public void testAttributeMulticolon() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/attr-multi-colon.sdp"));
      assertEquals("1:2", desc.getAttribute("control").getValue());
   }

   // Attribute no value trailing :
   @Test
   public void testAttributeTrailingColon() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/attr-trailing-colon.sdp"));
      assertNull(desc.getAttribute("recvonly").getValue());
   }


   // Media with multiple whitespace
   @Test
   public void testMediaMultiWhitespace() throws Exception
   {
      SessionDescription desc = objectUnderTest.parse(load("/media-multi-white.sdp"));
      assertEquals(49170, desc.getMediaDescriptions()[0].getMedia().getPort());
   }




   @Test
   public void testMinimal() throws Exception
   {
      String minimal = load("/minimal.sdp");
      SessionDescription desc = objectUnderTest.parse(minimal);
      assertEquals(0, desc.getMediaDescriptions().length);
      assertEquals(minimal.trim(), desc.toString().trim());
   }

   @Test
   public void testExample() throws Exception
   {
      String minimal = load("/example.sdp");
      SessionDescription desc = objectUnderTest.parse(minimal);
      assertEquals(2, desc.getMediaDescriptions().length);
      assertEquals(minimal.trim(), desc.toString().trim());
   }

   @Test
   public void testManheim() throws Exception
   {
      String minimal = load("/manheim.sdp");
      SessionDescription desc = objectUnderTest.parse(minimal);
      assertEquals(2, desc.getMediaDescriptions().length);
      assertEquals(minimal.trim(), desc.toString().trim());
   }



   private String load(String name) throws IOException
   {
      return IOUtils.toString(getClass().getResource(name).openStream(), Charset.forName("UTF-8"), true);
   }



}
