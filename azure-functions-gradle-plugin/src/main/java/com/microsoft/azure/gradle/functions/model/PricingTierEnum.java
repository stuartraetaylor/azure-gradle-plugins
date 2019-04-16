/*
 * MIT License
 *
 * Copyright (c) 2017-2019 Elena Lakhno
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.gradle.functions.model;

import com.microsoft.azure.management.appservice.PricingTier;

public enum PricingTierEnum {
    F1("F1"),
    f1("F1"),
    D1("D1"),
    d1("D1"),
    B1("B1"),
    b1("B1"),
    B2("B2"),
    b2("B2"),
    B3("B3"),
    b3("B3"),
    S1("S1"),
    s1("S1"),
    S2("S2"),
    s2("S2"),
    S3("S3"),
    s3("S3"),
    P1("P1"),
    p1("P1"),
    P2("P2"),
    p2("P2"),
    P3("P3"),
    p3("P3");

    private final String pricingTier;

    PricingTierEnum(final String pricingTier) {
        this.pricingTier = pricingTier;
    }

    public PricingTier toPricingTier() {
        switch (pricingTier) {
            case "F1":
                return PricingTier.FREE_F1;
            case "D1":
                return PricingTier.SHARED_D1;
            case "B1":
                return PricingTier.BASIC_B1;
            case "B2":
                return PricingTier.BASIC_B2;
            case "B3":
                return PricingTier.BASIC_B3;
            case "S2":
                return PricingTier.STANDARD_S2;
            case "S3":
                return PricingTier.STANDARD_S3;
            case "P1":
                return PricingTier.PREMIUM_P1;
            case "P2":
                return PricingTier.PREMIUM_P2;
            case "P3":
                return PricingTier.PREMIUM_P3;
            case "S1":
            default:
                return PricingTier.STANDARD_S1;
        }
    }
}
