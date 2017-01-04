/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
 *
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
 */

package com.agapsys.jpa;

public class Range<T extends Comparable> {
    private final T min;
    private final T max;
    
    public Range(T min, T max) {
        if (min == null)
            throw new IllegalArgumentException("Null min");
        
        if (max == null)
            throw new IllegalArgumentException("Null max");
        
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException(String.format("min (%s) is greater than max (%s)", min, max));
        
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }
    
    public T getMax() {
        return max;
    }
}
