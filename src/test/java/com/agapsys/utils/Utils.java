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

package com.agapsys.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
	
	// CLASS SCOPE =============================================================
	public static String trim(String str, String chars) {
		return str.replaceAll(String.format("%s$", chars), "").replaceAll(String.format("^%s", chars), "");
	}
	
	public static String[] splitWithEscape(String str, String delimiter, String escape) {
		String[] tokens = str.split(String.format("%s(?=(?:[^%s]*%s[^%s]*%s)*[^%s]*$)", delimiter, escape, escape, escape, escape, escape));
		
		for (int i = 0; i < tokens.length; i++)
			tokens[i] = trim(tokens[i].trim(), escape);
		
		return tokens;
	}
	
	public static <T> T parseObjectFromString(String s, Class<T> clazz) throws Exception {
		return clazz.getConstructor(new Class[] {String.class }).newInstance(s);
	}
	
	
	
	public static void main(String[] args) {
		String str = "hello, world!, how, \"are, you\"";
		
		String[] tokens = splitWithEscape(str, ",", "\"");
		for (String token : tokens) {
			System.out.println(token);
		}
	}
	// =========================================================================

	// INSTANCE SCOPE ==========================================================

	// =========================================================================
}
