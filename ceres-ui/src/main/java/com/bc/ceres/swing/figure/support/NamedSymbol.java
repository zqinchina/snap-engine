package com.bc.ceres.swing.figure.support;

import com.bc.ceres.core.Assert;
import com.bc.ceres.grender.Rendering;
import com.bc.ceres.swing.figure.FigureStyle;
import com.bc.ceres.swing.figure.Symbol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * A symbol with a (well-known) name.
 *
 * @author Norman Fomferra
 * @since Ceres 0.13
 */
public class NamedSymbol implements Symbol {

    public final static NamedSymbol PLUS = new NamedSymbol("plus", CrossSymbol.createPlus(14.0));
    public final static NamedSymbol CROSS = new NamedSymbol("cross", CrossSymbol.createCross(14.0));
    public final static NamedSymbol STAR = new NamedSymbol("star", CrossSymbol.createStar(14.0));
    public final static NamedSymbol SQUARE = new NamedSymbol("square", ShapeSymbol.createSquare(14.0));
    public final static NamedSymbol CIRCLE = new NamedSymbol("circle", ShapeSymbol.createCircle(14.0));
    public final static NamedSymbol PIN = new NamedSymbol("pin", ShapeSymbol.createPin(24.0));

    private final static Map<String, NamedSymbol> symbols = getDeclaredSymbols();

    private final String name;
    private final Symbol symbol;

    public static NamedSymbol getSymbol(String name) {
        return symbols.get(name.toLowerCase());
    }

    public NamedSymbol(String name, Symbol symbol) {
        Assert.notNull(name, "name");
        Assert.notNull(symbol, "symbol");
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isHitBy(double x, double y) {
        return symbol.isHitBy(x, y);
    }

    @Override
    public void draw(Rendering rendering, FigureStyle style) {
        symbol.draw(rendering, style);
    }

    private static Map<String, NamedSymbol> getDeclaredSymbols() {
        HashMap<String, NamedSymbol> symbols = new HashMap<String, NamedSymbol>();
        Field[] declaredFields = NamedSymbol.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            int modifiers = declaredField.getModifiers();
            if (Symbol.class.isAssignableFrom(declaredField.getType())
                    && Modifier.isPublic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isStatic(modifiers)) {
                NamedSymbol symbol;
                try {
                    symbol = (NamedSymbol) declaredField.get(null);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                symbols.put(symbol.getName(), symbol);
            }
        }
        return symbols;
    }
}
