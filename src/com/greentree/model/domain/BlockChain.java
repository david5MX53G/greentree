/*
 * The MIT License
 *
 * Copyright 2018 david5MX53G.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.greentree.model.domain;

import java.util.TreeSet;

/**
 * The <code>BlockChain</code> class serves as the master list of all {@link 
 * Block} objects. Each member of the chain will be unique and will refer to one
 * member after it. All members will refer to one previous member, with the 
 * exception of ROOT, which references itself. Members are ordered by reference 
 * such that a member referenced by another member is less than that other 
 * member.
 * 
 * @author david5MX53G
 */
public class BlockChain {
    /** stores all the {@link Block} objects in this {@link BlockChain} */
    private static final TreeSet<Block> BLOCKSET = new TreeSet<>();
    
    /**
     * appends the given {@link Block} to this {@link BlockChain} such that it 
     * refers to the <code>Block</code> before it and will be referenced by the 
     * next <code>Block</code> added after it. 
     * 
     * @param add {@link Block} will be appended to this {@link BlockChain}
     * @return true, if the {@link Block} was appended successfully
     */
    public boolean addBlock(Block add) {
        boolean result = false;
        //TODO: implement addBlock(Block add)
        return result;
    }
}
