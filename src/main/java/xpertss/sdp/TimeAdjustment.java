/*
 * TimeZoneAdjustment.java
 *
 * Created on January 9, 2002, 11:20 AM
 */

package xpertss.sdp;

import xpertss.lang.Numbers;
import xpertss.lang.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * TimeAdjustment represents the contents of the z= field. TimeAdjustment specifies
 * timezone changes which take place during or in between sessions announced in a
 * session description.
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
public final class TimeAdjustment implements Serializable, Cloneable {

   private long time;
   private long offset;

   TimeAdjustment(long time, long offset)
   {
      this.time = Numbers.gt(0L, time, "time must be greater than zero");
      this.offset = offset;
   }

   /**
    * Returns the time at which the adjustment offset should be applied.
    * <p>
    * This field is represented as an NTP time which is rounded down to the
    * nearest second.
    */
   public Date getTime()
   {
      return Utils.toDate(time);
   }

   /**
    * Return the offset that should be applied to the time in seconds.
    */
   public long getOffset()
   {
      return offset;
   }



   @Override
   public TimeAdjustment clone() {
      try {
         return (TimeAdjustment) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(time, offset);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof TimeAdjustment) {
         TimeAdjustment a = (TimeAdjustment) o;
         return Objects.equal(a.time, time) &&
               Objects.equal(a.getOffset(), offset);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(time).append(" ").append(Utils.toCompactTime(offset));
      return buf.toString();
   }
}

