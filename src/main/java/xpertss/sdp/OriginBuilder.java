/**
 * Created By: cfloersch
 * Date: 5/26/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

import xpertss.lang.Longs;
import xpertss.lang.Strings;
import xpertss.net.NetUtils;


import static xpertss.sdp.SdpConstants.ADDRESS_TYPE_IP4;
import static xpertss.sdp.SdpConstants.NETWORK_TYPE_INTERNET;

/**
 * Builder implementation used to build {@code Origin} objects.
 * <p>
 * This builder will attempt to ensure that each property has a value when the
 * origin is built. It will use default values for fields not explicitly set by
 * the caller. In some cases that may expose information about the machine the
 * software is running on. For example if a username is not specified then one
 * is created from the system property <tt>user.name</tt>. Likewise, the default
 * address is derived from the current machine's ip address. If you would like to
 * protect that information then please ensure you specify valid values for those
 * fields.
 * <p>Example<p>
 * <pre>
 * {@code
 *    OriginBuilder builder = OriginBuilder.create(currentOrigin);
 *    builder.setUsername("JoeBlow");  // change the username
 *    // Increment the current session version
 *    builder.setSessionVersion(builder.getSessionVersion() + 1);
 *    Origin newOrigin = builder.build();
 * }
 * </pre>
 * An instance of origin builder is not thread safe.
 */
public final class OriginBuilder {

   private String username;
   private String sessionId;
   private long version = -1;

   private String address;
   private String addressType;
   private String networkType;

   private OriginBuilder(Origin src)
   {
      if(src != null) {
         username = src.getUsername();
         sessionId = src.getSessionId();
         version = src.getSessionVersion();
         address = src.getAddress();
         addressType = src.getAddressType();
         networkType = src.getNetworkType();
      }
   }



   /**
    * Set or change the username the origin will be built with.
    * <p>
    * Defaults to <tt>user.name</tt> system property
    */
   public OriginBuilder setUsername(String username)
   {
      this.username = Strings.nullIfEmpty(username);
      return this;
   }

   /**
    * Get the username that the origin will be built with.
    */
   public String getUsername()
   {
      return username;
   }


   /**
    * Set or change the sessionId the origin will be built with.
    * <p>
    * Defaults to the current Ntp time. This will always take the absolute
    * value of any supplied argument.
    */
   public OriginBuilder setSessionId(String sessionId)
   {
      this.sessionId = Strings.nullIfEmpty(sessionId);;
      return this;
   }

   /**
    * Get the sessionId that the origin will be built with.
    */
   public String getSessionId()
   {
      return sessionId;
   }


   /**
    * Set or change the session version the origin will be built with.
    * <p>
    * Defaults to a random value between 1 and 65535. This will always take the
    * absolute value of any supplied argument.
    */
   public OriginBuilder setSessionVersion(long version)
   {
      this.version = Math.abs(version);
      return this;
   }

   /**
    * Get the session version that the origin will be built with.
    */
   public long getSessionVersion()
   {
      return version;
   }


   /**
    * Set or change the address the origin will be built with.
    * <p>
    * Defaults to the host address value returned from <tt>InetAddress.getLocalHost()</tt>.
    */
   public OriginBuilder setAddress(String address)
   {
      this.address = Strings.nullIfEmpty(address);
      return this;
   }

   /**
    * Get the address that the origin will be built with.
    */
   public String getAddress()
   {
      return address;
   }


   /**
    * Set or change the address type the origin will be built with.
    * <p>
    * Defaults to "IP4"
    */
   public OriginBuilder setAddressType(String addressType)
   {
      this.addressType = Strings.nullIfEmpty(addressType);
      return this;
   }

   /**
    * Get the address type that the origin will be built with.
    */
   public String getAddressType()
   {
      return addressType;
   }


   /**
    * Set or change the network type the origin will be built with.
    * <p>
    * Defaults to "IN"
    */
   public OriginBuilder setNetworkType(String networkType)
   {
      this.networkType = Strings.nullIfEmpty(networkType);
      return this;
   }

   /**
    * Get the  network type that the origin will be built with.
    */
   public String getNetworkType()
   {
      return networkType;
   }



   /**
    * Build an {@code Origin} object with the current builder properties. This
    * will use defaults if user specified valid values are not provided.
    */
   public Origin build()
   {
      // if address is null use InetAddress.getLocalHost();
      // if address is null && addressType is null use InetAddress.getLocalHost().type
      // if networkType is null use IN
      // if sessionId is 0 use current time (NTP format)
      // if version is 0 use random between 1 & 65535
      return new Origin(Strings.ifEmpty(username, System.getProperty("user.name")),
            Strings.ifEmpty(sessionId, Long.toString(Utils.currentNtpTime())),
            (version == -1) ? Longs.random(0,65535) : version,
            Strings.ifEmpty(address, NetUtils.getLocalAddress()),
            Strings.ifEmpty(addressType, ADDRESS_TYPE_IP4),
            Strings.ifEmpty(networkType, NETWORK_TYPE_INTERNET));
   }




   /**
    * Creates a default uninitialized origin builder.
    */
   public static OriginBuilder create()
   {
      return new OriginBuilder(null);
   }

   /**
    * Creates an origin builder with its values pre-set to those in the
    * specified source origin.
    */
   public static OriginBuilder create(Origin src)
   {
      return new OriginBuilder(src);
   }



}
