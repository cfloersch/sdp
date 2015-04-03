/**
 * Created By: cfloersch
 * Date: 6/2/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import xpertss.lang.Integers;
import xpertss.lang.Longs;
import xpertss.lang.Objects;
import xpertss.lang.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Parser to parse SessionDescription objects from standard SDP files.
 * <p>
 * This parser performs only rudimentary validation of data. Numbers
 * must properly parse to numbers, the minimum number of fields must
 * be defined, and field ordering is enforced. However, relational
 * rules are not currently implemented.
 * <p><pre>
 *   Session description
 *     v=  (one protocol version)
 *     o=  (one originator and session identifier)
 *     s=  (one session name)
 *     i=  (zero or one session information)
 *     u=  (zero or one URI of description)
 *     e=  (zero or more email address)
 *     p=  (zero or more phone number)
 *     c=  (zero or one connection information)
 *     b=  (zero or more bandwidth information lines)
 *     One or more time descriptions ("t=" and "r=" lines; see below)
 *     z=  (zero or one time zone adjustments)
 *     k=  (zero or one encryption key)
 *     a=  (zero or more session attribute lines)
 *     Zero or more media descriptions
 *
 *   Time description
 *     t=  (one time the session is active)
 *     r=  (zero or more repeat times)
 *
 *   Media description, if present
 *     m=  (one media name and transport address)
 *     i=  (zero or one media title)
 *     c=  (zero or one connection information)
 *     b=  (zero or more bandwidth information lines)
 *     k=  (zero or one encryption key)
 *     a=  (zero or more media attribute lines)
 * </pre>
 */
public class SessionParser {

   private static final Set<String> validchars = new HashSet<>();
   static {
      validchars.add("v");
      validchars.add("o");
      validchars.add("s");
      validchars.add("i");
      validchars.add("u");
      validchars.add("e");
      validchars.add("p");
      validchars.add("c");
      validchars.add("b");
      validchars.add("t");
      validchars.add("r");
      validchars.add("z");
      validchars.add("k");
      validchars.add("a");
      validchars.add("m");
   }

   /**
    * Parse a string which represents an SDP file returning a Session Description if
    * it successfully parsed the data.
    *
    * @throws SdpParseException If an error occurs parsing the structure of the document
    * @throws NullPointerException If the supplied source string is null
    */
   public SessionDescription parse(String str) throws SdpParseException, NullPointerException
   {
      return parse(new Scanner(str));
   }

   /**
    * Parse the contents of a given sdp file identified by a Path object returning a Session
    * Description if it successfully parsed the data.
    *
    * @throws SdpParseException If an error occurs parsing the structure of the document
    * @throws NullPointerException If the supplied source path is null
    * @throws IOException If an I/O error occurs opening source
    */
   public SessionDescription parse(Path path) throws SdpParseException, NullPointerException, IOException
   {
      return parse(new Scanner(path, "UTF-8"));
   }

   /**
    * Parse the sdp contents from a given Reader object returning a Session Description if it
    * successfully parsed the data.
    *
    * @throws SdpParseException If an error occurs parsing the structure of the document
    * @throws NullPointerException If the supplied source reader is null
    */
   public SessionDescription parse(Reader reader) throws SdpParseException, NullPointerException
   {
      return parse(new Scanner(reader));
   }

   /**
    * Parse the sdp contents from a given InputStream object returning a Session Description if it
    * successfully parsed the data.
    *
    * @throws SdpParseException If an error occurs parsing the structure of the document
    * @throws NullPointerException If the supplied source stream is null
    */
   public SessionDescription parse(InputStream stream) throws SdpParseException, NullPointerException
   {
      return parse(new Scanner(stream, "UTF-8"));
   }




