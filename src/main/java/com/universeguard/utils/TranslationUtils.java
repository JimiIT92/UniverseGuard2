/* 
 * Copyright (C) JimiIT92 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jimi, December 2017
 * 
 */
package com.universeguard.utils;

import com.universeguard.region.enums.RegionText;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

/**
 * 
 * Utility class for translations
 * @author Jimi
 *
 */
public class TranslationUtils {

	/**
	 * Set translations
	 * @param configNode
	 */
	public static void setDefaultValues(CommentedConfigurationNode configNode) {
		for(RegionText text : RegionText.values()) {
			if(configNode.getNode("texts", text.getName()).isVirtual())
				configNode.getNode("texts", text.getName()).setValue(text.getValue());
		}
	}
	
	/**
	 * Get translated text
	 * @param configNode
	 */
	public static void getValues(CommentedConfigurationNode configNode) {
		for(RegionText text : RegionText.values()) {
			text.setValue(configNode.getNode("texts", text.getName()).getString());
		}
	}
}
