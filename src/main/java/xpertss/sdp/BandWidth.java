/*
 * BandWidth.java
 *
 * Created on December 18, 2001, 3:59 PM
 */

package xpertss.sdp;

import xpertss.lang.Numbers;
import xpertss.lang.Objects;
import xpertss.lang.Strings;

/**
 * A Bandwidth represents the b= fields contained within either a MediaDescription or
 * a SessionDescription.
 * <p>
 * This specifies the proposed bandwidth to be used by the session or media, and is
 * optional. Multiple bandwidth specifiers of different types may be associated with
 * the same SessionDescription. Each consists of a token type and an integer value
 * measuring bandwidth in kilobits per second.
 * <p>
 * RFC 4566 defines two bandwidth types (or modifiers):
 * <p><pre>
 * CT
 *     Conference Total: An implicit maximum bandwidth is associated with each TTL on
 *     the Mbone or within a particular multicast administrative scope region (the
 *     Mbone bandwidth vs. TTL limits are given in the MBone FAQ). If the bandwidth
 *     of a session or media in a session is different from the bandwidth implicit
 *     from the scope, a 'b=CT:...' line should be supplied for the session giving the
 *     proposed upper limit to the bandwidth used. The primary purpose of this is to
 *     give an approximate idea as to whether two or more conferences can co-exist
 *     simultaneously.
 * AS
 *     Application-Specific Maximum: The bandwidth is interpreted to be application-
 *     specific, i.e., will be the application's concept of maximum bandwidth.
 *     Normally this will coincide with what is set on the application's "maximum
 *     bandwidth" control if applicable.
 * </pre><p>
 * Note that CT gives a total bandwidth figure for all the media at all sites. AS gives
 * a bandwidth figure for a single media at a single site, although there may be many
 * sites sending simultaneously.
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */ 
public final class BandWidth extends Field {

   private String type;
   private int value;

   BandWidth(String type, int value)
   {
      this.type = Strings.notEmpty(type, "type may not be empty");
      this.value = Numbers.gte(0, value, "bandwidth may not be negative");
   }


   /**
    * Returns the bandwidth type.
    *
    * @see SdpConstants#BANDWIDTH_TYPE_CT
    * @see SdpConstants#BANDWIDTH_TYPE_AS
    */
   public String getType()
   {
      return type;
   }
    

   /**
    * Returns the bandwidth value measured in kilobits per second.
    */
   public int getValue()
   {
      return value;
   }


   @Override
   public char getTypeChar()
   {
      return 'b';
   }

   @Override
   public BandWidth clone() {
      try {
         return (BandWidth) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(type, value);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof BandWidth) {
         BandWidth bw = (BandWidth) o;
         return Objects.equal(bw.getType(), type) &&
               Objects.equal(bw.getValue(), value);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=").append(type).append(":").append(value);
      return buf.toString();
   }

}

