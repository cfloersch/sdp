/**
 * Created By: cfloersch
 * Date: 5/27/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyTest {

   private static final String METHOD = "base64";
   private static final String KEY = "sfshkgskfhdkdfgdf==";

   private Key objectUnderTest;

   @Before
   public void setUp()
   {
      objectUnderTest = new Key(METHOD, KEY);
   }


   @Test(expected = IllegalArgumentException.class)
   public void testConstructionNullMethod()
   {
      new Key(null, KEY);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructionEmptyMethod()
   {
      new Key("", KEY);
   }

   @Test
   public void testConstructionEmptyKey()
   {
      Key key = new Key(METHOD, "");
      assertNull(key.getKey());
   }

   @Test
   public void testGetters()
   {
      assertEquals(METHOD, objectUnderTest.getMethod());
      assertEquals(KEY, objectUnderTest.getKey());
   }

   @Test
   public void testTypeChar()
   {
      // BandWidth should always be 'k'
      assertEquals('k', objectUnderTest.getTypeChar());
   }

   @Test
   public void testHashCode()
   {
      Key replica = new Key(METHOD, KEY);
      assertTrue(objectUnderTest.hashCode() == replica.hashCode());
      Key diffKey = new Key(METHOD, "sfsdjkskkk==");
      assertFalse(objectUnderTest.hashCode() == diffKey.hashCode());
      Key diffMethod = new Key("clear", KEY);
      assertFalse(objectUnderTest.hashCode() == diffMethod.hashCode());
   }

   @Test
   public void testEquals()
   {
      Key replica = new Key(METHOD, KEY);
      assertTrue(objectUnderTest.equals(replica));

      Key diffKey = new Key(METHOD, "sfsdjkskkk==");
      assertFalse(objectUnderTest.equals(diffKey));

      Key diffMethod = new Key("clear", KEY);
      assertFalse(objectUnderTest.equals(diffMethod));
   }

   @Test
   public void testCopy()
   {
      Key copy = objectUnderTest.clone();
      assertFalse(copy == objectUnderTest);
      assertEquals(METHOD, copy.getMethod());
      assertEquals(KEY, copy.getKey());
      assertEquals(objectUnderTest.hashCode(), copy.hashCode());
      assertTrue(objectUnderTest.equals(copy));
   }

   @Test
   public void testToString()
   {
      assertEquals("k=base64:sfshkgskfhdkdfgdf==", objectUnderTest.toString());
      objectUnderTest = new Key("prompt", "");
      assertEquals("k=prompt", objectUnderTest.toString());
      objectUnderTest = new Key("prompt", null);
      assertEquals("k=prompt", objectUnderTest.toString());
   }


}
