# sdp
Session Description Protocol implementation


An example of parsing a session description read from an input stream
````
SessionParser parser = new SessionParser();
SessionDescription sdp = parser.parse(inputstream);
````

Here is an example of building a session description
````
OriginBuilder origin = OriginBuilder.create();
origin.setUsername("joe").setSessionId("200");
SessionBuilder builder = SessionBuilder.create();
builder.setVersion(1).setOrigin(origin.build());
builder.setSessionName("Session Name").setInfo("My Info");

SessionDescription sdpOne = builder.build();

builder.setOrigin(OriginBuilder.create(builder.getOrigin()).setSessionId("201").build());
SessionDescription sdpTwo = builder.build();

builder.addEmail("joe@whatever.com").addEmail("fred@none.com");
SessionDescription sdpThree = builder.build();
````

For more info on Session Description Protocol please visit:
* https://tools.ietf.org/html/rfc4566
* http://en.wikipedia.org/wiki/Session_Description_Protocol

