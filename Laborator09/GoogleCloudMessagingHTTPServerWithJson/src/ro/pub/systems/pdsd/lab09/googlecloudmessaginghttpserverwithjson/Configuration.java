package ro.pub.systems.pdsd.lab09.googlecloudmessaginghttpserverwithjson;

public interface Configuration {
	
	// the address of the Google Cloud Messaging HTTP connection server 
	// to which the application server sends a POST request in order to be further transmitted to the mobile device
	
	final public static String REQUEST_SERVER_URL 	= "https://android.googleapis.com/gcm/send";
	
	// TO DO: replace these values with your own
	
	// HTTP header must contain the Authorization field which includes the API key
	// provided from the Google Developer Console
	// Authorization: key=...
	
    final public static String API_KEY 				= "AIzaSyASz6lZCK1PPOITYbBM1Mtju50OMles9qs";
    
    // HTTP body must contain the registration identifiers obtained by each mobile device
    // while registering to the Google Cloud Messaging 
    // a message can be sent to multiple recipients at one time as opposed to the CCS (XMPP) case
    // "registration_ids" : ["...", "...", ..., "..."]
    
    final public static String GCM_REGISTRATION_ID 	= "APA91bFgXEstYlPqN07BPbcCkHWI6ZD3J5BqjqEWhsCvxpGfDdaVota6780DJNG50BJbfRfS3QgDCg9GKHEDUOnDGaViWeo2P25TC6ex8qmvHPVThpR-BbvmNQvzrPl8sjnhiiYlJA117AC7C6H2PEzDrzw9JQLnB-8eMCUqTRt-Lygh434MKbHcbUAGeHefx4YFfcgf5a6c";
}
