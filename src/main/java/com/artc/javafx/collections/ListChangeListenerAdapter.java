/*
 * Copyright (c) 2012, Kristian Kime
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
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
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */

package com.artc.javafx.collections;

import javafx.collections.ListChangeListener;

public class ListChangeListenerAdapter<T> implements ListChangeListener<T> {
	@Override
	public void onChanged(Change<? extends T> c) {
		while (c.next()) {
			if (c.wasPermutated()) {
				for (int oldIndex = c.getFrom(); oldIndex < c.getTo(); ++oldIndex) {
					int newIndex = c.getPermutation(oldIndex);
					permutedChange(oldIndex, newIndex, c.getList().get(newIndex));
				}
			} else if (c.wasUpdated()) {
				for (int index = c.getFrom(); index < c.getTo(); ++index) {
					updatedChange(index, c.getList().get(index));
				}
			} else {
				for (T removedElement : c.getRemoved()) {
					removedChange(removedElement);
				}
				for (int index = c.getFrom(); index < c.getTo(); ++index) {
					T addedElement = c.getList().get(index);
					addedChange(addedElement, index);
				}
			}
		}
	}
	
	public void permutedChange(int oldIndex, int newIndex, T element){
		// NOOP
	}

	public void updatedChange(int index, T element) {
		// NOOP
	}
	
	public void removedChange(T item){
		// NOOP
	}
	
	public void addedChange(T item, int index){
		// NOOP
	}
}
