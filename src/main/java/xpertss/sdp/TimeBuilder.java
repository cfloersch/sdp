/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Builder used to construct instances of TimeDescription.
 */
public final class TimeBuilder {

   private List<RepeatTime> repeats = new ArrayList<>();
   private Time time;


   private TimeBuilder(TimeDescription existing)
   {
      if(existing != null) {
         time = existing.getTime();
         repeats = Arrays.asList(existing.getRepeatTimes());
      }
   }


   /**
    * Set the time to reflect the given start and end time.
    * <p>
    * Specifying null for either start or stop time will translate into NTP time of
    * 0 which is interpreted as meaning no start or stop time (aka open ended).
    * <p>
    * Time objects represent start and stop times as NTP time in seconds. Thus any
    * milliseconds in the given start or stop time will be rounded down.
    */
   public TimeBuilder setTime(Date start, Date stop)
   {
      this.time = new Time(Utils.toNtpTime(start), Utils.toNtpTime(stop));
      return this;
   }

   /**
    * Returns the current time or null if no time has been specified.
    */
   public Time getTime()
   {
      return time;
   }


   /**
    * Adds the specified repeat to the current state.
    *
    * @param interval The repeat interval in seconds
    * @param duration The duration of the session on each occurrence
    * @param offsets The start time offset in seconds for each occurrence
    */
   public TimeBuilder addRepeatTime(long interval, long duration, long ... offsets)
   {
      repeats.add(new RepeatTime(interval, duration, offsets));
      return this;
   }

   /**
    * Remove an existing repeat time from this builder. Returns true if the
    * repeat time was found and removed, false otherwise.
    */
   public boolean removeRepeatTime(RepeatTime repeat)
   {
      return repeats.remove(repeat);
   }

   /**
    * Remove all existing repeat times from this time builder.
    */
   public TimeBuilder clearRepeatRimes()
   {
      repeats.clear();
      return this;
   }

   /**
    * Returns the repeat times currently added to this builder.
    */
   public RepeatTime[] getRepeatTimes()
   {
      return repeats.toArray(new RepeatTime[repeats.size()]);
   }


   /**
    * Builds an instance of TimeDescription from the current builder state. If no time
    * has been specified then this will use an open ended time.
    * <p>
    * An IllegalStateException will be thrown if the builder has repeat times defined
    * but does not define an explicit start time.
    */
   public TimeDescription build()
   {
      Time t = (time == null) ? new Time(0,0) : time;
      if(repeats.size() > 0 && t.getStart() == null)
         throw new IllegalStateException("times with unspecified start times cannot define repeat times");
      return new TimeDescription(t, repeats.toArray(new RepeatTime[repeats.size()]));
   }



   /**
    * Create a new uninitialized TimeBuilder
    */
   public static TimeBuilder create()
   {
      return new TimeBuilder(null);
   }

   /**
    * Create a new TimeBuilder preconfigured with the values from the specified
    * TimeDescription object.
    */
   public static TimeBuilder create(TimeDescription existing)
   {
      return new TimeBuilder(existing);
   }

}
