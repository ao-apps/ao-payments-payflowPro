/*
 * ao-payments-payflowPro - Provider for the Payflow Pro XMLPay system.
 * Copyright (C) 2021, 2022  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-payments-payflowPro.
 *
 * ao-payments-payflowPro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-payments-payflowPro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-payments-payflowPro.  If not, see <https://www.gnu.org/licenses/>.
 */
module com.aoapps.payments.payflowPro {
  exports com.aoapps.payments.payflowPro;
  // Direct
  requires com.aoapps.lang; // <groupId>com.aoapps</groupId><artifactId>ao-lang</artifactId>
  requires com.aoapps.payments.api; // <groupId>com.aoapps</groupId><artifactId>ao-payments-api</artifactId>
  requires org.apache.commons.lang3; // <groupId>org.apache.commons</groupId><artifactId>commons-lang3</artifactId>
  requires payflow.v440; // <groupId>com.paypal</groupId><artifactId>payflow</artifactId>
  // Java SE
  requires java.logging;
} // TODO: Avoiding rewrite-maven-plugin-4.22.2 truncation
