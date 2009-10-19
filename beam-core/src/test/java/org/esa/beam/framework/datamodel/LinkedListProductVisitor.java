/*
 * $Id: LinkedListProductVisitor.java,v 1.1.1.1 2006/09/11 08:16:51 norman Exp $
 *
 * Copyright (C) 2002 by Brockmann Consult (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation. This program is distributed in the hope it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.esa.beam.framework.datamodel;

import java.util.LinkedList;
import java.util.List;


public class LinkedListProductVisitor extends ProductVisitorAdapter {

    private List<String> visitedList = new LinkedList<String>();

    public LinkedListProductVisitor() {
    }

    @Override
    public void visit(Product product) {
        visitedList.add(product.getName());
    }

    @Override
    public void visit(MetadataElement group) {
        visitedList.add(group.getName());
    }

    @Override
    public void visit(Band band) {
        visitedList.add(band.getName());
    }

    @Override
    public void visit(VirtualBand virtualBand) {
        visitedList.add(virtualBand.getName());
    }

    @Override
    public void visit(TiePointGrid grid) {
        visitedList.add(grid.getName());
    }

    @Override
    public void visit(FlagCoding flagCoding) {
        visitedList.add(flagCoding.getName());
    }

    @Override
    public void visit(MetadataAttribute attribute) {
        visitedList.add(attribute.getName());
    }

    @Override
    public void visit(BitmaskDef bitmaskDef) {
        visitedList.add(bitmaskDef.getName());
    }

    @Override
    public void visit(ProductNodeGroup group) {
        visitedList.add(group.getName());
    }

    @Override
    public void visit(IndexCoding indexCoding) {
        visitedList.add(indexCoding.getName());
    }

    @Override
    public void visit(Mask mask) {
        visitedList.add(mask.getName());
    }

    @Override
    public void visit(VectorData data) {
        visitedList.add(data.getName()); 
    }

    public List<String> getVisitedList() {
        return visitedList;
    }
}
