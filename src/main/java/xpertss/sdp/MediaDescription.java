/*
 * MediaDescription.java
 *
 * Created on December 19, 2001, 11:17 AM
 */

package xpertss.sdp;


import xpertss.lang.Objects;
import xpertss.lang.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A MediaDescription identifies the set of medias that may be received on a specific
 * port or set of ports. It includes:
 * <ul>
 *    <li>a mediaType (e.g., audio, video, etc.)</li>
 *    <li>a port number (or set of ports)</li>
 *    <li>a protocol to be used (e.g., RTP/AVP)</li>
 *    <li>a set of media formats which correspond to Attributes associated with the
 *        media description.</li>
 * </ul><p>
 * The following is an example
 * <p><pre>
 *    m=audio 60000 RTP/AVP 0
 *    a=rtpmap:0 PCMU/8000
 * </pre><p>
 * This example identifies that the client can receive audio on port 60000 in format
 * 0 which corresponds to PCMU/8000.
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class MediaDescription extends Section {

   private String info;

   private Key key;
   private Media media;
   private Connection connection;

   private BandWidth[] bandwidths;
   private Attribute[] attributes;


   MediaDescription(Media media, String info, Connection connection, BandWidth[] bandwidths, Key key, Attribute[] attributes)
   {
      this.media = Objects.notNull(media, "media may not be null");
      this.info = Strings.nullIfEmpty(info);
      this.connection = connection;
      this.bandwidths = Utils.emptyIfNull(bandwidths, BandWidth[].class).clone();
      this.key = key;
      this.attributes = Utils.emptyIfNull(attributes, Attribute[].class).clone();
   }

   /**
    * Return the Media field of the description.
    */
   public Media getMedia()
   {
      return media;
   }


   /**
    * Returns value of the info field (i=) of this object.
    */
   public String getInfo()
   {
      return info;
   }


   /**
    * Returns the connection information associated with this object. This may be
    * null for SessionDescriptions if all Media objects have a connection object
    * and may be null for Media objects if the corresponding session connection
    * is non-null.
    */
   public Connection getConnection()
   {
      return connection;
   }



   /**
    * Returns the Bandwidth of the specified type.
    */
   public BandWidth[] getBandwidths()
   {
      return bandwidths.clone();
   }


   /**
    * Returns the BandWidth with the specified type or <tt>null</tt> if no
    * bandwidth with the specified type exists.
    *
    * @param type - the type of the bandwidth
    */
   public BandWidth getBandwidth(String type)
   {
      for(BandWidth bw : bandwidths) {
         if(bw.getType().equals(type)) return bw;
      }
      return null;
   }



   /**
    * Returns the key data.
    */
   public Key getKey()
   {
      return key;
   }


   /**
    * Returns the set of attributes for this Description as an array of
    * Attribute objects in the order they were parsed.
    */
   public Attribute[] getAttributes()
   {
      return attributes.clone();
   }


   /**
    * Returns all of the attributes identified by the specified name as an
    * array of Attribute objects in the order they were parsed. This will
    * return an empty array if no attributes with the given name exist.
    */
   public Attribute[] getAttributes(String name)
   {
      List<Attribute> result = new ArrayList<>();
      for(Attribute att : attributes) {
         if(att.getName().equals(name)) result.add(att);
      }
      return result.toArray(new Attribute[result.size()]);
   }


   /**
    * Returns the first Attribute with the specified name or <tt>null</tt> if
    * no attribute with the specified name exists.
    *
    * @param name - the name of the attribute
    */
   public Attribute getAttribute(String name)
   {
      for(Attribute att : attributes) {
         if(att.getName().equals(name)) return att;
      }
      return null;

   }



   @Override
   public MediaDescription clone() {
      try {
         return (MediaDescription) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(media, info, connection, bandwidths, key, attributes);
   }

   @Override
   public boolean equals(Object obj)
   {
      if(obj instanceof MediaDescription) {
         MediaDescription m = (MediaDescription) obj;
         return Objects.equal(m.getMedia(), media) &&
                  Objects.equal(m.getInfo(), info) &&
                  Objects.equal(m.getConnection(), connection) &&
                  Arrays.equals(m.getBandwidths(), bandwidths) &&
                  Objects.equal(m.getKey(), key) &&
                  Arrays.equals(m.getAttributes(), attributes);
      }
      return false;
   }


   @Override
   public String toString()
   {
      OutputBuilder buf = new OutputBuilder();
      buf.append(media).append("i", info).append(connection);
      buf.appendAll(bandwidths).append(key).appendAll(attributes);
      return buf.toString();
   }
}

