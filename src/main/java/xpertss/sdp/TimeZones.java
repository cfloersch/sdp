/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import java.util.Arrays;

/**
 * TimeZones represents the SDP z= field. A TimeZones object combined with its contained
 * TimeAdjustments specify timezone changes which take place during or in between sessions
 * announced in a session description.
 * <p>
 * From RFC 4566:
 * <p><pre>
 *     To schedule a repeated session which spans a change from daylight-saving
 *     time to standard time or vice-versa, it is necessary to specify offsets
 *     from the base repeat times. This is required because different time zones
 *     change time at different times of day, different countries change to or
 *     from daylight time on different dates, and some countries do not have
 *     daylight saving time at all.
 *
 *     Thus in order to schedule a session that is at the same time winter and
 *     summer, it must be possible to specify unambiguously by whose time zone
 *     a session is scheduled. To simplify this task for receivers, we allow the
 *     sender to specify the NTP time that a time zone adjustment happens and the
 *     offset from the time when the session was first scheduled. The "z" field
 *     allows the sender to specify a list of these adjustment times and offsets
 *     from the base time.
 * </pre><p>
 * <pre>
 *    z=<adjustment time> <offset> <adjustment time> <offset>
 * </pre><p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class TimeZones extends Field {

   private TimeAdjustment[] adjustments;

   TimeZones(TimeAdjustment ... adjustments)
   {
      this.adjustments = Utils.hasItem(adjustments, "time zones must have at least one adjustment").clone();
   }

   public TimeAdjustment[] getAdjustments()
   {
      return adjustments.clone();
   }


   @Override
   public char getTypeChar()
   {
      return 'z';
   }

   @Override
   public TimeZones clone() {
      try {
         return (TimeZones) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }


   @Override
   public int hashCode()
   {
      return Arrays.hashCode(adjustments);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof TimeZones) {
         TimeZones tz = (TimeZones) o;
         return Arrays.equals(tz.getAdjustments(), adjustments);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=");
      for(int i = 0; i < adjustments.length; i++) {
         if(i > 0) buf.append(" ");
         buf.append(adjustments[i]);
      }
      return buf.toString();
   }

}
