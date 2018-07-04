package com.greentree.model.services.tokenservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;

import com.greentree.model.domain.Token;
import com.greentree.model.exception.TokenException;
import com.greentree.model.services.exception.InvalidTokenException;
import com.greentree.model.services.manager.PropertyManager;

/**
 * Implements <code>{@link ITokenService}</code> using the local filesystem for storage. Each 
 * <code>{@link Token}</code> is saved to a separate file named after the hashcode of the <code>
 * {@link RSAPublicKey}</code> for the <code>Token</code>.
 * 
 * @author david.dietrich
 *
 */
public class FileSystemTokenServiceImpl implements ITokenService {

	/** overrides <code>{@link ITokenService#commit(Token)}</code> */
	@Override
	public void commit(Token token) throws InvalidTokenException, IOException {
		try {
			if (token.validate() == false) {
				throw new InvalidTokenException ("The given Token does not validate", 
				    new Exception());
			} else {
				String filename = this.getFilename(token.getPublicKey());
				try (ObjectOutputStream out =
						new ObjectOutputStream (new FileOutputStream(filename))) {
					out.writeObject(token);
				} catch (IOException e) {
					System.out.println("Error writing to file " + filename + ": " + e.getMessage());
				}
			}
		} catch (TokenException e) {
			String msg = "Token commit threw " + e.getClass().getName() + ": " + e.getMessage();
			throw new InvalidTokenException(msg, new Exception());
		} catch (IOException e) {
			String msg = "commit(Token token) failed to load Properties file";
			throw new IOException(msg, e);
		}
	}
	
	/** 
	 * retrieves the full path to a unique filename, suitable for file IO.
	 * 
	 * @throws IOException when PropertyManager.getProperty("TokenFilesLocation") fails 
	 */
	@Override
	public String getFilename(RSAPublicKey key) throws IOException {
		String filename = String.valueOf(key.getModulus()); 
		filename = filename.substring(0, 9) + filename.substring(filename.length()-9);
		filename = PropertyManager.getProperty("TokenFilesLocation") + "\\" + filename + ".token";
		return filename;
	}

	/** 
	 * overrides {@link ITokenService#selectToken(RSAPublicKey)}
	 */
	@Override
	public Token selectToken(RSAPublicKey key) throws InvalidKeyException, IOException {		
		String filename;
		try {
			filename = this.getFilename(key);
		} catch (IOException e) {
			String msg = "selectToken(RSAPublicKey key) PropertyFileNotFoundException";
			throw new IOException(msg, e);
		}
		
		Token token = null;
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
			token = (Token) input.readObject();
		} catch (IOException e) {
			System.out.println("Error reading from " + filename + ": " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println (
					"Unable to find Token class in " + filename + ": " + e.getMessage()
			);
		};
		return token;
	}

}
