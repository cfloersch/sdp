/**
 * Created By: cfloersch
 * Date: 6/3/13
 * Copyright 2013 XpertSoftware
 */
package xpertss.sdp;

public class SdpParseException extends RuntimeException {

   public SdpParseException()
   {
      super();
   }

   public SdpParseException(String msg)
   {
      super(msg);
   }

   public SdpParseException(Throwable cause)
   {
      super(cause);
   }

   public SdpParseException(String msg, Throwable cause)
   {
      super(msg, cause);
   }


}
