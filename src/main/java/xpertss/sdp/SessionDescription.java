/*
 * SessionDescription.java
 *
 * Created on January 10, 2002, 2:38 PM
 */

package xpertss.sdp;


import xpertss.lang.Numbers;
import xpertss.lang.Objects;
import xpertss.lang.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A SessionDescription represents the data defined by the Session Description
 * Protocol (see IETF RFC 4566) and holds information about the originator of
 * a session, the media types that a client can support and the host and port
 * on which the client will listen for that media.
 * <p>
 * The SessionDescription also holds timing information for the session (e.g.
 * start, end, repeat, time zone) and bandwidth supported for the session.
 * <p>
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class SessionDescription extends Section {

   private int version;
   private String name;
   private String info;
   private String uri;

   private String[] emails;
   private String[] phones;

   private Key key;
   private Origin origin;
   private TimeZones zones;
   private Connection connection;

   private BandWidth[] bandwidths;
   private Attribute[] attributes;

   private TimeDescription[] times;
   private MediaDescription[] medias;



   SessionDescription(int version, Origin origin, String name, String info, String uri, String[] emails,
                        String[] phones, TimeDescription[] times, TimeZones zones, Connection connection,
                        BandWidth[] bandwidths, Key key, Attribute[] attributes, MediaDescription[] medias)
   {
      this.version = Numbers.gte(0, version, "version must not be negative");
      this.origin = Objects.notNull(origin, "origin may not be null");
      this.name = Strings.notEmpty(name, "name must be specified");
      this.info = info;
      this.uri = uri;
      this.emails = Utils.emptyIfNull(emails, String[].class);
      this.phones = Utils.emptyIfNull(phones, String[].class);
      this.times = Utils.emptyIfNull(times, TimeDescription[].class);
      this.zones = zones;
      this.connection = connection;
      this.bandwidths = Utils.emptyIfNull(bandwidths, BandWidth[].class);
      this.key = key;
      this.attributes = Utils.emptyIfNull(attributes, Attribute[].class);
      this.medias = Utils.emptyIfNull(medias, MediaDescription[].class);
   }





   /**
    * Returns the version of SDP in use. This corresponds to the v= field of
    * the SDP data.
    */
   public int getVersion()
   {
      return version;
   }


   /**
    * Returns information about the originator of the session. This corresponds
    * to the o= field of the SDP data.
    */
   public Origin getOrigin()
   {
      return origin;
   }


   /**
    * Returns the name of the session. This corresponds to the s= field of the
    * SDP data.
    */
   public String getSessionName()
   {
      return name;
   }


   /**
    * Returns value of the info field (i=) of this object.
    */
   public String getInfo()
   {
      return info;
   }


   /**
    * Returns a uri to the location of more details about the session. This
    * corresponds to the u= field of the SDP data.
    */
   public String getUri()
   {
      return uri;
   }


   /**
    * Returns an email address to contact for further information about the
    * session. This corresponds to the e= field of the SDP data.
    */
   public String[] getEmails()
   {
      return emails.clone();
   }


   /**
    * Returns a phone number to contact for further information about the session.
    * This corresponds to the p= field of the SDP data.
    */
   public String[] getPhones()
   {
      return phones.clone();
   }


   /**
    * Returns a Time indicating the start, stop, repetition and time zone
    * information of the session. This corresponds to the t= field of the SDP
    * data.
    */
   public TimeDescription[] getTimeDescriptions()
   {
      return times.clone();
   }


   /**
    * Returns the time zone adjustments for the Session
    */
   public TimeZones getTimeZones()
   {
      return zones;
   }


   /**
    * Returns the connection information associated with this object. This may
    * be null for SessionDescriptions if all Media objects have a connection
    * object and may be null for Media objects if the corresponding session
    * connection is non-null.
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


   /**
    * Returns all the MediaDescriptions assigned to the session description.
    * These correspond to the m= fields of the SDP data.
    */
   public MediaDescription[] getMediaDescriptions()
   {
      return medias.clone();
   }

   // TODO It might be worth our time to add a method to get the medias for a particular type
   // it must differ from attribute and bandwidth in the sense that we can support multiple
   // audio media types and multiple video media types


   @Override
   public SessionDescription clone()
   {
      try {
         return (SessionDescription) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }


   @Override
   public int hashCode()
   {
      return Objects.hash(version, origin, name, info, uri, emails, phones,
               times, zones, connection, bandwidths, key, attributes, medias);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof SessionDescription) {
         SessionDescription s = (SessionDescription) o;
         return Objects.equal(version, s.getVersion()) &&
                  Objects.equal(origin, s.getOrigin()) &&
                  Objects.equal(name, s.getSessionName()) &&
                  Objects.equal(info, s.getInfo()) &&
                  Objects.equal(uri, s.getUri()) &&
                  Arrays.equals(emails, s.getEmails()) &&
                  Arrays.equals(phones, s.getPhones()) &&
                  Arrays.equals(times, s.getTimeDescriptions()) &&
                  Objects.equal(zones, s.getTimeZones()) &&
                  Objects.equal(connection, s.getConnection()) &&
                  Arrays.equals(bandwidths, s.getBandwidths()) &&
                  Objects.equal(key, s.getKey()) &&
                  Arrays.equals(attributes, s.getAttributes()) &&
                  Arrays.equals(medias, s.getMediaDescriptions());
      }
      return false;
   }

   @Override
   public String toString()
   {
      OutputBuilder builder = new OutputBuilder();
      builder.append("v", version).append(origin).append("s", name);
      builder.append("i", info).append("u", uri).appendAll("e", emails);
      builder.appendAll("p", phones).append(connection).appendAll(bandwidths);
      builder.appendAll(times).append(zones).append(key).appendAll(attributes);
      builder.appendAll(medias);
      return builder.toString();
   }


}

