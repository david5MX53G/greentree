package com.greentree.model.services.tokenservice;

import com.greentree.model.exception.TokenServiceException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.interfaces.RSAPublicKey;
import com.greentree.model.domain.Token;
import com.greentree.model.services.manager.PropertyManager;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Implements <code>{@link ITokenService}</code> using the local filesystem for
 * storage. Each <code>{@link Token}</code> is saved to a separate file named
 * after the hashcode of the <code>
 * {@link RSAPublicKey}</code> for the <code>Token</code>.
 *
 * @author david5MX53G
 *
 */
public class FileSystemTokenServiceImpl implements ITokenService {

    /**
     * {@link org.apache.logging.log4j.Logger} is for logging logs to the log
     */
    private static final Logger LOG = LogManager.getLogger();

    /**
     * overrides {@link ITokenService#commit(Token)}
     *
     * @return boolean to indicate success or failure
     * @throws com.greentree.model.exception.TokenServiceException 
     * when the input {@link com.greentree.model.domain.Token} does not validate
     */
    @Override
    public boolean commit(Token token) throws TokenServiceException {
        boolean result = false;
        try {
            if (token.validate() == false) {
                LOG.error("commit(Token) could not validate Token");
            } else {
                String filename = this.getFilename(token.getPublicKey());
                try (ObjectOutputStream out
                    = new ObjectOutputStream(new FileOutputStream(filename))) {
                    out.writeObject(token);
                result = true;
                } catch (IOException e) {
                    LOG.error("Error writing to file " + filename + ": " 
                        + e.getMessage());
                }
            }
        } catch (IOException | ParserConfigurationException | 
            SAXException e) {
            throw new TokenServiceException(e.getMessage(), LOG, e);
        }
        return result;
    }

    /**
     * The filename of a <code>{@link RSAPublicKey}</code> allows us to save and
     * restore the <code>{@link Token}</code> of the given
     * <code>RSAPublicKey</code> from file IO. The
     * <code>{@link Java.util.Properties}</code> file is expected to have a key
     * named " TokenFilesLocation".
     *
     * @param key used to generate the filename
     * @return the unique, consistent filename of this <code>RSAPublicKey</code>
     * @throws IOException when the {@link Properties} file does not load
     * 
     * @throws javax.xml.parsers.ParserConfigurationException when {@link 
     *         PropertyManager#getProperty(java.lang.String)} fails
     * 
     * @throws org.xml.sax.SAXException when {@link 
     *         PropertyManager#getProperty(java.lang.String)} fails
     */
    public String getFilename(RSAPublicKey key) 
        throws IOException, ParserConfigurationException, SAXException {
        String filename = String.valueOf(key.getModulus());
        
        filename = filename.substring(0, 9) 
                 + filename.substring(filename.length() - 9);
        
        filename = 
            PropertyManager.getProperty("tokenfilepath") 
            + "/" + filename + ".token";
        
        return filename;
    }

    /**
     * overrides {@link ITokenService#selectToken(RSAPublicKey)}
     */
    @Override
    public Token selectToken(RSAPublicKey key) throws TokenServiceException {
        String filename;
        Token token = null;
        
        try {
            filename = this.getFilename(key);
            ObjectInputStream input = 
                new ObjectInputStream(new FileInputStream(filename));
            token = (Token) input.readObject();
        } 
        
        catch (IOException | ClassNotFoundException | 
            ParserConfigurationException | SAXException e) {
            throw new TokenServiceException(e.getMessage(), LOG, e);
        }
        
        return token;
    }
}
