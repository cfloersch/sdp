/**
 * Created By: cfloersch
 * Date: 5/30/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MediaTest {

   private static final String TYPE = "video";
   private static final String PROTO = "RTP/AVP";
   private static final int FORMAT = 98;
   private static final int PORT = 0;


   private Media objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Media(TYPE, PORT, 1, PROTO, FORMAT);
   }


   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullFormats()
   {
      new Media(TYPE,PORT,1,PROTO, null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyFormats()
   {
      new Media(TYPE,PORT,1,PROTO, new int[0]);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNegativeCount()
   {
      new Media(TYPE,PORT,-1,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionZeroCount()
   {
      new Media(TYPE,PORT,0,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyType()
   {
      new Media("",PORT,1,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullType()
   {
      new Media(null,PORT,1,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionInvalidPortNegative()
   {
      new Media(TYPE,-1,1,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionInvalidPortOutOfRange()
   {
      new Media(TYPE,65536,1,PROTO, FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionInvalidCount()
   {
      new Media(TYPE,PORT,0,PROTO, FORMAT);
   }



   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyProtocol()
   {
      new Media(TYPE,PORT,1," ", FORMAT);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullProtocol()
   {
      new Media(TYPE,PORT,1,null, FORMAT);
   }





   @Test
   public void testGetters()
   {
      assertEquals(TYPE, objectUnderTest.getType());
      assertEquals(PROTO, objectUnderTest.getProtocol());
      assertEquals(PORT, objectUnderTest.getPort());
      assertEquals(1, objectUnderTest.getPortCount());
   }

   @Test
   public void testGetFormats()
   {
      assertEquals(1, objectUnderTest.getFormats().length);
      assertEquals(FORMAT, objectUnderTest.getFormats()[0]);

      int[] formats = new int[] { 0, 1, 2};
      objectUnderTest = new Media(TYPE, PORT, 1, PROTO, formats);
      assertEquals(3, objectUnderTest.getFormats().length);
      assertEquals(0, objectUnderTest.getFormats()[0]);
      assertEquals(1, objectUnderTest.getFormats()[1]);
      assertEquals(2, objectUnderTest.getFormats()[2]);
      assertFalse(formats == objectUnderTest.getFormats());
      formats[0] = 10;
      assertEquals(0, objectUnderTest.getFormats()[0]);
      objectUnderTest.getFormats()[0] = 10;
      assertEquals(0, objectUnderTest.getFormats()[0]);
   }

   @Test
   public void testTypeChar()
   {
      assertEquals('m', objectUnderTest.getTypeChar());  // always should be 'm'
   }

   @Test
   public void testCopy()
   {
      Media copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertEquals(TYPE, copy.getType());
      assertEquals(PROTO, copy.getProtocol());
      assertEquals(PORT, copy.getPort());
      assertEquals(1, copy.getPortCount());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("m=video 0 RTP/AVP 98", objectUnderTest.toString());
      objectUnderTest = new Media(TYPE, PORT, 2, PROTO, FORMAT);
      assertEquals("m=video 0/2 RTP/AVP 98", objectUnderTest.toString());
      objectUnderTest = new Media(TYPE, PORT, 1, PROTO, FORMAT, 88);
      assertEquals("m=video 0 RTP/AVP 98 88", objectUnderTest.toString());
   }


}
