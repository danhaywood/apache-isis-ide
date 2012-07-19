package com.halware.nakedide.eclipse.ext.annot.mdd;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Table;

public final class MetadataDescriptorSet {
	private int currentPosition = 0;

	private final LinkedHashMap<Integer, MetadataDescriptor> propertyDescriptorByPosition = new LinkedHashMap<Integer, MetadataDescriptor>();
	private final Map<String, MetadataDescriptor> propertyDescriptorByName = new LinkedHashMap<String, MetadataDescriptor>();

	public MetadataDescriptorSet(IMetadataDescriptorSetOwner owner) {
		this.owner = owner;
	}

	private final IMetadataDescriptorSetOwner owner;
	public IMetadataDescriptorSetOwner getOwner() {
		return owner;
	}
	private Table getTable() {
		return owner.getTableViewer().getTable();
	}

	/**
	 * Adds the descriptors provided by the supplied {@link IMetadataDescriptorsProvider}.
	 * 
	 * @param metadataDescriptorsProvider
	 */
	public void add(IMetadataDescriptorsProvider metadataDescriptorsProvider) {
		List<MetadataDescriptor> descriptors = metadataDescriptorsProvider.getDescriptors();
		for(MetadataDescriptor descriptor: descriptors) {
			add(descriptor);
		}
	}
	

	private MetadataDescriptor add(MetadataDescriptor mpd) {
		mpd.init(currentPosition, getTable(), owner.getDefaultLabelProvider());
		
		propertyDescriptorByPosition.put(mpd.getPosition(), mpd);
		currentPosition++;
		
		propertyDescriptorByName.put(mpd.getName(), mpd);
		
		return mpd;
	}

	public MetadataDescriptor getByPosition(Integer position) {
		return propertyDescriptorByPosition.get(position);
	}
	
	public MetadataDescriptor getByName(String memberName) {
		return propertyDescriptorByName.get(memberName);
	}

	/**
	 * Returns the {@link CellEditor}s of each of the {@link MetadataDescriptor}s, 
	 * by position.
	 * 
	 * @return
	 */
	public CellEditor[] getCellEditors() {
		List<CellEditor> cellEditors = new ArrayList<CellEditor>();
		for(Map.Entry<Integer, MetadataDescriptor> mapEntry: propertyDescriptorByPosition.entrySet()) {
			MetadataDescriptor metadataDescriptor = mapEntry.getValue();
			cellEditors.add(metadataDescriptor.getCellEditor());
		}
		return cellEditors.toArray(new CellEditor[]{});
	}

	public String[] getColumnProperties() {
		List<String> columnProperties = new ArrayList<String>();
		for(Map.Entry<Integer, MetadataDescriptor> mapEntry: propertyDescriptorByPosition.entrySet()) {
			MetadataDescriptor metadataDescriptor = mapEntry.getValue();
			columnProperties.add(metadataDescriptor.getName());
		}
		return columnProperties.toArray(new String[]{});
	}

	public <T extends ICellModifier> T getCellModifier() {
		return owner.getCellModifier(this);
	}

	public <T extends IBaseLabelProvider> T getLabelProvider() {
		return owner.getLabelProvider(this);
	}

}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
