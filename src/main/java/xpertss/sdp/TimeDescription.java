/*
 * TimeDescription.java
 *
 * Created on January 9, 2002, 11:13 AM
 */

package xpertss.sdp;


import xpertss.lang.Objects;

import java.util.Arrays;

/**
 * A TimeDescription represents the fields present within a SDP time description.
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
public final class TimeDescription extends Section {

   private Time time;
   private RepeatTime[] repeats;

   TimeDescription(Time time, RepeatTime ... repeats)
   {
      this.time = Objects.notNull(time, "time may not be null");
      this.repeats = Utils.emptyIfNull(repeats, RepeatTime[].class).clone();
   }


   /**
    * Returns the Time field. This is guaranteed to not be null.
    */
   public Time getTime()
   {
      return time;
   }
    

   /**
    * Returns the list of repeat times (r= fields) specified in the TimeDescription.
    * This will never return null but it may return a zero length array.
    */
   public RepeatTime[] getRepeatTimes()
   {
      return repeats.clone();
   }


   @Override
   public TimeDescription clone() {
      try {
         return (TimeDescription) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }



   @Override
   public int hashCode()
   {
      return Objects.hash(time, repeats);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof TimeDescription) {
         TimeDescription t = (TimeDescription) o;
         return Objects.equal(t.getTime(), time) &&
               Arrays.equals(t.getRepeatTimes(), repeats);
      }
      return false;
   }


   @Override
   public String toString()
   {
      OutputBuilder buf = new OutputBuilder();
      buf.append(time).appendAll(repeats);
      return buf.toString();
   }

}

