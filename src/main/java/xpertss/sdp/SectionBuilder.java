/*
 * Copyright 2020 XpertSoftware
 *
 * Created By: cfloersch
 * Date: 10/6/2020
 */
package xpertss.sdp;

import java.net.InetAddress;

public abstract class SectionBuilder<T extends SectionBuilder> {

   /**
    * Set the session info. This will be excluded if not specified.
    */
   public abstract T setInfo(String info);

   /**
    * Returns the session info set on this session description builder or null
    * if no info has been set.
    */
   public abstract String getInfo();



   

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
   public abstract T setConnection(String address, String addressType, String networkType);

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
   public abstract T setConnection(InetAddress address);


   /**
    * Clear the current connection info from this media description builder's
    * state. This will result in a media description being built without a
    * connection property.
    */
   public abstract T clearConnection();

   /**
    * Returns the current media description's connection info or null if it is not
    * yet configured.
    */
   public abstract Connection getConnection();





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
   public abstract T addBandwidth(String type, int kbps);

   /**
    * Remove the specified bandwidth specification from this media description.
    *
    * @param type The bandwidth identifier type.
    * @return <tt>true</tt> if the bandwidth was found and removed, <tt>false</tt>
    *          otherwise.
    */
   public abstract boolean removeBandwidth(String type);

   /**
    * Return a bandwidth object identified by the specified type or null if no
    * such bandwidth object exists within this builder.
    *
    * @param type The bandwidth identifier type
    * @return The bandwidth of the specified type or null
    */
   public abstract BandWidth getBandwidth(String type);

   /**
    * Returns all bandwidths currently defined on this media description.
    */
   public abstract BandWidth[] getBandwidths();






   /**
    * Set the optional security key used by this media description.
    *
    * @param method The security method to use
    * @param key They possibly null key to use
    */
   public abstract T setKey(String method, String key);

   /**
    * Clear the current security key. This will result in a media description
    * being build that does not contain a security key.
    */
   public abstract T clearKey();

   /**
    * Return the currently configured security key or null if no key has been
    * configured.
    */
   public abstract Key getKey();


   



   /**
    * Add the specified attribute to this media description. Attributes are optional
    * but are very commonly used.
    *
    * @param name The name of the attribute
    * @param value The possible null attribute value
    */
   public abstract T addAttribute(String name, String value);

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
   public abstract boolean removeAttribute(Attribute attribute);

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
   public abstract boolean removeAttributes(String name);


   /**
    * Returns the first attribute with the given name if it exists, {@code null}
    * otherwise.
    *
    * @param name The name of the attribute to return
    */
   public abstract Attribute getAttribute(String name);

   /**
    * Returns the all the attribute with the given name if any exist, or an empty
    * array otherwise.
    *
    * @param name The name of the attributes to return
    */
   public abstract Attribute[] getAttributes(String name);

   /**
    * Returns all of the attributes currently defined on this media description.
    */
   public abstract Attribute[] getAttributes();


}
