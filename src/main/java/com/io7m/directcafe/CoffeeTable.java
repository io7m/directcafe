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


import java.io.IOException;
import java.util.Comparator;

/**
 * Show a table of coffees.
 */

public final class CoffeeTable
{
  private CoffeeTable()
  {

  }

  /**
   * The main program.
   *
   * @param args Command-line arguments
   *
   * @throws IOException On I/O errors
   */

  public static void main(
    final String[] args)
    throws IOException
  {
    final var coffees = new CoffeeFetcher().fetch();

    System.out.printf(
      "%-48s %-10s %-10s %-10s %-10s %-10s %-10s\n",
      "Name",
      "Strength",
      "Acidity",
      "Flavour",
      "Body",
      "Aroma",
      "Total");

    coffees.values()
      .stream()
      .filter(p -> !p.name().toUpperCase().contains("DECAF"))
      .sorted(
        Comparator.comparingInt(Coffee::flavour)
          .thenComparingInt(Coffee::body)
          .thenComparingInt(Coffee::aroma)
          .reversed())
      .forEach(
        coffee ->
          System.out.printf(
            "%-48s %-10d %-10d %-10d %-10d %-10d %-10d\n",
            coffee.name(),
            Integer.valueOf(coffee.strength()),
            Integer.valueOf(coffee.acidity()),
            Integer.valueOf(coffee.flavour()),
            Integer.valueOf(coffee.body()),
            Integer.valueOf(coffee.aroma()),
            Integer.valueOf(coffee.total())));
  }
}
