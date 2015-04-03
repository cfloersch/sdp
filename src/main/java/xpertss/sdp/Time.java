/*
 * Time.java
 *
 * Created on January 9, 2002, 10:53 AM
 */

package xpertss.sdp;


import xpertss.lang.Numbers;
import xpertss.lang.Objects;

import java.util.Date;

/**
 * A Time represents a t= field contained within a TimeDescription.
 * <p>
 * A Time specifies the start and stop times for a SessionDescription.
 * <p>
 * Quoting from RFC 4566:
 * <p><pre>
 *     Multiple "t=" fields may be used if a session is active at multiple 
 *     irregularly spaced times; each additional "t=" field specifies an
 *     additional period of time for which the session will be active. If
 *     the session is active at regular times, an "r=" field (see below)
 *     should be used in addition to and following a "t=" field - in which
 *     case the "t=" field specifies the start and stop times of the repeat
 *     sequence.
 * </pre><p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class Time extends Field {

   private long start;
   private long stop;


   Time(long start, long stop)
   {
      if(start > 0 && stop > 0 && stop <= start)
         throw new IllegalArgumentException("start must be before stop");
      this.start = Numbers.gte(0L, start, "start must not be negative");
      this.stop = Numbers.gte(0L, stop, "stop must not be negative");
   }

   /**
    * Returns the start time of the conference/session. A null value indicates
    * the start time was set to NTP zero.
    */
   public Date getStart()
   {
      return Utils.toDate(start);
   }


   /**
    * Returns the stop time of the session. A null value indicates
    * the start time was set to NTP zero.
    */
   public Date getStop()
   {
      return Utils.toDate(stop);
   }


   @Override
   public char getTypeChar()
   {
      return 't';
   }

   @Override
   public Time clone() {
      try {
         return (Time) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(start, stop);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof Time) {
         Time t = (Time) o;
         return Objects.equal(t.start, start) &&
               Objects.equal(t.stop, stop);
      }
      return false;
   }


   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder().append(getTypeChar()).append("=");
      buf.append(start).append(" ").append(stop);
      return buf.toString();
   }

}

