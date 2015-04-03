/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import xpertss.lang.Numbers;
import xpertss.lang.Objects;
import xpertss.lang.Strings;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP6;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;

/**
 * Builder used to construct SessionDescription instances.
 * <p>
 * This will use the default Origin if one is not explicitly specified. The default
 * origin may expose internal data about your network and users that may be deemed
 * undesirable. It is highly recommended that an Origin be explicitly set to avoid
 * this.
 *
 * @see SessionDescription
 * @see OriginBuilder
 */
public final class SessionBuilder {

   private int version;
   private String name;
   private String info;
   private String uri;

   private Key key;
   private Origin origin;
   private Connection connection;

   private List<String> emails = new ArrayList<>();
   private List<String> phones = new ArrayList<>();

   private Map<String,BandWidth> bandwidths = new LinkedHashMap<>();

   private List<TimeDescription> times = new ArrayList<>();
   private List<MediaDescription> medias = new ArrayList<>();
   private List<TimeAdjustment> adjustments = new ArrayList<>();

   private Set<Attribute> attributes = new LinkedHashSet<>();


   SessionBuilder(SessionDescription existing)
   {
      if(existing != null) {
         version = existing.getVersion();
         origin = existing.getOrigin();
         name = existing.getSessionName();
         info = existing.getInfo();
         uri = existing.getUri();
         emails = Arrays.asList(existing.getEmails());
         phones = Arrays.asList(existing.getPhones());
         connection = existing.getConnection();
         for(BandWidth bw: existing.getBandwidths()) bandwidths.put(bw.getType(), bw);
         times = Arrays.asList(existing.getTimeDescriptions());
         TimeZones zones = existing.getTimeZones();
         if(zones != null) adjustments = Arrays.asList(zones.getAdjustments());
         key = existing.getKey();
         Collections.addAll(attributes, existing.getAttributes());
         medias = Arrays.asList(existing.getMediaDescriptions());
      }
   }


   /**
    * Set the SDP version the Session description will use. This defaults to
    * zero if not specified.
    */
   public SessionBuilder setVersion(int version)
   {
      this.version = Numbers.gte(0, version, "version must not be negative");
      return this;
   }

   /**
    * Returns the version of this session description.
    */
   public int getVersion()
   {
      return version;
   }



   /**
    * Set the origin of the session description. This will use the default
    * derived from OriginBuilder if not specified.
    */
   public SessionBuilder setOrigin(Origin origin)
   {
      this.origin = origin;
      return this;
   }

   /**
    * Returns the origin set on this session description builder.
    */
   public Origin getOrigin()
   {
      return origin;
   }



   /**
    * Set the session's name. This will use "SessionName" if not explicitly
    * specified.
    */
   public SessionBuilder setSessionName(String name)
   {
      this.name = Strings.nullIfEmpty(name);
      return this;
   }

   /**
    * Returns the name currently set on this session description builder, or null
    * if one has not been specified.
    */
   public String getSessionName()
   {
      return name;
   }



   /**
    * Set the session info. This will be excluded if not specified.
    */
   public SessionBuilder setInfo(String info)
   {
      this.info = Strings.nullIfEmpty(info);
      return this;
   }

   /**
    * Returns the session info set on this session description builder or null
    * if no info has been set.
    */
   public String getInfo()
   {
      return info;
   }



   /**
    * Set the session uri. This will be excluded if not specified.
    */
   public SessionBuilder setUri(String uri)
   {
      this.uri = Strings.nullIfEmpty(uri);
      return this;
   }

   /**
    * Alternative method for setting the session uri. This will be excluded
    * if not specified.
    */
   public SessionBuilder setUri(URI uri)
   {
      this.uri = Objects.toString(uri);
      return this;
   }

   /**
    * Returns the session uri set on this session description builder or null
    * if no uri has been set.
    */
   public String getUri()
   {
      return uri;
   }



   /**
    * Add a session email. This will be excluded if not specified.
    */
   public SessionBuilder addEmail(String email)
   {
      if(!Strings.isEmpty(email)) emails.add(email);
      return this;
   }

   /**
    * Remove the specified email from this session description builder's set. Returns
    * true if the specified email existed in the set and was removed, false otherwise.
    */
   public boolean removeEmail(String email)
   {
      return emails.remove(email);
   }

   /**
    * Returns an array of emails currently added to this session description builder
    * or an empty array if none have yet been added.
    */
   public String[] getEmails()
   {
      return emails.toArray(new String[emails.size()]);
   }



   /**
    * Add a session phone. This will be excluded if not specified.
    */
   public SessionBuilder addPhone(String phone)
   {
      if(!Strings.isEmpty(phone)) phones.add(phone);
      return this;
   }

