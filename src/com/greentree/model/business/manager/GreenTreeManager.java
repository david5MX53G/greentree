/**
 * This package defines the business management layer of the GreenTree application.
 */
package com.greentree.model.business.manager;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.greentree.model.exception.TokenServiceException;
import com.greentree.model.domain.Block;
import com.greentree.model.domain.Claim;
import com.greentree.model.domain.Token;
import com.greentree.model.exception.ServiceLoadException;
import com.greentree.model.services.factory.ServiceFactory;
import com.greentree.model.services.manager.JDBCPoolManager;
import com.greentree.model.services.tokenservice.ITokenService;
import java.sql.SQLException;

/**
 * GreenTreeManager defines methods used by the presentation layer to manage
 * <code>{@link Token}</code> objects with associated <code>{@link Block}</code>
 * and <code>{@link Claim}</code> objects.
 *
 * @author david5MX53G
 *
 */
public class GreenTreeManager extends ManagerSuperType {

    /** <code>{@link ServiceFactory}</code> for building services.
     */
    private final ServiceFactory factory = ServiceFactory.getInstance();

    /**
     * stores the <code>{@link Token}</code> for the session
     */
    private Token token = null;

    /**
     * stores the encrypted passphrase of the <code>Token</code> for this
     * session
     */
    private String ciphertext;

    /**
     * Stores the <code>{@link ITokenService}</code> for storing and retrieving
     * <code>{@link Token}</code> objects
     */
    private ITokenService tokenService = null;

    /**
     * Stores the singleton instance of this class.
     */
    private static GreenTreeManager _instance;

    /**
     * constructs the Singleton instance of <code>GreenTreeManager</code>. This 
     * triggers the static init block in {@link ManagerSuperType}.
     */
    private GreenTreeManager() {}

    /**
     * Instantiates a new {@link GreenTreeManager} instance using the Singleton
     * Design Pattern.
     *
     * @return the <code>GreenTreeManager</code> Singleton instance
     */
    public static GreenTreeManager getInstance() {
        if (_instance == null) {
            _instance = new GreenTreeManager();
        }
        return _instance;
    }

    /**
     * Instantiates a new <code>{@link ITokenService}</code> for use by methods
     * in this class.
     *
     * @throws ServiceLoadException when the <code>{@link ITokenService}</code>
     * fails to load
     */
    private boolean registerTokenService() {
        boolean success = false;
        try {
            this.tokenService = (ITokenService) factory.getService(ITokenService.NAME);
            success = true;
        } catch (ServiceLoadException e) {
            String msg = "ServiceFactory failed to get " + ITokenService.NAME + ": "
                + e.getMessage();
            LOGGER.debug(this.getClass().getSimpleName() + ": "
                + e.getClass().getSimpleName() + ": " + msg);
        }
        return success;
    }

    /**
     * Instantiates a <code>{@link Token}</code>, adds an initial
     * <code>{@link Block}</code> to it, commits it to storage via
     * <code>{@link ITokenService}</code>, and saves it to this object.
     *
     * @param plaintext passphrase with which to create the <code>Token</code>
     * @return boolean indicates whether the method was successful or not
     */
    public boolean registerToken(String plaintext) {
        boolean success;

        if (this.tokenService == null) {
            this.registerService("TokenService");
        }

        try {
            this.token = new Token(plaintext);
            
            if (this.token instanceof Token) {
                success = this.getTokenService().commit(this.token);
                this.ciphertext = token.encrypt(plaintext);
            } else {
                LOGGER.error("registerToken(" + plaintext + ") "
                    + "failed to initialize Token");
                success = false;
            }
        } catch (TokenServiceException e) {
            LOGGER.error(
                e.getClass().getSimpleName() + ": "
                + e.getMessage()
            );
            success = false;
        }
        
        return success;
    }

