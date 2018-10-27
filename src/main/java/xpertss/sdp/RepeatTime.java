/*
 * RepeatTime.java
 *
 * Created on December 20, 2001, 3:09 PM
 */

package xpertss.sdp;

import xpertss.lang.Numbers;
import xpertss.lang.Objects;

import java.util.Arrays;

/**
 * A RepeatTime represents a r= field contained within a TimeDescription.
 * <p>
 * A RepeatTime specifies the repeat times for a SessionDescription.
 * <p>
 * This consists of a "repeat interval", an "active duration", and a list of
 * offsets relative to the t=start-time (see Time.getStart()).
 * <p>
 * Quoting from RFC 4566:
 * <p>{@code
 *     For example, if a session is active at 10am on Monday and 11am on Tuesday for
 *     one hour each week for three months, then the <start time> in the corresponding
 *     "t=" field would be the NTP representation of 10am on the first Monday, the
 *     <repeat interval> would be 1 week, the <active duration> would be 1 hour, and
 *     the offsets would be zero and 25 hours. The corresponding "t=" field stop time
 *     would be the NTP representation of the end of the last session three months later.
 *     By default all fields are in seconds, so the "r=" and "t=" fields might be:
 *
 *           t=3034423619 3042462419
 *           r=604800 3600 0 90000
 * }
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class RepeatTime extends Field {

   private long interval;
   private long duration;
   private long[] offsets;

   RepeatTime(long interval, long duration, long ... offsets)
   {
      this.interval = Numbers.gt(0L, interval, "interval must be greater than 0");
      this.duration = Numbers.gt(0L, duration, "duration must be greater than 0");
      this.offsets = Utils.hasItem(offsets, "must define at least one offset").clone();
   }

   /**
    * Returns the "repeat interval" in seconds.
    */
   public long getInterval()
   {
      return interval;
   }

   /**
    * Returns the "active duration" in seconds.
    */
   public long getDuration()
   {
      return duration;
   }

   /**
    * Returns a list of offsets. These are relative to the start-time given
    * in the Time object (t=field) with which this RepeatTime is associated.
    */
   public long[] getOffsets()
   {
      return offsets.clone();
   }


   @Override
   public char getTypeChar()
   {
      return 'r';
   }

   @Override
   public RepeatTime clone() {
      try {
         return (RepeatTime) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }


   @Override
   public int hashCode()
   {
      return Objects.hash(interval, duration, offsets);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof RepeatTime) {
         RepeatTime r = (RepeatTime) o;
         return Objects.equal(r.getInterval(), interval) &&
               Objects.equal(r.getDuration(), duration) &&
               Arrays.equals(r.getOffsets(), offsets);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=").append(Utils.toCompactTime(interval));
      buf.append(" ").append(Utils.toCompactTime(duration));
      for(long offset: offsets) buf.append(" ").append(Utils.toCompactTime(offset));
      return buf.toString();
   }
}

