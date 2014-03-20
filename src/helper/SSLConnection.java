package helper;
import com.novell.ldap.*;

import java.security.Security;
import java.io.UnsupportedEncodingException;
import com.novell.ldap.util.Base64;
import java.util.Enumeration;
import java.util.Iterator;
import model.*;

/**
 * creates an SSL connection to an LDAP server through which encrypted communication can happen
 * @author Will Henry
 *
 */
public class SSLConnection {
	/**
	 * LDAP connection to the SSL server
	 */
	private LDAPConnection conn; 
	/**
	 * the LDAP port. Default is port 636
	 */
	private int ldapPort;
	/**
	 * ldapVersion - we use V3 at UGA
	 */
    private int ldapVersion;
    /**
     * url for the LDAP host server
     */
    private String ldapHost;
    /**
     * the loginDN - aka the distinguished name of the user logging in to the LDAP server
     * The cn part of the string is the user's MyID
     */
    private String loginDN;
    /**
     * the corresponding password to the MyID
     */
    private String password;
    /**
     * path to Java security certificates
     */
    private String pathToJava;
	
    /**
     * default constructor
     */
	public SSLConnection(){
		ldapPort = LDAPConnection.DEFAULT_SSL_PORT;
        this.ldapVersion = LDAPConnection.LDAP_V3;
        this.ldapHost = "eds.uga.edu";
        this.loginDN = "cn=henryw14,ou=users,o=uga";
        this.password = "Firebolt1@";
        this.pathToJava = "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/security/cacerts";
	}
    /**
     * default constructor
     */
	public SSLConnection(String username, String password){
		ldapPort = LDAPConnection.DEFAULT_SSL_PORT;
        this.ldapVersion = LDAPConnection.LDAP_V3;
        this.ldapHost = "eds.uga.edu";
        this.loginDN = "cn=" +username +",ou=users,o=uga";
        this.password = password;
        this.pathToJava = "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/lib/security/cacerts";
	}
	/**
	 * creates a connection the the LDAP server. Returns a connection or null if unsuccessful
	 */
	public LDAPConnection connect(){
		LDAPSocketFactory ssf;
		LDAPConnection lc = null;
        try {
            // Dynamically set JSSE as a security provider
             Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            // Dynamically set the property that JSSE uses to identify
            // the keystore that holds trusted root certificates
             System.setProperty("javax.net.ssl.trustStore", pathToJava);
             ssf = new LDAPJSSESecureSocketFactory();
            // Set the socket factory as the default for all future connections
             LDAPConnection.setSocketFactory(ssf);
            // Note: the socket factory can also be passed in as a parameter
            // to the constructor to set it for this connection only.
             lc = new LDAPConnection();
            // connect to the server
             lc.connect( ldapHost, ldapPort );
            // authenticate to the server
             lc.bind( ldapVersion, loginDN, password.getBytes("UTF8"));
             return lc;
        }catch( LDAPException e ) {
        	System.out.println( "Error: " + e.toString() );
        	return lc;
        }catch( UnsupportedEncodingException e ) {
        	System.out.println( "Error: " + e.toString() );
        	return lc;
        }
	}
	public User getUser(String myID){
        int searchScope = LDAPConnection.SCOPE_ONE;
        String searchBase = "ou=users,o=uga";
      	String searchFilter = "(uid=" + myID + ")";
		String id = "";
		String name = "";
		String major = "";
		try{
			LDAPSearchResults searchResults = this.conn.search(searchBase, searchScope, searchFilter,null,false);
         	while ( searchResults.hasMore()) {
         		//prcocesses an entry of the search result
                LDAPEntry nextEntry = null;
                try {
                    nextEntry = searchResults.next();
                }
                catch(LDAPException e) {
                    System.out.println("Error: " + e.toString());
                   // Exception is thrown, go for next entry
                    if(e.getResultCode() == LDAPException.LDAP_TIMEOUT || e.getResultCode() == LDAPException.CONNECT_ERROR)
                       break;
                    else
                       continue;
                }
                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                Iterator allAttributes = attributeSet.iterator();
                while(allAttributes.hasNext()) {
                	//processes an attribute of the current search result
                    LDAPAttribute attribute = (LDAPAttribute)allAttributes.next();
                    String attributeName = attribute.getName();
                    boolean idPresent = false;
                    boolean namePresent = false;
                    boolean majorPresent = false;
                    if(attributeName.equals("ugaIDNumber")){
                    	idPresent = true;
                    }else if(attributeName.equals("fullname")){
                    	namePresent = true;
                    }else if(attributeName.equals("ugaDegreeMajorDesc")){
                    	majorPresent = true;
                    }
                    Enumeration allValues = attribute.getStringValues();
                    if( allValues != null) {
                        while(allValues.hasMoreElements()) {
                        	//processes a value for the current attribute
                            String value = (String) allValues.nextElement();
                            if (!Base64.isLDIFSafe(value)) {
                                value = Base64.encode(value.getBytes());
                            }
                        	if(idPresent){
                        		id = value;
                        	}else if(namePresent){
                        		name = value;
                        	}else if(majorPresent){
                        		major = value;
                        	}                         
                        }//while
                    }//if
                }//while
            }//while
         	User user = new User(name, id, major);
         	conn.disconnect();
         	return user;
         	
		}catch( LDAPException e ) {
        	System.out.println( "Error: " + e.toString() );
        	return null;
		}

	}

	/**
	 * getter for the connection
	 * @return the connection
	 */
	public LDAPConnection getConn() {
		return conn;
	}
	/**
	 * setter for the connection
	 * @param conn
	 */
	public void setConn(LDAPConnection conn) {
		this.conn = conn;
	}
}
