/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import xpertss.lang.Strings;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
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
 * Builder used to construct MediaDescription instances.
 */
public final class MediaBuilder {

   private Map<String,BandWidth> bandwidths = new LinkedHashMap<>();
   private Set<Attribute> attributes = new LinkedHashSet<>();

   private Key key;
   private Media media;
   private String info;
   private Connection conn;

   private MediaBuilder(MediaDescription existing)
   {
      if(existing != null) {
         this.media = existing.getMedia();
         this.key = existing.getKey();
         this.info = existing.getInfo();
         this.conn = existing.getConnection();
         for(BandWidth bw : existing.getBandwidths()) {
            bandwidths.put(bw.getType(), bw);
         }
         Collections.addAll(attributes, existing.getAttributes());
      }
   }






   /**
    * Set the REQUIRED media data for this media description.
    * <p>
    * There is no default for this and attempting to build a media description
    * without having previously specified this data will result in an exception.
    *
    * @param type The type of media. Typically something like audio or video
    * @param port The port the media will be distributed over
    * @param count The number of ports used to serve the stream. Typically 1
    * @param protocol The protocol the stream will be served over
    * @param formats The formats of the streamed media
    */
   public MediaBuilder setMedia(String type, int port, int count, String protocol, int ... formats)
   {
      this.media = new Media(type, port, count, protocol, formats);
      return this;
   }

   /**
    * Returns the media data currently set on this builder or null if nothing has been
    * specified yet.
    * <p>
    * There is no default for this and attempting to build a media description without
    * having previously specified this data will result in an exception.
    */
   public Media getMedia()
   {
      return media;
   }



   /**
    * Set the optional media info.
    */
   public MediaBuilder setInfo(String info)
   {
      this.info = Strings.nullIfEmpty(info);
      return this;
   }

   /**
    * Returns the currently configured media info or null if nothing has yet been specified.
    */
   public String getInfo()
   {
      return info;
   }



   /**
    * Set the connection arguments for this media description.
    * <p>
    * Media description level connection information is optional if it has been specified
    * at the session level.
    *
    * @param address The connection address
    * @param addressType The connection address type. Typically IP4 or IP6
    * @param networkType The network type. Typically IN (Internet)
    * @see SdpConstants#ADDRESS_TYPE_IP4
    * @see SdpConstants#ADDRESS_TYPE_IP6
    * @see SdpConstants#NETWORK_TYPE_INTERNET
    */
   public MediaBuilder setConnection(String address, String addressType, String networkType)
   {
      this.conn = new Connection(address, addressType, networkType);
      return this;
   }

   /**
    * An alternative means for specifying the media description's connection properties.
    * This will overwrite any existing connection properties with those derived from the
    * specified InetAddress. The address type will be IP6 if the supplied InetAddress
    * represents an IP6 address type. Otherwise, it will be IP4. The network type will
    * always be IN (for internet).
    * <p>
    * At present this supports only single unicast addresses. Multicast addresses should
    * include the additional ttl value which can not be derived from an InetAddress.
    * <p>
    * Media description level connection information is optional if it has been specified
    * at the session level.
    */
   public MediaBuilder setConnection(InetAddress address)
   {
      String addressType = (address instanceof Inet4Address) ? ADDRESS_TYPE_IP4 : ADDRESS_TYPE_IP6;
      setConnection(address.getHostAddress(), addressType, NETWORK_TYPE_INTERNET);
      return this;
   }

   /**
    * Clear the current connection info from this media description builder's
    * state. This will result in a media description being built without a
    * connection property.
    */
   public MediaBuilder clearConnection()
   {
      this.conn = null;
      return this;
   }

   /**
    * Returns the current media description's connection info or null if it is not
    * yet configured.
    */
   public Connection getConnection()
   {
      return conn;
   }



   /**
    * Add an optional bandwidth specification to this media description.
    * <p>
    * Bandwidth specifications are optional. Only a single specification for any
    * given type may exist concurrently. Adding an already existing type will
    * result in the new bandwidth overwriting the previous setting.
    *
    * @param type The bandwidth type parameter.
    * @param kbps The bandwidth in kilobits per second
    */
   public MediaBuilder addBandwidth(String type, int kbps)
   {
      bandwidths.put(type, new BandWidth(type, kbps));
      return this;
   }

   /**
    * Remove the specified bandwidth specification from this media description.
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
    * Returns all bandwidths currently defined on this media description.
    */
   public BandWidth[] getBandwidths()
   {
      return bandwidths.values().toArray(new BandWidth[bandwidths.size()]);
   }



   /**
    * Set the optional security key used by this media description.
    *
    * @param method The security method to use
    * @param key They possibly null key to use
    */
   public MediaBuilder setKey(String method, String key)
   {
      this.key = new Key(method, key);
      return this;
   }

   /**
    * Clear the current security key. This will result in a media description
    * being build that does not contain a security key.
    */
   public MediaBuilder clearKey()
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
    * Add the specified attribute to this media description. Attributes are optional
    * but are very commonly used.
    *
    * @param name The name of the attribute
    * @param value The possible null attribute value
    */
   public MediaBuilder addAttribute(String name, String value)
   {
      attributes.add(new Attribute(name, value));
      return this;
   }

   /**
    * Remove the specified attribute from this media description's attribute set.
    * <p>
    * This will return {@code true} the attribute was found and removed, {@code
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
    * Remove all of the attributes with the given name from this media description's
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
    * Returns all of the attributes currently defined on this media description.
    */
   public Attribute[] getAttributes()
   {
      return attributes.toArray(new Attribute[attributes.size()]);
   }




   /**
    * Build and return a MediaDescription object from the current state of this builder.
    *
    * @throws IllegalStateException If the required media property has not been configured.
    */
   public MediaDescription build()
   {
      if(media == null) throw new IllegalStateException("media property must be specified");
      return new MediaDescription(media, info, conn, getBandwidths(), key, getAttributes());
   }




   /**
    * Create a new uninitialized media description builder.
    */
   public static MediaBuilder create()
   {
      return new MediaBuilder(null);
   }

   /**
    * Create a new media description builder initialized with the values from an
    * existing media description object.
    */
   public static MediaBuilder create(MediaDescription existing)
   {
      return new MediaBuilder(existing);
   }

}
