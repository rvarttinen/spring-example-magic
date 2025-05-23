/*-
 *  
 * spring-example-magic
 *  
 * Copyright (C) 2025 Autocorrect Design HB
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *  
 */
package se.autocorrect.springexample.config;

import java.nio.charset.Charset;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * The configuration for the one and only Bloom Filter instance we are using in this example. 
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Bloom_filter">Wikipedia on Bloom filters</a>
 */
@Configuration
public class BloomFilterConfig {

	BloomFilter<String> bloomFilter = BloomFilter.create(
			  Funnels.stringFunnel(Charset.defaultCharset()),
			  500, 		// Allow up to 500 id:s 
			  0.01);	// False positive probability

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	BloomFilter<String> getSingleFilter() {
		return bloomFilter;
	}
}
