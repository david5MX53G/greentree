package com.greentree.model.services.tokenservice;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import com.greentree.model.business.exception.PropertyFileNotFoundException;
import com.greentree.model.domain.Token;
import com.greentree.model.services.IService;
import com.greentree.model.services.exception.InvalidTokenException;

/**
 * This is the separated interface class used for saving and loading <code>{@link Token}</code> 
 * objects from storage services as defined by implementation classes.
 * 
 * @author david.dietrich
 *
 */
public interface ITokenService extends IService {
	/**
	 * This is used by the <code>{@link ServiceFactory}</code> to build this 
	 * <code>{@link IService}</code>.
	 */
	public final String NAME = "ITokenService";
	
	/**
	 * Saves the token <code>Token</code> to storage for later use.
	 * 
	 * @param token to be saved in storage
	 * @throws InvalidTokenException {@link Token#validate()} is false
	 * @throws IOException when the {@link Properties} file does not load
	 */
	public void commit(Token token) throws InvalidTokenException, IOException;
	
	/**
	 * Finds the <code>Token</code> matching the given <code>RSAPublickey</code> in storage and 
	 * returns it.
	 * 
	 * @param key used to locate the <code>Token</code>
	 * @return the <code>Token</code> corresponding with the given <code>RSAPublicKey</code>
	 * @throws InvalidKeyException when the <code>{@link RSAPublicKey}</code> fails a <code>
	 * hashCode()</code> check against a copy of itself
	 * @throws NoSuchAlgorithmException when RSA algorithm is not available on the host
	 * @throws InvalidKeySpecException when the key fails to generate a proper <code>{@link 
	 * X509EncodedKeySpec}</code>
	 * @throws IOException when <code>{@link Properties}</code> fails to load
	 */
	public Token selectToken(RSAPublicKey key) throws InvalidKeyException, 
	NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	/**
	 * The filename of a <code>{@link RSAPublicKey}</code> allows us to save and restore the 
	 * <code>{@link Token}</code> of the given <code>RSAPublicKey</code> from file IO. The 
	 * <code>{@link Java.util.Properties}</code> file is expected to have a key named "
	 * TokenFilesLocation".
	 * 
	 * @param key used to generate the filename
	 * @return the unique, consistent filename of this <code>RSAPublicKey</code>
	 * @throws PropertyFileNotFoundException when the <code>{@link Properties}</code> file fails
	 * @throws IOException when the {@link Properties} file does not load
	 */
	String getFilename(RSAPublicKey key) throws IOException;
}
