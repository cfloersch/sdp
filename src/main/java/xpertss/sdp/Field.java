/*
 * Field.java
 *
 * Created on December 18, 2001, 10:42 AM
 */

package xpertss.sdp;


import java.io.Serializable;

/**
 * A Field represents a single line of information within a SDP
 * session description.
 *
 * @version 1.0
 */
public abstract class Field implements Serializable, Cloneable {

   /**
    * Returns the type character for the field.
    */
   public abstract char getTypeChar();
    
    

}

