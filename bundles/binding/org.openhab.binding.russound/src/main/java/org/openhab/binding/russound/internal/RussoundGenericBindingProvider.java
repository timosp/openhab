/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.util.regex.Pattern;

import org.openhab.binding.russound.RussoundBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class RussoundGenericBindingProvider extends AbstractGenericBindingProvider implements RussoundBindingProvider {

	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "russound";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
				
		String[] configParts = bindingConfig.trim().split(":");
		
		// devName:itemType:controller:zone:keypad
		RussoundBindingConfig config = null;
		
		if (configParts.length< 2) {
			throw new BindingConfigParseException(
					"Russound configuration must contain of at least two parts separated by a ':'");
		}
		
		RussoundItemType type = RussoundItemType.getRussoundItemType(configParts[0]);
		
		if(type == null)
			throw new BindingConfigParseException("unknow itemType " + configParts[0]);
		
		String deviceName = configParts[1];
		config = new RussoundBindingConfig(deviceName, type);
		switch(type){
		case ZONE_KEY:
		case ZONE_POWER:
		case ZONE_SOURCE:
		case ZONE_VOLUME:
		case ZONE_BALANCE:
		case ZONE_TREBLE:
		case ZONE_BASS:
		case ZONE_LOUDNESS:
		case ZONE_TURN_ON_VOLUME:
		case ZONE_BACKGROUND_COLOR:
		case ZONE_DND:
		case ZONE_PARTYMODE:
			if (configParts.length < 4) {
				throw new BindingConfigParseException(
						"type " + configParts[0] + " must contain of at least 4 or more parts separated by a ':'");
			}
			
			int controller = Integer.parseInt(configParts[2]);
			config.setController(controller);
			int zone = Integer.parseInt(configParts[3]);
			config.setZone(zone);
			break;
		case ALL_ZONES_POWER:
			break;
		case ALL_KEYPADS_DISPLAY_STRING:
			break;
		}
		
		//
//		
//		String device = configParts[0];
//		String cmd = configParts[1];
		
		addBindingConfig(item, config);		
	}	
	
}
