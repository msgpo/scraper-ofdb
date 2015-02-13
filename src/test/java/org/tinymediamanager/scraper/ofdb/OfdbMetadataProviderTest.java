/*
 * Copyright 2012 - 2015 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tinymediamanager.scraper.ofdb;

import org.junit.Assert;
import org.junit.Test;
import org.tinymediamanager.scraper.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class OfdbMetadataProviderTest {
  @Test
  public void testSearch() {
    IMovieMetadataProvider mp = null;
    List<MediaSearchResult> results = null;
    MediaSearchOptions options = null;

    try {
      options = new MediaSearchOptions(MediaType.MOVIE);
      // Die Piefke Saga
      results = null;
      try {
        mp = new OfdbMetadataProvider();
        options = new MediaSearchOptions(MediaType.MOVIE, MediaSearchOptions.SearchParam.QUERY, "Die Piefke Saga");
        options.set(MediaSearchOptions.SearchParam.LANGUAGE, "de");
        results = mp.search(options);
        // did we get a result?
        assertNotNull("Result", results);
        assertEquals("Die Piefke-Saga", results.get(0).getTitle());

        // result count
        assertEquals("Result count", 1, results.size());
      }
      catch (Exception e) {
        e.printStackTrace();
        fail();
      }

      // Lucky # Slevin
      results = null;
      try {
        mp = new OfdbMetadataProvider();
        options = new MediaSearchOptions(MediaType.MOVIE, MediaSearchOptions.SearchParam.QUERY, "Slevin");
        options.set(MediaSearchOptions.SearchParam.LANGUAGE, "de");
        results = mp.search(options);
        // did we get a result?
        assertNotNull("Result", results);

        // result count
        assertEquals("Result count", 1, results.size());

        assertEquals("Lucky # Slevin", results.get(0).getTitle());
        assertEquals("Lucky Number Slevin", results.get(0).getOriginalTitle());
        assertEquals("2006", results.get(0).getYear());
      }
      catch (Exception e) {
        e.printStackTrace();
        fail();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void testScrape() {
    IMovieMetadataProvider mp = null;
    MediaScrapeOptions options = null;
    MediaMetadata md = null;

    // Merida
    try {
      mp = new OfdbMetadataProvider();
      options = new MediaScrapeOptions(MediaType.MOVIE);
      options.setLanguage(MediaLanguages.de);
      options.setId(mp.getProviderInfo().getId(), "226045");

      md = mp.getMetadata(options);

      assertThat(md.getStringValue(MediaMetadata.TITLE)).isEqualTo("Merida - Legende der Highlands");
      assertThat(md.getStringValue(MediaMetadata.ORIGINAL_TITLE)).isEqualTo("Brave");
      assertThat(md.getStringValue(MediaMetadata.YEAR)).isEqualTo("2012");
      assertThat(md.getStringValue(MediaMetadata.PLOT))
          .startsWith(
                  "Merida wächst als Erstgeborene von König Fergus an, der im schottischen Hochland sein Volk, bestehend aus vier Clans, anführt. Fergus hatte, als Merida noch ein Kleinkind war, einen Teil seines linken Beines im Kampf gegen einen riesigen, gefährlichen Bären verloren -");
      assertThat(md.getStringValue(MediaMetadata.TAGLINE)).isEmpty();
      assertThat(md.getDoubleValue(MediaMetadata.RATING)).isBetween(6.5, 7d);

      assertThat(md.getCastMembers(MediaCastMember.CastType.ACTOR)).isNotNull();
      assertThat(md.getCastMembers(MediaCastMember.CastType.ACTOR).size()).isEqualTo(9);
      assertThat(md.getCastMembers(MediaCastMember.CastType.ACTOR).get(0).getName()).isEqualTo("Billy Connolly");
      assertThat(md.getCastMembers(MediaCastMember.CastType.ACTOR).get(0).getCharacter()).isEqualTo("Fergus");
      assertThat(md.getCastMembers(MediaCastMember.CastType.DIRECTOR)).isNotNull();
      assertThat(md.getCastMembers(MediaCastMember.CastType.DIRECTOR).size()).isEqualTo(3);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail();
    }

    // with imdbid (Bourne Vermächtnis)
    try {
      mp = new OfdbMetadataProvider();

      MediaScrapeOptions scop = new MediaScrapeOptions(MediaType.MOVIE);
      scop.setId(MediaMetadata.IMDBID, "tt1194173");
      md = mp.getMetadata(scop);

      assertThat(md.getStringValue(MediaMetadata.TITLE)).isEqualTo("Das Bourne Vermächtnis");
      assertThat(md.getStringValue(MediaMetadata.ORIGINAL_TITLE)).isEqualTo("The Bourne Legacy");
      assertThat(md.getStringValue(MediaMetadata.YEAR)).isEqualTo("2012");

    }
    catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}