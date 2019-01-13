/*
 * Copyright © 2019 Mark Raynsford <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.directcafe;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Fetch lists of coffee.
 */

public final class CoffeeFetcher
{
  private enum Aspect
  {
    ACIDITY("https://www.coffee-direct.co.uk/pages/coffee-by-acidity"),
    AROMA("https://www.coffee-direct.co.uk/pages/coffee-by-aroma"),
    BODY("https://www.coffee-direct.co.uk/pages/coffee-by-body"),
    FLAVOUR("https://www.coffee-direct.co.uk/pages/coffee-by-flavour"),
    STRENGTH("https://www.coffee-direct.co.uk/pages/coffee-by-strength");

    private final String url;

    Aspect(final String in_url)
    {
      this.url = Objects.requireNonNull(in_url, "url");
    }
  }

  CoffeeFetcher()
  {

  }

  /**
   * @return All available coffees
   *
   * @throws IOException On I/O errors
   */

  public Map<String, Coffee> fetch()
    throws IOException
  {
    final HashMap<String, Coffee> coffees = new HashMap<>();

    for (final var aspect : Aspect.values()) {
      final var document = Jsoup.connect(aspect.url).get();
      final var tbodies = document.select("tbody");
      final var tbody = tbodies.first();

      for (final var trow : tbody.children()) {
        final var e_name = trow.getElementsByTag("a");
        final var name = e_name.text();
        final var count = trow.getElementsByAttributeValue("class", "chart-on").size();

        Coffee coffee;
        if (coffees.containsKey(name)) {
          coffee = coffees.get(name);
        } else {
          coffee = Coffee.builder().setName(name).build();
        }

        switch (aspect) {
          case ACIDITY:
            coffee = coffee.withAcidity(count);
            break;
          case AROMA:
            coffee = coffee.withAroma(count);
            break;
          case BODY:
            coffee = coffee.withBody(count);
            break;
          case FLAVOUR:
            coffee = coffee.withFlavour(count);
            break;
          case STRENGTH:
            coffee = coffee.withStrength(count);
            break;
        }

        coffees.put(name, coffee);
      }
    }

    return Map.copyOf(coffees);
  }
}
