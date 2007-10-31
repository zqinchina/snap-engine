package org.esa.beam.framework.gpf.ui;

import junit.framework.TestCase;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.ui.AppContext;
import org.esa.beam.util.PropertyMap;

import java.awt.Window;
import java.io.File;

import javax.swing.JOptionPane;

/**
 * @author Ralf Quast
 * @version $Revision$ $Date$
 */
public class SourceProductSelectorTest extends TestCase {

    private Product[] defaultProducts;
    private AppContext appContext;


    @Override
    protected void setUp() throws Exception {
        defaultProducts = new Product[4];
        for (int i = 0; i < defaultProducts.length; i++) {
            defaultProducts[i] = new Product("P" + i, "T" + i, 10, 10);
        }
        appContext = new MockAppContext();

    }

    public void testCreatedUIComponentsNotNull() {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source:");
        selector.initProductList();
        assertNotNull(selector.getProductNameLabel());
        assertNotNull(selector.getProductNameComboBox());
        assertNotNull(selector.getProductFileChooserButton());
    }

    public void testCreatedUIComponentsAreSame() {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source:");
        selector.initProductList();
        assertSame(selector.getProductNameLabel(), selector.getProductNameLabel());
        assertSame(selector.getProductNameComboBox(), selector.getProductNameComboBox());
        assertSame(selector.getProductFileChooserButton(), selector.getProductFileChooserButton());
    }

    public void testSetSelectedProduct() throws Exception {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source");
        selector.initProductList();
        Product selectedProduct = selector.getSelectedProduct();
        assertNull(selectedProduct);

        selector.setSelectedProduct(defaultProducts[1]);
        selectedProduct = selector.getSelectedProduct();
        assertSame(defaultProducts[1], selectedProduct);

        Product oldProduct = new Product("new", "T1", 0, 0);
        oldProduct.setFileLocation(new File(""));
        selector.setSelectedProduct(oldProduct);
        selectedProduct = selector.getSelectedProduct();
        assertSame(oldProduct, selectedProduct);

        Product newProduct = new Product("new", "T2", 0, 0);
        selector.setSelectedProduct(newProduct);
        selectedProduct = selector.getSelectedProduct();
        assertSame(newProduct, selectedProduct);
        assertNull(oldProduct.getFileLocation()); // assert that old product is disposed
    }

    public void testSetSelectedProductThrowsException() {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source", "T.");
        selector.initProductList();
        
        Product newProduct = new Product("new", "T1", 0, 0);
        try {
            selector.setSelectedProduct(newProduct);
        } catch (Exception e) {
            fail("Exception not expected");
        }
        Product selectedProduct = selector.getSelectedProduct();
        newProduct.setFileLocation(new File(""));
        assertSame(newProduct, selectedProduct);

        Product otherProduct = new Product("other", "P1", 0, 0);
        try {
            selector.setSelectedProduct(otherProduct);
            fail("No exception expected");
        } catch (Exception expected) {
            // ignore
        }

        selectedProduct = selector.getSelectedProduct();
        assertSame(newProduct, selectedProduct);
        assertNotNull(newProduct.getFileLocation()); // assert that old product is not disposed
    }

    public void testDispose() throws Exception {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source");
        selector.initProductList();
        try {
            selector.dispose();
        } catch (Throwable e) {
            fail("No Throwable expected");
        }


        selector = new SourceProductSelector(appContext, "Source");
        selector.initProductList();
        
        Product newProduct = new Product("new", "T1", 0, 0);
        newProduct.setFileLocation(new File(""));
        selector.setSelectedProduct(newProduct);

        Product selectedProduct = selector.getSelectedProduct();
        assertSame(newProduct, selectedProduct);

        selector.dispose();
        assertNotNull(newProduct.getFileLocation()); // assert that new product is not disposed while it is selected

        selector.setSelectedProduct(defaultProducts[0]);
        selectedProduct = selector.getSelectedProduct();
        assertSame(defaultProducts[0], selectedProduct);

        assertNotNull(newProduct.getFileLocation());
        selector.dispose();
        assertNull(newProduct.getFileLocation()); // assert that new product is disposed
    }

    public void testFileChooserAction() {

    }

    public void testSetSelectedIndex() throws Exception {
        SourceProductSelector selector = new SourceProductSelector(appContext, "Source", "T.");
        selector.initProductList();
        assertNull(selector.getSelectedProduct());

        selector.setSelectedIndex(0);
        assertSame(defaultProducts[0], selector.getSelectedProduct());
    }
    
    private class MockAppContext implements AppContext {
        private PropertyMap preferences = new PropertyMap();

        public void addProduct(Product product) {
            System.out.println("product added: " + product);
        }

        public Product[] getProducts() {
            return defaultProducts;
        }

        public Product getSelectedProduct() {
            return defaultProducts[0];
        }

        public Window getApplicationWindow() {
            return null;
        }

        public String getApplicationName() {
            return "Killer App";
        }

        public void handleError(Throwable e) {
            JOptionPane.showMessageDialog(getApplicationWindow(), e.getMessage());
        }

        public PropertyMap getPreferences() {
            return preferences;
        }
    }
}
