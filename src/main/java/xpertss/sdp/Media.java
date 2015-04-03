/*
 * Media.java
 *
 * Created on December 19, 2001, 10:28 AM
 */

package xpertss.sdp;


import xpertss.lang.Numbers;
import xpertss.lang.Objects;
import xpertss.lang.Strings;

import java.util.Arrays;

/**
 * A Media represents an m= field contained within a MediaDescription. The Media
 * identifies information about the format(s) of the media associated with the
 * MediaDescription.
 * <p>
 * <pre>
 *    m=<media> <port>/<number of ports> <protocol> <media formats>
 * </pre>
 * <p>
 * The Media field includes:
 * <ul>
 *   <li>a mediaType (e.g. audio, video, etc.)</li>
 *   <li>a port number (or set of ports)</li>
 *   <li>a protocol to be used (e.g. RTP/AVP)</li>
 *   <li>a set of media formats which correspond to Attributes associated with the
 *       media description</li>
 * </ul>
 * Here is an example:
 * <p><pre>
 *    m=audio 60000 RTP/AVP 0
 *    a=rtpmap:0 PCMU/8000
 * </pre><p>
 * This example identifies that the client can receive audio on port 60000 in
 * format 0 which corresponds to PCMU/8000.
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class Media extends Field {

   private String protocol;
   private String type;
   private int port;
   private int count = 1;
   private int[] formats;


   Media(String type, int port, int count, String protocol, int ... formats)
   {
      this.type = Strings.notEmpty(Strings.trim(type), "type may not be empty");
      this.port = Numbers.within(0, 65535, port, "invalid port specified");
      this.count = Numbers.gt(0, count, "count must be a positive integer");
      this.protocol = Strings.notEmpty(Strings.trim(protocol), "protocol may not be empty");
      this.formats = Utils.hasItem(formats, "must define at least one format").clone();
   }

   /**
    * Returns the type (audio,video etc) of the media defined by this description.
    */
   public String getType()
   {
      return type;
   }


   /**
    * Returns the port of the media defined by this description
    */
   public int getPort()
   {
      return port;
   }


   /**
    * Returns the number of ports associated with this media description
    */
   public int getPortCount()
   {
      return count;
   }

   /**
    * Returns the protocol over which this media should be transmitted.
    */
   public String getProtocol()
   {
      return protocol;
   }


   /**
    * Returns an array of the media formats supported by this description.
    * <p>
    * Each element in this array will be an int value which matches one of
    * the a=rtpmap: attribute fields of the media description.
    */
   public int[] getFormats()
   {
      return formats.clone();
   }



   @Override
   public char getTypeChar()
   {
      return 'm';
   }

   @Override
   public Media clone() {
      try {
         return (Media) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }


   @Override
   public int hashCode()
   {
      return Objects.hash(type, port, count, protocol, formats);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof Media) {
         Media m = (Media) o;
         return Objects.equal(m.getType(), type) &&
               Objects.equal(m.getPort(), port) &&
               Objects.equal(m.getPortCount(), count) &&
               Objects.equal(m.getProtocol(), protocol) &&
               Arrays.equals(m.getFormats(), formats);
      }
      return false;
   }





   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=").append(type).append(" ").append(port);
      if(count > 1) buf.append("/").append(count);
      buf.append(" ").append(protocol);
      for(int format : formats) {
         buf.append(" ").append(format);
      }
      return buf.toString();
   }

}