    /**
     * Logs a logout event to the <code>{@link Token}</code>, saves it with <code>
     * {@link ITokenService}</code>, and removes it from this object.
     *
     * @throws TokenServiceException when the input <code>Token</code> is bad
     */
    public void logOut() throws TokenServiceException {
        String dateStamp = new Date().toString();
        this.token.addBlock("logged out at " + dateStamp, this.ciphertext);
        try {
            getTokenService().commit(token);
            LOGGER.debug("Token commit done");
            this.token = null;
            LOGGER.debug("Token is null");
            JDBCPoolManager.shutDown();
            LOGGER.debug("JDBC pool shut down");
        } catch (TokenServiceException | SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Retrieves the <code>Token</code> corresponding to the given key,
     * validates the passphrase, and registers the <code>Token</code> in the
     * current {@link GreenTreeManager} instance, if the passphrase is valid.
     *
     * @param key used to locate the <code>Token</code> and decrypt the
     * passphrase
     * @param ciphertext used to authenticate ownership of the
     * <code>Token</code>
     * @return {@link boolean} for success (true) or failure (false)
     */
    public boolean registerToken(RSAPublicKey key, String ciphertext) {
        boolean success = registerService("TokenService");
        LOGGER.debug(
            "registerService(\"TokenServce\") returned "
            + String.valueOf(success)
        );
        if (!success) {
            LOGGER.debug("registerService(\"TokenService\") FAILED");
        } else {
            try {
                this.token = this.tokenService.selectToken(key);
                if (this.token == null) {
                    LOGGER.error("getTokenService().selectToken(key) FAILED");
                } else if (token.checkPassphrase(ciphertext)) {
                    LOGGER.debug("token.checkPassphrase(ciphertext) is true");
                    this.ciphertext = ciphertext;
                    token.addBlock(
                        "authenticated at " + new Date().toString(), ciphertext
                    );
                    tokenService.commit(token);
                    success = true;
                } else {
                    LOGGER.debug("token.checkPassphrase(ciphertext) is false");
                    success = false;
                }
            } catch (TokenServiceException e) {
                LOGGER.error(e.getMessage());
                success = false;
            }
        }
        return success;
    }

    /**
     * retrieves an {@link IService} implementation
     *
     * @return true, if registration was successful
     */
    @Override
    public boolean registerService(String name) {
        boolean success = false;
        if (name.equalsIgnoreCase("TokenService")) {
            success = this.registerTokenService();
            if (!success) {
                String msg
                    = this.getClass().getSimpleName() + ": registerTokenService() failed";
                LOGGER.debug(msg);
            } else {
                success = true;
            }
        }
        return success;
    }

    /**
     * Instantiates a <code>{@link Token}</code>, adds an initial
     * <code>{@link Block}</code>, commits it to storage via
     * <code>{@link ITokenService}</code>, and saves it to this object. This
     * uses <code>char[]</code> for input rather than
     * <code>{@link String}</code> because the latter persists in memory longer
     * than the former, increasing the window of opportunity for an attacker
     * with read access to copy the pass in plaintext. See Stackoverflow <a href=
     * "https://stackoverflow.com/questions/8881291/why-is-char-preferred-over-string-for-passwords">
     * 8881291</a>.
     *
     * @param pass <code>{@link java.lang.Character}</code>
     * <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html">
     * array</a> for building a new <code>Token</code>
     */
    public void registerToken(char[] pass) {
        // TODO Auto-generated method stub

    }

    /**
     * @return {@link RSAPublicKey} for the {@link Token} registered to this
     * {@link GreenTreeManager}
     */
    public RSAPublicKey getPublicKey() {
        return this.token.getPublicKey();
    }

    /**
     * Returns the data from each <code>Block</code> in the block chain of the
     * given <code>Token
     * </code> where the <code>Block</code> contains at least one
     * <code>Claim</code> valid for the requesting <code>Token</code>.
     *
     * @param key {@link RSAPublicKey} identifying the {@link Token} from which
     * to retrieve data. Only data will be returned which its <code>Token</code>
     * has authorized via {@link Claim} objects for the active
     * {@link GreenTreeManager} <code>Token</code>.
     * @return {@link ArrayList}<{@link String}> obtained from the {@link com.greentree.model.
     *     domain.Block} objects in the <code>blockChain</code> of the given {@link com.greentree.
     *     model.domain.Token} where at least one
     * {@link com.greentree.model.domain.Claim} exists matching the
     * <code>Token</code> of the active <code>GreenTreeManager</code>.
     * @throws GreenTreeManagerException when there is an error selecting a
     * <code>Token</code> for the given <code>RSAPublicKey</code> or when the
     * current <code>GreenTreeManager</code> does not have a <code>Token</code>
     */
    public ArrayList<String> getData(RSAPublicKey key)
        throws GreenTreeManagerException {
        ArrayList<String> stringData = new ArrayList<>();
        if (this.token == null) {
            String msg = ("getData(Token token) throws TokenException because GreenTreeManager"
                + " missing token");
            throw new GreenTreeManagerException(msg, LOGGER);
        } else {
            try {
                Token server = getTokenService().selectToken(key);
                ArrayList<Block> list = server.getBlockChain();
                Iterator<Block> it = list.iterator();
                Block block;
                Claim claim;
                Long start;
                String data;
                while (it.hasNext()) {
                    block = it.next();
                    if (server.equals(this.token)) {
                        /**
                         * if it's the current token we're reading from, add
                         * claims to ensure every block is readable
                         */
                        start = new GregorianCalendar().getTimeInMillis();
                        claim = new Claim(token, start, start + 60000);
                        block.addClaim(claim, this.ciphertext);
                    }
                    // the hash can be used to id each message when adding claims
                    data = block.getData(token, this.ciphertext);
                    if (data != null) {
                        stringData.add(data);
                    }
                }
            } catch (TokenServiceException e) {
                throw new GreenTreeManagerException(e.getMessage(), LOGGER);
            }
        }
        return stringData;
    }

    /**
     * adds a {@link Block} to the active <code>Token</code> with a
     * {@link Claim} granting access to another <code>Token</code> for the given
     * duration.
     *
     * @param data {@link String} to be stored in the new <code>Block</code>
     * @param clientKey {@link RSAPublicKey} identifying the {@link Token} that
     * will have access to this <code>Block</code>
     * @param notBefore <code>long</code> specifies the earliest time in the
     * number of millis since January 1, 1970, 00:00:00 GMT that access to this
     * <code>Block</code> will be allowed
     * @param expirationDate <code>long</code> specifies the time after which
     * access to this <code>
     *     Block</code> will be denied in the number of millis since January 1,
     * 1970, 00:00:00 GMT
     * @return {@link Boolean} true, when execution is successful; otherwise,
     * false
     */
    public boolean addBlock(String data, RSAPublicKey clientKey, long notBefore,
        long expirationDate) {
        boolean success = false;
        try {
            Token clientToken = getTokenService().selectToken(clientKey);

            Claim claim = new Claim(clientToken, notBefore, expirationDate);

            this.token.addBlock(data, this.ciphertext, claim);
            getTokenService().commit(this.token);

            LOGGER.debug("getTokenService().commit(this.token) PASSED");
            success = true;
        } catch (TokenServiceException e) {
            LOGGER.debug(this.getClass().getSimpleName() + ": " + e.getClass().getName()
                + ": " + e.getMessage());
            success = false;
        }
        return success;
    }

    /**
     * removes the {@link Token} from the current {@link GreenTreeManager}
     * instance. This was originally done purely for testing the overloaded
     * {@link GreenTreeManager#registerToken()} method.
     */
    public void deregisterToken() {
        this.token = null;
    }

    /**
     * @return {@link ITokenService} tokenService
     */
    public ITokenService getTokenService() {
        return tokenService;
    }
}