   private SessionDescription parse(Scanner scanner)  throws SdpParseException
   {
      try {
         SessionBuilder builder = SessionBuilder.create();
         SessionFieldParser chain = createParserChain();
         while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(Strings.isEmpty(line)) break;
            if(line.length() < 3 || line.charAt(1) != '=') throw new SdpParseException("invalid line format: " + line);
            if(!validchars.contains(Character.toString(line.charAt(0))))
               throw new SdpParseException("invalid field: " + line);
            chain = chain.parse(builder, line);
         }
         chain.finish(builder);

         return builder.build();
      } finally {
         scanner.close();
      }
   }


   private SessionFieldParser createParserChain()
   {
      SessionFieldParser parser = new MediaFieldParser();
      parser = new KeyFieldParser(new AttributeFieldParser(parser));
      parser = new TimeFieldParser(new TimeZonesFieldParser(parser));
      parser = new ConnectionFieldParser(new BandwidthFieldParser(parser));
      parser = new EmailFieldParser(new PhoneFieldParser(parser));
      parser = new InfoFieldParser(new UriFieldParser(parser));
      parser = new OriginFieldParser(new SessionNameFieldParser(parser));
      return new VersionFieldParser((parser));
   }


   private interface SessionFieldParser {

      public SessionFieldParser parse(SessionBuilder builder, String line);
      public void finish(SessionBuilder builder);
   }

   private interface MediaSubFieldParser {

      public MediaSubFieldParser parse(MediaBuilder builder, String line);
      public void finish(MediaBuilder builder);
   }


   private class VersionFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public VersionFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("v")) {
            int version = Integers.parse(line.substring(2), -1);
            if(version < 0) throw new SdpParseException("invalid version specified: " + line.substring(2));
            builder.setVersion(version);
            return next;
         }
         throw new SdpParseException("invalid session description: expecting version");
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         throw new SdpParseException("premature end of stream: expecting version");
      }
   }

   private class OriginFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public OriginFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("o")) {
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length != 6) throw new SdpParseException("invalid origin line: " + line.substring(2));
            OriginBuilder origin = OriginBuilder.create().setUsername(parts[0]);
            long sessionId = Longs.parse(parts[1], -1);
            if(sessionId < 0) throw new SdpParseException("invalid origin sessionId: " + parts[1]);
            origin.setSessionId(sessionId);
            long version = Longs.parse(parts[2], -1);
            if(version < 0) throw new SdpParseException("invalid origin session version: " + parts[2]);
            origin.setSessionVersion(version);
            origin.setNetworkType(parts[3]).setAddressType(parts[4]).setAddress(parts[5]);
            builder.setOrigin(origin.build());
            return next;
         }
         throw new SdpParseException("invalid session description: expecting origin");
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         throw new SdpParseException("premature end of stream: expecting origin");
      }
   }

   private class SessionNameFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public SessionNameFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("s")) {
            builder.setSessionName(line.substring(2));
            return next;
         }
         throw new SdpParseException("invalid session description: expecting session name");
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         throw new SdpParseException("premature end of stream: expecting session name");
      }
   }

   private class InfoFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public InfoFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("i")) {
            builder.setInfo(line.substring(2));
            return next;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class UriFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public UriFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("u")) {
            builder.setUri(line.substring(2));
            return next;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class EmailFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public EmailFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("e")) {
            builder.addEmail(line.substring(2));
            return this;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class PhoneFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public PhoneFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("p")) {
            builder.addPhone(line.substring(2));
            return this;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class ConnectionFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public ConnectionFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("c")) {
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length != 3) throw new SdpParseException("invalid connection line: " + line.substring(2));
            builder.setConnection(parts[2], parts[1], parts[0]);
            return next;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class BandwidthFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public BandwidthFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("b")) {
            String[] parts = line.substring(2).split(":");
            if(parts.length != 2) throw new SdpParseException("invalid bandwidth line: " + line.substring(2));
            int kbps = Integers.parse(parts[1], -1);
            if(kbps < 0) throw new SdpParseException("invalid bandwidth value specified: " + parts[1]);
            builder.addBandwidth(parts[0], kbps);
            return this;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class TimeFieldParser implements  SessionFieldParser {

      private SessionFieldParser next;
      private TimeBuilder time;

      public TimeFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("t")) {
            if(time != null) builder.addTimeDescription(time.build());
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length != 2) throw new SdpParseException("invalid time field: " + line.substring(2));
            long start = Longs.parse(parts[0], -1);
            if(start < 0) throw new SdpParseException("invalid start time: " + parts[0]);
            long stop = Longs.parse(parts[1], -1);
            if(stop < 0) throw new SdpParseException("invalid stop time: " + parts[1]);
            time = TimeBuilder.create().setTime(Utils.toDate(start), Utils.toDate(stop));
            return this;
         } else if(line.startsWith("r")) {
            if(time == null) throw new SdpParseException("invalid session description: expecting time");
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length < 3) throw new SdpParseException("invalid repeat time field: " + line.substring(2));
            long interval = fromCompactTime(parts[0]);
            if(interval < 1) throw new SdpParseException("invalid repeat time interval: " + parts[0]);
            long duration = fromCompactTime(parts[1]);
            if(duration < 1) throw new SdpParseException("invalid repeat time duration: " + parts[1]);
            long[] offsets = new long[parts.length - 2];
            for(int i = 0; i < offsets.length; i++) {
               offsets[i] = fromCompactTime(parts[i+2]);
               if(offsets[i] < 0) throw new SdpParseException("invalid repeat time offset: " + parts[i+2]);
            }
            time.addRepeatTime(interval, duration, offsets);
            return this;
         } else if(time != null) {
            builder.addTimeDescription(time.build());
            return next.parse(builder, line);
         }
         throw new SdpParseException("invalid session description: expecting time");
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         if(time != null) builder.addTimeDescription(time.build());
         else throw new SdpParseException("premature end of stream: expecting time");
      }
   }

   private class TimeZonesFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public TimeZonesFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("z")) {
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length % 2 != 0) throw new SdpParseException("invalid timezones field: " + line.substring(2));
            for(int i = 0; i < parts.length - 1; i += 2) {
               long date = Longs.parse(parts[i], -1);
               if(date < 0) throw new SdpParseException("invalid date found: " + parts[i]);
               builder.addTimeAdjustment(Utils.toDate(date), fromCompactTime(parts[i + 1]));
            }
            return next;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class KeyFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public KeyFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("k")) {
            int idx = line.indexOf(":");
            if(idx < 0) {
               builder.setKey(line.substring(2), null);
            } else if(idx < line.length() - 1) {
               builder.setKey(line.substring(2, idx), line.substring(idx + 1));
            } else {
               builder.setKey(line.substring(2, idx), null);
            }
            return next;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class AttributeFieldParser implements SessionFieldParser {

      private SessionFieldParser next;

      public AttributeFieldParser(SessionFieldParser next)
      {
         this.next = Objects.notNull(next, "next may not be null");
      }

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("a")) {
            int idx = line.indexOf(":");
            if(idx < 0) {
               builder.addAttribute(line.substring(2), null);
            } else if(idx == line.length() - 1) {
               builder.addAttribute(line.substring(2, idx), null);
            } else {
               builder.addAttribute(line.substring(2, idx), line.substring(idx+1));
            }
            return this;
         }
         return next.parse(builder, line);
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         next.finish(builder);
      }
   }

   private class MediaFieldParser implements SessionFieldParser {

      private MediaBuilder media;
      private MediaSubFieldParser chain;

      @Override
      public SessionFieldParser parse(SessionBuilder builder, String line)
      {
         if(line.startsWith("m")) {
            if(media != null) {
               builder.addMediaDescription(media.build());
            }
            media = MediaBuilder.create();
            chain = createParserChain();
            String[] parts = line.substring(2).split("\\s+");
            if(parts.length < 4) throw new SdpParseException("incomplete media field: " + line.substring(2));

            String[] ports = parts[1].split("/", 2);
            int port = Integers.parse(ports[0], -1);
            if(port < 0 || port > 65535) throw new SdpParseException("found invalid port: " + ports[0]);
            int count = (ports.length < 2) ? 1 : Integers.parse(ports[1], -1);
            if(count < 1) throw new SdpParseException("found invalid port count: " + ports[1]);

            int[] formats = new int[parts.length - 3];
            for(int i = 0; i < formats.length; i++) {
               formats[i] = Integers.parse(parts[i+3], -1);
               if(formats[i] < 0) throw new SdpParseException("invalid format found: " + parts[i+3]);
            }
            media.setMedia(parts[0], port, count, parts[2], formats);
         } else if(media != null) {
            chain = chain.parse(media, line);
         } else {
            throw new SdpParseException("invalid session description: expecting media");
         }
         return this;
      }

      @Override
      public void finish(SessionBuilder builder)
      {
         if(media != null) {
            chain.finish(media);
            builder.addMediaDescription(media.build());
         }
      }




      private MediaSubFieldParser createParserChain()
      {
         MediaSubFieldParser parser = new AttributeMediaSubFieldParser();
         parser = new KeyMediaSubFieldParser(parser);
         parser = new BandwidthMediaSubFieldParser(parser);
         parser = new ConnectionMediaSubFieldParser(parser);
         return new InfoMediaSubFieldParser(parser);
      }


      private class InfoMediaSubFieldParser implements MediaSubFieldParser {

         private MediaSubFieldParser next;

         public InfoMediaSubFieldParser(MediaSubFieldParser next)
         {
            this.next = Objects.notNull(next, "next may not be null");
         }

         @Override
         public MediaSubFieldParser parse(MediaBuilder builder, String line)
         {
            if(line.startsWith("i")) {
               builder.setInfo(line.substring(2));
               return next;
            }
            return next.parse(builder, line);
         }

         @Override
         public void finish(MediaBuilder builder)
         {
            next.finish(builder);
         }
      }

      private class ConnectionMediaSubFieldParser implements MediaSubFieldParser {

         private MediaSubFieldParser next;

         public ConnectionMediaSubFieldParser(MediaSubFieldParser next)
         {
            this.next = Objects.notNull(next, "next may not be null");
         }

         @Override
         public MediaSubFieldParser parse(MediaBuilder builder, String line)
         {
            if(line.startsWith("c")) {
               String[] parts = line.substring(2).split("\\s+");
               if(parts.length != 3) throw new SdpParseException("invalid connection line: " + line.substring(2));
               builder.setConnection(parts[2], parts[1], parts[0]);
               return next;
            }
            return next.parse(builder, line);
         }

         @Override
         public void finish(MediaBuilder builder)
         {
            next.finish(builder);
         }
      }

      private class BandwidthMediaSubFieldParser implements MediaSubFieldParser {

         private MediaSubFieldParser next;

         public BandwidthMediaSubFieldParser(MediaSubFieldParser next)
         {
            this.next = Objects.notNull(next, "next may not be null");
         }

         @Override
         public MediaSubFieldParser parse(MediaBuilder builder, String line)
         {
            if(line.startsWith("b")) {
               String[] parts = line.substring(2).split(":");
               if(parts.length != 2) throw new SdpParseException("invalid bandwidth line: " + line.substring(2));
               int kbps = Integers.parse(parts[1], -1);
               if(kbps < 0) throw new SdpParseException("invalid bandwidth value specified: " + parts[1]);
               builder.addBandwidth(parts[0], kbps);
               return this;
            }
            return next.parse(builder, line);
         }

         @Override
         public void finish(MediaBuilder builder)
         {
            next.finish(builder);
         }
      }

      private class KeyMediaSubFieldParser implements MediaSubFieldParser {

         private MediaSubFieldParser next;

         public KeyMediaSubFieldParser(MediaSubFieldParser next)
         {
            this.next = Objects.notNull(next, "next may not be null");
         }

         @Override
         public MediaSubFieldParser parse(MediaBuilder builder, String line)
         {
            if(line.startsWith("k")) {
               int idx = line.indexOf(":");
               if(idx < 0) {
                  builder.setKey(line.substring(2), null);
               } else if(idx < line.length() - 1) {
                  builder.setKey(line.substring(2, idx), line.substring(idx + 1));
               } else {
                  builder.setKey(line.substring(2, idx), null);
               }
               return next;
            }
            return next.parse(builder, line);
         }

         @Override
         public void finish(MediaBuilder builder)
         {
            next.finish(builder);
         }
      }

      private class AttributeMediaSubFieldParser implements MediaSubFieldParser {

         @Override
         public MediaSubFieldParser parse(MediaBuilder builder, String line)
         {
            if(line.startsWith("a")) {
               int idx = line.indexOf(":");
               if(idx < 0) {
                  builder.addAttribute(line.substring(2), null);
               } else if(idx == line.length() - 1) {
                  builder.addAttribute(line.substring(2, idx), null);
               } else {
                  builder.addAttribute(line.substring(2, idx), line.substring(idx+1));
               }
               return this;
            }
            throw new SdpParseException("misplaced field: " + line);
         }

         @Override
         public void finish(MediaBuilder builder)
         {
            // what to do??
         }
      }


   }


   private static long fromCompactTime(String compact)
   {
      try {
         char lastChar = compact.charAt(compact.length() - 1);
         if(!Character.isDigit(lastChar)) {
            long value = Long.parseLong(compact.substring(0, compact.length() - 1));
            if(lastChar == 'd') {
               return DAYS.toSeconds(value);
            } else if(lastChar == 'h') {
               return HOURS.toSeconds(value);
            } else if(lastChar == 'm') {
               return MINUTES.toSeconds(value);
            } else if(lastChar == 's') {
               return SECONDS.toSeconds(value);
            }
            throw new SdpParseException("unknown time unit found: " + compact);
         } else {
            return Long.parseLong(compact);
         }
      } catch(NumberFormatException nfe) {
         throw new SdpParseException("invalid time unit found: " + compact);
      }
   }

}
