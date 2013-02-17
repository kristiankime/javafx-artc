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
package com.artclod.javafx.collections;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.sort;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import javafx.collections.ObservableList;

import org.junit.Test;

import com.artclod.javafx.collections.ObservableListMirror;
import com.artclod.javafx.collections.ObservableListMirror.MirrorFactory;

public class ObservableListMirrorTest {

	@Test
	public void constructor_has_a_mirror_for_every_entry_in_the_original_list() throws Exception {
		ObservableListMirror<FinishedInteger, Integer> mirror = new ObservableListMirror<>(new Mirror(), observableArrayList(0, 1, 2));

		assertEquals(3, mirror.size());
		assertEquals(fi(0), mirror.get(0));
		assertEquals(fi(1), mirror.get(1));
		assertEquals(fi(2), mirror.get(2));
	}

	@Test
	public void removing_an_item_from_the_original_removes_the_coresponding_item_from_the_mirror() throws Exception {
		ObservableList<Integer> originalList = observableArrayList(0, 1, 2);
		ObservableListMirror<FinishedInteger, Integer> mirror = new ObservableListMirror<>(new Mirror(), originalList);

		originalList.remove(1);
		
		assertEquals(2, mirror.size());
		assertEquals(fi(0), mirror.get(0));
		assertEquals(fi(2), mirror.get(1));
	}
	
	@Test
	public void removing_an_item_calls_finished_on_its_mirror() throws Exception {
		ObservableList<Integer> originalList = observableArrayList(0, 1, 2);
		ObservableListMirror<FinishedInteger, Integer> mirror = new ObservableListMirror<>(new Mirror(), originalList);

		FinishedInteger fi1 = mirror.get(1);
		assertFalse(fi1.finished);
		
		originalList.remove(1);
		assertTrue(fi1.finished);
	}
	
	@Test
	public void adding_an_item_adds_a_mirror() throws Exception {
		ObservableList<Integer> original = observableArrayList(0, 1, 2);
		ObservableListMirror<FinishedInteger, Integer> mirror = new ObservableListMirror<>(new Mirror(), original);

		original.add(3);
		
		assertEquals(4, mirror.size());
		assertEquals(fi(3), mirror.get(3));
	}
	
	@Test
	public void mirrored_list_is_correct_after_sorting_original() throws Exception {
		ObservableList<Integer> original = observableArrayList(2, 1, 0);
		ObservableListMirror<FinishedInteger, Integer> mirror = new ObservableListMirror<>(new Mirror(), original);

		sort(original);
		
		assertEquals(3, mirror.size());
		assertEquals(fi(0), mirror.get(0));
		assertEquals(fi(1), mirror.get(1));
		assertEquals(fi(2), mirror.get(2));
	}
	
	// ====
	private static FinishedInteger fi(int i){
		return new FinishedInteger(i);
	}
	
	private static class FinishedInteger {
		private final Integer value;
		private boolean finished;
		
		public FinishedInteger(Integer value){
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (finished ? 1231 : 1237);
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FinishedInteger other = (FinishedInteger) obj;
			if (finished != other.finished)
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
	
	private static class Mirror implements MirrorFactory<FinishedInteger, Integer> {
		@Override
		public FinishedInteger create(Integer original) {
			return new FinishedInteger(original);
		}

		@Override
		public void finished(FinishedInteger mirror) {
			mirror.finished = true;
		}
	}
}
