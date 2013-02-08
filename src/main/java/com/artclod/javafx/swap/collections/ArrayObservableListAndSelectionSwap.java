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

package com.artclod.javafx.swap.collections;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;

public class ArrayObservableListAndSelectionSwap<E> extends ArrayObservableListSwap<E> implements ObservableListAndSelectionSwap<E> {
	private final MultipleSelectionModel<E> multipleSelectionModel;
	
	public static <T> ArrayObservableListAndSelectionSwap<T> create(ObservableList<T> underlyingList){
		return new ArrayObservableListAndSelectionSwap<T>(underlyingList);
	}
	
	public ArrayObservableListAndSelectionSwap(ObservableList<E> underlyingList){
		this(underlyingList, SelectionMode.SINGLE);
	}
	
	public ArrayObservableListAndSelectionSwap(ObservableList<E> underlyingList, SelectionMode selectionMode) {
		swap(underlyingList);
		this.multipleSelectionModel = new ListView<E>(this).getSelectionModel();
		this.multipleSelectionModel.setSelectionMode(selectionMode);
	}

	public MultipleSelectionModel<E> selectionModel() {
		return multipleSelectionModel;
	}
}