   /**
    * Remove the specified phone from this session description builder's set. Returns
    * true if the specified phone existed in the set and was removed, false otherwise.
    */
   public boolean removePhone(String phone)
   {
      return phones.remove(phone);
   }

   /**
    * Returns an array of phones currently added to this session description builder
    * or an empty array if none have yet been added.
    */
   public String[] getPhones()
   {
      return phones.toArray(new String[phones.size()]);
   }



   /**
    * Add a session time. This will use 0 0 if not explicitly specified.
    */
   public SessionBuilder addTimeDescription(TimeDescription time)
   {
      if(time != null) times.add(time);
      return this;
   }

   /**
    * Remove the specified time description from this session description
    * builder. Returns true if it was found and removed, false otherwise.
    */
   public boolean removeTimeDescription(TimeDescription time)
   {
      return times.remove(time);
   }

   /**
    * Removes all time descriptions from this session description builder.
    */
   public SessionBuilder clearTimeDescriptions()
   {
      times.clear();
      return this;
   }

   /**
    * Returns all of the time descriptions added to this session description
    * builder or an empty array if none have yet been added.
    */
   public TimeDescription[] getTimeDescriptions()
   {
      return times.toArray(new TimeDescription[times.size()]);
   }



   /**
    * Add a time adjustment. This will be excluded if not specified.
    */
   public SessionBuilder addTimeAdjustment(Date time, long offset)
   {
      adjustments.add(new TimeAdjustment(Utils.toNtpTime(time), offset));
      return this;
   }

   /**
    * Removes the specified time adjustment from this session description
    * builder. Returns true if it is found and removed, false otherwise.
    */
   public boolean removeTimeAdjustment(TimeAdjustment adjustment)
   {
      return adjustments.remove(adjustment);
   }

   /**
    * Removes all existing time adjustments from this session description
    * builder.
    */
   public SessionBuilder clearTimeAdjustments()
   {
      adjustments.clear();
      return this;
   }

   /**
    * Returns all of the time adjustments added to this session description
    * builder or an empty array if none have yet been added.
    */
   public TimeAdjustment[] getTimeAdjustments()
   {
      return adjustments.toArray(new TimeAdjustment[adjustments.size()]);
   }



   /**
    * Set the  connection. This will be excluded if not specified.
    */
   public SessionBuilder setConnection(String address, String addressType, String networkType)
   {
      connection = new Connection(address, addressType, networkType);
      return this;
   }

   /**
    * Alternative method for setting the  connection. This will be excluded if not
    * specified.
    */
   public SessionBuilder setConnection(InetAddress address)
   {
      String addressType = (address instanceof Inet4Address) ? ADDRESS_TYPE_IP4 : ADDRESS_TYPE_IP6;
      return setConnection(address.getHostAddress(), addressType, NETWORK_TYPE_INTERNET);
   }

   /**
    * Clear the current connection info from this session description builder's
    * state. This will result in a session description being built without a
    * connection property.
    */
   public SessionBuilder clearConnection()
   {
      this.connection = null;
      return this;
   }

   /**
    * Returns the currently configured connection object or null if a connection
    * has not yet been specified.
    */
   public Connection getConnection()
   {
      return connection;
   }





   /**
    * Add an optional bandwidth specification to this session description builder.
    * <p>
    * Bandwidth specifications are optional. Only a single specification for any
    * given type may exist concurrently. Adding an already existing type will
    * result in the new bandwidth overwriting the previous setting.
    *
    * @param type The bandwidth type parameter.
    * @param kbps The bandwidth in kilobits per second
    */
   public SessionBuilder addBandwidth(String type, int kbps)
   {
      bandwidths.put(type, new BandWidth(type, kbps));
      return this;
   }

   /**
    * Remove the specified bandwidth specification from this session description
    * builder.
    *
    * @param type The bandwidth identifier type.
    * @return <tt>true</tt> if the bandwidth was found and removed, <tt>false</tt>
    *          otherwise.
    */
   public boolean removeBandwidth(String type)
   {
      return bandwidths.remove(type) != null;
   }

   /**
    * Return a bandwidth object identified by the specified type or null if no
    * such bandwidth object exists within this builder.
    *
    * @param type The bandwidth identifier type
    * @return The bandwidth of the specified type or null
    */
   public BandWidth getBandwidth(String type)
   {
      return bandwidths.get(type);
   }

   /**
    * Returns all bandwidths currently defined on this session description
    * builder.
    */
   public BandWidth[] getBandwidths()
   {
      return bandwidths.values().toArray(new BandWidth[bandwidths.size()]);
   }



   /**
    * Set the optional security key used by this session description.
    *
    * @param method The security method to use
    * @param key They possibly null key to use
    */
   public SessionBuilder setKey(String method, String key)
   {
      this.key = new Key(method, key);
      return this;
   }

