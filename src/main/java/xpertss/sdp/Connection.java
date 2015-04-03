/*
 * Connection.java
 *
 * Created on December 18, 2001, 4:20 PM
 */

package xpertss.sdp;

import xpertss.lang.Objects;
import xpertss.lang.Strings;

/**
 * A Connection represents the c= field associated with a SessionDescription
 * or with an individual MediaDescription and is used to identify a network
 * address on which media can be received.
 * <p>
 * The Connection in the SessionDescription applies to all MediaDescriptions
 * unless a MediaDescription specifically overrides it. The Connection identifies
 * the network type (IN for internet), address type (IP4 or IP6), the start of an
 * address range, the time to live of the session and the number of addresses in
 * the range. Both the time to live and number of addresses are optional.
 * <p>
 * A Connection could therefore be of one these forms:
 * <ul>
 *    <li>c=IN IP4 myhost.somewhere.com (no ttl and only one address)</li>
 *    <li>c=IN IP4 myhost.somewhere.com/5 (a ttl of 5)</li>
 *    <li>c=IN IP4 myhost.somewhere.com/5/2 (a ttl of 5 and 2 addresses)</li>
 * </ul>
 * This implementation does not explicitly support ttl and number of addresses.
 *
 * Please refer to IETF RFC 4566 for a description of SDP.
 *
 * @version 1.0
 */
public final class Connection extends Field {

   private String address;
   private String addressType;
   private String networkType;

   Connection(String address, String addressType, String networkType)
   {
      this.address = Strings.notEmpty(Strings.trim(address), "address may not be empty");
      this.addressType = Strings.notEmpty(Strings.trim(addressType), "addressType may not be empty");
      this.networkType = Strings.notEmpty(Strings.trim(networkType), "networkType may not be empty");
   }

   /**
    * Returns the type of the network for this Connection.
    */
   public String getAddress()
   {
      return address;
   }
    
   /**
    * Returns the type of the address for this Connection.
    *
    * @see SdpConstants#ADDRESS_TYPE_IP4
    * @see SdpConstants#ADDRESS_TYPE_IP6
    */
    public String getAddressType()
    {
       return addressType;
    }
    
   /**
    * Returns the type of the network for this Connection.
    *
    * @see SdpConstants#NETWORK_TYPE_INTERNET
    */
   public String getNetworkType()
   {
      return networkType;
   }


   @Override
   public char getTypeChar()
   {
      return 'c';
   }

   @Override
   public Connection clone() {
      try {
         return (Connection) super.clone();
      } catch(CloneNotSupportedException e) {
         throw new Error(e);
      }
   }



   @Override
   public int hashCode()
   {
      return Objects.hash(address, addressType, networkType);
   }

   @Override
   public boolean equals(Object o)
   {
      if(o instanceof Connection) {
         Connection conn = (Connection) o;
         return Objects.equal(conn.getAddress(), address) &&
               Objects.equal(conn.getAddressType(), addressType) &&
               Objects.equal(conn.getNetworkType(), networkType);
      }
      return false;
   }

   @Override
   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      buf.append(getTypeChar()).append("=").append(networkType).append(" ");
      buf.append(addressType).append(" ").append(address);
      return buf.toString();
   }

}

