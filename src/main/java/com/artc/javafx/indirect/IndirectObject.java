/*
 * Copyright (c) 2012, Kristian Kime
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */

package com.artc.javafx.indirect;

/**
 * Indicates this object is a proxy for an underlying object which can be changed.
 * 
 * Usually the indirect object will implement the same interface as the proxied object (ie an 
 * IndirectObject<List<E>> would implement List<E> and, in all likelihood, delegate most calls to an 
 * underlying list.
 * 
 * While this interface does not have any method to perform notification about changes it is 
 * assumed that if the proxied object has eventing/listeners events will "pass through" the
 * indirect layer. That is if a listener is added to the indirect object and the underlying object
 * fires an event the listener would receive an event. Additionally it is expected that if the 
 * underlying object was changed the listeners would receive an appropriate event.
 */
public interface IndirectObject<T> extends UncontrolledIndirectObject<T> {
	
	public void setUnderlyingObject(T underlyingObject);
	
}
