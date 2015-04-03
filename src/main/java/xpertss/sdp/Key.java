/*
 * Key.java
 *
 * Created on December 19, 2001, 10:10 AM
 */

package xpertss.sdp;

import xpertss.lang.Objects;
import xpertss.lang.Strings;

/**
 * A Key represents the k= field contained within either a MediaDescription or a
 * SessionDescription.
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class Key extends Field {

   private String method;
   private String key;

   Key(String method, String key)
   {
      this.method = Strings.notEmpty(method, "method may not be empty");
      this.key = Strings.nullIfEmpty(Strings.trim(key));
   }
    
   /**
    * Returns the name of this attribute
    */
   public String getMethod()
   {
      return method;
   }


   /**
    * Returns the value of this attribute.
    */
   public String getKey()
   {
      return key;
   }

   @Override
   public char getTypeChar()
   {
      return 'k';
   }

   @Override
   public Key clone() {
      try {
         return (Key) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }



   @Override
   public int hashCode()
   {
      return Objects.hash(method, key);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof Key) {
         Key k = (Key) o;
         return Objects.equal(k.getMethod(), method) &&
               Objects.equal(k.getKey(), key);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=").append(method);
      if(!Strings.isEmpty(key)) buf.append(":").append(key);
      return buf.toString();
   }

}

