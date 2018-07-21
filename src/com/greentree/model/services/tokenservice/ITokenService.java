package com.greentree.model.services.tokenservice;

import java.security.interfaces.RSAPublicKey;
import com.greentree.model.business.exception.TokenServiceException;
import com.greentree.model.domain.Token;
import com.greentree.model.services.IService;

/**
 * This is the separated interface class used for saving and loading
 * <code>{@link Token}</code> objects from storage services as defined by
 * implementation classes.
 *
 * @author david5MX53G
 *
 */
public interface ITokenService extends IService {

    /**
     * This is used by the <code>{@link ServiceFactory}</code> to build this
     * <code>{@link IService}</code>.
     */
    public final String NAME = "ITokenService";

    /**
     * Saves the token {@link com.greentree.model.domain.Token} to storage for
     * later use.
     *
     * @param token {@link com.greentree.model.domain.Token} to be saved in
     * storage
     * @throws com.greentree.model.business.exception.TokenServiceException when
     * the underlying implementation class has trouble.
     */
    public boolean commit(Token token) throws TokenServiceException;

    /**
     * Finds the <code>Token</code> matching the given <code>RSAPublickey</code>
     * in storage and returns it.
     *
     * @param key used to locate the <code>Token</code>
     * @return the {@link com.greentree.model.domain.Token} corresponding with
     * the given {@link java.security.interfaces.RSAPublicKey}
     *
     * @throws com.greentree.model.business.exception.TokenServiceException when
     * the underlying implementation class has trouble.
     */
    public Token selectToken(RSAPublicKey key) throws TokenServiceException;
}
