package xpertss.sdp;


class OutputBuilder {

   private StringBuilder buf = new StringBuilder();

   public OutputBuilder append(Section obj)
   {
      if(obj != null) buf.append(obj);
      return this;
   }

   public OutputBuilder appendAll(Section[] objs)
   {
      for(int i = 0; objs != null && i < objs.length; i++) {
         if(objs[i] != null) append(objs[i]);
      }
      return this;
   }

   public OutputBuilder append(Field obj)
   {
      if(obj != null) buf.append(obj).append(System.lineSeparator());
      return this;
   }

   public OutputBuilder appendAll(Field[] objs)
   {
      for(int i = 0; objs != null && i < objs.length; i++) {
         if(objs[i] != null) append(objs[i]);
      }
      return this;
   }

   public OutputBuilder append(String code, Object obj)
   {
      if(obj != null) buf.append(code).append("=").append(obj).append(System.lineSeparator());
      return this;
   }

   public OutputBuilder appendAll(String code, Object[] objs)
   {
      for(int i = 0; objs != null && i < objs.length; i++) {
         if(objs[i] != null) append(code, objs[i]);
      }
      return this;
   }

   public String toString()
   {
      return buf.toString();
   }
}