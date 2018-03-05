package org.wikidata.wdtk.datamodel.implementation;

/*
 * #%L
 * Wikidata Toolkit Data Model
 * %%
 * Copyright (C) 2014 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;
import org.wikidata.wdtk.datamodel.interfaces.ValueSnak;

public class StatementImplTest {

	EntityIdValue subject;
	ValueSnak mainSnak;
	Claim claim;

	Statement s1;
	Statement s2;

	@Before
	public void setUp() throws Exception {
		subject = new ItemIdValueImpl("Q42", "http://wikidata.org/entity/");
		PropertyIdValue property = new PropertyIdValueImpl("P42",
				"http://wikidata.org/entity/");
		mainSnak = new ValueSnakImpl(property, subject);

		claim = new ClaimImpl(subject, mainSnak, Collections.emptyList());
		s1 = new StatementImpl("MyId", StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);
		s2 = new StatementImpl("MyId", StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);
	}

	@Test
	public void gettersWorking() {
		assertEquals(s1.getClaim(), claim);
		assertEquals(s1.getReferences(),
				Collections.<List<? extends Snak>> emptyList());
		assertEquals(s1.getRank(), StatementRank.NORMAL);
		assertEquals(s1.getStatementId(), "MyId");
		assertEquals(s1.getValue(), subject);
	}

	@Test(expected = NullPointerException.class)
	public void mainSnakNotNull() {
		new StatementImpl("MyId", StatementRank.NORMAL, null,
				Collections.emptyList(), Collections.emptyList(), subject);
	}

	@Test(expected = NullPointerException.class)
	@SuppressWarnings("deprecation")
	public void claimNotNull() {
		new StatementImpl(null, Collections.<Reference> emptyList(),
				StatementRank.NORMAL, "MyId");
	}

	@Test
	public void referencesCanBeNull() {
		Statement statement = new StatementImpl("MyId", StatementRank.NORMAL, mainSnak,  Collections.emptyList(), null, subject);
		assertTrue(statement.getReferences().isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void rankNotNull() {
		new StatementImpl("MyId", null, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);
	}

	@Test
	public void idCanBeNull() {
		Statement statement = new StatementImpl(null, StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);
		assertEquals(statement.getStatementId(), "");
	}

	@Test
	public void hashBasedOnContent() {
		assertEquals(s1.hashCode(), s2.hashCode());
	}

	@Test
	public void equalityBasedOnContent() {
		Statement sDiffClaim = new StatementImpl("MyId", StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.emptyList(),
				new ItemIdValueImpl("Q43", "http://wikidata.org/entity/"));
		Statement sDiffReferences = new StatementImpl("MyId", StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.singletonList(new ReferenceImpl(
						Collections.singletonList(new SnakGroupImpl(Collections.singletonList(mainSnak)))
				)), subject);
		Statement sDiffRank = new StatementImpl("MyId", StatementRank.PREFERRED, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);
		Statement sDiffId = new StatementImpl("MyOtherId", StatementRank.NORMAL, mainSnak,
				Collections.emptyList(), Collections.emptyList(), subject);

		assertEquals(s1, s1);
		assertEquals(s1, s2);
		assertThat(s1, not(equalTo(sDiffClaim)));
		assertThat(s1, not(equalTo(sDiffReferences)));
		assertThat(s1, not(equalTo(sDiffRank)));
		assertThat(s1, not(equalTo(sDiffId)));
		assertThat(s1, not(equalTo(null)));
		assertFalse(s1.equals(this));
	}

}