   /**
    * Clear the current security key. This will result in a session description
    * being build that does not contain a security key.
    */
   public SessionBuilder clearKey()
   {
      this.key = null;
      return this;
   }

   /**
    * Return the currently configured security key or null if no key has been
    * configured.
    */
   public Key getKey()
   {
      return key;
   }



   /**
    * Add the specified attribute to this session description builder.
    * <p>
    * Attributes are optional but are very commonly used.
    *
    * @param name The name of the attribute
    * @param value The possible null attribute value
    */
   public SessionBuilder addAttribute(String name, String value)
   {
      attributes.add(new Attribute(name, value));
      return this;
   }


   /**
    * Remove the specified attribute from this session description's attribute set.
    * <p>
    * This will return {@code true} if the attribute was found and removed, {@code
    * false} otherwise.
    *
    * @param attribute The attribute to remove
    * @return {@code true} if the attribute was found and removed, {@code false}
    *    otherwise.
    */
   public boolean removeAttribute(Attribute attribute)
   {
      return attributes.remove(attribute);
   }


   /**
    * Remove all of the attributes with the given name from this session description's
    * attribute set.
    * <p>
    * This will return {@code true} if at least one named attribute was found and
    * removed, {@code false} otherwise.
    *
    * @param name The name of the attributes to remove
    * @return {@code true} if at least one named attribute was found and removed,
    *    {@code false} otherwise.
    */
   public boolean removeAttributes(String name)
   {
      boolean result = false;
      for(Iterator<Attribute> it = attributes.iterator(); it.hasNext(); ) {
         if(it.next().getName().equals(name)) {
            it.remove();
            result = true;
         }
      }
      return result;
   }

   /**
    * Returns the first attribute with the given name if it exists, {@code null}
    * otherwise.
    *
    * @param name The name of the attribute to return
    */
   public Attribute getAttribute(String name)
   {
      for(Attribute att : attributes) {
         if(att.getName().equals(name)) return att;
      }
      return null;
   }

   /**
    * Returns the all the attribute with the given name if any exist, or an empty
    * array otherwise.
    *
    * @param name The name of the attributes to return
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
    * Returns all of the attributes currently defined on this session description.
    */
   public Attribute[] getAttributes()
   {
      return attributes.toArray(new Attribute[attributes.size()]);
   }








   /**
    * Add a media description. This will be excluded if not specified.
    */
   public SessionBuilder addMediaDescription(MediaDescription media)
   {
      if(media != null) medias.add(media);
      return this;
   }

   /**
    * Remove the specified media description from this session description
    * builder. Returns true if the media description was found and removed,
    * false otherwise.
    */
   public boolean removeMediaDescription(MediaDescription media)
   {
      return medias.remove(media);
   }

   /**
    * Removes all the existing media descriptions from this session description
    * builder.
    */
   public SessionBuilder clearMediaDescriptions()
   {
      medias.clear();
      return this;
   }

   /**
    * Returns the media descriptions currently added to this session description
    * builder, or an empty array if none have yet been added.
    */
   public MediaDescription[] getMediaDescriptions()
   {
      return medias.toArray(new MediaDescription[medias.size()]);
   }



   /**
    * Builds an instance of SessionDescription from the current state of the session
    * description builder.
    * <p>
    * If no origin has been specified, one will be derived from the default instance
    * of OriginBuilder.
    * <p>
    * If a session name has not been specified then "SessionName" will be used.
    * <p>
    * If no time description has been specified a default will be provided by the
    * default TimeBuilder.
    *
    * @see OriginBuilder#build()
    * @see TimeBuilder#build()
    */
   public SessionDescription build()
   {
      // if version is not set then it defaults to 0
      // if origin is null, use Origin Builder and build a default one from all nulls
      Origin origin = (this.origin == null) ? OriginBuilder.create().build() : this.origin;
      // if session name is null use "SessionName"
      String sessionName = (name == null) ? "SessionName" : name;
      // timezones is null if no adjustments have been added
      TimeZones zones = (getTimeAdjustments().length > 0) ? new TimeZones(getTimeAdjustments()) : null;

      TimeDescription[] times = getTimeDescriptions();
      if(times.length < 1) times = new TimeDescription[] { TimeBuilder.create().build() };

      return new SessionDescription(version, origin, sessionName, info, uri, getEmails(), getPhones(),
                                    times, zones, connection, getBandwidths(), key, getAttributes(),
                                    getMediaDescriptions());

   }




   /**
    * Create a new uninitialized SessionBuilder.
    */
   public static SessionBuilder create()
   {
      return new SessionBuilder(null);
   }

   /**
    * Create a SessionBuilder pre-initialized with the values from the specified
    * pre-existing session description.
    */
   public static SessionBuilder create(SessionDescription existing)
   {
      return new SessionBuilder(existing);
   }

}
