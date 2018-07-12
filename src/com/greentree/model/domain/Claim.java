/**
 * 
 */
package com.greentree.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * A <code>Claim</code> states that a <code>User</code> can read a <code>Block</code>. A <code>
 * Claim</code> is <code>true</code> when both the owner and reader of the <code>Block</code> have 
 * a <code>Claim</code> stating that the reader can read the <code>Block</code>.
 * <p>
 * The <code>Claim</code> class is based on the <a href="https://tools.ietf.org/html/rfc7519">JSON 
 * Web Token (JWT)</a> standard, but does not include any encryption features. This class serves 
 * only as the basis of the GreenTree access control system.
 * 
 * @author david5MX53G
 * @see com.greentree.model.domain.ClaimTest
 * @see com.greentree.model.domain.Block
 * @see com.greentree.model.domain.User
 */
public class Claim implements Serializable {	
	/**
	 * Eclipse generated this {@link long} so this class can implement {@link 
	 * java.io.Serializable}
	 */
	private static final long serialVersionUID = -7196461677312132319L;

	/** The <code>Token</code> to which this was issued. */
	private final Token token;
	
	/** Milliseconds since Jan 1, 1970, 00:00:00 GMT at which this is no longer valid. */
	private final Long expirationTime;
	
	/** Milliseconds since Jan 1, 1970, 00:00:00 GMT at which this becomes valid. */
	private final Long notBefore;
	
	/** Milliseconds since Jan 1, 1970, 00:00:00 GMT at which this was issued. */
	private final Long issuedAt;
	
	/**
	 * Issues a new <code>{@link Claim}</code> object, suitable for assignment in the <code>
	 * claimSet</code> of a <code>{@link Block}</code>.
	 * 
	 * @param token {@link Token} which should have access to the data for the assigned <code>Block
	 *     </code>
	 * @param expirationTime <code>long</code> after which time this object no longer grants access to 
	 *     the <code> Block</code>
	 * @param notBefore <code>long</code> before which time this object will not allow access to the 
	 *     <code>Block </code> data
	 */
	public Claim(Token token, long notBefore, long expirationTime) {
		this.token = token;
		this.issuedAt = new Date().getTime();
		this.expirationTime = expirationTime;
		this.notBefore = notBefore;
	}
	
	/**
	 * Instantiates a bogus <code>{@link Claim}</code> object, suitable for testing.
	 */
	public Claim() {
		this.token = null;
		this.issuedAt = new Date().getTime();
		this.notBefore = this.issuedAt;
		this.expirationTime = this.issuedAt;
	}
	
	/** @return expirationTime of this object */
	public Long getExpirationTime() {
		return this.expirationTime;
	}
	
	/** @return <code>{@link Token}</code> to which this was issued */
	public Token getToken() {
		return this.token;
	}
	
	/** @return milliseconds time at which this object becomes valid. */
	public Long getNotBefore() {
		return this.notBefore;
	}
	
	/** @return milliseconds time at which this object was issued. */
	public Long getIssuedAt() {
		return this.issuedAt;
	}

	/**
	 * Checks the fields of this <code>Claim</code> to see if they are the same as the given <code>
	 * Object</code>.
	 * 
	 * @return true, if all fields of this <code>Claim</code> match the given <code>Object</code>
	 */
	@Override public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Claim) {
			Claim that = (Claim) other;
			
			if (Long.compare(that.getExpirationTime(), this.expirationTime) != 0) {
				result = false;
			} else if (Long.compare(that.getIssuedAt(), this.issuedAt) != 0) {
				result = false;
			} else if (Long.compare(that.getNotBefore(), this.notBefore) != 0) {
				result = false;
			} else if (this.getToken().equals(that.getToken()) == false) {
				result = false;
			} else {
				result = true;
			}
		}
		return result;
	}
	
	/** 
	 * If two <code>Claim</code> objects are equal according to the <code>{@link Claim#equals
	 * (Object)}</code> method, then calling this <code>hashCode()</code> method on each of the two
	 * objects must produce the same integer result. Credit for this method belongs to Joshua Bloch
	 * ("Effective Java", Ch. 3) as in duffymo (<a href="https://tinyurl.com/yb4xbg3m">How to 
	 * generate a hash code from three longs</a>).
     * 
	 * @return integer sum of the hashes for the member fields of this object.
	 */
	@Override public int hashCode() {
		int result = (int) (this.expirationTime ^ (this.expirationTime >>> 32));
		result = 31 * result + (int) (this.issuedAt ^ (this.issuedAt >>> 32));
		result = 31 * result + (int) (this.notBefore ^ (this.notBefore >>> 32));
		return result;
	}
	
	@Override public String toString() {
		String expires = String.valueOf(this.expirationTime);
		String issued = String.valueOf(this.issuedAt);
		String notBefore = String.valueOf(this.notBefore);
		String token = this.token.toString();
		return 
		    "expires: " + expires + "\n" +
			"issued: " + issued + "\n" +
			"notBefore: " + notBefore + "\n" +
			"## token ## \n" + token;
	}
	
	/** @return true, if this object is in working order. */
	public boolean validate() {
		long now = new Date().getTime();
		if (this.expirationTime.compareTo(now) <= 0) {
			return false;
		}
		if (this.issuedAt.compareTo(now) > 0) {
			return false;
		}
		if (this.notBefore.compareTo(now) > 0) {
			return false;
		}
		if (this.token == null) {
			return false;
		}
		return true;
	}
}
